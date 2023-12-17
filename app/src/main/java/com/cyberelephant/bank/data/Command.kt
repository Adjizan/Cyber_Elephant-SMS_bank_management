package com.cyberelephant.bank.data

sealed class Command() {
    abstract val pattern: String
    private val regex: Regex
        get() = Regex(pattern, RegexOption.IGNORE_CASE)

    fun help(): String = "Pattern is $regex"
    fun verify(value: String): MatchResult? {
        return regex.find(value)
    }
}

data object NewUserCommand : Command() {
    override val pattern: String
        get() = "^Inscription (\\w+)$"
}

data object ConsultBalanceCommand : Command() {
    override val pattern: String
        get() = "^Solde$"
}

data object TransferCommand : Command() {
    override val pattern: String
        get() = "^Virement (\\w+) (\\d+)$"
}

data object HelpCommand : Command() {
    override val pattern: String
        get() = "^Manuel$"
}
