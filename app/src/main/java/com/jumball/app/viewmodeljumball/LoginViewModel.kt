package com.jumball.app.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.jumball.app.network.NetworkResult
import com.jumball.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class LoginViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun socialLogin(successCallback: (response: NetworkResult<String>) -> Unit, name: String, email: String, fcmToken: String){
        repository.socialLogin({ successCallback(it) }, name,email,fcmToken)
    }

    suspend fun login(successCallback: (response: NetworkResult<String>) -> Unit, email: String, pass: String){
        repository.login({ successCallback(it) }, email,pass)
    }
    suspend fun resetPassword(successCallback: (response: NetworkResult<String>) -> Unit, email: String, pass: String,cnfPass:String){
        repository.resetPassword({ successCallback(it) }, email,pass,cnfPass)
    }

    suspend fun setting(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.setting { successCallback(it) }
    }

}
