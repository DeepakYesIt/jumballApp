package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.ScoreBoardResp
import kotlinx.coroutines.launch
import retrofit2.Response

class GetScoreBoardViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _getScoreBoardResponse = MutableLiveData<Response<ScoreBoardResp>?>()
    val getScoreBoardResponse: MutableLiveData<Response<ScoreBoardResp>?> get() = _getScoreBoardResponse

    fun getScoreBoard(token: String) {
        viewModelScope.launch {
            try {
                _getScoreBoardResponse.value = userRepository.getScoreBoard(token)
            }catch (e: Exception)
            {
                _getScoreBoardResponse.value = null
                Log.e("Get Scroe Board Error :" , e.toString())
            }
         }
    }

}
