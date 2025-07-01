package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.stickerResp
import kotlinx.coroutines.launch
import retrofit2.Response

class StickerViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _stickerResponse = MutableLiveData<Response<stickerResp>?>()
    val stickerResponse: MutableLiveData<Response<stickerResp>?> get() = _stickerResponse

    fun getSticker(token: String) {
        viewModelScope.launch {
            try {
                _stickerResponse.value = userRepository.getSticker(token)
            }catch (e: Exception)
            {
                _stickerResponse.value = null
                Log.e("Get All Activity List Error :" , e.toString())
            }
         }
    }

}
