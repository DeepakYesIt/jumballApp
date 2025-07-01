package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.model.Setting
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.repository.ForgotPasswordRepository
import com.yesitlabs.jumballapp.network.repository.SettingsRepository
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.SignupOtpResp
import com.yesitlabs.jumballapp.model.SingUpResp
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewmodel(private val userRepository: UserRepository = UserRepository()) : ViewModel()  {

    private val repository: SettingsRepository = SettingsRepository.getInstance()
    private val _signUpResponse = MutableLiveData<Response<SingUpResp>?>()

    val signUpResponse: MutableLiveData<Response<SingUpResp>?> get() = _signUpResponse
    private val forgotRepository: ForgotPasswordRepository = ForgotPasswordRepository.getInstance()

    private val _signUpOtpResponse = MutableLiveData<Response<SignupOtpResp>?>()

    val signUpOtpResponse: MutableLiveData<Response<SignupOtpResp>?> get() = _signUpOtpResponse


    fun userSingUp(name :String ,email: String,pass:String) {
        viewModelScope.launch {
            try {
                _signUpResponse.value = userRepository.userSingUp(name,email,pass)
            }catch (e : Exception){
                _signUpResponse.value = null
                Log.e("Login Error" , e.toString())
            }

        }
    }

    fun userSingUpOtp(email: String) {
        viewModelScope.launch {
            try {
                _signUpOtpResponse.value = userRepository.userSingUpOtp(email)
            }catch (e : Exception){
                _signUpOtpResponse.value = null
                Log.e("Login Error" , e.toString())
            }
        }
    }



    fun setting(successCallbackToFragment: (response: NetworkResult<Setting>) -> Unit,
                token:String){

        repository.setting ({
            successCallbackToFragment(it)
        },token)

    }


    private val _socialLoginResponse = MutableLiveData<Response<LoginResp>?>()
    val socialLoginResponse: MutableLiveData<Response<LoginResp>?> get() = _socialLoginResponse




    fun socialLogin(name :String, email: String, fcmToken:String) {
        viewModelScope.launch {
            try {
                _socialLoginResponse.value = userRepository.socialLogin(name,email,fcmToken)
            }catch (e : Exception){
                _socialLoginResponse.value = null
                Log.e("Login Error" , e.toString())
            }

        }
    }



    fun forgotPasswordOtp(successCallback: (forgotRepository: NetworkResult<Pair<String, Int>>) -> Unit, email:String){
        forgotRepository.forgotPasswordOtp({ response ->
            successCallback(response)
        },email)
    }




}