package com.yesitlabs.jumballapp.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.ActivityTossPlayingBinding


class TossPlayingActivity : AppCompatActivity() {

    private var headsImageVisible = true
    private var headsImageVisible1 = true
    private var headSelect = false
    var winner = "user"

    lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityTossPlayingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTossPlayingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@TossPlayingActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)


        sessionManager.changeMusic(2,0)

        if (intent.getStringExtra("toss") != null){
            if (intent.getStringExtra("toss").equals("head")) {
                headSelect = true
            }
        }


        doFlipCoin()
    }


    // This function is used for start the toss (Flip the coin)
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

    // This function is used for start the animation top to bottom
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

    // This function is used for open game screen
    private fun moveToPlayScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
            playAlertBox()
        }, 1000)

    }

    // This function is used for display toss result
    private fun playAlertBox() {
        sessionManager.changeMusic(19,0)
        val dialog = Dialog(this, R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        imgChange.setImageResource(R.drawable.kick_off_img)

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 2000)

        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.changeMusic(20,1)
            val intent = Intent(this, PenaltiesPlayActivity::class.java)
            intent.putExtra("userType",winner)
            startActivity(intent)
        }, 1000)

        dialog.show()
    }



}