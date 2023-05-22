package ayds.lisboa.songinfo.moredetails.dependencyInjector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToArtistMapper
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {

    private lateinit var lastFMAPI: ayds.lastfmservice.external.LastFMAPI
    private lateinit var lastFMToArtistResolver: ayds.lastfmservice.external.LastFMToArtistResolver
    private lateinit var artistService: ayds.lastfmservice.external.ArtistService

    private lateinit var cursorToArtistMapper: CursorToArtistMapper
    private lateinit var lastFMLocalStorage: ArtistLocalStorage

    private lateinit var artistRepository: ArtistRepository
    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var otherInfoWindow: OtherInfoView

    private val artistInfoResolver = ArtistInfoResolverImpl()

    fun init(otherInfoWindow: OtherInfoView) {
        this.otherInfoWindow = otherInfoWindow
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
        lastFMToArtistResolver = ayds.lastfmservice.external.JsonToArtistResolver()
        artistService =
            ayds.lastfmservice.external.ArtistServiceImpl(lastFMAPI, lastFMToArtistResolver)
    }

    private fun getLastFMAPI(): ayds.lastfmservice.external.LastFMAPI {
        return Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(ayds.lastfmservice.external.LastFMAPI::class.java)
    }

    private fun initializeArtistRepository() {
        artistRepository = ArtistRepositoryImpl(lastFMLocalStorage, artistService)
    }

    private fun initializePresenter() {
        otherInfoPresenter = OtherInfoPresenterImpl(artistRepository, artistInfoResolver)
        otherInfoWindow.setPresenter(otherInfoPresenter)
    }
}