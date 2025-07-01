package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.SaveScoreResp
import kotlinx.coroutines.launch
import retrofit2.Response

class WorldCupWonViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _worldCupWonResponse = MutableLiveData<Response<SaveScoreResp>?>()
    val worldCupWonResponse: MutableLiveData<Response<SaveScoreResp>?> get() = _worldCupWonResponse

    fun worldCupWon(token: String,count:String) {
        viewModelScope.launch {
            try {
                _worldCupWonResponse.value = userRepository.worldCupWon(token,count)
            }catch (e: Exception)
            {
                _worldCupWonResponse.value = null
                Log.e("Get All Activity List Error :" , e.toString())
            }
         }
    }

}
