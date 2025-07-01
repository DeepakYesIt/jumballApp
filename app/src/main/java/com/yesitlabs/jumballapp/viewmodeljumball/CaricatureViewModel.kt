package com.yesitlabs.jumballapp.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class CaricatureViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {


    suspend fun getCaricature(successCallback: (response: NetworkResult<String>) -> Unit, match_no: String){
        repository.getCaricature({ successCallback(it) }, match_no)
    }

    suspend fun getTeam(successCallback: (response: NetworkResult<String>) -> Unit, is_first: String){
        repository.getTeam({ successCallback(it) }, is_first)
    }


}
