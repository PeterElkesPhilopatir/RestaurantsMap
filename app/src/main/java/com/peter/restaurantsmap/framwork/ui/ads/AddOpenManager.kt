package com.peter.restaurantsmap.framwork.ui.ads

import androidx.lifecycle.Lifecycle.Event.ON_START


import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.peter.restaurantsmap.App


/** Prefetches App Open Ads.  */
class AppOpenManager(myApplication: App) : LifecycleObserver,Application.ActivityLifecycleCallbacks{
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private val myApplication: App
    private var currentActivity: Activity? = null
    private var isShowingAd = false

    /** Request an ad  */
    fun fetchAd() {
        // We will implement this below.
    }

    /** Creates and returns ad request.  */
    private val adRequest: AdRequest
        private get() = AdRequest.Builder().build()

    /** Utility method that checks if ad exists and can be shown.  */
    val isAdAvailable: Boolean
        get() = appOpenAd != null

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private const val AD_UNIT_ID = "ca-app-pub-2559564046715323/2204952129"
    }

    /** Constructor  */
    init {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    /** Request an ad  */
    fun getAD() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error.
            }
        }
        val request: AdRequest = adRequest
        AppOpenAd.load(
            myApplication, AD_UNIT_ID, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityPaused(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
    }

    /** Shows the ad if one isn't already showing.  */
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            Log.i(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.setFullScreenContentCallback(fullScreenContentCallback)
            appOpenAd!!.show(currentActivity)
        } else {
            Log.i(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    @OnLifecycleEvent(ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.i(LOG_TAG, "onStart")
    }
}