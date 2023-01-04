package com.example.jeumemonique

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jeumemonique.accesAuxDonnees.SourceDeDonnéesHTTP
import com.example.jeumemonique.presentation.modèle
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        modèle.sourceDeDonnées = SourceDeDonnéesHTTP(this, URL("http://10.0.2.2:64511/api"))
    }
}