package ayds.lisboa.songinfo.moredetails.fulllogic

import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMService

object LastFMInjector {

    val lastFMService: LastFMService = LastFMServiceInjector.lastFMService
}