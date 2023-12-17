package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.ConsultBalanceCommand
import com.cyberelephant.bank.data.HelpCommand
import com.cyberelephant.bank.data.NewUserCommand
import com.cyberelephant.bank.data.TransferCommand
import org.junit.Assert
import org.junit.Test

class VerifyCommandUseCaseTest {

    private val verifyCommandUseCase = VerifyCommandUseCase()

    @Test
    fun verify_command_new_user() {
        val command = verifyCommandUseCase.call("Inscription 123456")
        Assert.assertTrue(command is NewUserCommand)
    }

    @Test
    fun verify_command_consult() {
        val command = verifyCommandUseCase.call("Solde 123456")
        Assert.assertTrue(command is ConsultBalanceCommand)
    }

    @Test
    fun verify_command_transfer() {
        val command = verifyCommandUseCase.call("Virement destination 123456")
        Assert.assertTrue(command is TransferCommand)
    }

    @Test
    fun verify_command_help() {
        val command = verifyCommandUseCase.call("Manuel")
        Assert.assertTrue(command is HelpCommand)
    }

    @Test
    fun verify_command_good_command_space_before() {
        val command = verifyCommandUseCase.call(" Inscription 123456")
        Assert.assertTrue(command is NewUserCommand)
    }

    @Test
    fun verify_command_good_command_space_after() {
        val command = verifyCommandUseCase.call("Inscription 123456 ")
        Assert.assertTrue(command is NewUserCommand)
    }

    @Test
    fun verify_command_good_command_space_both() {
        val command = verifyCommandUseCase.call(" Inscription 123456 ")
        Assert.assertTrue(command is NewUserCommand)
    }

    @Test
    fun verify_command_bad_command() {
        val command = verifyCommandUseCase.call("PORTNAWAK")
        Assert.assertTrue(command == null)
    }
}