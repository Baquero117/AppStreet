package com.example.appinterface

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appinterface.R

class ImagenFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagen_fullscreen)

        val img = findViewById<ImageView>(R.id.imgFullscreen)
        val imagen = intent.getStringExtra("imagen")

        Glide.with(this)
            .load(imagen)
            .into(img)


        img.setOnClickListener {
            finish()
        }
    }
}
