package ayds.lisboa.songinfo.moredetails.presentation

const val IMAGE_LASTFM_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
data class OtherInfoUiState(
    val info: String = "",
    val url: String = "",
    val lastFMImage: String = IMAGE_LASTFM_LOGO
)