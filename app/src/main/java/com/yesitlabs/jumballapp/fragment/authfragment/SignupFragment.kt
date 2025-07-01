package com.yesitlabs.jumballapp.fragment.authfragment


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.yesitlabs.jumballapp.AlertBase
import com.yesitlabs.jumballapp.AppConstant
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.databinding.FragmentSignupBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.Setting
import com.yesitlabs.jumballapp.model.SignupOtpResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@AndroidEntryPoint
class SignupFragment : Fragment(), View.OnClickListener {

    lateinit var type: String
    private var checkType: String = "1"
    private var token = ""
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var viewModel: SignUpViewModel
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    lateinit var sessionManager : SessionManager


    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        signUpViewmodel = ViewModelProvider(this)[SignUpViewmodel::class.java]
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        sessionManager = SessionManager(requireContext())


        logOutGoogle()

        binding.btnSkip.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)
        binding.btnGmail.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
        binding.btnHide.setOnClickListener(this)
        binding.btnShow.setOnClickListener(this)
        binding.btnTac.setOnClickListener(this)
        binding.checkUncheck.setOnClickListener(this)
        binding.checkCheck.setOnClickListener(this)


        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                try {
                    Log.e("Result Code " , result.toString())
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    if (account != null) {
                        if (sessionManager.isNetworkAvailable()) {
                            socialLogin(account.displayName.toString(), account.email.toString(), token)
                        } else {
                            sessionManager.alertError(ErrorMessage.netWorkError)
                        }
                    }else{
                        logOutGoogle()
                        Log.e("account Error" , "******")
                    }
                }catch (e : Exception){
                    logOutGoogle()
                     Log.e("Auth Error" , e.message.toString())
                }
            } else {
                logOutGoogle()
                Log.e("result Error" , "******")
            }
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.signinFragment)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    fun logOutGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient!!.signOut()
    }



    override fun onClick(item: View?) {
        when (item!!.id) {
            R.id.btn_skip -> {
                sessionManager.setCheckLogin("no")
                moveToMainActivity()
            }
            R.id.btn_tac -> {
                findNavController().navigate(R.id.termsAndConditionFragment2)
            }
            R.id.btn_signin -> {
                findNavController().navigate(R.id.signinFragment)
            }
            R.id.btn_gmail -> {
                if (sessionManager.isNetworkAvailable()) {
                    gmailLogin()
                } else {
                    sessionManager.alertError(ErrorMessage.netWorkError)
                }
            }
            R.id.btn_signup -> {
                if (isValidation()) {
                    if (sessionManager.isNetworkAvailable()) {
                        sendOtp()
                    } else {
                        sessionManager.alertError(ErrorMessage.netWorkError)
                    }
                }
            }

            R.id.btn_hide -> {
                binding.edPassSu.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.edPassSu.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnShow.visibility = View.VISIBLE
                binding.btnHide.visibility = View.GONE
            }

            R.id.btn_show -> {
                binding.edPassSu.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.edPassSu.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnShow.visibility = View.GONE
                binding.btnHide.visibility = View.VISIBLE
            }

            R.id.check_uncheck -> {
                checkType = "2"
                binding.checkUncheck.visibility = View.GONE
                binding.checkCheck.visibility = View.VISIBLE
            }

            R.id.check_check -> {
                checkType = "1"
                binding.checkUncheck.visibility = View.VISIBLE
                binding.checkCheck.visibility = View.GONE
            }
        }

    }

    // This function is used for send sign up otp
    private fun sendOtp(){
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
                                        val bundle = Bundle()
                                        bundle.putString("type", "2")
                                        bundle.putString(AppConstant.NAME, binding.edFullname.text.toString().trim())
                                        bundle.putString(AppConstant.EMAIL, binding.edSignupEmail.text.toString().trim())
                                        bundle.putString(AppConstant.PASSWORD, binding.edPassSu.text.toString().trim())
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
                }, binding.edSignupEmail.text.toString())
        }

    }

    // This function is used for start gmail login
    private fun gmailLogin() {

        mGoogleSignInClient = GoogleSignIn.getClient(
            requireActivity(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firbase_webclient_id)).requestEmail()
                .build()
        )

        val signInIntent: Intent = mGoogleSignInClient!!.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    // This function is used for user social login
    private fun socialLogin(displayName: String, email: String, fcmToken: String) {
        logOutGoogle()
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.socialLogin({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, LoginResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    sessionManager.setCheckLogin("yes")
                                    sessionManager.setEmail(email)
                                    sessionManager.setName(signUpModel.data.name)
                                    sessionManager.saveAuthToken(signUpModel.data.token)
                                    val token: String = "Bearer " + sessionManager.fetchAuthToken()
                                    Log.d("********", token)
                                    if (signUpModel.data.is_profile_update==0){
                                        sessionManager.setCheckLogin("no")
                                        findNavController().navigate(R.id.playerProfileFragment)
                                    }else{
                                        setting()
                                    }
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
            }, displayName,email,fcmToken)
        }
    }

    // This function is used for get user setting details from database
    private fun setting() {
       /* sessionManager.showMe(requireContext())
        val token: String = "Bearer " + sessionManager.fetchAuthToken()
        signUpViewmodel.setting({
            sessionManager.dismissMe()
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data?.data?.setting?.music == 1) {
                        sessionManager.setMusic(1)
                    } else {
                        sessionManager.setMusic(0)
                    }
                    if (it.data?.data?.setting?.sound_effect == 1) {
                        sessionManager.setSoundEffect(1)
                    } else {
                        sessionManager.setSoundEffect(0)
                    }
                    moveToMainActivity()
                }
                is NetworkResult.Error -> {
                    Log.e("Setting Error",it.message.toString())
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                }
                else -> {
                    sessionManager.dismissMe()
                }
            }
        }, token)*/
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.setting {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, Setting::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    if (signUpModel.data.setting.music == 1) {
                                        sessionManager.setMusic(1)
                                    } else {
                                        sessionManager.setMusic(0)
                                    }
                                    if (signUpModel.data.setting.sound_effect == 1) {
                                        sessionManager.setSoundEffect(1)
                                    } else {
                                        sessionManager.setSoundEffect(0)
                                    }
                                    sessionManager.setCheckLogin("yes")
                                    moveToMainActivity()
                                } catch (e: Exception) {
                                    Log.d("signup", "message:---" + e.message)
                                }
                            } else {
                                sessionManager.alertError(signUpModel.message)
                            }
                        } catch (e: Exception) {
                            Log.d("signup", "message:---" + e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }
        }
    }

    // This function is used for validate all login details of user
    private fun isValidation(): Boolean {
        val patternEmail = Pattern.compile(ErrorMessage.emailPattern)
        val pattern = Pattern.compile(ErrorMessage.passwordPattern)
        val passMatcher = pattern.matcher(binding.edPassSu.text.toString().trim())
        val email = patternEmail.matcher(binding.edSignupEmail.text.toString().trim())
        if (binding.edFullname.text.toString().isEmpty()) {
            sessionManager.alertError(ErrorMessage.nameError)
            return false
        } else if (binding.edSignupEmail.text.toString().isEmpty()) {
            sessionManager.alertError( ErrorMessage.emailError)
            return false
        } else if (!email.matches()) {
            sessionManager.alertError( ErrorMessage.emailValidationError)
            return false
        } else if (binding.edPassSu.text.toString().trim().isEmpty()) {
            sessionManager.alertError(ErrorMessage.passwordError)
            return false
        } else if (!passMatcher.find()) {
            sessionManager.alertError(ErrorMessage.passwordValidationError)
            return false
        } else if (checkType == "1") {
            sessionManager.alertError( ErrorMessage.termsError)
            return false
        }
        return true
    }

    // This function is used for open home Screen
    private fun moveToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        getDeviceToken()
    }

    // This function is used for get device token of firebase
    private fun getDeviceToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.w("LogIn", "Fetching FCM registration token failed", task.exception)
                    token = task.result
                    // Log and toast
                    Log.d("LogIn", "Fcm token$token")
                }else{
                    token=""
                }
            }
        }catch (e:Exception){
            token=""
        }
     }
}