package com.example.jeumemonique.presentation.flashCards

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.presentation.connexion.IContratVuePrésentateurConnexion.IPrésentateurConnexion
import com.example.jeumemonique.presentation.connexion.IContratVuePrésentateurConnexion.IVueConnexion
import com.example.jeumemonique.presentation.connexion.PrésentateurConnexion
import com.example.jeumemonique.presentation.modèle
import org.w3c.dom.Text

class VueConnexion : Fragment(), IVueConnexion {

    lateinit var navController : NavController;

    lateinit var présentateur : IPrésentateurConnexion

    lateinit var txtIdentifiant : EditText
    lateinit var txtMotDePasse : EditText
    lateinit var lienInscription : TextView
    lateinit var btnConnexion : Button

    lateinit var loadingPanel : RelativeLayout

    lateinit var sharedPrefs : SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_connexion, container, false)

        sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)


        présentateur = PrésentateurConnexion( this, sharedPrefs)
        txtIdentifiant = vue.findViewById<EditText>(R.id.txtIdentifiant)
        txtMotDePasse = vue.findViewById<EditText>(R.id.txtMotDePasse)
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)


        attacherÉcouteurLienInscription( vue )
        attacherÉcouteurConnexion( vue )

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        présentateur.traiterVérifierUtilisateurDéjàConnecté()
    }

    private fun attacherÉcouteurConnexion(vue: View) {
        btnConnexion = vue.findViewById<Button>(R.id.btnConnexion)
        btnConnexion.setOnClickListener {
            présentateur?.traiterRequêteConnexion(txtIdentifiant.text.toString(), txtMotDePasse.text.toString())
        }
    }

    private fun attacherÉcouteurLienInscription(vue: View) {
        lienInscription = vue.findViewById<TextView>(R.id.lienInscription)
        lienInscription.setOnClickListener {
            présentateur?.traiterRequêtePageInscription()
        }
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun naviguerVersMenu() {
        navController.navigate(R.id.action_connexion_to_menu)
    }

    override fun naviguerVersInscription() {
        navController.navigate(R.id.action_connexion_to_inscription)
    }

    override fun afficherChargement() {
        loadingPanel.setVisibility(View.VISIBLE)
    }

    override fun masquerChargement() {
        loadingPanel.setVisibility(View.GONE)
    }
}