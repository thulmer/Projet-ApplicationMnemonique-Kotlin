package com.example.jeumemonique.presentation.inscription

interface IContratVuePrésentateurInscription {
    interface IVueInscription {
        fun afficherMessageErreur(s : String)
        fun naviguerVersConnexion()
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurInscription {
        fun traiterRequêtePageConnexion()
        fun traiterRequêteInscription(nomUtilisateur : String, motDePasse : String, courriel : String)

    }
}
