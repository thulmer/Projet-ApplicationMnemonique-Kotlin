package com.example.jeumemonique.accesAuxDonnees

import android.content.Context
import android.util.JsonReader
import android.util.JsonToken
import com.android.volley.Request
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import org.json.JSONObject
import java.io.IOException

import java.io.StringReader
import java.net.URL
import java.util.concurrent.ExecutionException

class SourceDeDonnéesHTTP(var ctx: Context, var urlSource: URL) : ISourceDeDonnées {

    override fun connexionJoueur(courriel: String, motDePasse: String): Boolean {
        var joueur = JSONObject()
        joueur.put("Courriel", courriel)
        joueur.put("MotDePasse", motDePasse)
        var body = joueur.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/Joueur/connexion",
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return false
        }
    }

    override fun obtenirJoueurParCourriel(courriel: String): Joueur {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/Joueur/courriel?courriel=" + courriel,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return réponseJsonToJoueur(JsonReader(StringReader(promesse.get())))
        }  catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return Joueur()
        }
    }

    override fun vérifierCourrielExistant(courriel: String): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/Joueur/courriel?courriel=" + courriel,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            var joueurExistant = réponseJsonToJoueur(JsonReader(StringReader(promesse.get())))
            return joueurExistant.courriel != null
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return true
        }
    }

    override fun ajouterJoueur(joueur: Joueur): Boolean {
        var joueurJSON = JSONObject()
        joueurJSON.put("Identifiant", joueur.nom)
        joueurJSON.put("Courriel", joueur.courriel)
        joueurJSON.put("MotDePasse", joueur.motDePasse)
        var body = joueurJSON.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/Joueur",
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }  catch (e: Exception) {
            return false
        }
    }

    override fun obtenirJoueurs(): List<Joueur> {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête =
            StringRequest(Request.Method.GET, urlSource.toString() + "/Joueur", promesse, promesse)
        queue.add(requête);

        try {
            return réponseJsonToJoueurs(JsonReader(StringReader(promesse.get())))
        }  catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }catch (e: Exception) {
            return mutableListOf<Joueur>()
        }
    }


    override fun obtenirListesDuJoueur(joueur: Joueur): MutableList<ListeQuestions> {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/ListeQuestions/joueur?courriel=" + joueur.courriel,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return réponseJsonToListes(JsonReader(StringReader(promesse.get())))
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return mutableListOf<ListeQuestions>()
        }
    }

    override fun ajouterListe(nouvelleListe: ListeQuestions): Boolean {
        var liste = JSONObject()
        var propriétaire = JSONObject()
        liste.put("NomListe", nouvelleListe.nom)
        propriétaire.put("Courriel", nouvelleListe.propriétaire?.courriel)
        liste.put("Proprietaire", propriétaire)

        var body = liste.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/ListeQuestions",
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }
        catch (e : Exception){
            return false
        }
    }

    override fun supprimerListe(id: Int): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.DELETE,
            urlSource.toString() + "/ListeQuestions/" + id,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        }  catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return false
        }
    }

    override fun modifierListe(listeModifiée: ListeQuestions): Boolean {
        var liste = JSONObject()
        var propriétaire = JSONObject()
        liste.put("NomListe", listeModifiée.nom)
        propriétaire.put("Courriel", listeModifiée.propriétaire?.courriel)
        liste.put("Proprietaire", propriétaire)

        var body = liste.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.PUT,
            urlSource.toString() + "/ListeQuestions",
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return false
        }
    }

    override fun obtenirQuestionsRéponsesListe(idListe: Int): MutableList<QuestionRéponse> {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/QuestionReponse/liste?listeId=" + idListe,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return réponseJsonToQuestionsRéponses(JsonReader(StringReader(promesse.get())))
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return mutableListOf<QuestionRéponse>()
        }
    }

    override fun ajouterQuestionRéponse(
        listeId: Int?,
        nouvelleQuestionRéponse: QuestionRéponse
    ): Boolean {
        var questionRéponse = JSONObject()
        questionRéponse.put("Question", nouvelleQuestionRéponse.question)
        questionRéponse.put("Reponse", nouvelleQuestionRéponse.réponse)

        var body = questionRéponse.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/QuestionReponse?liste=" + listeId,
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        }catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch(e : Exception){
            return false
        }
    }

    override fun supprimerQuestionRéponse(id: Int?): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.DELETE,
            urlSource.toString() + "/QuestionReponse/" + id,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }
        catch(e : Exception){
            return false
        }
    }


    override fun obtenirGroupesDuJoueur(joueur: Joueur): MutableList<Groupe> {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/Groupe?courriel=" + joueur.courriel,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return réponseJsonToGroupes(JsonReader(StringReader(promesse.get())))
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }
        catch (e: Exception) {
            return mutableListOf<Groupe>()
        }
    }

    override fun ajouterGroupe(nouveauGroupe: Groupe): Boolean {
        var groupe = JSONObject()
        var propriétaire = JSONObject()
        groupe.put("NomGroupe", nouveauGroupe.nom)
        propriétaire.put("Courriel", nouveauGroupe.propriétaire.courriel)
        groupe.put("Proprietaire", propriétaire)

        var body = groupe.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/Groupe",
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }
        catch (e: Exception) {
            return false
        }
    }

    override fun supprimerGroupe(id: Int): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.DELETE,
            urlSource.toString() + "/Groupe/" + id,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return false
        }
    }

    override fun modifierGroupe(groupeModifié: Groupe): Boolean {
        var groupe = JSONObject()
        var propriétaire = JSONObject()
        groupe.put("NomGroupe", groupeModifié.nom)
        propriétaire.put("Courriel", groupeModifié.propriétaire?.courriel)
        groupe.put("Proprietaire", propriétaire)

        var body = groupe.toString()

        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = object : StringRequest(
            Request.Method.PUT,
            urlSource.toString() + "/Groupe",
            promesse,
            promesse
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        queue.add(requête)

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }
        catch (e: InterruptedException) {
            return false
        }
    }

    override fun obtenirMembresDuGroupe(groupeId: Int): MutableList<Joueur> {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/Groupe/membres?groupeId=" + groupeId,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return réponseJsonToJoueurs(JsonReader(StringReader(promesse.get())))
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return mutableListOf<Joueur>()
        }
    }

    override fun ajouterMembreAuGroupe(groupeId: Int?, courriel: String?): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/Groupe/membre?courriel=" + courriel + "&groupeId=" + groupeId,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return false
        }
    }

    override fun supprimerJoueurDuGroupe(groupeId: Int?, courriel: String?): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.DELETE,
            urlSource.toString() + "/Groupe/membre?courriel=" + courriel + "&groupeId=" + groupeId,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return false
        }
    }

    override fun obtenirListesPartagées(groupeId: Int): MutableList<ListeQuestions> {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.GET,
            urlSource.toString() + "/Groupe/listes?groupeId=" + groupeId,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return réponseJsonToListes(JsonReader(StringReader(promesse.get())))
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        } catch (e: Exception) {
            return mutableListOf<ListeQuestions>()
        }
    }

    override fun partagerListe(groupeId: Int?, listeId: Int?): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.POST,
            urlSource.toString() + "/Groupe/liste?listeId=" + listeId + "&groupeId=" + groupeId,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            return false
        }catch (e: Exception) {
            return false
        }
    }

    override fun supprimerListePartagée(groupeId: Int?, listeId: Int?): Boolean {
        val queue = Volley.newRequestQueue(ctx)

        val promesse: RequestFuture<String> = RequestFuture.newFuture()

        val requête = StringRequest(
            Request.Method.DELETE,
            urlSource.toString() + "/Groupe/liste?listeId=" + listeId + "&groupeId=" + groupeId,
            promesse,
            promesse
        )
        queue.add(requête);

        try {
            return promesse.get().toBoolean()
        } catch (e: ExecutionException) {
            throw AccèsRessourcesException(e)
        }
        catch (e: Exception) {
            return false
        }
    }


    private fun réponseJsonToJoueurs(jsonReader: JsonReader): MutableList<Joueur> {
        var joueurs = mutableListOf<Joueur>()
        try {
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                joueurs.add(réponseJsonToJoueur(jsonReader))
            }
            jsonReader.endArray()
        } catch (e: java.lang.IllegalStateException) {
            e.printStackTrace()
        }
        return joueurs
    }

    private fun réponseJsonToJoueur(jsonReader: JsonReader): Joueur {
        var joueur = Joueur()
        try {
            jsonReader.beginObject()
            while (jsonReader.hasNext()) {
                val clé = jsonReader.nextName()
                when (clé) {
                    "Identifiant" -> {
                        if (jsonReader.peek() != JsonToken.NULL) {
                            joueur.nom = jsonReader.nextString()
                        } else {
                            jsonReader.skipValue()
                        }
                    }
                    "Courriel" -> {
                        joueur.courriel = jsonReader.nextString()
                    }
                    else -> {
                        jsonReader.skipValue()
                    }
                }
            }
            jsonReader.endObject()
        } catch (e: IOException) {
            return joueur
        }
        return joueur
    }

    private fun réponseJsonToListes(jsonReader: JsonReader): MutableList<ListeQuestions> {
        var listes = mutableListOf<ListeQuestions>()
        try {
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                listes.add(réponseJsonToListeQuestion(jsonReader))
            }
            jsonReader.endArray()
        } catch (e: java.lang.IllegalStateException) {
            e.printStackTrace()
        }
        return listes
    }

    private fun réponseJsonToListeQuestion(jsonReader: JsonReader): ListeQuestions {
        var nom: String? = null
        var id: Int? = null
        var proprietaire: Joueur? = null
        var questionsRéponses = mutableListOf<QuestionRéponse>()
        jsonReader.beginObject()
        while (jsonReader.hasNext()) {
            val clé = jsonReader.nextName()
            when (clé) {
                "ListeQuestionsId" -> {
                    id = jsonReader.nextInt()
                }
                "NomListe" -> {
                    nom = jsonReader.nextString()
                }
                "Proprietaire" -> {
                    proprietaire = réponseJsonToJoueur(jsonReader)
                }
                else -> {
                    jsonReader.skipValue()
                }
            }
        }
        jsonReader.endObject()

        return ListeQuestions(
            id ?: 0,
            nom ?: "Inconnu",
            proprietaire ?: Joueur(),
        )
    }

    private fun réponseJsonToQuestionsRéponses(jsonReader: JsonReader): MutableList<QuestionRéponse> {
        var questionsRéponses = mutableListOf<QuestionRéponse>()
        try {
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                questionsRéponses.add(réponseJsonToQuestionRéponse(jsonReader))
            }
            jsonReader.endArray()
        } catch (e: java.lang.IllegalStateException) {
            e.printStackTrace()
        }
        return questionsRéponses
    }

    private fun réponseJsonToQuestionRéponse(jsonReader: JsonReader): QuestionRéponse {
        var questionRéponse = QuestionRéponse()
        try {
            jsonReader.beginObject()
            while (jsonReader.hasNext()) {
                val clé = jsonReader.nextName()
                when (clé) {
                    "QuestionReponseId" -> {
                        questionRéponse.id = jsonReader.nextInt()
                    }
                    "Question" -> {
                        questionRéponse.question = jsonReader.nextString()
                    }
                    "Reponse" -> {
                        questionRéponse.réponse = jsonReader.nextString()
                    }
                    else -> {
                        jsonReader.skipValue()
                    }
                }
            }
            jsonReader.endObject()
        } catch (e: IOException) {
            return questionRéponse
        }
        return questionRéponse
    }

    private fun réponseJsonToGroupes(jsonReader: JsonReader): MutableList<Groupe> {
        var groupes = mutableListOf<Groupe>()
        try {
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                groupes.add(groupeJsonToGroupe(jsonReader))
            }
            jsonReader.endArray()
        } catch (e: java.lang.IllegalStateException) {
            e.printStackTrace()
        }
        return groupes
    }

    private fun groupeJsonToGroupe(jsonReader: JsonReader): Groupe {
        var nom: String? = null
        var id: Int? = null
        var proprietaire: Joueur? = null
        jsonReader.beginObject()
        while (jsonReader.hasNext()) {
            val clé = jsonReader.nextName()
            when (clé) {
                "GroupeId" -> {
                    id = jsonReader.nextInt()
                }
                "NomGroupe" -> {
                    nom = jsonReader.nextString()
                }
                "Proprietaire" -> {
                    proprietaire = réponseJsonToJoueur(jsonReader)
                }
                else -> {
                    jsonReader.skipValue()
                }
            }
        }
        jsonReader.endObject()

        return Groupe(nom ?: "Inconnu", proprietaire ?: Joueur(), id ?: 0)
    }
}