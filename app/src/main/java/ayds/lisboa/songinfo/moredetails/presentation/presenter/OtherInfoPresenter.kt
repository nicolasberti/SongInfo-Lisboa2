package ayds.lisboa.songinfo.moredetails.presentation.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

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
        val cards = cardRepository.getArtist(artistName)
        val uiState = getUiState(cards, artistName)
        notifyState(uiState)
    }

    private fun cardToUiCard(card: Card, artistName: String): UiCard{
        val info = cardResolver.getFormattedInfo(card, artistName)
        return UiCard(info, card.infoUrl, card.source, card.sourceLogoUrl)
    }

    private fun getUiState(cards: List<Card>, artistName: String): OtherInfoUiState {
        val cardsUiState:MutableList<UiCard> = mutableListOf()
        for (card in cards)
            cardsUiState.add(cardToUiCard(card,artistName))
        return OtherInfoUiState(cardsUiState)
    }

    private fun notifyState(uiState: OtherInfoUiState){
        uiEventObservable.notify(uiState)
    }
}