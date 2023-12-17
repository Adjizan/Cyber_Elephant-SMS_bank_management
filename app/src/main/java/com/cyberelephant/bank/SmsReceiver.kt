package com.cyberelephant.bank

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast
import com.cyberelephant.bank.core.util.exception.BankAccountAlreadyLinked
import com.cyberelephant.bank.core.util.exception.BankAccountUnknown
import com.cyberelephant.bank.core.util.extension.goAsync
import com.cyberelephant.bank.data.Command
import com.cyberelephant.bank.data.ConsultBalanceCommand
import com.cyberelephant.bank.data.HelpCommand
import com.cyberelephant.bank.data.NewUserCommand
import com.cyberelephant.bank.data.TransferCommand
import com.cyberelephant.bank.domain.use_case.AddUserUseCase
import com.cyberelephant.bank.domain.use_case.ConsultBalanceUseCase
import com.cyberelephant.bank.domain.use_case.VerifyCommandUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SmsReceiver : BroadcastReceiver(), KoinComponent {

    private val verifyCommandUseCase: VerifyCommandUseCase by inject()
    private val addUserUseCase: AddUserUseCase by inject()
    private val consultBalanceUseCase: ConsultBalanceUseCase by inject()

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
            verifyCommandUseCase.call(message.toString())?.let { command ->
                val originatingAddress =
                    SmsMessage.createFromPdu(pdus[0], pdusFormat).originatingAddress!!
                when (command) {
                    ConsultBalanceCommand -> handleConsultBalance(
                        context,
                        originatingAddress
                    )

                    HelpCommand -> "Aide"
                    NewUserCommand -> {
                        handleNewUser(
                            context,
                            command,
                            message,
                            originatingAddress
                        )
                    }

                    TransferCommand -> "Transfet"
                }
            } ?: "NOPE !"
        }

    }

    private fun handleConsultBalance(context: Context, phoneNumber: String) {
        lateinit var message: String
        goAsync(callback = {
            Toast.makeText(
                context,
                context.getString(R.string.consult_balance_internal_feedback, phoneNumber),
                Toast.LENGTH_SHORT
            ).show()
            sendSms(context, message, phoneNumber)
        }) {
            message = try {
                context.getString(
                    R.string.consult_balance_user_success,
                    consultBalanceUseCase.call(phoneNumber)
                )
            } catch (e: Exception) {
                when (e) {
                    is BankAccountUnknown -> context.getString(R.string.consult_balance_user_failure)
                    else -> context.getString(R.string.generic_user_no_idea)
                }
            }
        }
    }

    private fun handleNewUser(
        context: Context,
        command: Command,
        message: StringBuilder,
        phoneNumber: String
    ) {
        lateinit var feedback: String
        lateinit var feedbackForUser: String
        goAsync(callback = {
            Toast.makeText(
                context,
                feedback,
                Toast.LENGTH_SHORT
            ).show()
            sendSms(context, feedbackForUser, phoneNumber)
        }) {
            val bankAccount = command.verify(message.toString())!!.groups[1]!!.value
            try {

                addUserUseCase.call(
                    bankAccount, phoneNumber
                )
                feedback = context.getString(
                    R.string.link_phone_and_account_internal_success, bankAccount, phoneNumber
                )
                feedbackForUser = context.getString(R.string.link_phone_and_account_user_success)

            } catch (e: Exception) {
                when (e) {
                    is BankAccountAlreadyLinked -> {
                        feedback = context.getString(
                            R.string.link_phone_and_account_internal_already_linked,
                            bankAccount,
                            e.otherPhoneNumber
                        )
                        feedbackForUser =
                            context.getString(R.string.link_phone_and_account_user_already_linked)
                    }

                    is BankAccountUnknown -> {
                        feedback = context.getString(
                            R.string.link_phone_and_account_internal_unknown, bankAccount
                        )
                        feedbackForUser =
                            context.getString(R.string.link_phone_and_account_user_unknown)
                    }

                    else -> {
                        feedback = context.getString(
                            R.string.link_phone_and_account_internal_no_idea,
                            bankAccount,
                            phoneNumber
                        )
                        feedbackForUser =
                            context.getString(R.string.generic_user_no_idea)
                    }
                }
            }
        }
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


}