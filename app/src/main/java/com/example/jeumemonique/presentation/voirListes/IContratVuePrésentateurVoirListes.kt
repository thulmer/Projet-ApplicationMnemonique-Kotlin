package com.example.jeumemonique.presentation.voirListes

import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.ListeQuestions

interface IContratVuePrésentateurVoirListes {
    interface IVueVoirListes {

        fun initialiser()
        fun afficherMessageErreur(s: String)
        fun naviguerVersVoirQuestionsListe()
        fun naviguerVersMenu()
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurVoirListes {

        fun traiterListeQuestionsSelectionné(listeQuestionsSelectionné: ListeQuestions)
        fun traiterSupprimerListeQuestions(listeQuestions: ListeQuestions)
        fun traiterCréerListeQuestions(nomListeQuestions: String)
        fun traiterModifierListeQuestions(position: Int)
        fun remplirListesQuestions()
        fun traiterRetour()
    }
}