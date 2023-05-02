package ayds.lisboa.songinfo.moredetails.fulllogic.model.entities

sealed class Artist {
    data class ArtistImpl(
        val id: Int,
        val name: String,
        val info: String,
        val source: String,
        var isLocallyStored: Boolean = false
    ) : Artist()

    object EmptyArtist : Artist()
}

