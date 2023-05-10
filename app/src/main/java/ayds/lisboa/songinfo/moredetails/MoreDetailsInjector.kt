package ayds.lisboa.songinfo.moredetails

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.*
import ayds.lisboa.songinfo.moredetails.data.external.JsonToArtistResolver
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToArtistMapper
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.presentation.FormatterInfo
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa.songinfo.moredetails.presentation.Presenter
import ayds.lisboa.songinfo.moredetails.presentation.PresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {

    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var lastFMToArtistResolver: LastFMToArtistResolver
    private lateinit var artistService: ArtistService

    private lateinit var cursorToArtistMapper: CursorToArtistMapper
    private lateinit var lastFMLocalStorage: ArtistLocalStorage

    private lateinit var artistRepository: ArtistRepository
    private lateinit var presenter: Presenter
    private lateinit var otherInfoWindow: OtherInfoView
    private val formatterInfo = FormatterInfo()

    fun init(otherInfoWindow: OtherInfoView) {
        this.otherInfoWindow = otherInfoWindow
        otherInfoWindow.setFormatterInfo(formatterInfo)
        initializeLastFMLocalStorage()
        initializeLastFMService()
        initializeArtistRepository()
        initializePresenter()
    }

    private fun initializeLastFMLocalStorage() {
        cursorToArtistMapper = CursorToArtistMapperImpl()
        lastFMLocalStorage = ArtistLocalStorageImpl(otherInfoWindow as Context, cursorToArtistMapper)
    }

    private fun initializeLastFMService() {
        lastFMAPI = getLastFMAPI()
        lastFMToArtistResolver = JsonToArtistResolver()
        artistService = ArtistServiceImpl(lastFMAPI, lastFMToArtistResolver)
    }

    private fun getLastFMAPI(): LastFMAPI {
        return Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LastFMAPI::class.java)
    }

    private fun initializeArtistRepository() {
        artistRepository = ArtistRepositoryImpl(lastFMLocalStorage, artistService)
    }

    private fun initializePresenter() {
        presenter = PresenterImpl(artistRepository)
        presenter.setOtherInfoWindow(otherInfoWindow)
    }
}