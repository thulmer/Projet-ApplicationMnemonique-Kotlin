package com.example.jeumemonique.presentation.voirGroupe

interface IContratVuePrésentateurVoirGroupe {
    interface IVueVoirGroupe{
        fun afficherNomGroupe(nom: String)
        fun naviguerVersVoirGroupes()
        fun afficherMessageErreur(message: String)
    }
    interface IPrésentateurVoirGroupe{
        fun traiterInitialiser()
        fun traiterRetour()
    }
}