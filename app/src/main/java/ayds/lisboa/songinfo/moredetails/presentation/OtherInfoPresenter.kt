package ayds.lisboa.songinfo.moredetails.presentation


import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository

interface OtherInfoPresenter {

    val uiEventObservable: Observable<OtherInfoUiState>
    fun actionSearch(artistName: String)
}
internal class OtherInfoPresenterImpl(
    private var cardRepository: CardRepository,
    private var cardResolver: CardResolver
): OtherInfoPresenter {

    private val onActionSubject = Subject<OtherInfoUiState>()
    override val uiEventObservable = onActionSubject

    override fun actionSearch(artistName: String){
        Thread {
            threadActionSearch(artistName)
        }.start()
    }

    private fun threadActionSearch(artistName: String){
        //val cards = artistInfoRepository.getArtist(artistName)
        val cards = ArrayList<Card>()
        cards.add(Card("info1","infoUrl1",Source.LastFM,"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png", true))
        cards.add(Card("info2","infoUrl2",Source.LastFM,"logoUrl2", true))
        cards.add(Card("info3","infoUrl3",Source.LastFM,"logoUrl3", true))
        val uiState = getUiState(cards, artistName)
        notifyState(uiState)
    }

    private fun getNYTimesCard(cards: List<Card>): Card {
        val nYTimesCard = cards.find { it.source == Source.NYTimes }
        return nYTimesCard ?: Card(source = Source.NYTimes)
    }

    private fun getLastFMCard(cards: List<Card>): Card {
        val lastFMCard = cards.find { it.source == Source.LastFM }
        return lastFMCard ?: Card(source = Source.LastFM)
    }

    private fun getWikipediaCard(cards: List<Card>): Card {
        val wikipediaCard = cards.find { it.source == Source.Wikipedia }
        return wikipediaCard ?: Card(source = Source.Wikipedia)
    }

    private fun getUiState(cards: List<Card>, artistName: String): OtherInfoUiState{
        val lastFMCard = getLastFMCard(cards)
        cardResolver.setFormattedInfo(lastFMCard, artistName)
        val wikipediaCard = getWikipediaCard(cards)
        cardResolver.setFormattedInfo(wikipediaCard, artistName)
        val nYTimesCard = getNYTimesCard(cards)
        cardResolver.setFormattedInfo(nYTimesCard, artistName)
        return OtherInfoUiState(lastFMCard, wikipediaCard, nYTimesCard)
    }

    private fun notifyState(uiState: OtherInfoUiState){
        uiEventObservable.notify(uiState)
    }

}