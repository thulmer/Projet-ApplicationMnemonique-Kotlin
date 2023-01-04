package com.example.jeumemonique.presentation.voirGroupes

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur

interface IContratVuePrésentateurVoirMembresGroupe {
    interface IVueVoirMembresGroupe {
        fun afficherMessageErreur(message : String)
        fun initialiser()
        fun afficherAjouterMembre()
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurVoirMembresGroupe {
        fun remplirMembres(adapter: ArrayAdapter<Joueur>)
        fun traiterSupprimerMembre(membre : Joueur, adapter: ArrayAdapter<Joueur>)
        fun traiterAjouterMembre(nomGroupe: String, adapter: ArrayAdapter<Joueur>)
    }
}