package com.example.jeumemonique.presentation.voirGroupes

import android.widget.ArrayAdapter
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes.IPrésentateurVoirGroupes
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes.IVueVoirGroupes
import kotlinx.coroutines.*

class PrésentateurVoirGroupes(var modèle: Modèle, var vue : IVueVoirGroupes, var adapter: ArrayAdapter<Groupe>) : IPrésentateurVoirGroupes {
    override fun traiterGroupeSelectionné(groupeSelectionné : Groupe) {
        modèle.selectionnerGroupe(groupeSelectionné)
        vue.naviguerVersVoirMembresGroupe()
    }

    override fun traiterSupprimerGroupe(groupe: Groupe) {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.supprimerGroupe(groupe)
            }
            try{
                var suppressionRéussie = job.await()
                if(suppressionRéussie == true){
                    remplirGroupes()
                }
                else{
                    vue.afficherMessageErreur("Erreur lors de la suppression.")
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

    override fun traiterCréerGroupe(nomGroupe : String) {
        if(nomGroupe != null && !nomGroupe.isEmpty() && nomGroupe != ""){
            GlobalScope.launch(Dispatchers.Main) {

                var job = async(SupervisorJob() + Dispatchers.IO) {
                    modèle.créerGroupe(nomGroupe)
                }
                try{
                    var ajoutRéussi = job.await()
                    if(ajoutRéussi){
                        remplirGroupes()
                    }else{
                        vue.afficherMessageErreur("Erreur lors de la création du groupe.")
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

    override fun vérifierSiJoueurPropriétaireDuGroupe(groupe: Groupe) : Boolean {
        return (groupe.propriétaire == modèle.getJoueur())
    }

    override fun traiterRetour() {
        vue.naviguerVersMenu()
    }

    override fun traiterModifierGroupe(position: Int) {
        TODO()
    }

    override fun traiterQuitterGroupe(groupe: Groupe) {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.quitterGroupe(groupe)
            }
            try{
                var suppressionRéussie = job.await()
                if(suppressionRéussie == true){
                    remplirGroupes()
                }else{
                    vue.afficherMessageErreur("Erreur lors de cette action.")
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

    override fun remplirGroupes() {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.récupererGroupesDuJoueur()
            }
            try{
                var groupes = job.await()
                adapter.clear()
                adapter.addAll(groupes ?: mutableListOf<Groupe>())
                adapter.notifyDataSetChanged()
                vue.initialiser()
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