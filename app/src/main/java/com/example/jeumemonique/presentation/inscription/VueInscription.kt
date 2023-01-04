package com.example.jeumemonique.presentation.inscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.presentation.inscription.IContratVuePrésentateurInscription.IVueInscription
import com.example.jeumemonique.presentation.modèle

class VueInscription : Fragment(), IVueInscription {
    lateinit var navController : NavController;

    lateinit var présentateur : IContratVuePrésentateurInscription.IPrésentateurInscription

    lateinit var txtNomUtilisateur : EditText
    lateinit var txtMotDePasse : EditText
    lateinit var txtCourriel : EditText
    lateinit var loadingPanel : RelativeLayout


    lateinit var lienConnexion : TextView
    lateinit var btnInscription : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_inscription, container, false)
        présentateur = PrésentateurInscription( this)
        txtNomUtilisateur = vue.findViewById<EditText>(R.id.txtNomUtilisateur)
        txtMotDePasse = vue.findViewById<EditText>(R.id.txtMotDePasse)
        txtCourriel = vue.findViewById<EditText>(R.id.txtCourriel)
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)

        attacherÉcouteurInscription(vue)
        attacherÉcouteurLienConnexion(vue)
        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun attacherÉcouteurLienConnexion(vue: View) {
        lienConnexion = vue.findViewById<TextView>(R.id.lienConnexion)
        lienConnexion.setOnClickListener {
            présentateur?.traiterRequêtePageConnexion()
        }
    }

    private fun attacherÉcouteurInscription(vue: View) {
        btnInscription = vue.findViewById<Button>(R.id.btnInscription)
        btnInscription.setOnClickListener {
            présentateur?.traiterRequêteInscription(txtNomUtilisateur.text.toString(), txtMotDePasse.text.toString(), txtCourriel.text.toString())
        }
    }

    override fun afficherMessageErreur(s : String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun naviguerVersConnexion() {
        navController.navigate(R.id.action_inscription_to_connexion)
    }

    override fun afficherChargement() {
        loadingPanel.setVisibility(View.VISIBLE)
    }

    override fun masquerChargement() {
        loadingPanel.setVisibility(View.GONE)
    }
}