package com.yesitlabs.jumballapp.fragment.manfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.BaseApplication
import com.yesitlabs.jumballapp.model.navigateSafe
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentSelectTossBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectTossFragment : Fragment() {

    lateinit var sessionManager : SessionManager
    private var type=""

    private lateinit var binding: FragmentSelectTossBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectTossBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layRoot.setBackgroundResource(R.drawable.authbackground)

        sessionManager = SessionManager(requireContext())
        type=arguments?.getString("type","").toString()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                if (!type.equals("main",true)){
                    findNavController().navigate(R.id.chooseYourFormationFragment)
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnHead.setOnClickListener {
            onTossSelected("head")
        }

        binding.btnTails.setOnClickListener {
            onTossSelected("tail")
        }
    }

    private fun onTossSelected(toss: String) {
        sessionManager.playClickSound()
        val isHead = toss == "head"
        binding.card1.setBackgroundResource(if (isHead) R.drawable.active_bg else R.drawable.in_active_bg)
        binding.card2.setBackgroundResource(if (isHead) R.drawable.in_active_bg else R.drawable.active_bg)
        binding.tvHead.setTextColor(Color.parseColor(if (isHead) "#FFFFFF" else "#000000"))
        binding.tvTails.setTextColor(Color.parseColor(if (isHead) "#000000" else "#FFFFFF"))
        val bundle = Bundle().apply {
            putString("toss", toss)
            putString("type", type)
        }
        findNavController().navigate(R.id.tossPlayingFragment, bundle)
    }

}