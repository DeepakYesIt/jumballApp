package com.jumball.app.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.jumball.app.network.NetworkResult
import com.jumball.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class SaveScoreViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun saveScoreList(successCallback: (response: NetworkResult<String>) -> Unit, total_goal: String, total_goal_console: String, match_status: String,captianId:String,total_defence: String, opponent_guessed: String, my_guesses: String){
        repository.saveScoreList({ successCallback(it) }, total_goal , total_goal_console ,match_status,captianId,total_defence,opponent_guessed,my_guesses)
    }

}
