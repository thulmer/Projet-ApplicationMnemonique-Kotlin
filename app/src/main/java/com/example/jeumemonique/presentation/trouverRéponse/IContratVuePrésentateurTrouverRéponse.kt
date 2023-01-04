package com.example.jeumemonique.presentation.trouverRéponse

import com.example.jeumemonique.domaine.entité.QuestionRéponse

interface IContratVuePrésentateurTrouverRéponse {
    interface IVueTrouverRéponse {
        fun afficherMessageErreur(s: String)
        fun naviguerVersMenu()
        fun changerTexteQuestion(texteQuestion: String?)
        fun changerTexteRéponses(réponses: MutableList<QuestionRéponse>)
        fun afficherBonneRéponse(position : Int)
        fun afficherMauvaiseRéponse(position : Int)


        fun réinitialiserRéponses()
        fun afficherScore(socre: Int, longueur : Int)
    }

    interface IPresentateurTrouverRéponse {
        fun traiterDébuter()
        fun traiterSuivant()
        fun traiterRéinitialiser()
        fun traiterRetourMenu()
        fun traiterValiderRéponse(réponseSelectionnée : Int)

    }
}