package com.yesitlabs.jumballapp.fragment.authfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
import com.yesitlabs.jumballapp.databinding.FragmentResetBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@AndroidEntryPoint
class ResetFragment : Fragment(),View.OnClickListener {

    lateinit var email:String
    lateinit var sessionManager : SessionManager
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentResetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResetBinding.inflate(inflater, container, false)

        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        email= requireArguments().getString(AppConstant.EMAIL).toString()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.signinFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnSubmit.setOnClickListener(this)
        binding.btnHide.setOnClickListener(this)
        binding.btnShow.setOnClickListener(this)
        binding.btnCnfhide.setOnClickListener(this)
        binding.btnCnfshow.setOnClickListener(this)
    }

    override fun onClick(item: View?) {
        when(item!!.id){
            R.id.btn_submit->{
                if (isValidation()){
                    if(sessionManager.isNetworkAvailable()){
                        resetPassword()
                    }else{
                        Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.btn_hide -> {
                binding.btnShow.visibility= View.VISIBLE
                binding.btnHide.visibility= View.GONE
                binding.edPassSu.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

            R.id.btn_show -> {
                binding.edPassSu.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnShow.visibility= View.GONE
                binding.btnHide.visibility= View.VISIBLE
            }

            R.id.btn_cnfhide -> {
                binding.btnCnfshow.visibility= View.VISIBLE
                binding.btnCnfhide.visibility= View.GONE
                binding.edCnfpass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

            R.id.btn_cnfshow -> {
                binding.edCnfpass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnCnfshow.visibility= View.GONE
                binding.btnCnfhide.visibility= View.VISIBLE
            }

        }
    }


    private fun resetPassword(){
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.resetPassword({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, LoginResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
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
            }, email,binding.edPassSu.text.toString(),binding.edCnfpass.text.toString())
        }
    }

    // This function is used for check password validation
    private fun isValidation(): Boolean {

        val passwordPattern = ErrorMessage.passwordPattern
        val pattern = Pattern.compile(passwordPattern)
        val passwordMatcher = pattern.matcher(binding.edPassSu.text.toString().trim())
        if (binding.edPassSu.text.toString().isEmpty()) {
            sessionManager.alertError(ErrorMessage.passwordError)
            return false
        }else if (!passwordMatcher.find()) {
            sessionManager.alertError(ErrorMessage.passwordValidationError)
            return false
        }else if (binding.edCnfpass.text.toString().isEmpty()) {
            sessionManager.alertError(ErrorMessage.cnfPasswordError)
            return false
        }else if (binding.edPassSu.text.toString() != binding.edCnfpass.text.toString()) {
            sessionManager.alertError(ErrorMessage.passwordValidationError)
            return false
        }
        return true
    }

    // This function is used for display the alert box of password successfully change
    @SuppressLint("SetTextI18n")
    private fun showAlertBox() {
        val dialog= Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_success)

        val btnCross: LinearLayout=dialog.findViewById(R.id.btn_cross)
        val btnOk:LinearLayout =dialog.findViewById(R.id.btn_ok)
        val tvChange: TextView=dialog.findViewById(R.id.tv_change)

        tvChange.text="Your password has been changed\nsuccessfully!"

        btnCross.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            findNavController().navigate(R.id.signinFragment)
        }

        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

}