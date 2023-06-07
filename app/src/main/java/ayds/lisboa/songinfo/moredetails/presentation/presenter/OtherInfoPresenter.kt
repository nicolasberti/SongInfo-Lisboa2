package ayds.lisboa.songinfo.moredetails.presentation.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.presentation.presenter.CardResolverImpl.Companion.NO_RESULTS
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

    companion object {
        const val IMAGE_NO_RESULTS = "https://cdn-icons-png.flaticon.com/512/6134/6134065.png"
    }

    private val onActionSubject = Subject<OtherInfoUiState>()
    override val uiEventObservable = onActionSubject

    override fun actionSearch(artistName: String){
        Thread {
            threadActionSearch(artistName)
        }.start()
    }

    private fun threadActionSearch(artistName: String){
        val cards = cardRepository.getCards(artistName)
        val uiState = getUiState(cards, artistName)
        notifyState(uiState)
    }

    private fun getUiState(cards: List<Card>, artistName: String): OtherInfoUiState {
        val cardsUiState:MutableList<UiCard> = mutableListOf()
        for (card in cards)
            cardsUiState.add(cardToUiCard(card,artistName))
        if (cardsUiState.isEmpty())
            cardsUiState.add(getEmptyCard())
        return OtherInfoUiState(cardsUiState)
    }

    private fun cardToUiCard(card: Card, artistName: String): UiCard {
        val info = cardResolver.getFormattedInfo(card, artistName)
        val source = cardResolver.getSource(card.source)
        return UiCard(info, card.infoUrl, source, card.sourceLogoUrl)
    }

    private fun getEmptyCard(): UiCard{
        return UiCard(NO_RESULTS, "", "", IMAGE_NO_RESULTS)
    }

    private fun notifyState(uiState: OtherInfoUiState){
        uiEventObservable.notify(uiState)
    }
}