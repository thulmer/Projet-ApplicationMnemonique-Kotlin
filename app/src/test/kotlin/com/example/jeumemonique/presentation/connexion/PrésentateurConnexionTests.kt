package com.example.jeumemonique.presentation.connexion


import android.content.Context
import android.content.SharedPreferences
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.connexion.IContratVuePrésentateurConnexion.*
import kotlin.test.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.mockito.Mock

import org.mockito.Mockito



class PrésentateurConnexionTests{
    private val mainThreadSurrogate = newSingleThreadContext("fil principal")

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

        @Test
        fun `test étant donné un PrésentateurConnexion, losqu'on reçoit une requête «Page d'inscription», la vue passe à l'écran d'inscription`() {

            // Mise en place
            val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
            val mockVue = Mockito.mock( IVueConnexion::class.java )
            val présentateur = PrésentateurConnexion( mockVue, sharedPrefs )

            // Action
            présentateur.traiterRequêtePageInscription()

            // Validation
            Mockito.verify( mockVue ).naviguerVersInscription()

        }

    @Test
    fun `étant donné un PrésentateurConnexion, lorsqu'il reçoit la requête «Connexion», on vérifie si le joueur existe via le modèle`(){
        //Mise en place
        val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        val mockVue = Mockito.mock( IVueConnexion::class.java )
        val modèle = Mockito.mock( Modèle::class.java )

        val présentateur = PrésentateurConnexion( mockVue, sharedPrefs, modèle )

        // Action
        présentateur.traiterRequêteConnexion("test","123")

        Thread.sleep(100)

        //Validation
        Mockito.verify( modèle ).connexionJoueur("test", "123")

    }


}