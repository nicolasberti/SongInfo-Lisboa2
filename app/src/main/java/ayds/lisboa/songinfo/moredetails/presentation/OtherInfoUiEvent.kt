package ayds.lisboa.songinfo.moredetails.presentation

sealed class OtherInfoUiEvent {

    object UpdateViewInfo : OtherInfoUiEvent()
    object UpdateViewUrl : OtherInfoUiEvent()
}