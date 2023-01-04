package com.example.jeumemonique.presentation.voirGroupes

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
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes.IVueVoirGroupes
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirGroupes.IPrésentateurVoirGroupes


class VueVoirGroupes : Fragment(), IVueVoirGroupes {

    lateinit var navController : NavController

    lateinit var présentateur : IPrésentateurVoirGroupes

    lateinit var btnRetour : ImageView
    lateinit var listeGroupes : ListView
    lateinit var arrayAdapter: ArrayAdapter<Groupe>

    lateinit var btnCréerGroupe : ImageView
    lateinit var popupMenu : PopupMenu
    lateinit var loadingPanel : RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_voir_groupes, container, false)
        btnCréerGroupe = vue.findViewById<ImageView>(R.id.btnCréerGroupe)
        listeGroupes = vue.findViewById<ListView>(R.id.listeGroupes)
        arrayAdapter = ArrayAdapter<Groupe>(requireContext().applicationContext, android.R.layout.simple_list_item_1)
        listeGroupes.adapter = arrayAdapter
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)

        présentateur = PrésentateurVoirGroupes( modèle,this, arrayAdapter)

        attacherEcouteurCréerGroupe(vue)
        attacherEcouteurRetour(vue)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        présentateur.remplirGroupes()
    }

    override fun initialiser(){
        attacherEcouteursGroupes()
    }

    override fun afficherMessageErreur(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT)
    }

    override fun naviguerVersVoirMembresGroupe() {
        navController.navigate(R.id.action_voirGroupes_to_voirGroupe)

    }

    override fun naviguerVersMenu() {
        navController.navigate(R.id.action_voirGroupes_to_menu)
    }

    private fun attacherEcouteursGroupes(){
        listeGroupes.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var groupeSelectionné = arrayAdapter.getItem(position)
                if (groupeSelectionné != null) {
                    présentateur.traiterGroupeSelectionné(groupeSelectionné)
                }
            }
        }
        listeGroupes.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                val groupeSelectionné = arrayAdapter.getItem(position)
                if(groupeSelectionné != null) {
                    popupMenu = PopupMenu(requireContext().applicationContext, view)
                    popupMenu.inflate(R.menu.popup_menu_gestion)
                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.item_supprimer -> {
                                    demanderConfirmationSuppression(groupeSelectionné)
                                arrayAdapter.notifyDataSetChanged()
                                true
                            }
                            R.id.item_quitter -> {
                                    demanderConfirmationQuitter(groupeSelectionné)
                                arrayAdapter.notifyDataSetChanged()
                                true
                            }
                            else -> {
                                true
                            }
                        }
                    }
                    popupMenu.show()
                    vérifierSiJoueurPropriétaireEtAfficherLesItems(groupeSelectionné)

                }
                return true
            }
        }
    }

    private fun attacherEcouteurCréerGroupe(vue : View){
        btnCréerGroupe.setImageResource(android.R.drawable.ic_menu_add)
        btnCréerGroupe.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            val inflater = layoutInflater
            builder.setTitle("Nouveau groupe")
            val dialogLayout = inflater.inflate(R.layout.edittext_dialog_layout, null)
            val txtNomNouveauGroupe  = dialogLayout.findViewById<EditText>(R.id.editText1)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i -> présentateur.traiterCréerGroupe(txtNomNouveauGroupe.text.toString()) }
            builder.setNegativeButton("Annuler") { dialog, which -> null }
            builder.show()
        }
    }

    private fun demanderConfirmationSuppression(groupe : Groupe){
        val builder = AlertDialog.Builder(context)
            .setTitle("Supprimer " + groupe.nom + " ?")
            .setMessage("Êtes-vous sûr ?")
            .setPositiveButton("OK")  { dialog, i ->
                présentateur.traiterSupprimerGroupe(groupe)
            }
            .setNegativeButton("Annuler") {dialog, i -> null }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun demanderConfirmationQuitter(groupe : Groupe){
        val builder = AlertDialog.Builder(context)
            .setTitle("Quitter " + groupe.nom + " ?")
            .setMessage("Êtes-vous sûr ?")
            .setPositiveButton("OK")  { dialog, i ->
                présentateur.traiterQuitterGroupe(groupe)
            }
            .setNegativeButton("Annuler") {dialog, i -> null }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun vérifierSiJoueurPropriétaireEtAfficherLesItems(groupe : Groupe){
        if(présentateur.vérifierSiJoueurPropriétaireDuGroupe(groupe)) {
            popupMenu.menu.findItem(R.id.item_supprimer).setVisible(true)
            popupMenu.menu.findItem(R.id.item_modifier).setVisible(false)
            popupMenu.menu.findItem(R.id.item_quitter).setVisible(false)
        } else {
            popupMenu.menu.findItem(R.id.item_supprimer).setVisible(false)
            popupMenu.menu.findItem(R.id.item_modifier).setVisible(false)
            popupMenu.menu.findItem(R.id.item_quitter).setVisible(true)
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



}