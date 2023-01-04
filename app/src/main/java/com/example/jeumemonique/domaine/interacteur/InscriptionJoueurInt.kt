package com.example.jeumemonique.domaine.interacteur

import com.example.jeumemonique.accesAuxDonnees.ISourceDeDonnées
import com.example.jeumemonique.domaine.entité.Joueur

class InscriptionJoueurInt (var sourceDeDonnées: ISourceDeDonnées) {
    fun vérifierCourrielExistant(courriel : String) : Boolean {
        return sourceDeDonnées.vérifierCourrielExistant(courriel)
    }
    fun ajouterJoueur(joueur : Joueur) : Boolean {
        return sourceDeDonnées.ajouterJoueur(joueur)
    }

}