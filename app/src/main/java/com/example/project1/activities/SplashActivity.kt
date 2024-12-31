package com.example.project1.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        // Activer le mode plein écran
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Charger les animations
        val topAnim = android.view.animation.AnimationUtils.loadAnimation(this,
            R.anim.top_animation
        )
        val bottomAnim = android.view.animation.AnimationUtils.loadAnimation(this,
            R.anim.bottom_animation
        )

        // Récupérer les vues
        val imageView = findViewById<ImageView>(R.id.splash_img)
        val textView = findViewById<TextView>(R.id.splash_txt)

        // Appliquer les animations
        imageView.startAnimation(topAnim)
        textView.startAnimation(bottomAnim)

        // Appliquer les animations lors du changement d'activité
        //overridePendingTransition(R.anim.top_animation, R.anim.bottom_animation)

        // Délai avant de passer à l'activité principale
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)


            startActivity(intent)
        }, 4000)
    }
}
