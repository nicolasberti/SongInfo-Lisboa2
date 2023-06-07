package ayds.lisboa.songinfo.moredetails.dependencyInjector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.internal.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CardsLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToCardMapper
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToCardMapperImpl
import ayds.lastfmservice.LastFMInjector
import ayds.lastfmservice.ArtistService
import ayds.lisboa.songinfo.moredetails.data.broker.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.broker.CardsBrokerImpl
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.LastFMProxy
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.NewYorkTimesProxy
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.ProxyService
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.data.broker.proxys.mappers.*
import ayds.winchester3.wikiartist.artist.externalWikipedia.WikipediaService
import ayds.lisboa.songinfo.moredetails.domain.repository.*
import ayds.lisboa.songinfo.moredetails.presentation.presenter.*
import ayds.lisboa.songinfo.moredetails.presentation.presenter.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.view.OtherInfoView
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService
import ayds.ny3.newyorktimes.external.injector.NYTimesArtistInfoServiceInjector
import ayds.winchester3.wikiartist.artist.externalWikipedia.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var lastFMService: ArtistService
    private lateinit var wikipediaService: WikipediaService
    private lateinit var nyTimesService: NYTimesArtistInfoService

    private lateinit var cursorToCardMapper: CursorToCardMapper
    private lateinit var cardsLocalStorage: CardsLocalStorage

    private lateinit var lastFMProxy: ProxyService
    private lateinit var wikipediaProxy: ProxyService
    private lateinit var nyTimesProxy: ProxyService
    private var proxyServices = ArrayList<ProxyService>()

    private lateinit var cardRepository: CardRepository
    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var otherInfoWindow: OtherInfoView

    private lateinit var broker: CardsBroker

    private val labelFactory: LabelFactory = LabelFactoryImpl
    private lateinit var cardResolver: CardResolver

    private lateinit var artistLastFMToCardMapper: ArtistLastFMToCardMapper
    private lateinit var artistNYTimesToCardMapper: ArtistNYTimesToCardMapper
    private lateinit var artistWikipediaToCardMapper: ArtistWikipediaToCardMapper

    fun init(otherInfoWindow: OtherInfoView) {
        this.otherInfoWindow = otherInfoWindow
        initializeCardResolver()
        initializeCardsLocalStorage()
        initializeServices()
        initializeMappers()
        initializeProxys()
        initializeBroker()
        initializeCardRepository()
        initializePresenter()
    }

    private fun initializeMappers() {
        artistLastFMToCardMapper = ArtistLastFMToCardMapperImpl()
        artistNYTimesToCardMapper = ArtistNYTimesToCardMapperImpl()
        artistWikipediaToCardMapper = ArtistWikipediaToCardMapperImpl()
    }
    private fun initializeCardResolver() {
        cardResolver = CardResolverImpl(labelFactory)
    }

    private fun initializeCardsLocalStorage() {
        cursorToCardMapper = CursorToCardMapperImpl()
        cardsLocalStorage = CardsLocalStorageImpl(otherInfoWindow as Context, cursorToCardMapper)
    }

    private fun initializeServices() {
        lastFMService = LastFMInjector.getService()
        wikipediaService = WikipediaInjector.wikipediaService
        nyTimesService = NYTimesArtistInfoServiceInjector.newYorkTimesArtistInfoServiceImpl
    }

    private fun initializeProxys() {
        lastFMProxy = LastFMProxy(lastFMService, artistLastFMToCardMapper)
        wikipediaProxy = WikipediaProxy(wikipediaService, artistWikipediaToCardMapper)
        nyTimesProxy = NewYorkTimesProxy(nyTimesService, artistNYTimesToCardMapper)
        proxyServices.add(lastFMProxy)
        proxyServices.add(wikipediaProxy)
        proxyServices.add(nyTimesProxy)
    }

    private fun initializeBroker(){
        broker = CardsBrokerImpl(proxyServices)
    }

    private fun initializeCardRepository() {
        cardRepository = CardRepositoryImpl(cardsLocalStorage, broker)
    }

    private fun initializePresenter() {
        otherInfoPresenter = OtherInfoPresenterImpl(cardRepository, cardResolver)
        otherInfoWindow.setPresenter(otherInfoPresenter)
    }
}