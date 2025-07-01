package com.yesitlabs.jumballapp.fragment.manfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.model.navigateSafe
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentPitchAdvertisingBinding

class PitchAdvertisingFragment : Fragment(R.layout.fragment_pitch_advertising) {

    lateinit var sessionManager : SessionManager

    private lateinit var binding: FragmentPitchAdvertisingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPitchAdvertisingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         sessionManager = SessionManager(requireContext())

        sessionManager.changeMusic(0,1)


        inPlayScreen()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().navigateSafe(R.id.action_pitchAdvertisingFragment_to_chooseYourFormationFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)



    }


    // This function used form toss screen
    private fun inPlayScreen() {
        findNavController().navigateSafe(R.id.action_pitchAdvertisingFragment_to_selectTossFragment)
    }

}