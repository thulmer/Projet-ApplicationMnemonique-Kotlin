package com.example.jeumemonique.presentation.flashCards

interface IContratVuePrésentateurFlashCards {
    interface IVueFlashCards {
        fun afficherMessageErreur(s : String)
        fun naviguerVersMenu()
        fun afficherCarteAvant()
        fun afficherCarteArrière()
        fun changerTexteQuestion(texteQuestion: String?)
        fun changerTexteRéponse(texteRéponse: String?)

    }

    interface IPresentateurFlashCards{
        fun traiterDébuter()
        fun traiterRetournerCarte()
        fun traiterSuivant()
        fun traiterRéinitialiser()
        fun traiterRetourMenu()

    }
}