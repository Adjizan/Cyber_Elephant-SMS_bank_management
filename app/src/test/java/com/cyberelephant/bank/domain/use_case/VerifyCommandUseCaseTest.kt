package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.ConsultBalanceCommand
import com.cyberelephant.bank.data.HelpCommand
import com.cyberelephant.bank.data.NPCTransferCommand
import com.cyberelephant.bank.data.NewUserCommand
import com.cyberelephant.bank.data.TransferCommand
import org.junit.Assert
import org.junit.Test

class VerifyCommandUseCaseTest {

    private val verifyCommandUseCase = VerifyCommandUseCase()

    @Test
    fun verify_command_new_user() {
        val command = verifyCommandUseCase.call("Inscription 123456")
        Assert.assertEquals(NewUserCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_consult() {
        val command = verifyCommandUseCase.call("Solde")
        Assert.assertEquals(ConsultBalanceCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_transfer() {
        val command = verifyCommandUseCase.call("Virement destination 123456")
        Assert.assertEquals(TransferCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_npc_transfer() {
        val command = verifyCommandUseCase.call("Virement PNJ NOM destination 123456")
        Assert.assertEquals(NPCTransferCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_help() {
        val command = verifyCommandUseCase.call("Manuel")
        Assert.assertEquals(HelpCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_good_command_space_before() {
        val command = verifyCommandUseCase.call(" Inscription 123456")
        Assert.assertEquals(NewUserCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_good_command_space_after() {
        val command = verifyCommandUseCase.call("Inscription 123456 ")
        Assert.assertEquals(NewUserCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_good_command_space_both() {
        val command = verifyCommandUseCase.call(" Inscription 123456 ")
        Assert.assertEquals(NewUserCommand.javaClass, command?.javaClass)
    }

    @Test
    fun verify_command_bad_command() {
        val command = verifyCommandUseCase.call("PORTNAWAK")
        Assert.assertTrue(command == null)
    }
}
