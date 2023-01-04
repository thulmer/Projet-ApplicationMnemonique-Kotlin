package com.example.jeumemonique.presentation.voirGroupe

import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.voirGroupe.IContratVuePrésentateurVoirGroupe.IVueVoirGroupe
import com.example.jeumemonique.presentation.flashCards.PrésentateurFlashCards
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class PrésentateurVoirGroupeTests {
    @Test
    fun `test étant donné un PrésentateurVoirGroupe, losqu'on reçoit une requête «retourner en arrière», la vue passe à l'écran de «voir groupes»`() {
        // Mise en place
        val mockVue = mock( IVueVoirGroupe::class.java )
        val présentateur = PrésentateurVoirGroupe(mockVue)

        // Action
        présentateur.traiterRetour()

        // Validation
        Mockito.verify( mockVue ).naviguerVersVoirGroupes()
    }

}