package com.example.project1.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.project1.R

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.in_sign)

        // Charger les animations
        val topAnim = android.view.animation.AnimationUtils.loadAnimation(this,
            R.anim.top_animation
        )
        val bottomAnim = android.view.animation.AnimationUtils.loadAnimation(this,
            R.anim.bottom_animation
        )

        // Récupérer les vues
        val loginButtom = findViewById<Button>(R.id.login)
        val cardView = findViewById<CardView>(R.id.cardviewLogin)
        val toSignUp = findViewById<TextView>(R.id.toSignUp)


        // Appliquer les animations
        cardView.startAnimation(bottomAnim)

       loginButtom.setOnClickListener{
           val intent = Intent(this, MainActivity::class.java)
           startActivity(intent)
       }

        toSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


    }
}