package com.example.jeumemonique.presentation.flashCards

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
import com.example.jeumemonique.presentation.flashCards.IContratVuePrésentateurFlashCards.IVueFlashCards
import com.example.jeumemonique.presentation.modèle


class VueFlashCards : Fragment(), IVueFlashCards {

    lateinit var navController : NavController;

    lateinit var présentateur : IContratVuePrésentateurFlashCards.IPresentateurFlashCards

    lateinit var carte_avant : TextView
    lateinit var carte_arriere : TextView
    lateinit var layoutCartes : FrameLayout
    lateinit var anim_avant: AnimatorSet
    lateinit var anim_arriere: AnimatorSet
    lateinit var btnSuivant : Button
    lateinit var btnRecommencer : Button
    lateinit var btnMenu : Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vue = inflater.inflate(R.layout.fragment_flash_card, container, false)
        présentateur = PrésentateurFlashCards(this)

        carte_avant = vue.findViewById<TextView>(R.id.carte_avant)
        carte_arriere = vue.findViewById<TextView>(R.id.carte_arriere)
        var scale = requireContext().applicationContext.resources.displayMetrics.density
        carte_avant.cameraDistance = 200000 * scale
        carte_arriere.cameraDistance = 200000 * scale
        anim_avant = AnimatorInflater.loadAnimator(requireContext().applicationContext, R.animator.animation_avant) as AnimatorSet
        anim_arriere = AnimatorInflater.loadAnimator(requireContext().applicationContext, R.animator.animation_arriere) as AnimatorSet

        attacherÉcouteurSuivant( vue )
        attacherÉcouteurRecommencer( vue )
        attacherÉcouteurMenu( vue )
        attacherÉcouteurCartes( vue )

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

    private fun attacherÉcouteurCartes(vue: View) {
        présentateur?.traiterDébuter()
        layoutCartes = vue.findViewById<FrameLayout>(R.id.layoutCartes)
        layoutCartes.setOnClickListener {
            présentateur?.traiterRetournerCarte()
        }
    }

    private fun attacherÉcouteurMenu(vue: View) {
//        btnMenu = vue.findViewById<Button>(R.id.btnMenu)
//        btnMenu.setOnClickListener {
//            présentateur?.traiterRetourMenu()
//        }
    }

    override fun afficherMessageErreur(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun naviguerVersMenu() {
        //navController.navigate(R.id.action_flashcards_to_menu)
    }

    override fun changerTexteQuestion(texteQuestion: String?) {
        carte_avant.text = texteQuestion
    }

    override fun changerTexteRéponse(texteRéponse: String?) {
        carte_arriere.text = texteRéponse
    }

    override fun afficherCarteAvant() {
        anim_avant.setTarget(carte_arriere)
        anim_arriere.setTarget(carte_avant)
        anim_arriere.start()
        anim_avant.start()
    }

    override fun afficherCarteArrière() {
        anim_avant.setTarget(carte_avant)
        anim_arriere.setTarget(carte_arriere)
        anim_avant.start()
        anim_arriere.start()
    }

}