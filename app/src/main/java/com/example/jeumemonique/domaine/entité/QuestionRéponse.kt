package com.example.jeumemonique.domaine.entité

class QuestionRéponse(var id : Int? = null, var question : String? = null, var réponse : String? = null) {
    override fun toString(): String {
        return "Question : $question \nRéponse : $réponse"
    }

    override operator fun equals(autre : Any?) : Boolean {
        if (autre == null) {
            return false
        }
        if (autre !is QuestionRéponse) {
            return false
        }
        return this.id == autre.id && this.question == autre.question && this.réponse == autre.réponse

    }
}