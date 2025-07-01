package com.yesitlabs.jumballapp.fragment.authfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.yesitlabs.jumballapp.AppConstant
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentForgetPasswordBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.forgotmodel.ForgotApiModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@AndroidEntryPoint
class ForgetPasswordFragment : Fragment(), View.OnClickListener {


    lateinit var type: String

    private lateinit var viewModel: SignUpViewModel
    lateinit var sessionManager : SessionManager

    private lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            if (requireArguments().getString("email") != null){
                binding.edEmail.setText(requireArguments().getString("email").toString())
            }
        }catch (e:Exception){
            Log.e("Forgot have","No argument")
        }

        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.signinFragment)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.btnSubmit.setOnClickListener(this)


    }

    override fun onClick(item: View?) {
        when (item!!.id) {
            R.id.btn_submit -> {
                if (isValidation()) {
                    if(sessionManager.isNetworkAvailable()){
                        sendOtp()
                    }else{
                        Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun sendOtp(){
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.forgotPasswordOtp({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, ForgotApiModel::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    val email = signUpModel.data.email
                                    val otp = signUpModel.data.otp.toString()
                                    val bundle = Bundle()
                                    bundle.putString("type", "1")
                                    bundle.putString(AppConstant.EMAIL, email)
                                    bundle.putString(AppConstant.OTP, otp)
                                    Log.e("OTP", otp)
                                    findNavController().navigate(R.id.otpVerificationFragment, bundle)
                                }catch (e:Exception){
                                    Log.d("signup","message:---"+e.message)
                                }
                            } else {
                                sessionManager.alertError(signUpModel.message)
                            }
                        }catch (e:Exception){
                            Log.d("signup","message:---"+e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }, binding.edEmail.text.toString())
        }
    }

    // This function is used for check email is valid or not
    private fun isValidation(): Boolean {
        val result = true
        val emailString = ErrorMessage.emailPattern
        val patternEmail = Pattern.compile(emailString)
        val email = patternEmail.matcher(binding.edEmail.text.toString().trim())
        if (binding.edEmail.text.toString().isEmpty()) {
            sessionManager.alertError("Please enter your registered email")
            return false
        } else if (!email.matches()) {
            sessionManager.alertError("Invalid email")
            return false
        }
        return result
    }


}