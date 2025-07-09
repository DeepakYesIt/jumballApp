package com.yesitlabs.jumballapp.network.viewModel

import androidx.lifecycle.ViewModel

class PenaltyScreenViewModel : ViewModel() {

  var gameOn = true
  // CURRENTcHANCE -1 FOR USER CURRENTCHANCE 2 FOR CPU
  var currentChance = 1

  var count =0
  var cpuPoint =0
  var userPoint =0
  var loopCount =0
  var cpuCount =0
  var userCount =0

  var userlist = mutableListOf<Boolean>()
  var cpuList = mutableListOf<Boolean>()



}