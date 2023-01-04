package com.example.jeumemonique.domaine.entité

class Groupe(var nom : String, var propriétaire : Joueur, val id : Int? = null,  var membres : MutableList<Joueur> = mutableListOf<Joueur>()) {

    init{
        if(!this.membres.contains(propriétaire)) {
            this.membres.add(propriétaire)
        }
    }

    fun ajouterMembre(membre : Joueur){
        if(this.membres.contains(membre)){
            throw IllegalArgumentException("Ce joueur est déjà membre du groupe.")
        }
        this.membres.add(membre)
    }

    fun supprimerMembre(membre : Joueur){
        this.membres.remove(membre)
    }

    fun supprimerMembre(position : Int){
        this.membres.removeAt(position)
    }

    override fun toString(): String {
        return this.nom
    }

    override operator fun equals(autre : Any?) : Boolean {
        if (autre == null) {
            return false
        }
        if (autre !is Groupe) {
            return false
        }
        return this.id == autre.id

    }

}