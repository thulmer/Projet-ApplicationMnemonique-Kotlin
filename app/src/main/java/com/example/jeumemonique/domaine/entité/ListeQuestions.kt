package com.example.jeumemonique.domaine.entité

class ListeQuestions(var id : Int? = null, var nom : String? = null, var propriétaire : Joueur? =null) {

    override fun toString(): String {
        return this.nom ?: "Liste inéxistante."
    }

    override operator fun equals(autre : Any?) : Boolean {
        if (autre == null) {
            return false
        }
        if (autre !is ListeQuestions) {
            return false
        }
        return this.id == autre.id && this.nom == autre.nom && this.propriétaire == autre.propriétaire

    }
}