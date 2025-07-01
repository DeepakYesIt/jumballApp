package com.yesitlabs.jumballapp.model

data class GetGroupDetailResp (
    val success : Boolean ,
    val code : Int ,
    val message : String ,
    val data : ArrayList<TeamDetail>? ,

)

data class TeamDetail(
    val id : Int,
    val user_id : Int,
    val total_goal : Int,
    val total_goal_console : Int,
    val match_status : Int,
    //// key type change Int to Any two field
    val my_guesses : Any?,
    val opponent_guessed : Any?,
    val cpu_captain_id : Int?,
    val name : String,
    val totalgame : Int,
    val totalLoss : Int,
    val total_defence : Int,
    val totalWin : Int,)