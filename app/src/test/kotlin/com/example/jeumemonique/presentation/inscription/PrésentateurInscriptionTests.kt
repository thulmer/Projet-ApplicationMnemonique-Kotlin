package com.example.jeumemonique.presentation.inscription

import android.content.SharedPreferences
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.connexion.*
import org.mockito.Mockito
import kotlin.test.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*

import com.example.jeumemonique.presentation.inscription.IContratVuePrésentateurInscription.*

class PrésentateurInscriptionTests {
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
        fun `test étant donné un PrésentateurInscription, losqu'on reçoit une requête «Page de connexion», la vue passe à l'écran de connexion`() {

            // Mise en place
            val mockVue = Mockito.mock(IVueInscription::class.java)
            val présentateur = PrésentateurInscription(mockVue)

            // Action
            présentateur.traiterRequêtePageConnexion()

            // Validation
            Mockito.verify(mockVue).naviguerVersConnexion()

        }

        @Test
        fun `test étant donné un PrésentateurInscription, losqu'on reçoit une requête «Inscription» avec un identifiant invalide, on obtient une erreur`() {

            // Mise en place
            val mockVue = Mockito.mock(IVueInscription::class.java)
            val présentateur = PrésentateurInscription(mockVue)

            // Action
            présentateur.traiterRequêteInscription("Tt", "motDePasse1+", "bob@max.com")

            // Validation
            Mockito.verify(mockVue)
                .afficherMessageErreur("Votre nom d'utilisateur doit comprendre au moins 5 caractères.")
        }

        @Test
        fun `test étant donné un PrésentateurInscription, losqu'on reçoit une requête «Inscription» avec un courriel invalide, on obtient une erreur`() {

            // Mise en place
            val mockVue = Mockito.mock(IVueInscription::class.java)
            val présentateur = PrésentateurInscription(mockVue)

            // Action
            présentateur.traiterRequêteInscription("Testtttt", "motDePasse1+", "bomax.com")

            // Validation
            Mockito.verify(mockVue).afficherMessageErreur("Veuillez entrer un email valide.")
        }

        @Test
        fun `test étant donné un PrésentateurInscription, losqu'on reçoit une requête «Inscription» avec un mot de passe invalide, on obtient une erreur`() {

            // Mise en place
            val mockVue = Mockito.mock(IVueInscription::class.java)
            val présentateur = PrésentateurInscription(mockVue)

            // Action
            présentateur.traiterRequêteInscription("Testttt", "motdepasse", "bob@max.com")

            // Validation
            Mockito.verify(mockVue)
                .afficherMessageErreur("Le mot de passe doit faire au moins 8 caractères, contenir au moins 1 chiffre, 1 majuscule, 1 minuscule et un charactère spécial : !,+^$*.")
        }

        @Test
        fun `étant donné un PrésentateurInscription, lorsqu'il reçoit la requête «Inscription» valide, on inscrit si le joueur via le modèle`(){
            //Mise en place
            val mockVue = Mockito.mock( IVueInscription::class.java )
            val modèle = Mockito.mock( Modèle::class.java )

            val présentateur = PrésentateurInscription( mockVue, modèle )

            // Action
            présentateur.traiterRequêteInscription("Teeeeest", "motDePasse1+", "bob@max.com")

            Thread.sleep(100)

            //Validation
            Mockito.verify( modèle ).inscriptionJoueur("Teeeeest", "motDePasse1+", "bob@max.com")

        }

    @Test
    fun `étant donné un PrésentateurInscription, lorsqu'il reçoit la requête «Inscription» valide et que le joueur n'existe pas encore, la vue navigue vers la page de connexion`(){
        //Mise en place
        val mockVue = Mockito.mock( IVueInscription::class.java )
        val modèle = Mockito.mock( Modèle::class.java )

        val présentateur = PrésentateurInscription( mockVue, modèle )

        Mockito.`when`(modèle.inscriptionJoueur("Teeeeest", "motDePasse1+", "bob@max.com"))
            .thenReturn(true)

        // Action
        présentateur.traiterRequêteInscription("Teeeeest", "motDePasse1+", "bob@max.com")

        Thread.sleep(100)

        //Validation
        Mockito.verify( mockVue ).naviguerVersConnexion()
    }

    @Test
    fun `étant donné un PrésentateurInscription, lorsqu'il reçoit la requête «Inscription» valide mais qu'un joueur existe déja, la vue envoie un message d'erreur`(){
        //Mise en place
        val mockVue = Mockito.mock( IVueInscription::class.java )
        val modèle = Mockito.mock( Modèle::class.java )

        val présentateur = PrésentateurInscription( mockVue, modèle )

        Mockito.`when`(modèle.inscriptionJoueur("Teeeeest", "motDePasse1+", "bob@max.com"))
            .thenReturn(false)

        // Action
        présentateur.traiterRequêteInscription("Teeeeest", "motDePasse1+", "bob@max.com")

        Thread.sleep(100)

        //Validation
        Mockito.verify( mockVue ).afficherMessageErreur("Identifiants invalides.")

    }
}