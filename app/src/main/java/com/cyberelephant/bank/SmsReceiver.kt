package com.cyberelephant.bank

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast
import com.cyberelephant.bank.core.util.debugLog
import com.cyberelephant.bank.core.util.exception.BankAccountAlreadyLinked
import com.cyberelephant.bank.core.util.exception.BankAccountUnknown
import com.cyberelephant.bank.core.util.exception.InsufficientBalance
import com.cyberelephant.bank.core.util.exception.PhoneNumberUnknown
import com.cyberelephant.bank.core.util.extension.goAsync
import com.cyberelephant.bank.data.ConsultBalanceCommand
import com.cyberelephant.bank.data.HelpCommand
import com.cyberelephant.bank.data.NPCTransferCommand
import com.cyberelephant.bank.data.NewUserCommand
import com.cyberelephant.bank.data.TransferCommand
import com.cyberelephant.bank.data.TransferSuccessful
import com.cyberelephant.bank.domain.use_case.AddUserParam
import com.cyberelephant.bank.domain.use_case.AssociatePhoneNumberUseCase
import com.cyberelephant.bank.domain.use_case.BadCommandUseCase
import com.cyberelephant.bank.domain.use_case.ConsultBalanceUseCase
import com.cyberelephant.bank.domain.use_case.RequireHelpUseCase
import com.cyberelephant.bank.domain.use_case.TransferParam
import com.cyberelephant.bank.domain.use_case.TransferUseCase
import com.cyberelephant.bank.domain.use_case.VerifyCommandUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SmsReceiver : BroadcastReceiver(), KoinComponent {

    private val verifyCommandUseCase: VerifyCommandUseCase by inject()
    private val associatePhoneNumberUseCase: AssociatePhoneNumberUseCase by inject()
    private val consultBalanceUseCase: ConsultBalanceUseCase by inject()
    private val transferUseCase: TransferUseCase by inject()
    private val requireHelpUseCase: RequireHelpUseCase by inject()
    private val badCommandUseCase: BadCommandUseCase by inject()

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != "android.provider.Telephony.SMS_RECEIVED" || intent.component?.className == this.javaClass.name) {
            return
        }

        intent.extras?.let { extras ->
            val message: StringBuilder = StringBuilder()

            // pdus is key for SMS in bundle
            @Suppress("DEPRECATION", "UNCHECKED_CAST")
            val pdus = extras.get("pdus") as Array<ByteArray>

            val pdusFormat = extras.getString("format") ?: "3gpp"
            for (i in pdus.indices) {
                message.append(
                    SmsMessage.createFromPdu(
                        pdus[i],
                        pdusFormat
                    ).messageBody
                )
            }

            val originatingAddress =
                SmsMessage.createFromPdu(pdus[0], pdusFormat).originatingAddress!!
            val originalMessage = message.toString()
            verifyCommandUseCase.call(originalMessage)?.let { command ->
                when (command) {
                    ConsultBalanceCommand -> handleConsultBalance(
                        context,
                        originatingAddress
                    )

                    HelpCommand -> handleHelp(context, originatingAddress)
                    NewUserCommand -> {
                        handleNewUser(
                            context,
                            AddUserParam.from(
                                command.verify(originalMessage)!!,
                                originatingAddress
                            )
                        )
                    }

                    TransferCommand -> handleTransfer(
                        context,
                        TransferParam.from(
                            originatingAddress,
                            command.verify(originalMessage)!!
                        ),
                        originatingAddress
                    )

                    NPCTransferCommand -> handleTransfer(
                        context,
                        TransferParam.fromNPC(
                            originatingAddress,
                            command.verify(originalMessage)!!,
                        ),
                        originatingAddress
                    )

                }
            } ?: {
                handleNoCommandFound(
                    context,
                    originatingAddress,
                    originalMessage,
                    badCommandUseCase.call(originalMessage)
                )
            }
        }
    }

    private fun handleNoCommandFound(
        context: Context,
        phoneNumber: String,
        originalMessage: String,
        badCommandAttemptedHelp: String?
    ) {
        lateinit var internalFeedback: String
        lateinit var userFeedback: String
        badCommandAttemptedHelp?.let {
            internalFeedback = context.getString(
                R.string.bad_command_recognized_internal_feedback,
                phoneNumber,
                originalMessage
            )
            userFeedback = context.getString(
                R.string.bad_command_recognized_user_feedback,
                badCommandAttemptedHelp
            )
        }
            ?: run {
                internalFeedback = context.getString(
                    R.string.bad_command_not_recognized_internal_feedback,
                    phoneNumber,
                    originalMessage
                )
                userFeedback = context.getString(R.string.bad_command_not_recognized_user_feedback)
            }
        operationCallback(
            context = context,
            internalFeedback = internalFeedback,
            userFeedback = userFeedback,
            phoneNumber = phoneNumber
        )
    }

    private fun handleHelp(context: Context, phoneNumber: String) {
        lateinit var internalFeedback: String
        lateinit var userFeedback: String
        goAsync(callback = {
            operationCallback(
                context = context,
                internalFeedback = internalFeedback,
                userFeedback = userFeedback,
                phoneNumber = phoneNumber
            )
        }) {
            try {
                internalFeedback =
                    context.getString(R.string.consult_help_internal_feedback, phoneNumber)
                userFeedback = context.getString(
                    R.string.consult_help_user_success,
                    requireHelpUseCase.call(phoneNumber).joinToString { "- $it\n" }
                )
            } catch (e: Exception) {
                debugLog(exception = e)
                val feedbacks = handleException(context, e)
                internalFeedback = feedbacks.first
                userFeedback = feedbacks.second
            }
        }

    }

    private fun handleTransfer(
        context: Context,
        transferParam: TransferParam,
        phoneNumber: String,
    ) {
        lateinit var internalFeedback: String
        lateinit var userFeedback: String
        lateinit var toOther: TransferSuccessful
        goAsync(callback = {
            operationCallback(
                context = context,
                internalFeedback = internalFeedback,
                userFeedback = userFeedback,
                phoneNumber = phoneNumber
            )
            sendSms(
                context,
                context.getString(
                    R.string.transfer_money_user_other_feedback_success,
                    transferParam.amount,
                    toOther.fromName,
                    toOther.newBalance
                ),
                transferParam.destinationBankAccount
            )
        }) {
            try {
                toOther = transferUseCase.call(transferParam)
            } catch (e: Exception) {
                debugLog(exception = e)
                val feedbacks = handleException(context, e)
                internalFeedback = feedbacks.first
                userFeedback = feedbacks.second
            }
        }
    }

    private fun handleConsultBalance(context: Context, phoneNumber: String) {
        lateinit var internalFeedback: String
        lateinit var userFeedback: String
        goAsync(callback = {
            operationCallback(
                context = context,
                internalFeedback = internalFeedback,
                userFeedback = userFeedback,
                phoneNumber = phoneNumber
            )
        }) {
            try {
                internalFeedback =
                    context.getString(R.string.consult_balance_internal_feedback, phoneNumber)
                val nameBalance = consultBalanceUseCase.call(phoneNumber)
                userFeedback = context.getString(
                    R.string.consult_balance_user_success,
                    nameBalance.first,
                    nameBalance.second,
                )
            } catch (e: Exception) {
                debugLog(exception = e)
                val feedbacks = handleException(context, e)
                internalFeedback = feedbacks.first
                userFeedback = feedbacks.second
            }
        }
    }


    private fun handleNewUser(
        context: Context,
        addUserParam: AddUserParam
    ) {
        lateinit var internalFeedback: String
        lateinit var userFeedback: String
        goAsync(callback = {
            operationCallback(
                context = context,
                internalFeedback = internalFeedback,
                userFeedback = userFeedback,
                phoneNumber = addUserParam.phoneNumber
            )
        }) {
            try {

                val name = associatePhoneNumberUseCase.call(addUserParam)
                internalFeedback = context.getString(
                    R.string.link_phone_and_account_internal_success,
                    addUserParam.bankAccount,
                    addUserParam.phoneNumber
                )
                userFeedback = context.getString(R.string.link_phone_and_account_user_success, name)

            } catch (e: Exception) {
                debugLog(exception = e)
                val feedbacks = handleException(context, e)
                internalFeedback = feedbacks.first
                userFeedback = feedbacks.second
            }
        }
    }

    private fun operationCallback(
        context: Context,
        internalFeedback: String,
        userFeedback: String,
        phoneNumber: String
    ) {
        Toast.makeText(
            context,
            internalFeedback,
            Toast.LENGTH_SHORT
        ).show()
        sendSms(context, userFeedback, phoneNumber)
    }

    private fun sendSms(context: Context, message: String, phoneNumber: String) {
        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }
        smsManager?.sendTextMessage(phoneNumber, null, message, null, null) ?: run {
            Toast.makeText(
                context,
                "Je n'ai pas réussi à récupérer le SMS Manager",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * @return a [Pair]<[String],[String]> with the first [String] as the internal feedback and
     * the second one, the user feedback
     */
    private fun handleException(
        context: Context,
        e: Exception,
    ): Pair<String, String> {
        lateinit var internalFeedback: String
        lateinit var userFeedback: String
        when (e) {
            is BankAccountAlreadyLinked -> {
                internalFeedback = context.getString(
                    R.string.link_phone_and_account_internal_already_linked,
                    e.bankAccount,
                    e.otherPhoneNumber
                )
                userFeedback =
                    context.getString(R.string.link_phone_and_account_user_already_linked)
            }

            is BankAccountUnknown -> {
                internalFeedback = context.getString(
                    R.string.link_phone_and_account_internal_unknown, e.bankAccount
                )
                userFeedback =
                    context.getString(R.string.link_phone_and_account_user_unknown)
            }

            is PhoneNumberUnknown -> {
                internalFeedback = context.getString(
                    R.string.consult_balance_internal_feedback,
                    e.phoneNumber
                )
                userFeedback = context.getString(R.string.sms_operation_unknown_user)
            }

            is InsufficientBalance -> {
                internalFeedback =
                    context.getString(R.string.transfer_money_internal_insufficient_balance)
                userFeedback =
                    context.getString(R.string.transfer_money_user_insufficient_balance)
            }

            else -> {
                internalFeedback = context.getString(
                    R.string.generic_user_no_idea,
                )
                userFeedback =
                    context.getString(R.string.generic_user_no_idea)
            }
        }
        return Pair(
            internalFeedback,
            userFeedback
        )
    }

}