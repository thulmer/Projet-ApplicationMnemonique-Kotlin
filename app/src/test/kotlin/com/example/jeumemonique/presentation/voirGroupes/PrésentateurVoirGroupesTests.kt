package com.example.jeumemonique.presentation.voirGroupes

import android.widget.ArrayAdapter
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes.IVueVoirGroupes
import com.example.jeumemonique.presentation.flashCards.PrésentateurFlashCards
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class PrésentateurVoirGroupesTests {
    @Test
    fun `Étant donné un PrésentateurVoirGroupes, lorsqu'on reçoit une requête «retourner en arrière», la vue passe à l'écran de «voir groupes»`() {
        // Mise en place
        val mockModele = mock( Modèle::class.java )
        val mockVue = mock( IVueVoirGroupes::class.java )
        val mockArray = mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurVoirGroupes(mockModele, mockVue,
            mockArray as ArrayAdapter<Groupe>
        )
        // Action
        présentateur.traiterRetour()

        // Validation
        Mockito.verify( mockVue ).naviguerVersMenu()
    }

    @Test
    fun `Étant donné un PrésentateurVoirGroupes, vérifier si un joueur est le propriétaire d'un groupe`(){

       // Mise en place
        val mockModele = mock( Modèle::class.java )
        val mockGroupe = mock( Groupe::class.java )
        val mockVue = mock( IVueVoirGroupes::class.java )
        val mockArray = mock(ArrayAdapter::class.java)
        val présentateur = PrésentateurVoirGroupes(mockModele, mockVue,
            mockArray as ArrayAdapter<Groupe>
        )
        val résultats_obtenus = présentateur.vérifierSiJoueurPropriétaireDuGroupe(mockGroupe)
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }
}