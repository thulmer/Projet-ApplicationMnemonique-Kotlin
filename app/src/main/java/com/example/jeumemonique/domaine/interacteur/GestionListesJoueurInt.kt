package com.example.jeumemonique.domaine.interacteur

import com.example.jeumemonique.accesAuxDonnees.ISourceDeDonnées
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse

class GestionListesJoueurInt(var sourceDeDonnées: ISourceDeDonnées) {
    fun récupererListesDuJoueur(joueur : Joueur) : MutableList<ListeQuestions>{
        return sourceDeDonnées.obtenirListesDuJoueur(joueur)
    }

    fun créerListe(nomListe: String, joueur: Joueur) : Boolean {
        var nouvelleListe = ListeQuestions(0,nomListe,joueur)
        return sourceDeDonnées.ajouterListe(nouvelleListe)
    }

    fun supprimerListe(id : Int) : Boolean{
        return sourceDeDonnées.supprimerListe(id)
    }

    fun récupererQuestionsRéponses(id : Int) : MutableList<QuestionRéponse>{
        return sourceDeDonnées.obtenirQuestionsRéponsesListe(id)
    }

    fun ajouterQuestionRéponse(listeId : Int?, questionRéponse: QuestionRéponse) : Boolean{
        return sourceDeDonnées.ajouterQuestionRéponse(listeId, questionRéponse)
    }

    fun supprimerQuestionRéponse(questionId: Int?) : Boolean{
        return sourceDeDonnées.supprimerQuestionRéponse(questionId)
    }



}