package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.Command

class VerifyCommandUseCase {

    private val commands: List<Command> =
        Command::class.sealedSubclasses.map { it.objectInstance as Command }

    fun call(value: String): Command? {
        return commands.firstOrNull { it.verify(value.trim()) != null }
    }

}
