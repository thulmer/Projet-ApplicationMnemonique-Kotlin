package com.example.jeumemonique.accesAuxDonnees

import android.widget.ListPopupWindow
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import com.example.jeumemonique.presentation.modèle

object SourceDeDonnéesBidon : ISourceDeDonnées {

    var joueurMichel = Joueur("michel", "michel@lol.com", "michel123")
    var joueurToto = Joueur("Toto", "toto@lol.com", "toto123")
    var joueurBob = Joueur("Bob", "bob@lol.com", "bob123")

    var joueurs = mutableListOf<Joueur>(joueurMichel, joueurToto, joueurBob)

    var listeMichelCapitales = ListeQuestions(0,"Liste des capitales", joueurMichel)
    var listeMichelInformatique = ListeQuestions(1,"Liste info", joueurMichel)
    var listeTotoCalcul = ListeQuestions(2,"Liste calcul", joueurToto, )
    var listeBobEspagnol = ListeQuestions(3,"Liste allemand", joueurBob)
    var listeBobAllemand = ListeQuestions(4,"Liste espagnol", joueurBob)

    var listesDesJoueurs = mutableListOf<ListeQuestions>(listeMichelCapitales,listeMichelInformatique, listeTotoCalcul, listeBobAllemand, listeBobEspagnol)

    var listeDesQuestionsRéponses = mutableListOf<QuestionRéponse>(QuestionRéponse(0,"Capitale des USA","Washington"), QuestionRéponse(1,"Capitale de la France","Paris"), QuestionRéponse(2,"Capitale de l'Allemagne","Berlin"),
        QuestionRéponse(3,"Que veut dire USB ?","Universal Serial Bus"),
        QuestionRéponse(4,"C'est quoi Kotlin?","Un langage de programmation"), QuestionRéponse(5,"Que veut dire FTP","File Tranfer Protocol"),
        QuestionRéponse(6,"2+2 =  ?", "4"),
        QuestionRéponse(7,"6*5 = ?","30"), QuestionRéponse(8,"4/2 = ?","2"),
        QuestionRéponse(9,"Comment dit-on bonjour?","Guten tag"),QuestionRéponse(10,"Comment dit-on merci?","Danke"),
        QuestionRéponse(11,"Comment dit-on bonjour?","Hola"),QuestionRéponse(12,"Comment dit-on merci?","Gracias")
    )

    var questionsRéponsesListes = mutableListOf<Pair<Int, Int>>(Pair(0,0), Pair(0,1), Pair(0,2),
        Pair(1,3), Pair(1,4), Pair(1,5),
        Pair(2,6), Pair(2,7), Pair(2,8),
        Pair(3,9), Pair(3,10),
        Pair(4,11), Pair(4,12)
    )

    var listesPartagées = mutableListOf<Pair<Int, Int>>(Pair(0,0), Pair(0,1), Pair(1,1),
        Pair(4,3)
    )


    var membresGroupes = mutableListOf<Pair<String, Int>>(Pair("toto@lol.com",0), Pair("bob@lol.com",0), Pair("toto@lol.com",1),
        Pair("michel@lol.com",2), Pair("bob@lol.com",2), Pair("michel@lol.com",3)
    )

    var groupes = mutableListOf<Groupe>(
        Groupe("Groupe informatique", joueurMichel,0), Groupe("Groupe géographie", joueurMichel,1), Groupe("C'est le fun", joueurToto, 2,
          ), Groupe("Langues",joueurBob,3)
    )

    override fun connexionJoueur(courriel : String, motDePasse : String): Boolean {
        var connexion = false
        println(courriel)
        println(motDePasse)
        println(joueurs.filter { it.courriel == courriel && it.motDePasse == motDePasse }.isNotEmpty())
        println(joueurs.filter { it.courriel == courriel && it.motDePasse == motDePasse })
        if (joueurs.filter { it.courriel == courriel && it.motDePasse == motDePasse }.isNotEmpty()){
            connexion = true
        }
        return connexion
    }

    override fun vérifierCourrielExistant(courriel: String): Boolean {
        var inscription = false
        if (obtenirJoueurs().filter { it.courriel == courriel }.isEmpty()){
            inscription = true
        }
        return inscription    }


    override fun ajouterJoueur(joueur : Joueur) : Boolean {
        joueurs.add(joueur)
        return true
    }

    override fun obtenirJoueurs(): List<Joueur> {
        return joueurs
    }

    override fun obtenirJoueurParCourriel(courriel : String): Joueur {
        var joueur = Joueur()
        if (obtenirJoueurs().filter { it.courriel == courriel }.isNotEmpty()){
            joueur.nom = obtenirJoueurs().filter { it.courriel == courriel }[0].nom
            joueur.courriel = obtenirJoueurs().filter { it.courriel == courriel }[0].courriel
        }
        return joueur
    }

    override fun obtenirListesDuJoueur(joueur : Joueur): MutableList<ListeQuestions> {
        var listesDuJoueur = mutableListOf<ListeQuestions>()
        listesDuJoueur = listesDesJoueurs.filter { it.propriétaire == joueur }.toMutableList()
        return listesDuJoueur
    }

