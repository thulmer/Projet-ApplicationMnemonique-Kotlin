package com.example.jeumemonique.presentation.voirListesPartagées

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.jeumemonique.R
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirListesPartagées.IContratVuePrésentateurVoirListesPartagées.*
import java.util.*

class VueVoirListesPartagées : Fragment(), IVueVoirListesPartagées {
    lateinit var navController : NavController

    lateinit var présentateur : IPrésentateurVoirListesPartagées

    lateinit var listesPartagées : ListView
    lateinit var arrayAdapter: ArrayAdapter<ListeQuestions>
    lateinit var loadingPanel : RelativeLayout


    lateinit var btnPartagerListe : ImageView
    lateinit var popupMenu : PopupMenu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_voir_listes_partagees, container, false)
        btnPartagerListe = vue.findViewById<ImageView>(R.id.btnPartagerListe)
        listesPartagées = vue.findViewById<ListView>(R.id.listesPartagées)
        arrayAdapter = ArrayAdapter<ListeQuestions>(requireContext().applicationContext, android.R.layout.simple_list_item_1)
        listesPartagées.adapter = arrayAdapter
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)

        présentateur = PrésentateurVoirListesPartagées( modèle,this, arrayAdapter)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = this.findNavController()
        présentateur.remplirListesPartagées()
        attacherEcouteursJouerListePartagée()
        attacherEcouteursActionsPropriétaire()

    }

    override fun initialiser(){
        afficherPartagerListe()
    }

    override fun afficherPartagerListe() {
        btnPartagerListe.setImageResource(R.drawable.ic_partager_liste)
        btnPartagerListe.isVisible = true
        attacherEcouteurPartagerListe()
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun naviguerVersJeu() {
        navController.navigate(R.id.action_voirGroupe_to_selectionnerListes)
    }

    private fun attacherEcouteursActionsPropriétaire(){
        listesPartagées.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                var listePartagéeSelectionnée = arrayAdapter.getItem(position)
                popupMenu = PopupMenu(requireContext().applicationContext, view)
                popupMenu.inflate(R.menu.popup_menu_gestion)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item_supprimer -> {
                            if (listePartagéeSelectionnée != null) {
                                demanderConfirmationSuppression(listePartagéeSelectionnée)
                            }
                            arrayAdapter.notifyDataSetChanged()
                            true
                        }
                        R.id.item_rappel -> {
                            val calendarEvent: Calendar = Calendar.getInstance()
                            val intent = Intent(Intent.ACTION_EDIT)
                            intent.type = "vnd.android.cursor.item/event"
                            intent.putExtra("allDay", true)
                            intent.putExtra("title", "Jouer à liste : " + listePartagéeSelectionnée?.nom)
                            try {
                                ContextCompat.startActivity(requireContext(), intent, null)
                            }catch (e :Exception){
                                e.message?.let { afficherMessageErreur("Erreur lors de l'ouverture du calendrier.") }
                            }
                            true
                        }
                        else -> {
                            true
                        }
                    }
                }
                popupMenu.show()
                vérifierSiJoueurPropriétaireEtAfficherLesItems()

                return true
            }
        }
    }

    private fun attacherEcouteursJouerListePartagée(){
        listesPartagées.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var listeSelectionnée = arrayAdapter.getItem(position)
                if (listeSelectionnée != null) {
                    présentateur.traiterListePartagéeSelectionnée(listeSelectionnée)
                }
            }
        }
    }

    private fun attacherEcouteurPartagerListe(){
        btnPartagerListe.setOnClickListener {
            val dialogAdapter = ArrayAdapter<ListeQuestions>(requireContext(),android.R.layout.simple_list_item_1, présentateur.getListesDuJoueur())
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Selectionnez une liste à partager :")
                .setAdapter(dialogAdapter,
                    { dialog, which -> dialogAdapter.getItem(which)
                        ?.let { it1 -> présentateur.traiterPartagerListe(it1) } })
            //builder.setPositiveButton("OK") { dialogInterface, i -> présentateur.traiterPartagerListe(listeAPartager) }
            builder.setNegativeButton("Annuler") { dialog, which -> null }
            builder.show()
        }
    }

    private fun demanderConfirmationSuppression(liste : ListeQuestions){
        val builder = AlertDialog.Builder(context)
            .setTitle("Ne plus partager " + liste.nom + " ?")
            .setMessage("Êtes-vous sûr ?")
            .setPositiveButton("OK")  { dialog, i ->
                présentateur.traiterSupprimerListePartagée(liste)
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

    private fun vérifierSiJoueurPropriétaireEtAfficherLesItems(){
        if(présentateur.vérifierSiJoueurPropriétaireDuGroupe()) {
            popupMenu.menu.findItem(R.id.item_supprimer).setVisible(true)
            popupMenu.menu.findItem(R.id.item_modifier).setVisible(false)
            popupMenu.menu.findItem(R.id.item_quitter).setVisible(false)
            popupMenu.menu.findItem(R.id.item_rappel).setVisible(true)

        } else {
            popupMenu.menu.findItem(R.id.item_supprimer).setVisible(false)
            popupMenu.menu.findItem(R.id.item_modifier).setVisible(false)
            popupMenu.menu.findItem(R.id.item_quitter).setVisible(false)
            popupMenu.menu.findItem(R.id.item_rappel).setVisible(true)

        }
    }

}