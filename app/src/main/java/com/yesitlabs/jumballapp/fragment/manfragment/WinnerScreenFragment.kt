package com.yesitlabs.jumballapp.fragment.manfragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.model.navigateSafe
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentWinerScreenBinding


class WinnerScreenFragment : Fragment() {

    lateinit var sessionManager: SessionManager


    private lateinit var binding: FragmentWinerScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWinerScreenBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())


        sessionManager.changeMusic(1,1)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().navigateSafe(R.id.action_winerScreenFragment_to_dashBoardFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.imgChange.setImageResource(R.drawable.full_time)
        Handler(Looper.myLooper()!!).postDelayed({
            if (sessionManager.getCheckLogin().equals("2")){
                val intent=Intent(context, FinalScoreFragment::class.java)
                startActivity(intent)
            }else {
                binding.imgChange.setImageResource(R.drawable.winner_img)
            }
        },3000)
    }

}