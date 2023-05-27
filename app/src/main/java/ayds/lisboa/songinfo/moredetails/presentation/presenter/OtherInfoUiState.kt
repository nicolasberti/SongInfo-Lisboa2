package ayds.lisboa.songinfo.moredetails.presentation.presenter

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

data class OtherInfoUiState(
    val lastFMCard: Card,
    val wikipediaCard: Card,
    val nYTimesCard: Card
)