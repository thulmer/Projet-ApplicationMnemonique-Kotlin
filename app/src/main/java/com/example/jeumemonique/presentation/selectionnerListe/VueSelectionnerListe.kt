package com.example.jeumemonique.presentation.selectionnerListe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jeumemonique.presentation.selectionnerListe.IContratVuePrésentateurSelectionnerListe.IPrésentateurSelectionnerListe
import com.example.jeumemonique.presentation.selectionnerListe.IContratVuePrésentateurSelectionnerListe.IVueSelectionnerListe
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.domaine.entité.ListeQuestions


class VueSelectionnerListe : Fragment(), IVueSelectionnerListe {

    lateinit var navController: NavController;

    lateinit var présentateur: IPrésentateurSelectionnerListe
    lateinit var btnRetour : ImageView

    lateinit var loadingPanel : RelativeLayout

    lateinit var spinner: Spinner
    lateinit var arrayAdapter: ArrayAdapter<ListeQuestions>
    lateinit var btnJouer : Button
    lateinit var btnJouer2 : Button

    lateinit var btnCreerListe : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_selectionner_liste, container, false)

        spinner = vue.findViewById<Spinner>(R.id.spinner1)
        arrayAdapter = ArrayAdapter<ListeQuestions>(requireContext().applicationContext, android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        présentateur = PrésentateurSelectionnerListe(this, arrayAdapter)
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)

        attacherEcouteurSpinner()
        attacherÉcouteurJouerFlashCards(vue)
        attacherÉcouteurAjouterListe(vue)
        attacherEcouteurRetour(vue)
        attacherÉcouteurJouerTrouver(vue)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        présentateur.remplirListes()
    }

    override fun naviguerVersFlashCards() {
        navController.navigate(R.id.action_selectionnerListe_to_flashCards)
    }

    override fun naviguerVersTrouverRéponse() {
        navController.navigate(R.id.action_selectionnerListe_to_trouverRéponse)
    }

    override fun naviguerVersVoirListes() {
        navController.navigate(R.id.action_selectionnerListe_to_voirListes)
    }

    override fun naviguerVersMenu() {
        navController.navigate(R.id.action_selectionnerListe_to_menu)
    }

    override fun afficherMessageErreur(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    private fun attacherEcouteurSpinner(){
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                arrayAdapter.getItem(p2)?.let { présentateur?.traiterListeSelectionnée(it) }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun attacherÉcouteurJouerFlashCards(vue: View) {
        btnJouer = vue.findViewById<Button>(R.id.btnJouer)
        btnJouer.setOnClickListener {
            présentateur?.traiterJouerFlashCards()
        }
    }

    private fun attacherÉcouteurJouerTrouver(vue: View) {
        btnJouer2 = vue.findViewById<Button>(R.id.btnJouer2)
        btnJouer2.setOnClickListener {
            présentateur?.traiterJouerTrouver()
        }
    }

    private fun attacherÉcouteurAjouterListe(vue: View) {
        btnCreerListe = vue.findViewById<Button>(R.id.btnGererListes)
        btnCreerListe.setOnClickListener {
            présentateur?.traiterVoirListes()
        }
    }

    private fun attacherEcouteurRetour(vue : View){
        btnRetour = vue.findViewById<ImageView>(R.id.btnRetour)
        btnRetour.setImageResource(R.drawable.ic_retour)
        btnRetour.setOnClickListener {
            présentateur?.traiterRetour()
        }
    }

    override fun afficherChargement() {
        loadingPanel.setVisibility(View.VISIBLE)
    }

    override fun masquerChargement() {
        loadingPanel.setVisibility(View.GONE)
    }

    override fun afficherListeSelectionnéeDansLeSpinner(position : Int) {
        spinner.setSelection(position)
    }
}