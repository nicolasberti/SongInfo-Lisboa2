package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Context
import android.util.Log
import ayds.lisboa.songinfo.moredetails.fulllogic.data.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.*
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.JsonToArtistResolver
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMServiceImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb.CursorToArtistMapper
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb.CursorToArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.FormatterInfo
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoView
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.Presenter
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.PresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {

    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var lastFMToArtistResolver: LastFMToArtistResolver
    private lateinit var lastFMService: LastFMService

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
        lastFMService = LastFMServiceImpl(lastFMAPI, lastFMToArtistResolver)
    }

    private fun getLastFMAPI(): LastFMAPI {
        return Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LastFMAPI::class.java)
    }

    private fun initializeArtistRepository() {
        artistRepository = ArtistRepositoryImpl(lastFMLocalStorage, lastFMService)
    }

    private fun initializePresenter() {
        presenter = PresenterImpl(artistRepository)
        presenter.setOtherInfoWindow(otherInfoWindow)
    }
}
