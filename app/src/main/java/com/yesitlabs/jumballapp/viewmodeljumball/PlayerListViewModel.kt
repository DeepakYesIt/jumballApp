package com.yesitlabs.jumballapp.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class PlayerListViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {


    suspend fun getGuessPlayerList(successCallback: (response: NetworkResult<String>) -> Unit,  defender : String, midFielder : String, attacker : String, userCaptainId : String, cpuCaptainId : String,match_no:String){
        repository.getGuessPlayerList({ successCallback(it) }, defender ,midFielder, attacker,userCaptainId,cpuCaptainId,match_no)
    }




}
