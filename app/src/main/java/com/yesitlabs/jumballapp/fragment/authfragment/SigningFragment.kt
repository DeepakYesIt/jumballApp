package com.yesitlabs.jumballapp.fragment.authfragment


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.databinding.FragmentSigninBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.Setting
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@AndroidEntryPoint
class SigningFragment : Fragment(R.layout.fragment_signin), View.OnClickListener {

    private var mGoogleSignInClient: GoogleSignInClient? = null

    lateinit var sessionManager: SessionManager
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var token = ""
    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)

        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        logOutGoogle()



        binding.btnSkip.setOnClickListener(this)
        binding.btnForget.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)
        binding.btnGmail.setOnClickListener(this)
        binding.btnHideSi.setOnClickListener(this)
        binding.btnShowSi.setOnClickListener(this)


        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                try {
                    Log.e("Result Code " , result.toString())
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    if (account != null) {
                        if (sessionManager.isNetworkAvailable()) {
                            socialLogin(account.displayName!!, account.email!!, token)
                        } else {
                            sessionManager.alertError(ErrorMessage.netWorkError)
                        }
                    }else{
                        logOutGoogle()
                        Log.e("result Error" , "******")
                    }
                }catch (e : Exception){
                    logOutGoogle()
                    Log.e("result Error" , "******")
                }
            } else {
                logOutGoogle()
                Log.e("result Error" , "******")
            }
        }

        signUpButtonClickable()

        // This line use for system back button
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

    }

    fun logOutGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient!!.signOut()
    }

    override fun onClick(item: View) {

        sessionManager.playClickSound()

        when (item.id) {
            R.id.btn_skip -> {
                sessionManager.setCheckLogin("no")
                moveToMainActivity()
            }

            R.id.btn_forget -> {
//                findNavController().navigate(R.id.action_signinFragment_to_forgetPasswordFragment)
                findNavController().navigate(R.id.forgetPasswordFragment)
            }

            R.id.btn_signin -> {
                if (isValidation()) {
                    if (sessionManager.isNetworkAvailable()) {
                        loginResp()
                    } else {
                        sessionManager.alertError(ErrorMessage.netWorkError)
                    }

                }
            }

            R.id.btn_gmail -> {
                gmailLogin()
            }


            R.id.btn_hide_si -> {
                binding.btnShowSi.visibility = View.VISIBLE
                binding.btnHideSi.visibility = View.GONE
                binding.edPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.edPass.transformationMethod = HideReturnsTransformationMethod.getInstance()

            }

            R.id.btn_show_si -> {
                binding.edPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding. btnShowSi.visibility = View.GONE
                binding.btnHideSi.visibility = View.VISIBLE
                binding.edPass.transformationMethod = HideReturnsTransformationMethod.getInstance()

            }

        }
    }

    // This function is used for  sign up page open
    private fun signUpButtonClickable(){

        val spannedString = SpannableString(getString(R.string.create_an_account))

        val signUpStart = object : ClickableSpan() {

            override fun onClick(p0: View) {
//                findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
                findNavController().navigate(R.id.signupFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.hint_color_worng)
                ds.isUnderlineText = false
            }

        }


        val signUpStartIndex = spannedString.toString().indexOf("Sign Up")
        val signUpEndIndex = signUpStartIndex + "Sign Up".length


        spannedString.setSpan(
            signUpStart,
            signUpStartIndex,
            signUpEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.btnSignUpOpen.text = spannedString
        binding.btnSignUpOpen.movementMethod = LinkMovementMethod.getInstance()
    }

    // This function is used for  user social login
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

    // This function is used for user login
    private fun loginResp(){
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.login({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, LoginResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    sessionManager.setCheckLogin("yes")
                                    sessionManager.setEmail(binding.edEmail.text.toString())
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
            }, binding.edEmail.text.toString(),binding.edPass.text.toString())
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

    // This function is used for validate all login details of user
    private fun isValidation(): Boolean {
        val result = true
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"
        val emailString = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val patternEmail = Pattern.compile(emailString)
        val pattern = Pattern.compile(passwordPattern)
        val passMatcher = pattern.matcher(binding.edPass.text.toString().trim())
        val email = patternEmail.matcher(binding.edEmail.text.toString().trim())

        if (binding.edEmail.text.toString().isEmpty()) {
            sessionManager.alertError( "Please enter your registered email")
            return false
        } else if (!email.matches()) {
            sessionManager.alertError( "Invalid Email")
            return false
        } else if (binding.edPass.text.toString().trim().isEmpty()) {
            sessionManager.alertError( "Please enter password")
            return false
        } else if (!passMatcher.find()) {
            sessionManager.alertError(
                
                "Password should be in 8 characters and should contain at least one numeric value, alphabets and one special character."
            )
            return false
        }
        return result
    }

    // This function is used for get user setting details from database
    private fun setting() {
//        binding.progressLogin.visibility = View.VISIBLE
//        val token: String = "Bearer " + sessionManager.fetchAuthToken()
//        loginViewModel.setting({
//            when (it) {
//                is NetworkResult.Success -> {
//                    binding.progressLogin.visibility = View.GONE
//                    if (it.data?.data?.setting?.music == 1) {
//                        sessionManager.setMusic(1)
//                    } else {
//                        sessionManager.setMusic(0)
//                    }
//
//                    if (it.data?.data?.setting?.sound_effect == 1) {
//                        sessionManager.setSoundEffect(1)
//                    } else {
//                        sessionManager.setSoundEffect(0)
//                    }
//                    sessionManager.setCheckLogin("yes")
//                    moveToMainActivity()
//                }
//
//                is NetworkResult.Error -> {
//                    binding.progressLogin.visibility = View.GONE
//                    Log.e("Setting Error",it.message.toString())
//                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
//                }
//
//                else -> {
//                    Log.e("Setting Error",it.message.toString())
//                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
//                    binding.progressLogin.visibility = View.GONE
//                }
//            }
//        }, token)
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

    // This function is used for open home Screen
    private fun moveToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    // This function is used for get device token of firebase
    private fun getDeviceToken(){
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

    override fun onResume() {
        super.onResume()
        getDeviceToken()
    }

}

