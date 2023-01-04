package com.example.jeumemonique.presentation.voirGroupes

import android.widget.ArrayAdapter
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirMembresGroupe.IPrésentateurVoirMembresGroupe
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirMembresGroupe.IVueVoirMembresGroupe
import kotlinx.coroutines.*

class PrésentateurVoirMembresGroupe(var modèle: Modèle, var vue : IVueVoirMembresGroupe = VueVoirMembresGroupe()) : IPrésentateurVoirMembresGroupe {

    override fun traiterSupprimerMembre(membre : Joueur, adapter: ArrayAdapter<Joueur>) {
        if(modèle.getGroupeSelectionné().propriétaire != membre) {
            GlobalScope.launch(Dispatchers.Main) {

                var job = async(SupervisorJob() + Dispatchers.IO) {
                    modèle.supprimerMembreDuGroupe(membre)
                }
                try {
                    var suppressionRéussie = job.await()
                    if (suppressionRéussie == true) {
                        remplirMembres(adapter)
                    }
                    vue.masquerChargement()
                } catch (e: AccèsRessourcesException) {
                    vue.afficherMessageErreur("Ressource indisponible.")
                    vue.masquerChargement()
                }
            }
            vue.afficherChargement()
        }else{
            vue.afficherMessageErreur("Impossible de supprimer le propriétaire.")
        }
    }

    override fun traiterAjouterMembre(courrielJoueur : String, adapter: ArrayAdapter<Joueur>) {
        if(courrielJoueur != null && !courrielJoueur.isEmpty() && courrielJoueur != ""){
            var nouveauMembre = Joueur()
            GlobalScope.launch(Dispatchers.Main) {
                var job = async(SupervisorJob() + Dispatchers.IO) {
                    modèle.getMembreParCourriel(courrielJoueur)
                }
                try{
                    nouveauMembre = job.await()
                    vue.masquerChargement()
                }
                catch (e:IllegalArgumentException){
                    vue.afficherMessageErreur(e.message.toString())
                    vue.masquerChargement()
                }
                if(nouveauMembre.nom != null){
                    var job = async(SupervisorJob() + Dispatchers.IO) {
                        modèle.ajouterMembreAuGroupe(nouveauMembre)
                    }
                    try{
                        var ajoutRéussi = job.await()
                        if (ajoutRéussi == true) {
                            remplirMembres(adapter)
                            vue.masquerChargement()
                        } else{
                            vue.afficherMessageErreur("Ce joueur est déjà dans le groupe.")
                            vue.masquerChargement()
                        }
                    }
                    catch (e:IllegalArgumentException){
                        vue.afficherMessageErreur(e.message.toString())
                        vue.masquerChargement()
                    }
                }else{
                    vue.afficherMessageErreur("Ce joueur n'existe pas.")
                    vue.masquerChargement()
                }
            }
            vue.afficherChargement()

        }
    }

    override fun remplirMembres(adapter: ArrayAdapter<Joueur>) {
        //vue.afficherNomGroupe(modèle.getGroupeSelectionné().nom)
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.récupererMembresDuGroupe()
            }
            try{
                var membres = job.await()
                adapter.clear()
                adapter.addAll(membres ?: mutableListOf<Joueur>())
                adapter.notifyDataSetChanged()
                if(modèle.getGroupeSelectionné().propriétaire == modèle.getJoueur()){
                    vue.initialiser() //renommer cette methode?
                }
                vue.masquerChargement()
            }
            catch(e: AccèsRessourcesException){
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()
    }
}