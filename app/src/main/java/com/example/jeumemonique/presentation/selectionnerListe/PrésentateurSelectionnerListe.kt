package com.example.jeumemonique.presentation.selectionnerListe

import android.widget.ArrayAdapter
import com.example.jeumemonique.accesAuxDonnees.AccèsRessourcesException
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.selectionnerListe.IContratVuePrésentateurSelectionnerListe.*
import kotlinx.coroutines.*

class PrésentateurSelectionnerListe(var vue : IVueSelectionnerListe, var adapter: ArrayAdapter<ListeQuestions>) : IPrésentateurSelectionnerListe {
    override fun traiterListeSelectionnée(listeSelectionnée: ListeQuestions) {
        modèle.selectionnerListe(listeSelectionnée)

        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.questionsRéponsesListeSelectionnée()
            }
            try {
                job.await()
                vue.masquerChargement()
            } catch (e: AccèsRessourcesException) {
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()
    }

    override fun traiterRetour() {
        vue.naviguerVersMenu()
    }

    override fun remplirListes() {
        var listes = mutableListOf<ListeQuestions>()
        listes.addAll(modèle.getListesDuJoueur())

        GlobalScope.launch(Dispatchers.Main) {

            var job = async(SupervisorJob() + Dispatchers.IO) {
                modèle.getToutesLesListesPartagées()
            }
            try {
                listes.addAll(job.await())
                listes = listes.distinctBy { it.id }.toMutableList()
                adapter.clear()
                adapter.addAll(listes ?: mutableListOf<ListeQuestions>())


                var indexListeSelectionnée = listes.indexOfFirst { it == modèle.getListeSelectionnée()}
                if(indexListeSelectionnée != -1){
                    vue.afficherListeSelectionnéeDansLeSpinner(indexListeSelectionnée)
                }

                adapter.notifyDataSetChanged()
                vue.masquerChargement()
            } catch (e: AccèsRessourcesException) {
                vue.afficherMessageErreur("Ressource indisponible.")
                vue.masquerChargement()
            }
        }
        vue.afficherChargement()
    }

    override fun traiterJouerFlashCards() {
        if(!modèle.getquestionsRéponsesListeSelectionnée().isNullOrEmpty()){
            vue.naviguerVersFlashCards()
        }else{
            vue.afficherMessageErreur("Votre liste est vide, ajoutez-y des questions.")
        }
    }

    override fun traiterJouerTrouver() {
        if(!modèle.getquestionsRéponsesListeSelectionnée().isNullOrEmpty() && modèle.getquestionsRéponsesListeSelectionnée()?.size!! > 4){
            vue.naviguerVersTrouverRéponse()
        }else{
            vue.afficherMessageErreur("Votre n'est pas valide, ajoutez-y des questions (5 minimum).")
        }
    }

    override fun traiterVoirListes() {
        vue.naviguerVersVoirListes()
    }
}