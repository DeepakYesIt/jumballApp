package com.yesitlabs.jumballapp.model.penaltiesmodel

data class PenalitesUserCPUStore (
     var userMaxCount: Int = 0,
     var cpuMaxCount: Int = 0,
     var userTotal: Int = 0,
     var cpuTotal: Int = 0,
     var cupScore:MutableList<Int>,
     var userScore:MutableList<Int>)

