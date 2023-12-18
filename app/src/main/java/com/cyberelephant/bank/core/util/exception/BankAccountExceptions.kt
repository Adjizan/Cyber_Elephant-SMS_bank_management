package com.cyberelephant.bank.core.util.exception

class BankAccountAlreadyLinked(val bankAccount: String, val otherPhoneNumber: String) : Exception()

class BankAccountUnknown(val bankAccount: String) : Exception()
class PhoneNumberUnknown(val phoneNumber: String) : Exception()
class InsufficientBalance : Exception()