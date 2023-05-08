package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

sealed class OtherInfoUiEvent {

    object GetInfo : OtherInfoUiEvent()
    object OpenInfoUrl : OtherInfoUiEvent()
}