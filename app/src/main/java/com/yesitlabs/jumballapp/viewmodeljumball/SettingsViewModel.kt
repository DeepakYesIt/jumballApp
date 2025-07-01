package com.yesitlabs.jumballapp.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class SettingsViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun socialLogin(successCallback: (response: NetworkResult<String>) -> Unit, name: String, email: String, fcmToken: String){
        repository.socialLogin({ successCallback(it) }, name,email,fcmToken)
    }

    suspend fun soundEffectStatusChange(successCallback: (response: NetworkResult<String>) -> Unit, sound: String){
        repository.soundEffectStatusChange({ successCallback(it) },sound)
    }

    suspend fun musicStatusChange(successCallback: (response: NetworkResult<String>) -> Unit, sound: String){
        repository.musicStatusChange({ successCallback(it) },sound)
    }

    suspend fun logOut(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.logOut { successCallback(it) }
    }

    suspend fun profileDelete(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.profileDelete { successCallback(it) }
    }

}
