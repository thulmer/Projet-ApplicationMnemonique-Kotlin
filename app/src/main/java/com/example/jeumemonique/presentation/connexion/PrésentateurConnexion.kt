package com.example.jeumemonique.presentation.connexion

import android.content.SharedPreferences
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.connexion.IContratVuePrésentateurConnexion.*
import com.example.jeumemonique.presentation.modèle
import kotlinx.coroutines.*


class PrésentateurConnexion(var vue : IVueConnexion, var sharedPreferences : SharedPreferences, var _modèle : Modèle = modèle) : IPrésentateurConnexion {

    override fun traiterVérifierUtilisateurDéjàConnecté() {
        val courriel = sharedPreferences.getString("courriel","") ?: ""
        val mdp = sharedPreferences.getString("mdp","") ?: ""
        if(courriel != "" && mdp != ""){
            traiterRequêteConnexion(courriel,mdp)
        }
    }


    override fun traiterRequêteConnexion(identifiant : String, motDePasse : String) {

        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                _modèle.connexionJoueur(identifiant, motDePasse)
            }
            try{
                var connexion = job.await()

                if(connexion) {
                    val editor = sharedPreferences.edit()

                    //Login persistant, pas sécurisé--------------------------
                    editor.putString("courriel",identifiant)
                    editor.putString("mdp",motDePasse)
                    editor.commit()
                    //--------------------------------------------------------

                    vue.naviguerVersMenu()
                }else{
                    vue.masquerChargement()
                    vue.afficherMessageErreur("Identifiants invalides.")
                }
            }
            catch(e: AccèsRessourcesException){
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()

    }

    override fun traiterRequêtePageInscription() {
        vue.naviguerVersInscription()
    }

}