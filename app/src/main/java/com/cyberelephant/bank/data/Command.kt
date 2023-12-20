package com.cyberelephant.bank.data

// All "helps" should be done otherwise for a better I18N but... for now, it 
sealed class Command {
    abstract val instruction: String
    abstract val pattern: String
    private val regex: Regex
        get() = Regex(pattern, RegexOption.IGNORE_CASE)

    abstract val help: String

    open val forOrganizerOnly: Boolean = false

    fun verify(value: String): MatchResult? {
        return regex.find(value)
    }

    fun verifyInstruction(value: String): Boolean {
        return value.startsWith(instruction, ignoreCase = true)
    }
}

data object NewUserCommand : Command() {
    override val instruction: String
        get() = "Inscription"
    override val pattern: String
        get() = "^$instruction (\\w+)$"
    override val help: String
        get() = "S'inscrire : Inscription [NUMÉRO_DE_COMPTE]"
}

data object ConsultBalanceCommand : Command() {
    override val instruction: String
        get() = "Solde"
    override val pattern: String
        get() = "^$instruction"
    override val help: String
        get() = "Consulter son solde : Solde"
}

data object TransferCommand : Command() {
    override val instruction: String
        get() = "Virement"
    override val pattern: String
        get() = "^$instruction (\\w+) (\\d+)$"
    override val help: String
        get() = "Transfer des fonds : Virement [NUMÉRO_DE_COMPTE_DESTINATAIRE] [MONTANT]"
}

data object NPCTransferCommand : Command() {
    override val instruction: String
        get() = "Virement PNJ"
    override val pattern: String
        get() = "^$instruction (\\w)+ (\\w+) (\\d+)$"
    override val help: String
        get() = "Transfer des fonds PNJ : Virement PNJ [NOM_DE_PERSONNAGE] [NUMÉRO_DE_COMPTE_DESTINATAIRE] [MONTANT]"
    override val forOrganizerOnly: Boolean
        get() = true
}

data object HelpCommand : Command() {
    override val instruction: String
        get() = "Manuel"
    override val pattern: String
        get() = "^$instruction"
    override val help: String
        get() = "Liste des commandes : Manuel"
}
