package com.example.wheatherzone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val splashAppName: TextView = findViewById(R.id.tv_app_name)
        val splashSubAppName: TextView = findViewById(R.id.tv_sub_app_name)
        val splashLogo: ImageView = findViewById(R.id.iv_splash_logo)
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
        val translateTop: Animation = AnimationUtils.loadAnimation(this,R.anim.text_animation)
        splashAppName.startAnimation(translateTop)
        splashSubAppName.startAnimation(translateTop)
        splashLogo.startAnimation(fadeIn)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()} ,3000)
    }
}