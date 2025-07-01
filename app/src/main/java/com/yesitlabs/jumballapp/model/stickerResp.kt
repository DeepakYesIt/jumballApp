package com.yesitlabs.jumballapp.model

data class stickerResp(
    val success : Boolean,
    val code : Int,
    val message : String,
    val data : ArrayList<StickerData>?
)

data class StickerData(
    val country_id : Int,
    val sticker_id : Int,
    val image :String,
    val country : countryDetails
)
data class countryDetails(
    val name : String,
    val country_worldcup_flag : String,
)
