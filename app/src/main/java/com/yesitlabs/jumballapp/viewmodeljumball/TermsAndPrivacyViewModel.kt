package com.yesitlabs.jumballapp.viewmodeljumball

import androidx.lifecycle.ViewModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class TermsAndPrivacyViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel() {


    suspend fun getTermAndCondition(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.getTermAndCondition { successCallback(it) }
    }

    suspend fun getPrivacyAndPolicy(successCallback: (response: NetworkResult<String>) -> Unit){
        repository.getPrivacyAndPolicy { successCallback(it) }
    }

}
