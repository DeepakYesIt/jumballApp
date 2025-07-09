package com.jumball.app.fragment.manfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jumball.app.R
import com.jumball.app.SessionManager
import com.jumball.app.databinding.FragmentTrainingBinding



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
        binding.btnNext.setOnClickListener {
            sessionManager.playClickSound()
            findNavController().navigate(R.id.traningPlayerInfoFragment)
        }

    }
}