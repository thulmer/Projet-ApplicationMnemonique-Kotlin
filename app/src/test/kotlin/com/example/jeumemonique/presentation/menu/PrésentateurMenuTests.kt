package com.example.jeumemonique.presentation.menu

import android.content.SharedPreferences
import com.example.jeumemonique.presentation.connexion.IContratVuePrésentateurConnexion
import com.example.jeumemonique.presentation.connexion.PrésentateurConnexion
import org.junit.Test
import org.mockito.Mockito

class PrésentateurMenuTests {

    @Test
    fun `test étant donné un PrésentateurMenu, losqu'on reçoit une requête «Page Flash Cards», la vue passe à l'écran où on affiche la page pour la selection de liste`() {

        // Mise en place
        val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        val mockVue = Mockito.mock( IContractVuePrésentateurMenu.IVueMenu::class.java )
        val présentateur = PrésentateurMenu( mockVue, sharedPrefs )

        // Action
        présentateur.traiterRequêtePageSelectionnerListe()

        // Validation
        Mockito.verify( mockVue ).naviguerVersSelectionnerListe()

    }

    @Test
    fun `test étant donné un PrésentateurMenu, losqu'on reçoit une requête «Page voir liste», la vue passe à l'écran où on affiche les listes du joueur`() {

        // Mise en place
        val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        val mockVue = Mockito.mock( IContractVuePrésentateurMenu.IVueMenu::class.java )
        val présentateur = PrésentateurMenu( mockVue, sharedPrefs )

        // Action
        présentateur.traiterVoirListes()

        // Validation
        Mockito.verify( mockVue ).naviguerVersListes()

    }

    @Test
    fun `test étant donné un PrésentateurMenu, losqu'on reçoit une requête «Page voir groupes», la vue passe à l'écran où on affiche les groupes du joueur`() {

        // Mise en place
        val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        val mockVue = Mockito.mock( IContractVuePrésentateurMenu.IVueMenu::class.java )
        val présentateur = PrésentateurMenu( mockVue, sharedPrefs )

        // Action
        présentateur.traiterVoirGroupes()

        // Validation
        Mockito.verify( mockVue ).naviguerVersGroupes()

    }

    @Test
    fun `test étant donné un PrésentateurMenu, losqu'on reçoit une requête «Déconnexion», le joueur se déconnecte et la vue passe à l'écran de connexion`() {
        TODO("Ne fonctionne pas")
        // Mise en place
        val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        val mockVue = Mockito.mock( IContractVuePrésentateurMenu.IVueMenu::class.java )
        val présentateur = PrésentateurMenu( mockVue, sharedPrefs )

        // Action
        présentateur.traiterDéconnexion()

        // Validation
        Mockito.verify( mockVue ).naviguerVersConnexion()

    }
}