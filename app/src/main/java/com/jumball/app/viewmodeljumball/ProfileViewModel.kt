package com.jumball.app.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.jumball.app.network.NetworkResult
import com.jumball.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class ProfileViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun sendProfileData(
        successCallback: (response: NetworkResult<String>) -> Unit,
        surname: String,
        countryId: String,
        skillLevel: String,
        autoPosition: String,
        autoPlay: String,
        worldCupId: String,
        filepath: String
    ){
        repository.sendProfileData({ successCallback(it) }, surname,countryId,skillLevel,autoPosition,autoPlay,worldCupId,filepath)
    }

    suspend fun login(successCallback: (response: NetworkResult<String>) -> Unit, email: String, pass: String){
        repository.login({ successCallback(it) }, email,pass)
    }

    suspend fun getProfile(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.getProfile { successCallback(it) }
    }

}
