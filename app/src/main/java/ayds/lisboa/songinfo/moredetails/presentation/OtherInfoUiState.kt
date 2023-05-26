package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

data class OtherInfoUiState(
    val lastFMCard: Card,
    val wikipediaCard: Card,
    val nYTimesCard: Card
)