package com.example.jeumemonique.presentation.voirListes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.modèle

class VueVoirListes : Fragment(), IContratVuePrésentateurVoirListes.IVueVoirListes {

    lateinit var navController : NavController

    lateinit var présentateur : IContratVuePrésentateurVoirListes.IPrésentateurVoirListes

    lateinit var btnRetour : ImageView
    lateinit var listeListesQuestions : ListView
    lateinit var arrayAdapter: ArrayAdapter<ListeQuestions>
    lateinit var loadingPanel : RelativeLayout


    lateinit var btnCréerListeQuestions : ImageView
    lateinit var popupMenu : PopupMenu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_voir_listes, container, false)
        listeListesQuestions = vue.findViewById<ListView>(R.id.listeListes)
        btnCréerListeQuestions = vue.findViewById<ImageView>(R.id.btnCréerListe)
        arrayAdapter = ArrayAdapter<ListeQuestions>(requireContext().applicationContext, android.R.layout.simple_list_item_1)
        listeListesQuestions.adapter = arrayAdapter
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)


        présentateur = PrésentateurVoirListes(this, arrayAdapter)

        attacherEcouteurCréerListeQuestions(vue)
        attacherEcouteurRetour(vue)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        présentateur.remplirListesQuestions()
    }

    override fun initialiser(){
        attacherEcouteursListesQuestions()
    }

    override fun afficherMessageErreur(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT)
    }

    override fun naviguerVersVoirQuestionsListe() {
        navController.navigate(R.id.action_voirListes_to_voirQuestionsListe)
    }

    override fun naviguerVersMenu() {
        navController.navigate(R.id.action_voirListes_to_menu)
    }

    private fun attacherEcouteursListesQuestions(){
        listeListesQuestions.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var listeQuestionsSelectionné = arrayAdapter.getItem(position)
                if (listeQuestionsSelectionné != null) {
                    présentateur.traiterListeQuestionsSelectionné(listeQuestionsSelectionné)
                }
            }
        }
        listeListesQuestions.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                val listeQuestionsSelectionné = arrayAdapter.getItem(position)
                if(listeQuestionsSelectionné != null) {
                    popupMenu = PopupMenu(requireContext().applicationContext, view)
                    popupMenu.inflate(R.menu.popup_menu_gestion)
                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.item_supprimer -> {
                                demanderConfirmationSuppression(listeQuestionsSelectionné, arrayAdapter)
                                arrayAdapter.notifyDataSetChanged()
                                true
                            }
                            else -> {
                                true
                            }
                        }
                    }
                    popupMenu.show()
                    popupMenu.menu.findItem(R.id.item_quitter).setVisible(false)

                }
                return true
            }
        }
    }

    private fun attacherEcouteurCréerListeQuestions(vue : View){
        btnCréerListeQuestions.setImageResource(android.R.drawable.ic_menu_add)
        btnCréerListeQuestions.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            val inflater = layoutInflater
            builder.setTitle("Nouvelle liste")
            val dialogLayout = inflater.inflate(R.layout.edittext_dialog_layout, null)
            val txtNomListeQuestions  = dialogLayout.findViewById<EditText>(R.id.editText1) //MODIF NOM TXT
            txtNomListeQuestions.setHint("Nom de liste...")
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i -> présentateur.traiterCréerListeQuestions(txtNomListeQuestions.text.toString()) }
            builder.setNegativeButton("Annuler") { dialog, which -> null }
            builder.show()
        }
    }

    private fun demanderConfirmationSuppression(listeQuestions : ListeQuestions, arrayAdapter: ArrayAdapter<ListeQuestions>){
        val builder = AlertDialog.Builder(context)
            .setTitle("Supprimer " + listeQuestions.nom + " ?")
            .setMessage("Êtes-vous sûr ?")
            .setPositiveButton("OK")  { dialog, i ->
                présentateur.traiterSupprimerListeQuestions(listeQuestions)
            }
            .setNegativeButton("Annuler") {dialog, i -> null }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
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
}