package com.jumball.app.fragment.manfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jumball.app.R
import com.jumball.app.SessionManager
import com.jumball.app.databinding.FragmentTraningPlayerInfoBinding

class TrainingPlayerInfoFragment : Fragment() {
    private lateinit var binding: FragmentTraningPlayerInfoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTraningPlayerInfoBinding.inflate(inflater, container, false)

        return binding.root
    }
    var status:Int=0
    lateinit var sessionManager : SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(0,1)
        backButton()
        binding.btnBack.setOnClickListener {
            sessionManager.playClickSound()
            onBack()
        }
        binding.btnNext.setOnClickListener {
            sessionManager.playClickSound()
            onNext()
        }
    }
    private fun backButton(){
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    // This function is used for open next image
    private fun onNext() {
        status += 1
        val imageRes = when (status) {
            1 -> R.drawable.training_data_show
            2 -> R.drawable.training_three
            3 -> R.drawable.training_four
            4 -> R.drawable.training_five
            5 -> R.drawable.training_saven
            6 -> R.drawable.tarining_eight
            7 -> R.drawable.training_six
            else -> null
        }
        if (status == 3) {
            binding.layShoot.visibility = View.VISIBLE
        } else if (status in 1..7) {
            binding.layShoot.visibility = View.GONE
        }
        imageRes?.let { binding.img.setBackgroundResource(it) }
        if (status == 8) {
            findNavController().navigate(R.id.dashBoardFragment)
        }
    }

    // This function is used for open previous image
    private fun onBack() {
        status -= 1
        if (status == -1) {
            findNavController().navigateUp()
            return
        }
        binding.layShoot.visibility = if (status == 3) View.VISIBLE else View.GONE
        val imageRes = when (status) {
            0 -> R.drawable.choose_formation_screen
            1 -> R.drawable.training_data_show
            2 -> R.drawable.training_three
            3 -> R.drawable.training_four
            4 -> R.drawable.training_five
            5 -> R.drawable.training_saven
            6 -> R.drawable.tarining_eight
            else -> null
        }
        imageRes?.let { binding.img.setBackgroundResource(it) }
    }
}