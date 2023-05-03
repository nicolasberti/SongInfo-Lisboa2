package ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities

sealed class Artist {
    data class ArtistImpl(
        val name: String,
        val info: String,
        val source: String,
        var isLocallyStored: Boolean = false
    ) : Artist()

    object EmptyArtist : Artist()
}

