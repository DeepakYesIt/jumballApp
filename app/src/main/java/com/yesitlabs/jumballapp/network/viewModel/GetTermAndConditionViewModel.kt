package com.yesitlabs.jumballapp.network.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesitlabs.jumballapp.network.repository.UserRepository
import com.yesitlabs.jumballapp.model.termAndCondion.Term_condition_resp
import kotlinx.coroutines.launch
import retrofit2.Response

class GetTermAndConditionViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _getTermAndConditionResponse = MutableLiveData<Response<Term_condition_resp>?>()
    val getTermAndConditionResponse: MutableLiveData<Response<Term_condition_resp>?> get() = _getTermAndConditionResponse

    fun getTermAndCondition(token: String) {
        viewModelScope.launch {
            try {
                _getTermAndConditionResponse.value = userRepository.getTermAndCondition(token)
            }catch (e : Exception)
            {
                _getTermAndConditionResponse.value = null
                Log.e("Term And Condition Error", e.toString())
            }
         }
    }

}
