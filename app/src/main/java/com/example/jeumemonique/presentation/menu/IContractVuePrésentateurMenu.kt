package com.example.jeumemonique.presentation.menu

interface IContractVuePrésentateurMenu {
    interface IVueMenu {
        fun naviguerVersSelectionnerListe()
        fun naviguerVersGroupes()
        fun naviguerVersListes()
        fun naviguerVersConnexion()
        fun afficherMessageErreur(message: String)
    }

    interface IPrésentateurMenu {
        fun traiterRequêtePageSelectionnerListe()
        fun traiterVoirGroupes()
        fun traiterVoirListes()
        fun traiterDéconnexion()
    }
}