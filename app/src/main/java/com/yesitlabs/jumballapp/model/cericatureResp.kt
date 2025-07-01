package com.yesitlabs.jumballapp.viewModel.gameapi.stickerViewModel

data class cericatureResp(
    val success : Boolean,
    val code : Int,
    val message : String,
    val data : ArrayList<cericatureRespDATA>?
)

data class cericatureRespDATA(
    val country_id : Int,
    val image :String,
    val country : cericatureRespcountryDetails
)
data class cericatureRespcountryDetails(
    val name : String,
    val country_worldcup_flag : String,
)



