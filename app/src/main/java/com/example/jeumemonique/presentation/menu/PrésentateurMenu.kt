package com.example.jeumemonique.presentation.menu

import android.content.SharedPreferences
import com.example.jeumemonique.presentation.Modèle
import com.example.jeumemonique.presentation.menu.IContractVuePrésentateurMenu.IPrésentateurMenu
import com.example.jeumemonique.presentation.menu.IContractVuePrésentateurMenu.IVueMenu

class PrésentateurMenu(var vue : IVueMenu, var sharedPreferences: SharedPreferences) : IPrésentateurMenu {

    override fun traiterRequêtePageSelectionnerListe() {
        vue.naviguerVersSelectionnerListe()
    }

    override fun traiterVoirGroupes() {
        vue.naviguerVersGroupes()
    }

    override fun traiterVoirListes() {
        vue.naviguerVersListes()
    }

    override fun traiterDéconnexion() {
        val editor = sharedPreferences.edit()
        editor.remove("courriel")
        editor.remove("mdp")
        editor.commit()
        vue.naviguerVersConnexion()
    }
}