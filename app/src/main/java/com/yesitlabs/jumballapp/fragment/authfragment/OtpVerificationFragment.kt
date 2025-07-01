package com.yesitlabs.jumballapp.fragment.authfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.yesitlabs.jumballapp.databinding.FragmentOtpVerificationBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.SignupOtpResp
import com.yesitlabs.jumballapp.model.SingUpResp
import com.yesitlabs.jumballapp.model.forgotmodel.ForgotApiModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.viewModel.SignUpViewmodel
import com.yesitlabs.jumballapp.viewmodeljumball.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class OtpVerificationFragment : Fragment(), View.OnClickListener {

    private lateinit var type: String
    private var countDownTimer: CountDownTimer? = null
    private val startTimeInMills: Long = 120000
    private var mTimeLeftInMillis = startTimeInMills
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var  otp:String

    private lateinit var viewModel: SignUpViewModel

    lateinit var sessionManager: SessionManager

    private lateinit var binding: FragmentOtpVerificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtpVerificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        type = requireArguments().getString("type", "1")
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        email=requireArguments().getString(AppConstant.EMAIL, "")
        otp=requireArguments().getString(AppConstant.OTP,"")
        Log.d("otp", "*****$otp")
        if (type == "2") {
            name = requireArguments().getString(AppConstant.NAME, "")
            password = requireArguments().getString(AppConstant.PASSWORD, "")
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnSubmit.setOnClickListener(this)

        binding.btnResend.setOnClickListener(this)


    }

    override fun onClick(item: View?) {
        when (item!!.id) {
            R.id.btn_submit -> {
                if (sessionManager.isNetworkAvailable()) {
                    if (isOtpValid()) {
                        if (type.equals("1",true)) {
                            val bundle=Bundle()
                            bundle.putString(AppConstant.EMAIL,email)
                            findNavController().navigate(R.id.resetFragment,bundle)
                        } else { // For Create Account
                            signUpAccount()
                        }
                    }
                } else {
                    sessionManager.alertError(ErrorMessage.netWorkError)
                }
            }

            R.id.btn_resend -> {
                if(sessionManager.isNetworkAvailable()){
                    if (type.equals("1",true)){
                        forgetPasswordOtp()
                    }else{
                        signupOtpSend(email)
                    }
                }else{
                    Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // This function is used for  register account
    private fun signUpAccount() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.userSingUp({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, SingUpResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    sessionManager.setCheckLogin("No")
                                    sessionManager.setEmail(email)
                                    sessionManager.setName(name)
                                    sessionManager.saveAuthToken(signUpModel.data.token)
                                    showAlertBox()
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
            },name, email,password)
        }

    }


    // This function is used for  send otp for sign up
    private fun signupOtpSend(email: String) {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.userSingUpOtp({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, SignupOtpResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    binding.edOtp.setOTP("")
                                    Log.e("Otp is", signUpModel.data.otp.toString())
                                    otp = signUpModel.data.otp.toString()
                                    startCountDown()
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
            }, email)
        }

    }

    // This function is used for send otp for forget password
    private fun forgetPasswordOtp(){
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
                                    val otpLocal = signUpModel.data.otp.toString()
                                    Log.e("OTP", otp)
                                    binding.edOtp.setOTP("")
                                    otp = otpLocal
                                    Log.e("OTP", otp)
                                    startCountDown()
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
            }, email)
        }


    }

    // This function is used for check otp
    private fun isOtpValid(): Boolean {
        val result = true
        if (binding.edOtp.otp?.isEmpty() == true) {
            sessionManager.alertError( "Please enter otp")
            return false
        } else if (!binding.edOtp.otp.equals(otp)) {
            sessionManager.alertError("Please enter the correct verification code")
            return false
        }
        return result
    }

    // This function is used for start timer
    private fun startCountDown() {
        binding.layTimer.visibility = View.VISIBLE
        binding.btnTimer1.visibility = View.VISIBLE
        binding.btnResend.visibility = View.GONE
        countDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(l: Long) {
                mTimeLeftInMillis = l
                updateCountDownText()
            }

            override fun onFinish() {
                mTimeLeftInMillis = 120000
                binding.layTimer.visibility = View.GONE
                binding.btnTimer1.visibility = View.GONE
                binding.btnResend.visibility = View.VISIBLE
            }
        }.start()

    }

    // This function is used for reset countdown
    @SuppressLint("SetTextI18n")
    private fun updateCountDownText() {
        val minutes = mTimeLeftInMillis.toInt() / 1000 / 60
        val seconds = mTimeLeftInMillis.toInt() / 1000 % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.tvTimer.text = "$timeLeftFormatted sec "
    }

    // This function is used for show dialog account successfully created
    private fun showAlertBox() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_success)
        val btnCross: LinearLayout = dialog.findViewById(R.id.btn_cross)
        val btnOk: LinearLayout = dialog.findViewById(R.id.btn_ok)

        btnCross.setOnClickListener {
            dialog.dismiss()
            moveToMainActivity()
        }

        btnOk.setOnClickListener {
            dialog.dismiss()
            moveToMainActivity()
        }

        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }


    // This function is used for open Home screen
    private fun moveToMainActivity() {
        findNavController().navigate(R.id.playerProfileFragment)
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

}