package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lastfmservice.Artist
import java.util.*

interface ArtistInfoResolver {
    fun getFormattedInfo(artistInfo: Artist, artistName: String): String
    fun getUrl(artistInfo: Artist): String
}

class ArtistInfoResolverImpl : ArtistInfoResolver {

    companion object {
        const val HTML_WIDTH = "<html><div width=400>"
        const val HTML_FONT = "<font face=\"arial\">"
        const val HTML_END = "</font></div></html>"
        const val NO_RESULTS = "No results"
        const val NO_RESULTS_URL = "URL NOT FOUND"
        const val PREFIX_LOCALLY_STORED = "[*]"
    }

    override fun getFormattedInfo(artistInfo: Artist, artistName: String): String{
        val info = getInfoFromArtistInfo(artistInfo)
        val infoFormatted = formatInfo(info, artistName)
        return textToHtml(infoFormatted)
    }

    override fun getUrl(artistInfo: Artist): String =
        when (artistInfo) {
            is Artist.EmptyArtist -> NO_RESULTS_URL
            is Artist.LastFMArtist -> artistInfo.url
        }

    private fun formatInfo(info: String, artist: String): String {
        val textoSinComillas = info.replace("'", " ")
        val textoConSaltosDeLineaHTML = textoSinComillas.replace("\\n", "<br>")
        val textoArtistaEnNegrita = textoConSaltosDeLineaHTML.replace("(?i)$artist".toRegex(), "<b>$artist</b>")
        val artistUpperCase = artist.uppercase(Locale.getDefault())
        return textoArtistaEnNegrita.replace("(?i)$artist".toRegex(), artistUpperCase)
    }

    private fun textToHtml(text: String): String {
        val builder = StringBuilder()
        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT)
        builder.append(text)
        builder.append(HTML_END)
        return builder.toString()
    }

    private fun getInfoFromArtistInfo(artistInfo: Artist): String{
        return when (artistInfo){
            is Artist.EmptyArtist -> NO_RESULTS
            is Artist.LastFMArtist ->{
                var info = artistInfo.info
                if (artistInfo.isLocallyStored)
                    info = PREFIX_LOCALLY_STORED + info
                else if (info == "")
                    info = NO_RESULTS
                info
            }
        }
    }

}