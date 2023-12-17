package com.cyberelephant.bank.core.util.exception

class BankAccountAlreadyLinked(val otherPhoneNumber: String): Exception()

class BankAccountUnknown: Exception()