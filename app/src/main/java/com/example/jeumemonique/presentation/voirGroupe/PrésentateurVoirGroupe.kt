package com.example.jeumemonique.presentation.voirGroupe

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirListesPartagées.IContratVuePrésentateurVoirListesPartagées
import com.example.jeumemonique.presentation.voirListesPartagées.VueVoirListesPartagées

class PrésentateurVoirGroupe(var vue : IContratVuePrésentateurVoirGroupe.IVueVoirGroupe = VueVoirGroupe()) :
    IContratVuePrésentateurVoirGroupe.IPrésentateurVoirGroupe {
    override fun traiterInitialiser() {
        vue.afficherNomGroupe(modèle.getGroupeSelectionné().nom)
    }

    override fun traiterRetour() {
        vue.naviguerVersVoirGroupes()
    }
}