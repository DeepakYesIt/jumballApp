package com.yesitlabs.jumballapp.network.viewModel

import androidx.lifecycle.ViewModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.repository.ForgotPasswordRepository

class ForgotPasswordViewModel(val forgotRrepository: ForgotPasswordRepository? = ForgotPasswordRepository.getInstance()) :ViewModel(){

    fun forgotPasswordOtp(successCallback: (forgotRrepository: NetworkResult<Pair<String, Int>>) -> Unit, email:String){
        forgotRrepository?.forgotPasswordOtp({ response ->
          successCallback(response)
                                    },email)
    }


    fun resetPassword(successCallback: (response: NetworkResult<Int>) -> Unit, email:String,password:String,
                      confirmPassword:String){
        forgotRrepository?.resetPassword({
                                  successCallback(it)
        }, email, password, confirmPassword)
    }


}