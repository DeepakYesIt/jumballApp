package com.yesitlabs.jumballapp.model.teamListModel

data class TeamListModel (
    val teamsn: String,
    val teamID: ArrayList<Int>,
    val captainName: ArrayList<String>,
    val cardImage: ArrayList<String>,
    val enable:  ArrayList<Int>,
    val PLD:  ArrayList<Int>,
    val W:  ArrayList<Int>,
    val D:  ArrayList<Int>,
    val L:  ArrayList<Int>,
    val F:  ArrayList<Int>,
    val A:  ArrayList<Int>,
    val GD:  ArrayList<Int>,
    val PTS:  ArrayList<Int>,
)