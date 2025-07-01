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
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentTermsAndConditionBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.termAndCondion.Term_condition_resp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.TermsAndPrivacyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TermsAndConditionFragment : Fragment() {
    private lateinit var binding: FragmentTermsAndConditionBinding
    lateinit var sessionManager: SessionManager
    var token: String? = null
    private lateinit var viewModel: TermsAndPrivacyViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTermsAndConditionBinding.inflate(inflater, container, false)

        return binding.root
    }





    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TermsAndPrivacyViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        if(sessionManager.isNetworkAvailable()){
            getTermCondition()
        }else{
            Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

    }

    // This function is used for get and set term and condition from server to screen
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getTermCondition() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.getTermAndCondition {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, Term_condition_resp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        Html.fromHtml(signUpModel.data.termandcondition.description, Html.FROM_HTML_MODE_LEGACY)
                                    } else {
                                        Html.fromHtml(signUpModel.data.termandcondition.description, Html.FROM_HTML_MODE_LEGACY)
                                    }
                                    binding.termConditionText.text = spanned
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

}