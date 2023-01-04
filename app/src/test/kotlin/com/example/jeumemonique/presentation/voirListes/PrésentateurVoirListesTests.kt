package com.example.jeumemonique.presentation.voirListes

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.flashCards.IContratVuePrésentateurFlashCards
import com.example.jeumemonique.presentation.flashCards.PrésentateurFlashCards
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class PrésentateurVoirListesTests {
    @Test
    fun `Étant donné un PrésentateurVoirListes, lorsqu'on reçoit une requête «Page Flash Cards», la vue passe à l'écran où on affiche la page pour la selection de liste`() {

        // Mise en place
        val mockVue = Mockito.mock(IContratVuePrésentateurVoirListes.IVueVoirListes::class.java)
        val mockArray = Mockito.mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurVoirListes(mockVue,
            mockArray as ArrayAdapter<ListeQuestions>)

        // Action
        présentateur.traiterRetour()

        // Validation
        Mockito.verify( mockVue ).naviguerVersMenu()

    }
}