package com.jumball.app.fragment.manfragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.gson.Gson
import com.jumball.app.AppConstant
import com.jumball.app.R
import com.jumball.app.SessionManager
import com.jumball.app.databinding.FragmentShowingCardBinding
import com.jumball.app.errormassage.ErrorMessage
import com.jumball.app.network.NetworkResult
import com.jumball.app.viewModel.gameapi.stickerViewModel.cericatureResp
import com.jumball.app.viewmodeljumball.CaricatureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShowingCardFragment : Fragment() {

    lateinit var sessionManager: SessionManager
    private lateinit var viewModel: CaricatureViewModel
    private lateinit var binding: FragmentShowingCardBinding
    // Declare these at class level (or where appropriate)
    private var handler: Handler ? = null
    private lateinit var runnable: Runnable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowingCardBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(0,1)
        viewModel = ViewModelProvider(requireActivity())[CaricatureViewModel::class.java]

        backButton()

        if (sessionManager.isNetworkAvailable()) {
            getCaricature()
        } else {
            Toast.makeText(requireContext(),ErrorMessage.netWorkError,Toast.LENGTH_SHORT).show()
        }

    }

    private fun backButton(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    handler?.removeCallbacks(runnable ?: return)
                    findNavController().navigate(R.id.dashBoardFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    // This is used for get caricature image from database
    private fun getCaricature() {
        binding.loader.visibility = View.VISIBLE
        val matchNo = sessionManager.getGameNumber()-1
        lifecycleScope.launch {
            viewModel.getCaricature({
                binding.loader.visibility = View.GONE
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, cericatureResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    val creatureImg= model.data?.get(0)?.image
                                    binding.loader.visibility =View.VISIBLE
                                    Glide.with(requireContext())
                                        .load("${AppConstant.STICKER_URL}$creatureImg")
                                        .placeholder(R.drawable.s_image)
                                        .error(R.drawable.s_image)
                                        .listener(object : RequestListener<Drawable> {
                                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                                binding.loader.visibility =View.GONE
                                                moveToNextScreen()
                                                return false
                                            }

                                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                                binding.loader.visibility =View.GONE
                                                moveToNextScreen()
                                                return false
                                            }
                                        })
                                        .into(binding.friendlyicon)
                                }catch (e:Exception){
                                    Log.d("signup","message:---"+e.message)
                                }
                            } else {
                                sessionManager.alertError(model.message)
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
            }, matchNo.toString() )
        }

    }

    private fun moveToNextScreen() {
        handler = Handler(Looper.myLooper()!!)
        runnable = Runnable {
            sessionManager.setMatchType("friendly")
            sessionManager.resetScore()
            sessionManager.resetGameNumberScore()
            findNavController().navigate(R.id.chooseYourFormationFragment)
        }
        handler?.postDelayed(runnable, 3000)
    }

}