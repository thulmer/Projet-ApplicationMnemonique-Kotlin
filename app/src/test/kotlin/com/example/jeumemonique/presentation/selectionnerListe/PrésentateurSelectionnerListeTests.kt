package com.example.jeumemonique.presentation.selectionnerListe

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.selectionnerListe.IContratVuePrésentateurSelectionnerListe.IVueSelectionnerListe
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class PrésentateurSelectionnerListeTests {
    @Test
    fun `test étant donné un PrésentateurSelectionnerListe, losqu'on reçoit une requête «retourner en arrière», la vue passe à l'écran de menu`() {

        // Mise en place
        val mockVue = mock( IVueSelectionnerListe::class.java )
        val mockArray = mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurSelectionnerListe( mockVue, mockArray as ArrayAdapter<ListeQuestions>)

        // Action
        présentateur.traiterRetour()

        // Validation
        Mockito.verify( mockVue ).naviguerVersMenu()

    }

    @Test
    fun `test étant donné un PrésentateurSelectionnerListe, losqu'on reçoit une requête «gérer liste», la vue passe à l'écran de gestion de liste`() {

        // Mise en place
        val mockVue = mock( IVueSelectionnerListe::class.java )
        val mockArray = mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurSelectionnerListe( mockVue, mockArray as ArrayAdapter<ListeQuestions>)

        // Action
        présentateur.traiterVoirListes()

        // Validation
        Mockito.verify( mockVue ).naviguerVersVoirListes()

    }

}