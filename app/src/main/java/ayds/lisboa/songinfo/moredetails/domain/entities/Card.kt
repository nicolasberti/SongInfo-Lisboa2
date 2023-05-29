package ayds.lisboa.songinfo.moredetails.domain.entities

enum class Source {
    LastFM,
    Wikipedia,
    NYTimes
}
data class Card(
    var description: String = "",
    val infoUrl: String = "",
    val source: Source,
    val sourceLogoUrl: String = "",
    var isLocallyStored: Boolean = false
)