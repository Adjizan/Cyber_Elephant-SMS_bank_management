package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.Command
import com.cyberelephant.bank.data.ConsultBalanceCommand
import com.cyberelephant.bank.data.HelpCommand
import com.cyberelephant.bank.data.NewUserCommand
import com.cyberelephant.bank.data.TransferCommand

class VerifyCommandUseCase {

    private val commands: List<Command> =
        listOf(
            NewUserCommand,
            ConsultBalanceCommand,
            TransferCommand,
            HelpCommand
        )

    fun call(value: String): Command? {
        return commands.firstOrNull { it.verify(value.trim()) != null }
    }

}