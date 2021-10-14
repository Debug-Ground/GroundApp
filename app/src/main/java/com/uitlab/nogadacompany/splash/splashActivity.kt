package com.uitlab.nogadacompany.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.uitlab.nogadacompany.MainActivity
import com.uitlab.nogadacompany.databinding.ActivitySplashBinding
import com.uitlab.nogadacompany.kLogin.KakaoLogin

class splashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding;
    companion object { private const val SPLASH_DELAY_TIME = 1500L }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root);

        start()
    }

    private fun start(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, KakaoLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },SPLASH_DELAY_TIME)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}