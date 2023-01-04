package com.example.jeumemonique.presentation.selectionnerListe

import com.example.jeumemonique.domaine.entité.ListeQuestions

interface IContratVuePrésentateurSelectionnerListe {
    interface IVueSelectionnerListe {
        fun naviguerVersFlashCards()
        fun naviguerVersVoirListes()
        fun afficherMessageErreur(s: String)
        fun naviguerVersMenu()
        fun afficherChargement()
        fun masquerChargement()
        fun afficherListeSelectionnéeDansLeSpinner(position : Int)
        fun naviguerVersTrouverRéponse()
    }

    interface IPrésentateurSelectionnerListe {
        fun remplirListes()
        fun traiterJouerFlashCards()
        fun traiterVoirListes()
        fun traiterListeSelectionnée(listeSelectionnée: ListeQuestions)
        fun traiterRetour()
        fun traiterJouerTrouver()
    }
}