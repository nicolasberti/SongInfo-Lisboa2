package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities.Artist

class FormatterInfo {

    fun getInfoFromArtistInfo(artistInfo: Artist.ArtistImpl?): String{
        var info = artistInfo?.info
        if (artistInfo?.isLocallyStored == true)
            info = PREFIX_LOCALLY_STORED +"$info"
        else if (info == null)
            info = NO_RESULTS
        return info
    }

    fun textToHtml(text: String): String {
        val builder = StringBuilder()
        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT)
        builder.append(text)
        builder.append(HTML_END)
        return builder.toString()
    }
}