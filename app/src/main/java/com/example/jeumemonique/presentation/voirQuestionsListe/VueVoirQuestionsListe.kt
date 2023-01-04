package com.example.jeumemonique.presentation.voirQuestionsListe

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
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import com.example.jeumemonique.presentation.modèle
import com.example.jeumemonique.presentation.voirQuestionsListe.IContratVuePrésentateurVoirQuestionsListe.IVueVoirQuestionsListe
import com.example.jeumemonique.presentation.voirQuestionsListe.IContratVuePrésentateurVoirQuestionsListe.IPrésentateurVoirQuestionsListe


class VueVoirQuestionsListe: Fragment(), IVueVoirQuestionsListe {
    lateinit var navController : NavController

    lateinit var présentateur : IPrésentateurVoirQuestionsListe

    lateinit var listeQuestionsRéponses : ListView
    lateinit var arrayAdapter: ArrayAdapter<QuestionRéponse>
    lateinit var loadingPanel : RelativeLayout

    lateinit var txtNomListe : TextView
    lateinit var btnRetour : ImageView
    lateinit var btnAjouterQuestionRéponse : ImageView
    lateinit var popupMenu : PopupMenu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_voir_questions_liste, container, false)
        txtNomListe = vue.findViewById(R.id.txtNomListeSélectionnée)
        listeQuestionsRéponses = vue.findViewById<ListView>(R.id.listeQuestionsRéponses)
        btnAjouterQuestionRéponse = vue.findViewById<ImageView>(R.id.btnAjouterQuestionRéponse)
        arrayAdapter = ArrayAdapter<QuestionRéponse>(requireContext().applicationContext, android.R.layout.simple_list_item_1)
        listeQuestionsRéponses.adapter = arrayAdapter
        loadingPanel = vue.findViewById<RelativeLayout>(R.id.loadingPanel)

        présentateur = PrésentateurVoirQuestionsListe( modèle,this, arrayAdapter)

        attacherEcouteurAjouterQuestionRéponse()
        attacherEcouteurRetour(vue)

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        présentateur.remplirListeQuestionsRéponse()
    }

    override fun initialiser(){
        attacherEcouteursQuestionsRéponses()
    }

    override fun afficherNomListe(nom : String) {
        txtNomListe.setText(nom)
    }

    override fun afficherMessageErreur(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT)
    }

    override fun naviguerVersVoirListesQuestions() {
        navController.navigate(R.id.action_voirQuestionsListe_to_voirListes)
    }

    private fun attacherEcouteursQuestionsRéponses(){
        listeQuestionsRéponses.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                val questionRéponseSelectionnée = arrayAdapter.getItem(position)
                if(questionRéponseSelectionnée != null) {
                    popupMenu = PopupMenu(requireContext().applicationContext, view)
                    popupMenu.inflate(R.menu.popup_menu_gestion)
                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.item_supprimer -> {
                                demanderConfirmationSuppression(questionRéponseSelectionnée)
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

    private fun attacherEcouteurAjouterQuestionRéponse(){
        btnAjouterQuestionRéponse.setImageResource(android.R.drawable.ic_menu_add)
        btnAjouterQuestionRéponse.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            val inflater = layoutInflater
            builder.setTitle("Nouvelle question/réponse")
            val dialogLayout = inflater.inflate(R.layout.edittext_dialog_layout, null)
            val txtQuestion  = dialogLayout.findViewById<EditText>(R.id.editText1)
            txtQuestion.setHint("Question...")
            val txtRéponse  = dialogLayout.findViewById<EditText>(R.id.editText2) //MODIF NOM TXT
            txtRéponse.isVisible = true
            txtRéponse.setHint("Réponse...")
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i -> présentateur.traiterAjouterQuestionRéponse(txtQuestion.text.toString(),txtRéponse.text.toString() ) }
            builder.setNegativeButton("Annuler") { dialog, which -> null }
            builder.show()
        }
    }

    private fun attacherEcouteurRetour(vue : View){
        btnRetour = vue.findViewById<ImageView>(R.id.btnRetour)
        btnRetour.setImageResource(R.drawable.ic_retour)
        btnRetour.setOnClickListener {
            présentateur?.traiterRetour()
        }
    }

    private fun demanderConfirmationSuppression(questionRéponse: QuestionRéponse){
        val builder = AlertDialog.Builder(context)
            .setTitle("Supprimer cette question : " + questionRéponse.question)
            .setMessage("Êtes-vous sûr ?")
            .setPositiveButton("OK")  { dialog, i ->
                présentateur.traiterSupprimerQuestionRéponse(questionRéponse)
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