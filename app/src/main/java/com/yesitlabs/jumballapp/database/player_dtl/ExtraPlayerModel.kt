package com.yesitlabs.jumballapp.database.player_dtl

data class ExtraPlayerModel(
    val id: String,
    val name: String,
    val is_captain: String,
    val country_id: String,
    val type: String,
    val designation: String,
    val jersey_number: String,
    val answer: String,
    val userType : String,
)
