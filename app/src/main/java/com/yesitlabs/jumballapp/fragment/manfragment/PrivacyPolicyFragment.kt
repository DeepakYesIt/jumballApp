package com.yesitlabs.jumballapp.fragment.manfragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.BaseApplication
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentPrivacyPolicyBinding
import com.yesitlabs.jumballapp.databinding.FragmentProfileBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.privacyPolicy.PrivacyPolicyResp
import com.yesitlabs.jumballapp.model.termAndCondion.Term_condition_resp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.viewModel.GetPrivacyPolicyViewModel
import com.yesitlabs.jumballapp.viewmodeljumball.TermsAndPrivacyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PrivacyPolicyFragment : Fragment() , View.OnClickListener{

    private lateinit var getPrivacyPolicyViewmodel: GetPrivacyPolicyViewModel

    var token: String? = null
    lateinit var sessionManager: SessionManager

    private lateinit var binding: FragmentPrivacyPolicyBinding
    private lateinit var viewModel: TermsAndPrivacyViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)

        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this)[TermsAndPrivacyViewModel::class.java]
        getPrivacyPolicyViewmodel = ViewModelProvider(this)[GetPrivacyPolicyViewModel::class.java]



        if (sessionManager.isNetworkAvailable()) {
            getPrivacyPolicy()
        } else {
            sessionManager.alertError(ErrorMessage.netWorkError)
        }



        binding.btnBack.setOnClickListener(this)
    }

    // This function is used for get and set privacy policy content form server to screen
    @RequiresApi(Build.VERSION_CODES.N)
    fun getPrivacyPolicy() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.getPrivacyAndPolicy {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, PrivacyPolicyResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        Html.fromHtml(signUpModel.data.privacypolicy.description, Html.FROM_HTML_MODE_LEGACY)
                                    } else {
                                        Html.fromHtml(signUpModel.data.privacypolicy.description, Html.FROM_HTML_MODE_LEGACY)
                                    }
                                    binding.privacyPolicyTxtText.text = spanned
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



    override fun onClick(v: View?) {
        sessionManager.playClickSound()
        when(v!!.id){
            R.id.btn_back ->{
                findNavController().navigateUp()
            }
        }
    }

}