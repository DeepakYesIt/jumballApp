package com.yesitlabs.jumballapp.fragment.manfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.BaseApplication
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentTraningPlayerInfoBinding

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


        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnBack.setOnClickListener(View.OnClickListener {
            sessionManager.playClickSound()
            onBack()
        })

        binding.btnNext.setOnClickListener(View.OnClickListener {
            sessionManager.playClickSound()
            onNext()
        })

    }

    // This function is used for open next image
    private fun onNext() {
        status += 1
        if (status==1){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_data_show)
        }
        if (status==2){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_three)
        }
        if (status==3){
            binding.layShoot.visibility=View.VISIBLE
            binding.img.setBackgroundResource(R.drawable.training_four)
        }
        if (status==4){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_five)
        }
        if (status==5){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_saven)
        }
        if (status==6){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.tarining_eight)
        }
        if (status==7){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_six)
        }
        if (status==8){
            findNavController().navigate(R.id.dashBoardFragment)
        }
    }

    // This function is used for open previous image
    private fun onBack() {
        status -= 1
        if (status==-1){
            findNavController().navigateUp()
        }

        if (status==0){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.choose_formation_screen)
        }

        if (status==1){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_data_show)
        }
        if (status==2){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_three)
        }
        if (status==3){
            binding.layShoot.visibility=View.VISIBLE
            binding.img.setBackgroundResource(R.drawable.training_four)
        }
        if (status==4){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_five)
        }
        if (status==5){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.training_saven)
        }
        if (status==6){
            binding.layShoot.visibility=View.GONE
            binding.img.setBackgroundResource(R.drawable.tarining_eight)
        }
    }

}