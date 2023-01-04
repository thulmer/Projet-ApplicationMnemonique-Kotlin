package com.example.jeumemonique.presentation.voirQuestionsListe

import com.example.jeumemonique.domaine.entité.QuestionRéponse

interface IContratVuePrésentateurVoirQuestionsListe {
    interface IVueVoirQuestionsListe{
        fun afficherMessageErreur(s: String)
        fun naviguerVersVoirListesQuestions()
        fun initialiser()
        fun afficherNomListe(nom: String)
        fun afficherChargement()
        fun masquerChargement()
    }

    interface IPrésentateurVoirQuestionsListe{
        fun traiterRetour()
        fun traiterSupprimerQuestionRéponse(questionRéponse: QuestionRéponse)
        fun traiterAjouterQuestionRéponse(question: String, réponse: String)
        fun traiterModifierListeQuestions(position: Int)
        fun remplirListeQuestionsRéponse()
    }

}