package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.privacyPolicy.PrivacyPolicyResp
import kotlinx.coroutines.launch
import retrofit2.Response

class GetPrivacyPolicyViewModel(private val userRepository: UserRepository = UserRepository()) :
    ViewModel() {

    private val _getPrivacyPolicyResponse = MutableLiveData<Response<PrivacyPolicyResp>?>()
    val getPrivacyPolicyResponse: MutableLiveData<Response<PrivacyPolicyResp>?> get() = _getPrivacyPolicyResponse

    fun getPrivacyAndPolicy(token: String) {
        viewModelScope.launch {
            try {
                _getPrivacyPolicyResponse.value = userRepository.getPrivacyAndPolicy(token)
            } catch (e: Exception) {
                _getPrivacyPolicyResponse.value = null
                Log.e("Privacy Policy Error", e.toString())
            }
        }
    }

}
