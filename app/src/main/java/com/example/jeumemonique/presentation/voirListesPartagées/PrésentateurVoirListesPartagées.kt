package com.example.jeumemonique.presentation.voirListesPartagées

import android.content.Intent
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.voirListesPartagées.IContratVuePrésentateurVoirListesPartagées.IVueVoirListesPartagées
import com.example.jeumemonique.presentation.voirListesPartagées.IContratVuePrésentateurVoirListesPartagées.IPrésentateurVoirListesPartagées
import kotlinx.coroutines.*
import java.util.*

class PrésentateurVoirListesPartagées(var modèle: Modèle, var vue : IVueVoirListesPartagées = VueVoirListesPartagées(), var adapter: ArrayAdapter<ListeQuestions>) :
    IPrésentateurVoirListesPartagées {

    override fun remplirListesPartagées() {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.récupererListesPartagées()
            }
            try{
                var listes = job.await()
                adapter.clear()
                adapter.addAll(listes ?: mutableListOf<ListeQuestions>())
                adapter.notifyDataSetChanged()
                if(modèle.getGroupeSelectionné().propriétaire == modèle.getJoueur()){
                    vue.initialiser() //renommer cette methode?
                }
                vue.masquerChargement()
            }
            catch(e: Exception){
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()
    }

    override fun traiterListePartagéeSelectionnée(listeSelectionnée : ListeQuestions) {
        modèle.selectionnerListePartagée(listeSelectionnée)

        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.questionsRéponsesListeSelectionnée()
            }
            try {
                job.await()
                vue.masquerChargement()
                vue.naviguerVersJeu()
            } catch (e: AccèsRessourcesException) {
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()
    }

    override fun getListePartagéeSelectionnée(): ListeQuestions {
        return modèle.getListePartagéeSelectionnée()
    }

    override fun traiterSupprimerListePartagée(listePartagée: ListeQuestions) {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.supprimerListePartagée(listePartagée)
            }
            try {
                var suppressionRéussie = job.await()
                if (suppressionRéussie == true) {
                    remplirListesPartagées()
                }

                vue.masquerChargement()
            } catch (e: AccèsRessourcesException) {
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()
    }

    override fun vérifierSiJoueurPropriétaireDuGroupe() : Boolean {
        return (modèle.getGroupeSelectionné().propriétaire == modèle.getJoueur())
    }

    override fun traiterPartagerListe(listeAPartager: ListeQuestions) {
        GlobalScope.launch(Dispatchers.Main) {
            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.partagerListe(listeAPartager)
            }
            try{
                var ajoutRéussi = job.await()
                if (ajoutRéussi == true) {
                    remplirListesPartagées()
                } else{
                    vue.afficherMessageErreur("Cette liste est déjà partagée.")
                }
                vue.masquerChargement()

            }
            catch (e:IllegalArgumentException){
                vue.afficherMessageErreur(e.message.toString())
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()

    }

    override fun getListesDuJoueur(): MutableList<ListeQuestions>{
        return modèle.getListesDuJoueur()
    }


}