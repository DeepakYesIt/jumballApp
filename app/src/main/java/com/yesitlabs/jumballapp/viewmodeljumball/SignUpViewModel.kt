package com.yesitlabs.jumballapp.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class SignUpViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun signUpModel(successCallback: (response: NetworkResult<String>) -> Unit, emailOrPhone: String, password: String){
        repository.signUpModel({ successCallback(it) }, emailOrPhone,password)
    }

    suspend fun userSingUpOtp(successCallback: (response: NetworkResult<String>) -> Unit, email: String){
        repository.userSingUpOtp({ successCallback(it) }, email)
    }

    suspend fun userSingUp(successCallback: (response: NetworkResult<String>) -> Unit, name:String, email: String,pass:String){
        repository.userSingUp({ successCallback(it) },name, email , pass)
    }
    suspend fun forgotPasswordOtp(successCallback: (response: NetworkResult<String>) -> Unit, email: String){
        repository.forgotPasswordOtp({ successCallback(it) }, email)
    }

    suspend fun socialLogin(successCallback: (response: NetworkResult<String>) -> Unit, name: String, email: String, fcmToken: String){
        repository.socialLogin({ successCallback(it) }, name,email,fcmToken)
    }

    suspend fun setting(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.setting { successCallback(it) }
    }

}