    override fun ajouterListe(nouvelleListe: ListeQuestions): Boolean {

        val liste = listesDesJoueurs.sortedByDescending { it.id }.toMutableList()
        nouvelleListe.id = liste[0].id?.plus(1)
        return listesDesJoueurs.add(nouvelleListe)
    }

    override fun supprimerListe(id: Int): Boolean {
        var suppression = false
        if (listesDesJoueurs.filter { it.id == id }.isNotEmpty()){
            listesDesJoueurs.remove(listesDesJoueurs.filter { it.id == id }[0])
            suppression = true
        }
        return suppression       }

    override fun modifierListe(liste: ListeQuestions): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenirQuestionsRéponsesListe(idListe: Int): MutableList<QuestionRéponse> {
        var QRListe = mutableListOf<QuestionRéponse>()
        var filtre = questionsRéponsesListes.filter { it.first == idListe }
        for (paire in filtre){
            QRListe.add(listeDesQuestionsRéponses.first { it.id == paire.second })
        }
        return QRListe
    }

    override fun ajouterQuestionRéponse(listeId: Int?, questionRéponse: QuestionRéponse): Boolean {
        val liste = listeDesQuestionsRéponses.sortedByDescending { it.id }.toMutableList()
        questionRéponse.id = liste[0].id?.plus(1)
        questionsRéponsesListes.add(Pair(listeId,questionRéponse.id) as Pair<Int, Int>)
        return listeDesQuestionsRéponses.add(questionRéponse)
    }

    override fun supprimerQuestionRéponse(questionId: Int?): Boolean {
        var suppression = false
        if (listeDesQuestionsRéponses.filter { it.id == questionId }.isNotEmpty()){
            listeDesQuestionsRéponses.remove(listeDesQuestionsRéponses.filter { it.id == questionId }[0])
            questionsRéponsesListes.remove(questionsRéponsesListes.filter { it.second == questionId }[0])
            suppression = true
        }
        return suppression        }



    override fun obtenirGroupesDuJoueur(joueur: Joueur): MutableList<Groupe> {
        var GroupesDuJoueur = mutableListOf<Groupe>()
        GroupesDuJoueur = groupes.filter { it.membres.contains(joueur) }.toMutableList()
        var groupesAutres = membresGroupes.filter { it.first == joueur.courriel }.toMutableList()
        for (groupe in groupesAutres){
            GroupesDuJoueur.add(groupes.filter { it.id == groupe.second }[0])
        }

        return GroupesDuJoueur
    }

    override fun supprimerGroupe(id: Int): Boolean {
        var suppressionValidée = false
        var groupeASupprimer = groupes.filter { it.id == id }[0]
        suppressionValidée = groupes.remove(groupeASupprimer)
        for (i in 0 until membresGroupes.size){
            if(membresGroupes[i].second == id){
                membresGroupes.removeAt(i)
            }
        }
        return suppressionValidée
    }

    override fun supprimerJoueurDuGroupe(groupeId: Int?, courriel: String?): Boolean {
        for (i in 0 until membresGroupes.size){
            if(membresGroupes[i].second == groupeId && membresGroupes[i].first == courriel){
                membresGroupes.removeAt(i)
            }
        }
        return true
    }

    override fun obtenirListesPartagées(groupeId: Int): MutableList<ListeQuestions> {
        var liste = mutableListOf<ListeQuestions>()
        for (i in 0 until listesPartagées.size){
            if(listesPartagées[i].second == groupeId){
                var listePartagée = listesDesJoueurs.filter { it.id == listesPartagées[i].first }[0]
                liste.add(listePartagée)
            }
        }
        return liste
    }

    override fun partagerListe(groupeId: Int?, listeId: Int?): Boolean {
        listesPartagées.add(Pair(listeId, groupeId) as Pair<Int, Int>)
        return true    }

    override fun supprimerListePartagée(groupeId: Int?, listeId: Int?): Boolean {
        for (i in 0 until listesPartagées.size){
            if(listesPartagées[i].second == groupeId && listesPartagées[i].first == listeId){
                listesPartagées.removeAt(i)
            }
        }
        return true    }

    override fun ajouterMembreAuGroupe(groupeId: Int?, courriel: String?): Boolean {
        membresGroupes.add(Pair(courriel, groupeId) as Pair<String, Int>)
        return true
    }

    override fun ajouterGroupe(nouveauGroupe: Groupe): Boolean {
        groupes.add(nouveauGroupe)
        return true
    }

    override fun modifierGroupe(groupe: Groupe): Boolean {
        TODO()
    }

    override fun obtenirMembresDuGroupe(groupeId: Int): MutableList<Joueur> {
        var liste = mutableListOf<Joueur>()
        groupes.firstOrNull { it.id == groupeId }?.let {
            liste.add(
                it.propriétaire
            )
        }
        for (i in 0 until membresGroupes.size){
            if(membresGroupes[i].second == groupeId){
                var joueur = obtenirJoueurParCourriel(membresGroupes[i].first)
                liste.add(joueur)
            }
        }

        return liste
    }


}