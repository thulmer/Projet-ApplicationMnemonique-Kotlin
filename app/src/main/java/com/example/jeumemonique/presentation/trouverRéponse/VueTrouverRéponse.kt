package com.example.jeumemonique.presentation.trouverRéponse

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jeumemonique.R
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import com.example.jeumemonique.presentation.trouverRéponse.IContratVuePrésentateurTrouverRéponse.*


class VueTrouverRéponse : Fragment(), IVueTrouverRéponse {

    lateinit var navController : NavController;

    lateinit var présentateur : IPresentateurTrouverRéponse

    lateinit var btnSuivant : Button
    lateinit var btnRecommencer : Button
    lateinit var btnMenu : Button

    lateinit var txtQuestion : TextView
    lateinit var txtReponse1 : TextView
    lateinit var txtReponse2 : TextView
    lateinit var txtReponse3 : TextView
    lateinit var txtReponse4 : TextView
    lateinit var txtScore : TextView

    lateinit var listeTxtRéponses : List<TextView>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_trouver_reponse, container, false)
        présentateur = PrésentateurTrouverRéponse(this)

        txtQuestion = vue.findViewById(R.id.txtQuestion)
        txtReponse1 = vue.findViewById(R.id.txtRéponse1)
        txtReponse2 = vue.findViewById(R.id.txtRéponse2)
        txtReponse3 = vue.findViewById(R.id.txtRéponse3)
        txtReponse4 = vue.findViewById(R.id.txtRéponse4)
        txtScore = vue.findViewById(R.id.txtScore)

        listeTxtRéponses = listOf(txtReponse1,txtReponse2,txtReponse3,txtReponse4)

        attacherÉcouteurSuivant( vue )
        attacherÉcouteurRecommencer( vue )
        attacherÉcouteursQuestions( vue )
        présentateur?.traiterDébuter()

        return vue
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun attacherÉcouteurSuivant(vue: View) {
        btnSuivant = vue.findViewById<Button>(R.id.btnSuivant)
        btnSuivant.setOnClickListener {
            présentateur?.traiterSuivant()
        }
    }

    private fun attacherÉcouteurRecommencer(vue: View) {
        btnRecommencer = vue.findViewById<Button>(R.id.btnRecommencer)
        btnRecommencer.setOnClickListener {
            présentateur?.traiterRéinitialiser()
        }
    }

    private fun attacherÉcouteursQuestions(vue: View) {
        txtReponse1.setOnClickListener {
            présentateur?.traiterValiderRéponse(0)
        }
        txtReponse2.setOnClickListener {
            présentateur?.traiterValiderRéponse(1)
        }
        txtReponse3.setOnClickListener {
            présentateur?.traiterValiderRéponse(2)
        }
        txtReponse4.setOnClickListener {
            présentateur?.traiterValiderRéponse(3)
        }
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun naviguerVersMenu() {
        //navController.navigate(R.id.action_flashcards_to_menu)
    }

    override fun changerTexteQuestion(texteQuestion: String?) {
        txtQuestion.text = texteQuestion
    }

    override fun changerTexteRéponses(réponses : MutableList<QuestionRéponse>) {
        txtReponse1.text = réponses[0].réponse
        txtReponse2.text = réponses[1].réponse
        txtReponse3.text = réponses[2].réponse
        txtReponse4.text = réponses[3].réponse

    }

    override fun afficherScore(score : Int, longueur : Int){
        txtScore.text = score.toString() + " / " + longueur
    }

    override fun afficherBonneRéponse(position : Int) {
        listeTxtRéponses[position].setBackgroundColor(resources.getColor(R.color.valide))
        listeTxtRéponses[position].setTextColor(resources.getColor(R.color.white))
    }

    override fun afficherMauvaiseRéponse(position : Int) {
        listeTxtRéponses[position].setBackgroundColor(resources.getColor(R.color.invalide))
        listeTxtRéponses[position].setTextColor(resources.getColor(R.color.white))
    }

    override fun réinitialiserRéponses() {
        txtReponse1.setBackgroundColor(resources.getColor(R.color.black))
        txtReponse1.setTextColor(resources.getColor(R.color.principale_jaune))

        txtReponse2.setBackgroundColor(resources.getColor(R.color.black))
        txtReponse2.setTextColor(resources.getColor(R.color.principale_jaune))

        txtReponse3.setBackgroundColor(resources.getColor(R.color.black))
        txtReponse3.setTextColor(resources.getColor(R.color.principale_jaune))

        txtReponse4.setBackgroundColor(resources.getColor(R.color.black))
        txtReponse4.setTextColor(resources.getColor(R.color.principale_jaune))

    }

}