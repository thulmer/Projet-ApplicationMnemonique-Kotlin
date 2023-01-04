package com.example.jeumemonique.presentation.voirQuestionsListe

import android.widget.ArrayAdapter
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.voirQuestionsListe.IContratVuePrésentateurVoirQuestionsListe.*
import com.example.jeumemonique.presentation.voirQuestionsListe.VueVoirQuestionsListe
import kotlinx.coroutines.*

class PrésentateurVoirQuestionsListe(var modèle: Modèle, var vue : IVueVoirQuestionsListe, var adapter: ArrayAdapter<QuestionRéponse>) : IPrésentateurVoirQuestionsListe{
    override fun traiterRetour() {
        vue.naviguerVersVoirListesQuestions()
    }

    override fun traiterSupprimerQuestionRéponse(questionRéponse: QuestionRéponse) {
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.supprimerQuestionRéponse(questionRéponse)
            }
            try{
                var suppressionRéussie = job.await()
                if(suppressionRéussie == true){
                    remplirListeQuestionsRéponse()
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

    override fun traiterAjouterQuestionRéponse(question: String, réponse: String) {
        if(question != null && !question.isEmpty() && question != "" && réponse != null && !réponse.isEmpty() && réponse != ""){
            var questionRéponse = QuestionRéponse(null, question, réponse)
            GlobalScope.launch(Dispatchers.Main) {

                var job = async(SupervisorJob() + Dispatchers.IO) {
                    modèle.ajouterQuestionRéponse(questionRéponse)
                }
                try{
                    var ajoutRéussi = job.await()
                    if(ajoutRéussi){
                        remplirListeQuestionsRéponse()
                    } else{
                        vue.afficherMessageErreur("Problème lors de l'ajout.")
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

    override fun remplirListeQuestionsRéponse() {
        modèle.getListeSelectionnée().nom?.let { vue.afficherNomListe(it) }
        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.récupererQuestionsRéponsesListe()
            }
            try{
                var questionsRéponses = job.await()
                adapter.clear()
                adapter.addAll(questionsRéponses ?: mutableListOf<QuestionRéponse>())
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