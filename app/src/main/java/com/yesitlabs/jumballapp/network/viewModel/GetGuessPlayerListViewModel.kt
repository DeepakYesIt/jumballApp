package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import kotlinx.coroutines.launch
import retrofit2.Response

class GetGuessPlayerListViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _getGuessPlayerListResponse = MutableLiveData<Response<GuessPlayerListResp>?>()
    val getGuessPlayerListResponse: MutableLiveData<Response<GuessPlayerListResp>?> get() = _getGuessPlayerListResponse

    fun getGuessPlayerList(token: String, defender : String, midFielder : String, attacker : String, userCaptainId : String, cpuCaptainId : String,match_no:String) {
        viewModelScope.launch {
            try {
                _getGuessPlayerListResponse.value = userRepository.getGuessPlayerList(token,defender ,midFielder, attacker,userCaptainId,cpuCaptainId,match_no)
            }catch (e: Exception)
            {
                _getGuessPlayerListResponse.value = null
                Log.e("Get All Guess List List Error :" , e.toString())
            }
         }
    }

}
