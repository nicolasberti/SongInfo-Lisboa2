package ayds.lisboa.songinfo.moredetails.presentation.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import java.util.*

interface CardResolver {
    fun setFormattedInfo(card: Card, artistName: String)
}

class CardResolverImpl : CardResolver {

    companion object {
        const val HTML_WIDTH = "<html><div width=400>"
        const val HTML_FONT = "<font face=\"arial\">"
        const val HTML_END = "</font></div></html>"
        const val NO_RESULTS = "No results"
        const val PREFIX_LOCALLY_STORED = "[*]"
    }

    override fun setFormattedInfo(card: Card, artistName: String){
        val info = getInfoFromCard(card)
        val infoFormatted = formatInfo(info, artistName)
        card.description = textToHtml(infoFormatted)
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

    private fun getInfoFromCard(card: Card): String {
        var info = card.description
        return if (info == "")
            NO_RESULTS
        else{
            if (card.isLocallyStored)
                info = PREFIX_LOCALLY_STORED + info
            info
        }
    }
}