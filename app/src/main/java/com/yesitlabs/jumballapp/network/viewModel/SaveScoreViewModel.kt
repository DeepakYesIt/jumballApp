package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.SaveScoreResp
import kotlinx.coroutines.launch
import retrofit2.Response

class SaveScoreViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _saveScoreListResponse = MutableLiveData<Response<SaveScoreResp>?>()
    val saveScoreListResponse: MutableLiveData<Response<SaveScoreResp>?> get() = _saveScoreListResponse

    fun saveScoreList(token: String, total_goal: String, total_goal_console: String, match_status: String,captianId:String,total_defence: String, opponent_guessed: String, my_guesses: String) {
        viewModelScope.launch {
            try {
                _saveScoreListResponse.value = userRepository.saveScore(token,total_goal , total_goal_console ,match_status,captianId,total_defence,opponent_guessed,my_guesses)
            }catch (e: Exception)
            {
                _saveScoreListResponse.value = null
                Log.e("Get All Activity List Error :" , e.toString())
            }
         }
    }

}
