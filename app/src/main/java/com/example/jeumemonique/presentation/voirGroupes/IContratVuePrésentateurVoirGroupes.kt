package com.example.jeumemonique.presentation.voirGroupes

import android.view.View
import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur

interface IContratVuePrésentateurVoirGroupes {
    interface IVueVoirGroupes {

        fun naviguerVersVoirMembresGroupe()
        fun initialiser()
        fun afficherMessageErreur(s: String)
        fun naviguerVersMenu()
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurVoirGroupes {
        fun remplirGroupes()
        fun traiterGroupeSelectionné(groupe: Groupe)
        fun traiterSupprimerGroupe(groupe: Groupe)
        fun traiterModifierGroupe(position: Int)
        fun traiterQuitterGroupe(groupe: Groupe)
        fun traiterCréerGroupe(nomGroupe: String)
        fun vérifierSiJoueurPropriétaireDuGroupe(groupe: Groupe) : Boolean
        fun traiterRetour()
    }
}