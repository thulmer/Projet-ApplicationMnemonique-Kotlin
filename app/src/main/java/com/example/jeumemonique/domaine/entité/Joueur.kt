package com.example.jeumemonique.domaine.entité

class Joueur(var nom : String? = null, var courriel : String? = null, var motDePasse : String? = null) {

    override fun toString(): String {
        return "$nom $courriel"
    }

    override operator fun equals(autre : Any?) : Boolean {
        if (autre == null) {
            return false
        }
        if (autre !is Joueur) {
            return false
        }
        return this.courriel == autre.courriel

    }

}