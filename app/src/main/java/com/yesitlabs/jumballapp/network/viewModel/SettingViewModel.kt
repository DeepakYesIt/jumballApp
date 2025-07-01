package com.yesitlabs.jumballapp.network.viewModel
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.Setting
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.repository.SettingsRepository

class SettingViewModel(
    private val repository: SettingsRepository?
= SettingsRepository.getInstance()): ViewModel() {


    fun profileDelete(successCallbackToFragment: (response: NetworkResult<JsonObject>) -> Unit,
                      token:String){

        repository?.profileDelete ({
            successCallbackToFragment(it)
        },token)

    }

    fun userLogout(successCallbackToFragment: (response: NetworkResult<JsonObject>) -> Unit,
                   token:String){

        repository?.userLogout ({
            successCallbackToFragment(it)
        },token)

    }

    fun setting(successCallbackToFragment: (response: NetworkResult<Setting>) -> Unit,
                    token:String){

        repository?.setting ({
            successCallbackToFragment(it)
        },token)

    }

    fun musicStatusChange(successCallbackToFragment: (response: NetworkResult<JsonObject>) -> Unit,
                    token:String,music:String){

        repository?.musicStatusChange ({
            successCallbackToFragment(it)
        },token,music)

    }

    fun soundEffectStatusChange(successCallbackToFragment: (response: NetworkResult<JsonObject>) -> Unit,
                          token:String,sound:String){

        repository?.soundEffectStatusChange ({
            successCallbackToFragment(it)
        },token,sound)



    }



 }