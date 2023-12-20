package com.cyberelephant.bank.data

// All "helps" should be done otherwise for a better I18N but... for now, it 
sealed class Command {
    abstract val pattern: String
    private val regex: Regex
        get() = Regex(pattern, RegexOption.IGNORE_CASE)

    abstract val help: String

    open val forOrganizerOnly: Boolean = false

    fun verify(value: String): MatchResult? {
        return regex.find(value)
    }
}

data object NewUserCommand : Command() {
    override val pattern: String
        get() = "^Inscription (\\w+)$"
    override val help: String
        get() = "S'inscrire : Inscription [NUMÉRO_DE_COMPTE]"
}

data object ConsultBalanceCommand : Command() {
    override val pattern: String
        get() = "^Solde$"
    override val help: String
        get() = "Consulter son solde : Solde"
}

data object TransferCommand : Command() {
    override val pattern: String
        get() = "^Virement (\\w+) (\\d+)$"
    override val help: String
        get() = "Transfer des fonds : Virement [NUMÉRO_DE_COMPTE_DESTINATAIRE] [MONTANT]"
}

data object NPCTransferCommand : Command() {
    override val pattern: String
        get() = "^Virement PNJ (\\w)+ (\\w+) (\\d+)$"
    override val help: String
        get() = "Transfer des fonds PNJ : Virement PNJ [NOM_DE_PERSONNAGE] [NUMÉRO_DE_COMPTE_DESTINATAIRE] [MONTANT]"
    override val forOrganizerOnly: Boolean
        get() = true
}

data object HelpCommand : Command() {
    override val pattern: String
        get() = "^Manuel$"
    override val help: String
        get() = "Liste des commandes : Manuel"
}
