package com.example.jeumemonique.domaine.interacteur

import com.example.jeumemonique.accesAuxDonnees.ISourceDeDonnées
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions

class GestionGroupesInt(var sourceDeDonnées: ISourceDeDonnées) {
    fun récupererGroupesDuJoueur(joueur : Joueur) : MutableList<Groupe>{
        return sourceDeDonnées.obtenirGroupesDuJoueur(joueur)
    }

    fun récupererMembresDuGroupe(groupeId: Int) : MutableList<Joueur>{
        return sourceDeDonnées.obtenirMembresDuGroupe(groupeId)
    }

    fun supprimerGroupe(id : Int) : Boolean{
        return sourceDeDonnées.supprimerGroupe(id)
    }

    fun modifierGroupe(groupe: Groupe) : Boolean{
        return sourceDeDonnées.modifierGroupe(groupe)
    }

    fun supprimerMembre(groupeId : Int?, courriel: String?) : Boolean{
        return sourceDeDonnées.supprimerJoueurDuGroupe(groupeId, courriel)
    }

    fun ajouterMembre(groupeId : Int?, courriel: String?) : Boolean{
        return sourceDeDonnées.ajouterMembreAuGroupe(groupeId, courriel)
    }

    fun créerGroupe(nomGroupe: String, joueur: Joueur) : Boolean {
        var nouveauGroupe = Groupe(nomGroupe,joueur)
        return sourceDeDonnées.ajouterGroupe(nouveauGroupe)
    }

    fun récupererListesPartagées(groupeId: Int) : MutableList<ListeQuestions> {
        return sourceDeDonnées.obtenirListesPartagées(groupeId)
    }

    fun partagerListe(groupeId: Int?, listeId: Int?): Boolean {
        return sourceDeDonnées.partagerListe(groupeId, listeId)
    }

    fun supprimerListePartagéee(groupeId: Int?, listeId: Int?): Boolean {
        return sourceDeDonnées.supprimerListePartagée(groupeId, listeId)
    }
}