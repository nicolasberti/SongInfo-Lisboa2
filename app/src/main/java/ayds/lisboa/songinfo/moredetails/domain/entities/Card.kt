package ayds.lisboa.songinfo.moredetails.domain.entities

sealed class Card {
    data class CardImpl(
        val description: String,
        val infoUrl: String,
        val source: String,
        val sourceLogoUrl: String = "",
        var isLocallyStored: Boolean = false
    ) : Card()

    object EmptyCard : Card()
}