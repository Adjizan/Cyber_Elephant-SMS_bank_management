package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.Command

class BadCommandUseCase {
    private val commands: List<Command> =
        Command::class.sealedSubclasses.map { it.objectInstance as Command }

    fun call(message: String): String? =
        commands.firstOrNull { it.verifyInstruction(message.trim()) }?.help

}
