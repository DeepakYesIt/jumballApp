package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.network.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PenaltyScreenViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

  var gameOn = true;

    // CURRENTcHANCE -1 FOR USER CURRENTCHANCE 2 FOR CPU
  var currentChance = 1

  var count =0;
  var cpuPoint =0;
  var userPoint =0;
  var loopCount =0;
  var cpuCount =0;
  var userCount =0;

  var userlist = mutableListOf<Boolean>()
  var cpuList = mutableListOf<Boolean>()



}