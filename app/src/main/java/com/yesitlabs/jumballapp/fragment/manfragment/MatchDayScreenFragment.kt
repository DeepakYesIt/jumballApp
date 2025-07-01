package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.yesitlabs.jumballapp.AppConstant
import com.yesitlabs.jumballapp.R

import com.yesitlabs.jumballapp.SessionManager

import com.yesitlabs.jumballapp.databinding.FragmentMatchdayScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MatchDayScreenFragment : Fragment() {


    lateinit var sessionManager: SessionManager

    private var creatureImg :String = ""

    private lateinit var binding: FragmentMatchdayScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMatchdayScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.dashBoardFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        creatureImg = requireArguments().getString("creatureImg").toString()


        binding.tvProgressBar.visibility=View.VISIBLE


        Glide.with(requireContext())
            .load("${AppConstant.STICKER_URL}$creatureImg")
            .placeholder(R.drawable.s_image)
            .error(R.drawable.s_image)
            .listener(object :RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.tvProgressBar.visibility=View.GONE
                    moveToNextScreen()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    binding.tvProgressBar.visibility=View.GONE
                    moveToNextScreen()
                    return false
                }

            })
            .into(binding.MatchScreenImg)


        Log.d("@Error","number :-  "+sessionManager.getGameNumber())

        binding.MatchNumber.text = "M A T C H D A Y   ${sessionManager.getGameNumber()}"



    }

    private fun moveToNextScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.setMatchType("worldcup")
            findNavController().navigate(R.id.chooseYourFormationFragment )
        }, 3000)
    }


}