package com.yesitlabs.jumballapp.database.player_dtl

data class PlayerModel(
    val id: String,
    val name: String,
    val is_captain: String,
    val country_id: String,
    val type: String,
    val designation: String,
    val jersey_number: String,
    var answer: String,
    var use: String,
)
