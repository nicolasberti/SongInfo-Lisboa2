package ayds.lisboa.songinfo.moredetails.domain.entities
data class Card(
    val description: String,
    val infoUrl: String,
    val source: String,
    val sourceLogoUrl: String,
    var isLocallyStored: Boolean = false
)