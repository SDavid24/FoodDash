package com.example.fooddash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
/*
        startBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }*/

        //This method makes the Splash activity move to the Intro activity after the stipulated time
        Handler().postDelayed({

          /*  val currentUserId = FirestoreClass().getCurrentUserId()

            if (currentUserId.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                startActivity(Intent(this, IntroActivity::class.java))

            }*/
            startActivity(Intent(this, MainActivity::class.java))

            finish()  //Finishes the app and makes sure the user can"t come back to the activity if he presses back
        }, 2500)
    }
}