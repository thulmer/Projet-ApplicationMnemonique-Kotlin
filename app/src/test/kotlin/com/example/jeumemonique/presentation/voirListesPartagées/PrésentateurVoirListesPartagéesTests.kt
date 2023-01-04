package com.example.jeumemonique.presentation.voirListesPartagées

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.flashCards.IContratVuePrésentateurFlashCards
import com.example.jeumemonique.presentation.flashCards.PrésentateurFlashCards
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes
import com.example.jeumemonique.presentation.voirGroupes.PrésentateurVoirGroupes
import com.example.jeumemonique.presentation.voirListesPartagées.IContratVuePrésentateurVoirListesPartagées

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import kotlin.test.assertEquals

class PrésentateurVoirListesPartagéesTests {
    @Test
    fun `Étant donné un PrésentateurVoirListesPartagées, lorsqu'on clique sur «partager liste», on récupère  les listes du joueur`() {


        // Mise en place
        val mockModele = Mockito.mock(Modèle::class.java)
        val mockVue = Mockito.mock(IContratVuePrésentateurVoirListesPartagées.IVueVoirListesPartagées::class.java)
        val mockArray = Mockito.mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurVoirListesPartagées(mockModele,mockVue,
            mockArray as ArrayAdapter<ListeQuestions>)

        // Action
        val résultats_obtenus = présentateur.getListesDuJoueur()
        val résultats_attendus = mockModele.getListesDuJoueur()

        // Validation
        assertEquals( résultats_attendus, résultats_obtenus )

    }
    @Test
    fun `Étant donné un PrésentateurVoirListesPartagées, lorsqu'on selectionne une liste partagée, on reçoit la liste partagée`() {

        // Mise en place
        val mockModele = Mockito.mock(Modèle::class.java)
        val mockVue = Mockito.mock(IContratVuePrésentateurVoirListesPartagées.IVueVoirListesPartagées::class.java)
        val mockArray = Mockito.mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurVoirListesPartagées(mockModele,mockVue,
            mockArray as ArrayAdapter<ListeQuestions>)

        // Action
        val résultats_obtenus = présentateur.getListePartagéeSelectionnée()
        val résultats_attendus = mockModele.getListePartagéeSelectionnée()

        // Validation
        assertEquals( résultats_attendus, résultats_obtenus )
    }
}
