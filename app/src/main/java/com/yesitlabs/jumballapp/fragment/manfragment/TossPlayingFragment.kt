package com.yesitlabs.jumballapp.fragment.manfragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.model.navigateSafe
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.activity.PenaltiesPlayActivity
import com.yesitlabs.jumballapp.databinding.FragmentTossPlayingBinding

class TossPlayingFragment : Fragment() {
    private var headsImageVisible = true
    private var headsImageVisible1 = true
    private var headSelect = false
    var winner = "user"
    private var type=""

    lateinit var sessionManager : SessionManager

    private lateinit var binding: FragmentTossPlayingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTossPlayingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(2,0)
        type=arguments?.getString("type","").toString()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (!type.equals("main",true)){
                        findNavController().navigate(R.id.chooseYourFormationFragment)
                    }
                }
            }

        if (arguments != null) {
            if (requireArguments().containsKey("toss")) {
                if (requireArguments().getString("toss").toString() == "head") {
                    headSelect = true
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        doFlipCoin()

    }

    // This function is used for start the coin animation bottom to top
    private fun doFlipCoin() {
        if (binding.flipButton.isEnabled) {
            binding.flipButton.isEnabled = false
            val coinAnimator = ObjectAnimator.ofFloat(binding.coinView, "rotationX", 0f, 360f)
            coinAnimator.duration = 1000
            val translateYAnimator = ObjectAnimator.ofFloat(binding.coinView, "translationY", 0f, -350f)
            translateYAnimator.duration = 1000
            translateYAnimator.interpolator = AccelerateDecelerateInterpolator()

            coinAnimator.interpolator = AccelerateDecelerateInterpolator()

            coinAnimator.addUpdateListener {
                val value = it.animatedValue as Float
                if (value >= 180f) {
                    if (headsImageVisible) {
                        binding.coinView.setImageResource(R.drawable.head_coin)
                    } else {
                        binding.coinView.setImageResource(R.drawable.tails_coin)
                    }
                }
            }

            // Chain the two animations together
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(coinAnimator, translateYAnimator)
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    headsImageVisible = !headsImageVisible
                    binding.flipButton.isEnabled = true
                    topToBottomAnimator()
                }
            })
            animatorSet.start()
        }
    }

    // This function is used for start the coin animation top to  bottom
    private fun topToBottomAnimator() {

        val coinAnimator = ObjectAnimator.ofFloat(binding.coinView, "rotationX", 0f, 360f)
        coinAnimator.duration = 1000
        val translateYAnimator = ObjectAnimator.ofFloat(binding.coinView, "translationY", -350f, 0f)
        translateYAnimator.duration = 1000
        translateYAnimator.interpolator = AccelerateDecelerateInterpolator()
        coinAnimator.interpolator = AccelerateDecelerateInterpolator()


        // Chain the two animations together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(coinAnimator, translateYAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val randomNumber = (1..2).random()
                if (randomNumber % 2 == 0) {
                    winner = if (headSelect) {
                        "user"
                    } else {
                        "cpu"
                    }
                    headsImageVisible1 = true
                } else {
                    //println("odd"+random_number)
                    headsImageVisible1 = false
                    winner = if (!headSelect) {
                        "user"
                    } else {
                        "cpu"
                    }
                }

                if (headsImageVisible1) {
                    binding.coinView.setImageResource(R.drawable.head_coin)
                    binding.imgChange.setImageResource(R.drawable.head_win)
                    moveToPlayScreen()
                } else {
                    binding.coinView.setImageResource(R.drawable.tails_coin)
                    binding.imgChange.setImageResource(R.drawable.tails_win)
                    moveToPlayScreen()
                }
            }
        })
        animatorSet.start()

    }

    // This function is used for open play screen
    private fun moveToPlayScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
            Log.e("Winner", winner)
            if (!type.equals("main",true)){
                playAlertBox()
            }else{
                val bundle=Bundle()
                bundle.putString("userType","winner")
                findNavController().navigate(R.id.penaltiesPlayUserFragment,bundle)
            }
        }, 1000)

    }

    // This function is used for display toss result
    private fun playAlertBox() {
        sessionManager.changeMusic(19,0)
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        imgChange.setImageResource(R.drawable.kick_off_img)

        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.changeMusic(1,1)
            dialog.dismiss()
        }, 2000)

        Handler(Looper.myLooper()!!).postDelayed({
            Log.e("Winner", winner)
            val bundle = Bundle()
            bundle.putString("userType", "user"/*winner*/)
            findNavController().navigate(R.id.playScreenFragment, bundle)
        }, 1000)
        dialog.show()
    }


}