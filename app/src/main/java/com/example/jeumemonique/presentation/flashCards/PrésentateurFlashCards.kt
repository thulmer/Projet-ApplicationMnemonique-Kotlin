package com.example.jeumemonique.presentation.flashCards

import com.example.jeumemonique.presentation.flashCards.IContratVuePrésentateurFlashCards.*
import com.example.jeumemonique.presentation.modèle

class PrésentateurFlashCards(var vue : IVueFlashCards) : IPresentateurFlashCards {

    override fun traiterDébuter() {
        traiterRéinitialiser()
    }

    override fun traiterRetournerCarte() {
        val faceAvant = getFace()
        if(faceAvant) {
            vue.changerTexteRéponse(modèle.getquestionsRéponsesListeSelectionnée()?.get(modèle.numeroQuestion)?.réponse)
            vue.afficherCarteArrière()
        } else {
            vue.changerTexteQuestion(modèle.getquestionsRéponsesListeSelectionnée()?.get(modèle.numeroQuestion)?.question)
            vue.afficherCarteAvant()
        }
        modèle.retournerCarte()
    }

    override fun traiterSuivant() {
        if(!getFace()) {
            vue.afficherCarteAvant()
        }
        modèle.questionSuivante()
        vue.changerTexteQuestion(modèle.getquestionsRéponsesListeSelectionnée()?.get(modèle.numeroQuestion)?.question)

    }

    override fun traiterRéinitialiser() {
        if(!getFace()) {
            vue.afficherCarteAvant()
        }
        modèle.initialiserFlashCards()
        vue.changerTexteQuestion(modèle.getquestionsRéponsesListeSelectionnée()?.get(modèle.numeroQuestion)?.question)
    }

    override fun traiterRetourMenu() {
        vue.naviguerVersMenu()
    }

    private fun getFace() : Boolean {
       return modèle.getFace()
    }
}