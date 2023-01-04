package com.example.jeumemonique.presentation.connexion

import android.view.View

interface IContratVuePrésentateurConnexion {
    interface IVueConnexion {
        fun afficherMessageErreur(message : String)
        fun naviguerVersMenu()
        fun naviguerVersInscription()
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurConnexion {
        fun traiterRequêteConnexion(identifiant : String, motDePasse : String)
        fun traiterRequêtePageInscription()

        fun traiterVérifierUtilisateurDéjàConnecté()
    }
}