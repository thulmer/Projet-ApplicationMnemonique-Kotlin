package com.example.jeumemonique.presentation.menu

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.presentation.menu.IContractVuePrésentateurMenu.IPrésentateurMenu
import com.example.jeumemonique.presentation.menu.IContractVuePrésentateurMenu.IVueMenu
import com.example.jeumemonique.presentation.modèle
import java.util.*


class VueMenu : Fragment(), IVueMenu {
    lateinit var navController : NavController;
    lateinit var btnFlashCards : CardView
    lateinit var présentateur : IPrésentateurMenu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_menu, container, false)

        val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)

        présentateur = PrésentateurMenu( this, sharedPrefs)
        btnFlashCards = vue.findViewById<CardView>(R.id.btnFlashCards)

        attacherÉcouteurFlashCards( vue )
        attacherÉcouteurGroupes( vue )
        attacherÉcouteurListes(vue)
        attacherÉcouteurDéconnexion(vue)
        //attacherÉcouteurTestCalendrier(vue)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    private fun attacherÉcouteurFlashCards(vue: View) {
        btnFlashCards = vue.findViewById<CardView>(R.id.btnFlashCards)
        btnFlashCards.setOnClickListener {
            présentateur?.traiterRequêtePageSelectionnerListe()
        }
    }

    private fun attacherÉcouteurGroupes(vue: View) {
        var btnVoirGroupes = vue.findViewById<CardView>(R.id.btnGroupes)
        btnVoirGroupes.setOnClickListener {
            présentateur?.traiterVoirGroupes()
        }
    }

    private fun attacherÉcouteurDéconnexion(vue: View) {
        var btnDéconnexion = vue.findViewById<CardView>(R.id.btnDéconnexion)
        btnDéconnexion.setOnClickListener {
            présentateur?.traiterDéconnexion()
        }
    }

    private fun attacherÉcouteurListes(vue: View) {
        var btnVoirListes = vue.findViewById<CardView>(R.id.btnListes)
        btnVoirListes.setOnClickListener {
            présentateur?.traiterVoirListes()
        }
    }

//    private fun attacherÉcouteurTestCalendrier(vue: View) {
//        var btnCalendrier = vue.findViewById<Button>(R.id.btnCalendrier)
//        btnCalendrier.setOnClickListener {
//            val calendarEvent: Calendar = Calendar.getInstance()
//            val intent = Intent(Intent.ACTION_EDIT)
//            intent.type = "vnd.android.cursor.item/event"
//            intent.putExtra("allDay", true)
//            intent.putExtra("title", "Jouer à liste :")
//            startActivity(intent)
//
//
//        }
//    }

    override fun naviguerVersSelectionnerListe() {
        navController.navigate(R.id.action_menu_to_selectionnerListe)
    }

    override fun naviguerVersGroupes() {
        navController.navigate(R.id.action_menu_to_voirGroupes)
    }

    override fun naviguerVersListes() {
        navController.navigate(R.id.action_menu_to_voirListes)
    }

    override fun naviguerVersConnexion() {
        navController.navigate(R.id.action_menu_to_connexion)
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}