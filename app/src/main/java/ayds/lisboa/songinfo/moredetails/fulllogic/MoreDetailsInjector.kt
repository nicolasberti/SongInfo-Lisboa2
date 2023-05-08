package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Context
import ayds.lisboa.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.*
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.JsonToArtistResolver
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMServiceImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb.ArtistLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb.CursorToArtistMapper
import ayds.lisboa.songinfo.moredetails.fulllogic.data.internal.sqldb.CursorToArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoWindow
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.Presenter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
object MoreDetailsInjector {

    private val lastFMAPIRetrofit = Retrofit.Builder()
        .baseUrl(LASTFM_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val lastFMAPI = lastFMAPIRetrofit.create(LastFMAPI::class.java)
    private val lastFMToArtistResolver: LastFMToArtistResolver = JsonToArtistResolver()
    private val lastFMService: LastFMService = LastFMServiceImpl(lastFMAPI, lastFMToArtistResolver)

    private var cursorToArtistMapper: CursorToArtistMapper = CursorToArtistMapperImpl()
    private lateinit var lastFMLocalStorage: ArtistLocalStorage
    private val artistRepository: ArtistRepository = ArtistRepositoryImpl(lastFMLocalStorage, lastFMService)
    private lateinit var presenter: Presenter
    private lateinit var otherInfoWindow: OtherInfoWindow

    fun init(otherInfoWindow: OtherInfoWindow){
        this.otherInfoWindow = otherInfoWindow
        presenter.setOtherInfoWindow(otherInfoWindow)
        presenter.setArtistInfoRepository(artistRepository)
        initializeLastFMLocalStorage()
    }

    private fun initializeLastFMLocalStorage(){
        lastFMLocalStorage = ArtistLocalStorageImpl(otherInfoWindow as Context, cursorToArtistMapper)
    }
}