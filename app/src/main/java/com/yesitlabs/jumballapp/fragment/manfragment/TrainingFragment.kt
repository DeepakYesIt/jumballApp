package com.yesitlabs.jumballapp.fragment.manfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.BaseApplication
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentTrainingBinding



class TrainingFragment : Fragment() {
    private lateinit var binding: FragmentTrainingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTrainingBinding.inflate(inflater, container, false)

        return binding.root
    }

    lateinit var sessionManager : SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        binding.btnNext.setOnClickListener(View.OnClickListener {
            sessionManager.playClickSound()
//            findNavController().navigate(R.id.action_trainingFragment_to_traningPlayerInfoFragment)
            findNavController().navigate(R.id.traningPlayerInfoFragment)
        })

    }
}