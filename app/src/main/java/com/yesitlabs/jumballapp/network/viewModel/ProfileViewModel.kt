package com.yesitlabs.jumballapp.network.viewModel

import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.PlayerProfileModel
import com.yesitlabs.jumballapp.model.UserModel
import com.yesitlabs.jumballapp.model.WorldCupModel
import com.yesitlabs.jumballapp.network.NetworkResult

import com.yesitlabs.jumballapp.network.repository.PlayerProfileRepository
import com.yesitlabs.jumballapp.network.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(val repository: ProfileRepository?
= ProfileRepository.getInstance()): ViewModel() {

    var countriesList = MutableLiveData<List<String>>()
    var skillList = MutableLiveData<List<String>>()
    var positionList  = MutableLiveData<List<String>>()
    var stylePlayList  = MutableLiveData<List<String>>()
    var countryIdMap = MutableLiveData<HashMap<String,Int>>()
    var worldCupMapData = MutableLiveData<HashMap<String,Int>>()
    var worldCupListData = MutableLiveData<List<String>>()
    var worldCupmodelList = MutableLiveData<List<WorldCupModel>>()
    var userModel =MutableLiveData<UserModel>()

    fun launchDataLoad(playerProfile:PlayerProfileModel) {
        viewModelScope.launch {
            formation(playerProfile)
        }
    }

    fun getProfileData(successCallbackToFragment: (response: NetworkResult<PlayerProfileModel>) -> Unit, token:String){

        repository?.getProfileData ({
            successCallbackToFragment(it)
        },token)

    }
    fun sendProfileDataWithImage(successCallbackToFragment: (response: NetworkResult<JsonObject>) -> Unit,
                        token:String,name:String, country_id:Int ,skill_level:String,
                        position:String,
                        play_style:String,
                        world_cup_id:Int,
                                 filepath:String){

        repository?.sendProfileDataWithImage ({
            successCallbackToFragment(it)
        },token,name, country_id, skill_level, position, play_style, world_cup_id,
            filepath)

    }


    suspend fun formation(playerProfile:PlayerProfileModel) = withContext(Dispatchers.IO) {
        Log.d("ThreadName_Formation", Thread.currentThread().toString())
        var d= Looper.myLooper() == Looper.getMainLooper()
        Log.d("ThreadName_Formation","dgf"+d )
        var countryName= mutableListOf<String>()
        var skillSet = mutableListOf<String>()
        var position = mutableListOf<String>()
        var styleOfPlay = mutableListOf<String>()
        var countryMap = HashMap<String,Int>()
        var worldCupMap = HashMap<String,Int>()
        var worldCupList = mutableListOf<String>()

//        playerProfile.user.let {
//            userModel.postValue(it)
//        }
//
//        playerProfile.countries.forEach {
//            countryName.add(it.name)
//            countryMap.put(it.name,it.id)
//        }
//
//         countriesList.postValue(countryName)
//         countryIdMap.postValue(countryMap)
//
//
//        playerProfile.skillLevel.forEach {
//            skillSet.add(it.name)
//        }
//
//        skillList.postValue(skillSet)
//
//        playerProfile.position.forEach {
//            position.add(it.name)
//        }
//
//        positionList.postValue(position)
//
//        playerProfile.stylePlay.forEach {
//            styleOfPlay.add(it.name)
//        }
//
//        stylePlayList.postValue(styleOfPlay)
//
//        playerProfile.worldCup.forEach {
//            worldCupMap.put( it.year+"-"+it.country.name,it.id)
//            worldCupList.add(it.year+"-"+it.country.name)
//        }
//        if (!playerProfile.worldCup.isEmpty()){
//            worldCupmodelList.postValue(playerProfile.worldCup)
//        }
//        worldCupMapData.postValue(worldCupMap)
//        worldCupListData.postValue(worldCupList)
    }



 }