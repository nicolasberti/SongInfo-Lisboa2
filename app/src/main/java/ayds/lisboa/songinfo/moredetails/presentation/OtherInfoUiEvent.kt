package ayds.lisboa.songinfo.moredetails.presentation

sealed class OtherInfoUiEvent {

    object GetInfo : OtherInfoUiEvent()
    object OpenInfoUrl : OtherInfoUiEvent()
}