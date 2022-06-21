package com.peter.restaurantsmap

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {
            val testDeviceIds = Arrays.asList("BE3125C50084B0C0E539B7A449F81FBF")
            val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)
        }
    }
    override fun onBackPressed() {
        // Move the task containing the MainActivity to the back of the activity stack, instead of
        // destroying it. Therefore, MainActivity will be shown when the user switches back to the app.
        moveTaskToBack(true)
    }
}