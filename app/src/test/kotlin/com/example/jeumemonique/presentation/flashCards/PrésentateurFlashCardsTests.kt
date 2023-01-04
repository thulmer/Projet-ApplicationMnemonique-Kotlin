package com.example.jeumemonique.presentation.flashCards

import com.example.jeumemonique.domaine.entité.QuestionRéponse
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.flashCards.IContratVuePrésentateurFlashCards.IVueFlashCards
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class PrésentateurFlashCardsTests {
    @Test
    fun `test étant donné un PrésentateurFlashCards, losqu'on reçoit une requête «Page Flash Cards», la vue passe à l'écran où on affiche la page pour la selection de liste`() {

        // Mise en place
        val mockVue = mock(IVueFlashCards::class.java )
        val présentateur = PrésentateurFlashCards(mockVue)

        // Action
        présentateur.traiterRetourMenu()

        // Validation
        verify( mockVue ).naviguerVersMenu()

    }

}