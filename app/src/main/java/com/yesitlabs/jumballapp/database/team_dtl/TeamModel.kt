package com.yesitlabs.jumballapp.database.team_dtl

data class TeamModel(
    val teamID: Int,
    val captainName: String,
    val country: String,
    val enable: Int,
    val PLD: Int,
    val W: Int,
    val D: Int,
    val L: Int,
    val F: Int,
    val A: Int,
)
