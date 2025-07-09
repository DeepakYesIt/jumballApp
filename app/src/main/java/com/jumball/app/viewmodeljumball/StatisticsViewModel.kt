package com.jumball.app.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.jumball.app.network.NetworkResult
import com.jumball.app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class StatisticsViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {

    suspend fun statisticsRequest(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.statisticsRequest { successCallback(it) }
    }

    suspend fun getSticker(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.getSticker { successCallback(it) }
    }

}
