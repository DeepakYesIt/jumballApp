package com.yesitlabs.jumballapp.network.viewModel

import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.PlayerProfileModel
import com.yesitlabs.jumballapp.model.WorldCupModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.repository.PlayerProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerProfileViewModel(
    private val repository: PlayerProfileRepository? =
                                 PlayerProfileRepository.getInstance()): ViewModel() {

    var countriesList = MutableLiveData<List<String>>()

    var skillList = MutableLiveData<List<String>>()
    var positionList  = MutableLiveData<List<String>>()
    var stylePlayList  = MutableLiveData<List<String>>()
    var countryIdMap = MutableLiveData<HashMap<String,Int>>()
    private var worldCupMapData = MutableLiveData<HashMap<Int,String>>()
    private var worldCupListData = MutableLiveData<List<String>>()
    var worldCupModelList = MutableLiveData<List<WorldCupModel>>()

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
    fun sendProfileData(successCallbackToFragment: (response: NetworkResult<JsonObject>) -> Unit,
                        token:String, name:String, countryId:Int, skillLevel:String,
                        position:String,
                        playStyle:String,
                        worldCupId:Int){

        repository?.sendProfileData ({
            successCallbackToFragment(it)
        },token,name, countryId, skillLevel, position, playStyle, worldCupId)

    }


    private suspend fun formation(playerProfile:PlayerProfileModel) = withContext(Dispatchers.IO) {
        Log.d("ThreadName_Formation", Thread.currentThread().toString())
        val d= Looper.myLooper() == Looper.getMainLooper()
        Log.d("ThreadName_Formation", "dgf$d")
        val countryName= mutableListOf<String>()
        val skillSet = mutableListOf<String>()
        val position = mutableListOf<String>()
        val styleOfPlay = mutableListOf<String>()
        val countryMap = HashMap<String,Int>()
        val worldCupMap = HashMap<Int,String>()
        val worldCupList = mutableListOf<String>()


//        playerProfile.countries.forEach {
//            countryName.add(it.name)
//            countryMap[it.name] = it.id
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
//            worldCupMap[it.id] = it.countryWorldCupFlag
//            worldCupList.add(it.countryWorldCupFlag)
//        }
//
//
//        if (playerProfile.worldCup.isNotEmpty()){
//            worldCupModelList.postValue(playerProfile.worldCup)
//        }
//
//
//        worldCupMapData.postValue(worldCupMap)
//        worldCupListData.postValue(worldCupList)
    }



 }