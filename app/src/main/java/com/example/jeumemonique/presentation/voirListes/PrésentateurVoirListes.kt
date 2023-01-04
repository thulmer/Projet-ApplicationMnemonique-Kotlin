package com.example.jeumemonique.presentation.voirListes

import android.widget.ArrayAdapter
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirListes.IContratVuePrésentateurVoirListes.IPrésentateurVoirListes
import com.example.jeumemonique.presentation.voirListes.IContratVuePrésentateurVoirListes.IVueVoirListes
import kotlinx.coroutines.*

class PrésentateurVoirListes(var vue : IVueVoirListes, var adapter: ArrayAdapter<ListeQuestions>) : IPrésentateurVoirListes {
    override fun traiterListeQuestionsSelectionné(listeQuestionsSelectionné : ListeQuestions) {
        modèle.selectionnerListe(listeQuestionsSelectionné)
        vue.naviguerVersVoirQuestionsListe()
    }

    override fun traiterSupprimerListeQuestions(listeQuestions: ListeQuestions) {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.supprimerListeQuestions(listeQuestions)
            }
            try{
                var suppressionRéussie = job.await()
                if(suppressionRéussie == true){
                    remplirListesQuestions()
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

    override fun traiterCréerListeQuestions(nomListeQuestions : String) {
        if(nomListeQuestions != null && !nomListeQuestions.isEmpty() && nomListeQuestions != ""){
            GlobalScope.launch(Dispatchers.Main) {

                var job = async(SupervisorJob() + Dispatchers.IO) {
                    modèle.créerListeQuestions(nomListeQuestions)
                }
                try{
                    var ajoutRéussi = job.await()
                    if(ajoutRéussi){
                        remplirListesQuestions()
                    }
                    else{
                        vue.afficherMessageErreur("Erreur lors de l'ajout.")
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

    override fun traiterModifierListeQuestions(position: Int) {
        TODO()
    }

    override fun remplirListesQuestions() {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.récupererListesDuJoueur()
            }
            try{
                var listeQuestions = job.await()
                adapter.clear()
                adapter.addAll(listeQuestions ?: mutableListOf<ListeQuestions>())
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

    override fun traiterRetour() {
        vue.naviguerVersMenu()
    }

}