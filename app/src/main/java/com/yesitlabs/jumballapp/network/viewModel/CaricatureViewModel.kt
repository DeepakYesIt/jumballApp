package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.model.GetGroupDetailResp
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.viewModel.gameapi.stickerViewModel.cericatureResp
import kotlinx.coroutines.launch
import retrofit2.Response

class CaricatureViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _caricatureResponse = MutableLiveData<Response<cericatureResp>?>()
    val caricatureResponse: MutableLiveData<Response<cericatureResp>?> get() = _caricatureResponse

    fun getCaricature(token: String,match_no:String) {
        viewModelScope.launch {
            try {
                _caricatureResponse.value = userRepository.getCaricature(token,match_no)
            }catch (e: Exception)
            {
                _caricatureResponse.value = null
                Log.e("Get Caricature Error :" , e.toString())
            }
         }
    }

    private val _getTeamResponse = MutableLiveData<Response<GetGroupDetailResp>?>()
    val getTeamResponse: MutableLiveData<Response<GetGroupDetailResp>?> get() = _getTeamResponse

    fun getTeam(token: String,is_first:String) {
        viewModelScope.launch {
            try {
                _getTeamResponse.value = userRepository.getTeam(token,is_first)
                Log.d("********", "_getTeamResponse$_getTeamResponse")
            }catch (e: Exception) {
                _getTeamResponse.value = null
                Log.d("****** Error :" ,""+e.toString())
            }
        }
    }


}
