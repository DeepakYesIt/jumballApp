package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.model.Setting
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.repository.SettingsRepository
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.LoginResp
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _loginResponse = MutableLiveData<Response<LoginResp>?>()
    val loginResponse: MutableLiveData<Response<LoginResp>?> get() = _loginResponse

    private val _socialLoginResponse = MutableLiveData<Response<LoginResp>?>()
    val socialLoginResponse: MutableLiveData<Response<LoginResp>?> get() = _socialLoginResponse

    private val repository: SettingsRepository = SettingsRepository.getInstance()

    fun login(email: String,pass:String) {
        viewModelScope.launch {
            try {
                _loginResponse.value = userRepository.login(email,pass)
            }catch (e : Exception){
                _loginResponse.value = null
                Log.e("Login Error" , e.toString())
            }

        }
    }




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

    fun setting(successCallbackToFragment: (response: NetworkResult<Setting>) -> Unit, token:String){

        repository?.setting ({
            successCallbackToFragment(it)
        },token)

    }

}