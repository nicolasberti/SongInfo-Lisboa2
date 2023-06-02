package ayds.lisboa.songinfo.moredetails.presentation.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import java.util.*

interface CardResolver {
    fun getFormattedInfo(card: Card, artistName: String): String
    fun getSource(source: Source): String
}

internal class CardResolverImpl(
    private var labelFactory: LabelFactory
) : CardResolver {

    companion object {
        const val HTML_WIDTH = "<html><div width=400>"
        const val HTML_FONT = "<font face=\"arial\">"
        const val HTML_END = "</font></div></html>"
        const val NO_RESULTS = "No results"
        const val PREFIX_LOCALLY_STORED = "[*]"
    }

    override fun getFormattedInfo(card: Card, artistName: String): String{
        val info = getInfoFromCard(card)
        val infoFormatted = formatInfo(info, artistName)
        return textToHtml(infoFormatted)
    }

    override fun getSource(source: Source): String {
        return labelFactory.getLabelFromSource(source)
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
        if (info.isEmpty())
            return NO_RESULTS
        if (card.isLocallyStored)
            info = PREFIX_LOCALLY_STORED + info
        return info
    }
}