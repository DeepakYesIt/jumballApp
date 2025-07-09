package com.jumball.app.fragment.manfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jumball.app.R
import com.jumball.app.SessionManager
import com.jumball.app.database.team_dtl.TeamDatabaseHelper
import com.jumball.app.databinding.FragmentDashBoardBinding


class DashBoardFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDashBoardBinding
    lateinit var sessionManager: SessionManager

    private lateinit var teamDbHelper: TeamDatabaseHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        Log.e("Match No.", sessionManager.getGameNumber().toString())

        binding.layWorldCup.setOnClickListener(this)
        binding.layTraining.setOnClickListener(this)
        binding.layFriendly.setOnClickListener(this)
        binding.layStickerBook.setOnClickListener(this)
        binding.laySettings.setOnClickListener(this)
        binding.layStatistics.setOnClickListener(this)

        // This line use for system back button
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })


    }

    override fun onClick(item: View?) {

        sessionManager.playClickSound()


        when (item!!.id) {
            R.id.lay_world_cup -> {
                Log.d("@Error","number :-  "+sessionManager.getGameNumber())
                sessionManager.setFirstGamgeStartUser(false)
                sessionManager.setFirstGamgeStartCPU(false)
                sessionManager.setGameGameCondition(1)
                teamDbHelper = TeamDatabaseHelper(requireContext())
                teamDbHelper.deleteAllTeams()
                if (sessionManager.getCheckLogin().equals("yes",true)) {
                    findNavController().navigate(R.id.groupFragment)
                } else {
                    sessionManager.alertError("Login First")
                }
            }

            R.id.lay_training -> {
                findNavController().navigate(R.id.trainingFragment)
            }

            R.id.lay_friendly -> {
                Glide.get(requireContext()).clearMemory()
                sessionManager.setFirstGamgeStartUser(false)
                sessionManager.setFirstGamgeStartCPU(false)
                if (sessionManager.getCheckLogin().equals("yes",true)) {
                    findNavController().navigate(R.id.Matchdayscreen_fragment)
                } else {
                    sessionManager.alertError("Login First")
                }
            }

            R.id.lay_sticker_book -> {
                findNavController().navigate(R.id.stickerbookFragment)
            }

            R.id.lay_settings -> {
                findNavController().navigate(R.id.settingsFragment)
            }

            R.id.lay_statistics -> {
                findNavController().navigate(R.id.statisticsFragment)
            }
        }
    }

}