package com.example.jeumemonique.presentation.voirGroupes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirMembresGroupe.IPrésentateurVoirMembresGroupe
import com.example.jeumemonique.presentation.voirGroupes.IContratVuePrésentateurVoirMembresGroupe.IVueVoirMembresGroupe

class VueVoirMembresGroupe : Fragment(), IVueVoirMembresGroupe {

    lateinit var navController : NavController

    lateinit var présentateur : IPrésentateurVoirMembresGroupe

    lateinit var listeMembresGroupe : ListView
    lateinit var arrayAdapter: ArrayAdapter<Joueur>

    lateinit var btnAjouterMembre : ImageView
    lateinit var popupMenu : PopupMenu
    lateinit var loadingPanel : RelativeLayout



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_voir_membres_groupe, container, false)
        présentateur = PrésentateurVoirMembresGroupe( modèle,this)

        btnAjouterMembre = vue.findViewById<ImageView>(R.id.btnAjouterMembre)
        listeMembresGroupe = vue.findViewById<ListView>(R.id.listeMembresGroupe)
        arrayAdapter = ArrayAdapter<Joueur>(requireContext().applicationContext, android.R.layout.simple_list_item_1)
        listeMembresGroupe.adapter = arrayAdapter
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navController = Navigation.findNavController(view)
        présentateur.remplirMembres(arrayAdapter)
        attacherEcouteursMembres()
    }

    override fun initialiser(){
        attacherEcouteursMembres()
        afficherAjouterMembre()
    }

    override fun afficherAjouterMembre() {
        btnAjouterMembre.setImageResource(R.drawable.ic_ajouter_membre)
        btnAjouterMembre.isVisible = true
        attacherEcouteurAjouterMembre()
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(requireContext().applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun attacherEcouteursMembres(){
        listeMembresGroupe.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                popupMenu = PopupMenu(requireContext().applicationContext, view)
                popupMenu.inflate(R.menu.popup_menu_gestion)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item_supprimer -> {
                            var membreSelectionné = arrayAdapter.getItem(position)
                            if (membreSelectionné != null) {
                                demanderConfirmationSuppression(membreSelectionné,arrayAdapter)
                            }
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
                popupMenu.menu.findItem(R.id.item_modifier).setVisible(false)

                return true
            }
        }
    }

    private fun attacherEcouteurAjouterMembre(){
        btnAjouterMembre.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            val inflater = layoutInflater
            builder.setTitle("Entrez le courriel d'un membre")
            val dialogLayout = inflater.inflate(R.layout.edittext_dialog_layout, null)
            val txtCourrielNouveauMembre  = dialogLayout.findViewById<EditText>(R.id.editText1)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i -> présentateur.traiterAjouterMembre(txtCourrielNouveauMembre.text.toString(), arrayAdapter) }
            builder.setNegativeButton("Annuler") { dialog, which -> null }
            builder.show()
        }
    }


    private fun demanderConfirmationSuppression(membre : Joueur, arrayAdapter: ArrayAdapter<Joueur>){
        val builder = AlertDialog.Builder(context)
        .setTitle("Supprimer " + membre.nom + " du groupe")
        .setMessage("Êtes-vous sûr ?")
        .setPositiveButton("OK")  { dialog, i ->
                présentateur.traiterSupprimerMembre(membre, arrayAdapter)
            }
        .setNegativeButton("Annuler") {dialog, i -> null }
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
    }

    override fun afficherChargement() {
        loadingPanel.setVisibility(View.VISIBLE)
    }

    override fun masquerChargement() {
        loadingPanel.setVisibility(View.GONE)
    }

}
