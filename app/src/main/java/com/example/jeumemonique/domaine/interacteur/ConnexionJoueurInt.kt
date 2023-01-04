package com.example.jeumemonique.domaine.interacteur

import com.example.jeumemonique.accesAuxDonnees.ISourceDeDonnées
import com.example.jeumemonique.domaine.entité.Joueur

class ConnexionJoueurInt( var sourceDeDonnées: ISourceDeDonnées) {
    fun connexionJoueur(courriel : String, motDePasse : String) : Boolean {
        return sourceDeDonnées.connexionJoueur(courriel, motDePasse)
    }

    fun obtenirJoueurParCourriel(courriel : String) : Joueur {
        return sourceDeDonnées.obtenirJoueurParCourriel(courriel)
    }

}