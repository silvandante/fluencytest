package com.walker.fluencytest.ui.auth

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.walker.fluencytest.FluenyTestApplication
import com.walker.fluencytest.R
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.local.SecurityPreferences
import com.walker.fluencytest.ui.home.FeedDeckActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 1000 //3 seconds
    private var mDelayHandler: Handler? = null
    private var progressBarStatus = 0
    var dummy:Int = 0
    lateinit var token: String
    lateinit var userId: String

    private lateinit var mSharedPreferences: SecurityPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mSharedPreferences = SecurityPreferences(this)
        mDelayHandler = Handler()


        token = mSharedPreferences.get(AppConstants.SHARED_PREFERENCES.TOKEN_KEY)
        userId = mSharedPreferences.get(AppConstants.SHARED_PREFERENCES.USER_ID)


        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }



    private fun launchMainActivity() {
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
        mDelayHandler!!.removeCallbacks(mRunnable)

    }


    private fun launchDecksActivity(userId: String) {
        var intent = Intent(this, FeedDeckActivity::class.java)
        intent.putExtra(AppConstants.SHARED_PREFERENCES.USER_ID, userId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
        mDelayHandler!!.removeCallbacks(mRunnable)

    }

    private val mRunnable: Runnable = Runnable {

        Thread(Runnable {
            while (progressBarStatus < 100) {
                // performing some dummy operation
                try {
                    dummy = dummy + 25
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                // tracking progress
                progressBarStatus = dummy

                // Updating the progress bar
                splash_screen_progress_bar.progress = progressBarStatus
            }


            if (token.isNullOrEmpty()) {
                launchMainActivity()
            } else {

                launchDecksActivity(userId)
            }

        }).start()
    }

    override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}