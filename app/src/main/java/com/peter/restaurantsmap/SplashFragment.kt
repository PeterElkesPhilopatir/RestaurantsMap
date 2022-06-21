package com.peter.restaurantsmap

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.peter.restaurantsmap.databinding.FragmentSplashBinding

private const val COUNTER_TIME = 3L

class SplashFragment : Fragment() {
private lateinit var binding : FragmentSplashBinding
    private var secondsRemaining: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater)
        createTimer(COUNTER_TIME)
        return binding.root
    }

    private fun createTimer(seconds: Long) {


        val countDownTimer: CountDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000 + 1
            }

            override fun onFinish() {
                secondsRemaining = 0

                val application = App() as? App

                // If the application is not an instance of MyApplication, log an error message and
                // start the MainActivity without showing the app open ad.
                if (application == null) {
                    Log.e("Ad", "Failed to cast application to MyApplication.")
                    NavigateToMainFragment()
                    return
                }

                // Show the app open ad.
                application.showAdIfAvailable(
                    requireActivity(),
                    object : App.OnShowAdCompleteListener {
                        override fun onShowAdComplete() {
                            NavigateToMainFragment()
                        }
                    })
            }
        }
        countDownTimer.start()
    }

    private fun NavigateToMainFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
    }

}