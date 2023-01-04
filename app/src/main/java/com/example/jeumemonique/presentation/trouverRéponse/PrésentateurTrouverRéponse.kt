package com.example.jeumemonique.presentation.trouverRéponse

import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.trouverRéponse.IContratVuePrésentateurTrouverRéponse.*

class PrésentateurTrouverRéponse(var vue : IVueTrouverRéponse): IPresentateurTrouverRéponse{

    override fun traiterDébuter() {
        traiterRéinitialiser()
    }

    override fun traiterSuivant() {
        modèle.questionSuivanteTrouverRéponse()
        vue.changerTexteQuestion(modèle.getquestionsRéponsesListeSelectionnée()?.get(modèle.numeroQuestion)?.question)
        vue.changerTexteRéponses(modèle.réponses)
        vue.réinitialiserRéponses()


    }

    override fun traiterRéinitialiser() {
        modèle.initialiserTrouverRéponse()
        vue.changerTexteQuestion(modèle.getquestionsRéponsesListeSelectionnée()?.get(modèle.numeroQuestion)?.question)
        vue.changerTexteRéponses(modèle.réponses)
        vue.réinitialiserRéponses()
        vue.afficherScore(modèle.compteurScore, modèle.getquestionsRéponsesListeSelectionnée()?.size ?: 0)
    }

    override fun traiterRetourMenu() {
        vue.naviguerVersMenu()
    }

    override fun traiterValiderRéponse(réponseSelectionnée : Int) {
        if(!modèle.déjàRépondu) {
            if (modèle.réponses[réponseSelectionnée].réponse == modèle.getquestionsRéponsesListeSelectionnée()
                    ?.get(modèle.numeroQuestion)?.réponse
            ) {
                vue.afficherBonneRéponse(réponseSelectionnée)
                modèle.compteurScore++
                vue.afficherScore(modèle.compteurScore, modèle.getquestionsRéponsesListeSelectionnée()?.size ?: 0)
            } else {
                vue.afficherMauvaiseRéponse(réponseSelectionnée)
            }
            modèle.répondreQuestion()
        }
    }
}