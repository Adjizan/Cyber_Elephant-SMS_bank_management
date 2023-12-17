package com.cyberelephant.bank

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
import com.cyberelephant.bank.domain.use_case.VerifyCommandUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SmsReceiver : BroadcastReceiver(), KoinComponent {

    private val verifyCommandUseCase: VerifyCommandUseCase by inject()
    private val addUserUseCase: AddUserUseCase by inject()

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") {
            return
        }

        intent.extras?.let { extras ->
            val message: StringBuilder = StringBuilder()

            // pdus is key for SMS in bundle
            val pdus = extras.get("pdus") as Array<ByteArray>

            for (i in pdus.indices) {
                message.append(
                    SmsMessage.createFromPdu(pdus[i]).messageBody
                )
            }
            verifyCommandUseCase.call(message.toString())?.let { command ->
                when (command) {
                    ConsultBalanceCommand -> "Consultations"
                    HelpCommand -> "Aide"
                    NewUserCommand -> handleNewUser(
                        context,
                        command,
                        message,
                        SmsMessage.createFromPdu(pdus[0]).originatingAddress!!
                    )

                    TransferCommand -> "Transfet"
                }
            } ?: "NOPE !"
        }

    }

    private fun handleNewUser(
        context: Context,
        command: Command,
        message: StringBuilder,
        phoneNumber: String
    ) {
        lateinit var feedback: String
        goAsync(callback = {
            Toast.makeText(
                context, feedback, Toast.LENGTH_SHORT
            ).show()
        }) {
            val bankAccount = command.verify(message.toString())!!.groups[1]!!.value
            feedback = try {

                addUserUseCase.call(
                    bankAccount, phoneNumber
                )
                context.getString(
                    R.string.link_phone_and_account_success, bankAccount, phoneNumber
                )

            } catch (e: Exception) {
                when (e) {
                    is BankAccountAlreadyLinked -> context.getString(
                        R.string.link_phone_and_account_already_linked,
                        bankAccount,
                        e.otherPhoneNumber
                    )

                    is BankAccountUnknown -> context.getString(
                        R.string.link_phone_and_account_unknown, bankAccount
                    )

                    else -> context.getString(
                        R.string.link_phone_and_account_no_idea, bankAccount, phoneNumber
                    )
                }
            }
        }
    }
}