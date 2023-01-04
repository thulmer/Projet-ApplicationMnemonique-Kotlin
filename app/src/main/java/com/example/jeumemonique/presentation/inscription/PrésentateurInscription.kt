package com.example.jeumemonique.presentation.inscription

import androidx.core.util.PatternsCompat
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.inscription.IContratVuePrésentateurInscription.*
import com.example.jeumemonique.presentation.modèle
import kotlinx.coroutines.*

class PrésentateurInscription(var vue : IVueInscription, var _modèle : Modèle = modèle ) : IPrésentateurInscription {
    override fun traiterRequêtePageConnexion() {
        vue.naviguerVersConnexion()
    }

    override fun traiterRequêteInscription(nomUtilisateur : String, motDePasse : String, courriel : String) {
        var champsValides = false;

        try {
            champsValides = validerChamps(nomUtilisateur,motDePasse,courriel)
        } catch (e : IllegalArgumentException){
            e.message?.let { vue.afficherMessageErreur(it) }
        }
        if (champsValides) {
            GlobalScope.launch(Dispatchers.Main) {

                var job = async(SupervisorJob() + Dispatchers.IO) {
                    _modèle.inscriptionJoueur(nomUtilisateur, motDePasse, courriel)
                }
                try {
                    var inscription = job.await()

                    if (inscription) {
                        vue.naviguerVersConnexion()
                    } else {
                        vue.masquerChargement()
                        vue.afficherMessageErreur("Identifiants invalides.")
                    }
                } catch (e: AccèsRessourcesException) {
                    vue.afficherMessageErreur("Ressource indisponible.")
                    vue.masquerChargement()
                }
            }
            vue.afficherChargement()
        }
    }

    private fun validerChamps(nomUtilisateur : String, motDePasse : String, courriel : String) : Boolean{

        //Code adapté de StackOverflow - Auteur : cactustictacs, lien : https://stackoverflow.com/questions/69928312/password-validation-with-kotlin
        fun String.assezLongue() = length >= 8
        fun String.contientUnChiffre() = count(Char::isDigit) > 0
        fun String.contientMajusculesEtMinuscules() = any(Char::isLowerCase) && any(Char::isUpperCase)
        fun String.contientCaratèreSpécial() = any { it in "!,+^$*" }

        val requisMotDePasse = listOf(String::assezLongue, String::contientUnChiffre, String::contientMajusculesEtMinuscules, String::contientCaratèreSpécial)
        fun String.estValide() = requisMotDePasse.all { check -> check(this) }

        return if(nomUtilisateur.length < 5){
            throw IllegalArgumentException("Votre nom d'utilisateur doit comprendre au moins 5 caractères.")
        } else if(!motDePasse.estValide()){
            throw IllegalArgumentException("Le mot de passe doit faire au moins 8 caractères, contenir au moins 1 chiffre, 1 majuscule, 1 minuscule et un charactère spécial : !,+^$*.")
        } else if(!PatternsCompat.EMAIL_ADDRESS.matcher(courriel).matches()) {
            throw IllegalArgumentException("Veuillez entrer un email valide.")
        } else {
            true
        }
    }
}
