package ayds.lisboa.songinfo.moredetails.presentation.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface LabelFactory{
    fun getLabelFromSource(source: Source): String
}

internal object LabelFactoryImpl: LabelFactory{
    private const val LAST_FM_LABEL = "Last FM"
    private const val NEW_YORK_TIMES_LABEL = "New York Times"
    private const val WIKIPEDIA_LABEL = "Wikipedia"

    override fun getLabelFromSource(source: Source): String {
        return when (source) {
            Source.LastFM -> LAST_FM_LABEL
            Source.NYTimes -> NEW_YORK_TIMES_LABEL
            Source.Wikipedia -> WIKIPEDIA_LABEL
        }
    }
}