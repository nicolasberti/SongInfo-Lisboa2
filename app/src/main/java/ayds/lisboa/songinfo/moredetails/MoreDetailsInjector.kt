package ayds.lisboa.songinfo.moredetails

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.*
import ayds.lisboa.songinfo.moredetails.data.external.ArtistServiceImpl
import ayds.lisboa.songinfo.moredetails.data.external.JsonToArtistResolver
import ayds.lisboa.songinfo.moredetails.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToArtistMapper
import ayds.lisboa.songinfo.moredetails.data.internal.sqldb.CursorToArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa.songinfo.moredetails.presentation.Presenter
import ayds.lisboa.songinfo.moredetails.presentation.PresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
object MoreDetailsInjector {

    private val lastFMAPI: LastFMAPI = Retrofit.Builder()
        .baseUrl(LASTFM_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(LastFMAPI::class.java)

    private val lastFMToArtistResolver: LastFMToArtistResolver = JsonToArtistResolver()
    private val artistService: ArtistService = ArtistServiceImpl(lastFMAPI, lastFMToArtistResolver)

    private var cursorToArtistMapper: CursorToArtistMapper = CursorToArtistMapperImpl()
    private lateinit var lastFMLocalStorage: ArtistLocalStorage
    private lateinit var artistRepository: ArtistRepository
    private var presenter: Presenter = PresenterImpl()
    private lateinit var otherInfoWindow: OtherInfoView

    fun init(otherInfoWindow: OtherInfoView) {
        this.otherInfoWindow = otherInfoWindow
        initializeLastFMLocalStorage()
        initializeArtistRepository()
        presenter.setOtherInfoWindow(otherInfoWindow)
        presenter.setArtistInfoRepository(artistRepository)
    }

    private fun initializeLastFMLocalStorage() {
        lastFMLocalStorage = ArtistLocalStorageImpl(otherInfoWindow as Context, cursorToArtistMapper)
    }

    private fun initializeArtistRepository() {
        artistRepository = ArtistRepositoryImpl(lastFMLocalStorage, artistService)
    }
}