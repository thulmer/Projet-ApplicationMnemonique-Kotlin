package com.example.jeumemonique.accesAuxDonnees

import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse

interface ISourceDeDonnées {


    fun connexionJoueur(courriel : String, motDePasse : String) : Boolean
    fun vérifierCourrielExistant(courriel : String) : Boolean

    fun ajouterJoueur(joueur : Joueur) : Boolean
    fun obtenirJoueurParCourriel(courriel: String): Joueur
    fun obtenirJoueurs() : List<Joueur>

    fun obtenirListesDuJoueur(joueur : Joueur) : MutableList<ListeQuestions>
    fun ajouterListe(nouvelleListe : ListeQuestions) : Boolean
    fun supprimerListe(id : Int) : Boolean
    fun modifierListe(liste : ListeQuestions) : Boolean

    fun obtenirQuestionsRéponsesListe(idListe: Int): MutableList<QuestionRéponse>
    fun ajouterQuestionRéponse(listeId : Int?, questionRéponse: QuestionRéponse) : Boolean
    fun supprimerQuestionRéponse(questionId: Int?) : Boolean

    fun obtenirGroupesDuJoueur(joueur: Joueur) : MutableList<Groupe>
    fun ajouterGroupe(nouveauGroupe: Groupe) : Boolean
    fun supprimerGroupe(id : Int) : Boolean
    fun modifierGroupe(groupe: Groupe) : Boolean

    fun obtenirMembresDuGroupe(groupeId : Int) : MutableList<Joueur>
    fun ajouterMembreAuGroupe(groupeId : Int?, courriel: String?) : Boolean
    fun supprimerJoueurDuGroupe(groupeId : Int?, courriel: String?) : Boolean

    fun obtenirListesPartagées(groupeId: Int) : MutableList<ListeQuestions>
    fun partagerListe(groupeId: Int?, listeId: Int?): Boolean
    fun supprimerListePartagée(groupeId: Int?, listeId: Int?): Boolean

}