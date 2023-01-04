package com.example.jeumemonique.presentation.voirListesPartagées

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions

interface IContratVuePrésentateurVoirListesPartagées {
    interface IVueVoirListesPartagées {
        fun afficherMessageErreur(message : String)
        fun initialiser()
        fun afficherPartagerListe()
        fun naviguerVersJeu()
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurVoirListesPartagées {
        fun remplirListesPartagées()
        fun traiterListePartagéeSelectionnée(listePartagée : ListeQuestions)
        fun getListePartagéeSelectionnée(): ListeQuestions
        fun traiterSupprimerListePartagée(listePartagée : ListeQuestions)
        fun traiterPartagerListe(listeAPartager : ListeQuestions)
        fun getListesDuJoueur(): MutableList<ListeQuestions>
        fun vérifierSiJoueurPropriétaireDuGroupe(): Boolean
    }
}