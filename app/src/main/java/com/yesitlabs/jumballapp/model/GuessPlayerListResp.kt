package com.yesitlabs.jumballapp.model

data class GuessPlayerListResp(
    val code: Int,
    val `data`: PlayerData?,
    val message: String,
    val success: Boolean
)

data class PlayerData(
    val cpuplayer: ArrayList<PlayerDetails>?,
    val myplayer: ArrayList<PlayerDetails>?,
    val SubtitutePlyer: ArrayList<PlayerDetails>?
)


data class PlayerDetails(
    val id: Int,
    val user_id: Int?,
    val name: String?,
    val card_image: Any?,
    val is_captain: Int,
    val country_id: String?,
    val type: String?,
    val designation: String?,
    val jersey_number: Int?,
)