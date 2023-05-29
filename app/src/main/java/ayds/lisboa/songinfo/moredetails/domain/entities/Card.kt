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
    val sourceLogoUrl: String = "https://cdn-icons-png.flaticon.com/512/6134/6134065.png",
    var isLocallyStored: Boolean = false
)