package com.example.jeumemonique.presentation

import com.example.jeumemonique.accesAuxDonnees.ISourceDeDonnées
import com.example.jeumemonique.accesAuxDonnees.SourceDeDonnéesBidon
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import com.example.jeumemonique.domaine.interacteur.ConnexionJoueurInt
import com.example.jeumemonique.domaine.interacteur.GestionGroupesInt
import com.example.jeumemonique.domaine.interacteur.GestionListesJoueurInt
import com.example.jeumemonique.domaine.interacteur.InscriptionJoueurInt
import kotlin.random.Random

class Modèle (var sourceDeDonnées: ISourceDeDonnées = SourceDeDonnéesBidon) {

    private lateinit var joueur : Joueur
    private lateinit var listesDuJoueur : MutableList<ListeQuestions>
    private var listeSelectionnée = ListeQuestions()
    private lateinit var groupeSelectionné : Groupe
    private var questionsRéponsesListeSelectionnée : MutableList<QuestionRéponse>? = mutableListOf()

    // Méthodes pour le jeu de FlashCard --------------------------------------------------

    var faceAvant = true
    var numeroQuestion = 0

    fun retournerCarte(){
        faceAvant = !faceAvant
    }

    fun getFace() : Boolean {
        return faceAvant
    }

    fun setFaceAvant(){
        faceAvant = true
    }

    fun questionSuivante(){
        if(numeroQuestion < questionsRéponsesListeSelectionnée?.size?.minus(1) ?: 0) {
            numeroQuestion++
            setFaceAvant()
        }
    }

    fun initialiserFlashCards(){
        setFaceAvant()
        numeroQuestion = 0
    }

    // Méthodes pour le jeu de Trouver Réponses --------------------------------------------------

    var compteurScore = 0
    var déjàRépondu = false
    var réponses = mutableListOf<QuestionRéponse>()

    fun mélangerRéponses(){
        réponses = mutableListOf<QuestionRéponse>()
        questionsRéponsesListeSelectionnée?.get(numeroQuestion)?.let { réponses.add(it) }

        var répAl = (0..questionsRéponsesListeSelectionnée?.size?.minus(1)!!).shuffled().take(4).toSet()
        for (i in 0 until 3){
            if(questionsRéponsesListeSelectionnée!![répAl.elementAt(i)] != questionsRéponsesListeSelectionnée?.get(numeroQuestion)) {
                réponses.add(questionsRéponsesListeSelectionnée!![répAl.elementAt(i)])
            }
        }
        if(réponses.size < 4){
            réponses.add(questionsRéponsesListeSelectionnée!![répAl.elementAt(3)])
        }
        réponses.shuffle()

    }

    fun répondreQuestion(){
        déjàRépondu = true
    }

    fun nouvelleQuestion(){
        déjàRépondu = false
    }

    fun questionSuivanteTrouverRéponse(){
        if(numeroQuestion < questionsRéponsesListeSelectionnée?.size?.minus(1) ?: 0) {
            numeroQuestion++
            setFaceAvant()
            nouvelleQuestion()
            mélangerRéponses()
        }
    }

    fun initialiserTrouverRéponse(){
        setFaceAvant()
        numeroQuestion = 0
        compteurScore = 0
        déjàRépondu = false
        mélangerRéponses()
    }

    // Méthodes pour la gestion des joueurs --------------------------------------------------

    fun connexionJoueur(courriel : String, motDePasse : String) : Boolean {
        var connexionValide = ConnexionJoueurInt(sourceDeDonnées).connexionJoueur(courriel, motDePasse)
        if(connexionValide){
            joueur = ConnexionJoueurInt(sourceDeDonnées).obtenirJoueurParCourriel(courriel)
            listesDuJoueur = GestionListesJoueurInt(sourceDeDonnées).récupererListesDuJoueur(joueur)
        }
        return connexionValide
    }

    fun inscriptionJoueur(nomUtilisateur : String, motDePasse : String, courriel : String) : Boolean {
        var joueurExistant = InscriptionJoueurInt(sourceDeDonnées).vérifierCourrielExistant(courriel)
        var inscriptionValide = false
        if(!joueurExistant){
            inscriptionValide = InscriptionJoueurInt(sourceDeDonnées).ajouterJoueur(Joueur(nomUtilisateur,courriel,motDePasse))
        }
        return inscriptionValide
    }

    fun getJoueur() : Joueur {
        return joueur
    }

    // Méthodes pour la gestion des listes des joueurs --------------------------------------------------

    fun récupererListesDuJoueur(): MutableList<ListeQuestions> {
        listesDuJoueur = GestionListesJoueurInt(sourceDeDonnées).récupererListesDuJoueur(joueur)
        return listesDuJoueur
    }

    fun getListesDuJoueur() : MutableList<ListeQuestions>{
        return listesDuJoueur
    }

    fun selectionnerListe(listeQuestions: ListeQuestions) {
        listeSelectionnée = listeQuestions
    }

    fun questionsRéponsesListeSelectionnée() {
        questionsRéponsesListeSelectionnée = récupererQuestionsRéponsesListe()
    }

    fun getquestionsRéponsesListeSelectionnée() : MutableList<QuestionRéponse>?{
        return questionsRéponsesListeSelectionnée
    }

    fun getListeSelectionnée() : ListeQuestions{
        return listeSelectionnée
    }

    fun créerListeQuestions(nomListeQuestions: String) : Boolean {
        return GestionListesJoueurInt(sourceDeDonnées).créerListe(nomListeQuestions,joueur)
    }

    fun supprimerListeQuestions(listeQuestions: ListeQuestions) : Boolean? {
        return listeQuestions.id?.let { GestionListesJoueurInt(sourceDeDonnées).supprimerListe(it) }
    }

    fun récupererQuestionsRéponsesListe() : MutableList<QuestionRéponse>? {
        return listeSelectionnée.id?.let { GestionListesJoueurInt(sourceDeDonnées).récupererQuestionsRéponses(it) }
    }

    fun ajouterQuestionRéponse(questionRéponse: QuestionRéponse) : Boolean{
        return GestionListesJoueurInt(sourceDeDonnées).ajouterQuestionRéponse(listeSelectionnée.id, questionRéponse)
    }

    fun supprimerQuestionRéponse(questionRéponse: QuestionRéponse) : Boolean {
        return GestionListesJoueurInt(sourceDeDonnées).supprimerQuestionRéponse(questionRéponse.id)
    }

    // Méthodes pour la gestion des groupes des joueurs --------------------------------------------------

    fun récupererGroupesDuJoueur(): MutableList<Groupe> {
        return GestionGroupesInt(sourceDeDonnées).récupererGroupesDuJoueur(joueur)
    }

    fun selectionnerGroupe(groupe: Groupe) {
        groupeSelectionné = groupe
    }

    fun getGroupeSelectionné() : Groupe{
        return groupeSelectionné
    }

    fun supprimerGroupe(groupe: Groupe) : Boolean? {
        return groupe.id?.let { GestionGroupesInt(sourceDeDonnées).supprimerGroupe(it) }
    }

    fun créerGroupe(nomGroupe: String) : Boolean {
        return GestionGroupesInt(sourceDeDonnées).créerGroupe(nomGroupe, joueur)
    }

    fun quitterGroupe(groupe: Groupe) : Boolean {
        return GestionGroupesInt(sourceDeDonnées).supprimerMembre(groupe.id, joueur.courriel)
    }

    fun récupererMembresDuGroupe(): MutableList<Joueur>? {
        var membres = groupeSelectionné.id?.let { GestionGroupesInt(sourceDeDonnées).récupererMembresDuGroupe(it) }
        return membres
    }

    fun getMembreParCourriel(courriel : String) : Joueur{
        var joueur = ConnexionJoueurInt(sourceDeDonnées).obtenirJoueurParCourriel(courriel)
        return joueur
    }

    fun supprimerMembreDuGroupe(membre : Joueur) : Boolean {
        return GestionGroupesInt(sourceDeDonnées).supprimerMembre(groupeSelectionné.id, membre.courriel)
    }

    fun ajouterMembreAuGroupe(membre : Joueur) : Boolean{
        return GestionGroupesInt(sourceDeDonnées).ajouterMembre(groupeSelectionné.id, membre.courriel)
    }

    fun récupererListesPartagées() : MutableList<ListeQuestions>?{
        var listes = groupeSelectionné.id?.let { GestionGroupesInt(sourceDeDonnées).récupererListesPartagées(it) }
        return listes
    }

    fun getToutesLesListesPartagées() : MutableList<ListeQuestions> {
        var toutesLesListesPartagées = mutableListOf<ListeQuestions>()
        var groupes = récupererGroupesDuJoueur()
        for (groupe in groupes){
            val listesPartagéesDuGroupe = groupe.id?.let {
                GestionGroupesInt(sourceDeDonnées).récupererListesPartagées(
                    it
                )
            }
            if (listesPartagéesDuGroupe != null) {
                toutesLesListesPartagées.addAll(listesPartagéesDuGroupe)
            }
        }
        return toutesLesListesPartagées
    }

    fun partagerListe(listeAPartager: ListeQuestions) : Boolean {
        return GestionGroupesInt(sourceDeDonnées).partagerListe(groupeSelectionné.id, listeAPartager.id)
    }

    fun supprimerListePartagée(listePartagée: ListeQuestions) : Boolean {
        return GestionGroupesInt(sourceDeDonnées).supprimerListePartagéee(groupeSelectionné.id, listePartagée.id)
    }

    fun getListePartagéeSelectionnée(): ListeQuestions {
        return listeSelectionnée
    }

    fun selectionnerListePartagée(listePartagée: ListeQuestions) {
        listeSelectionnée = listePartagée
    }
}

val modèle = Modèle()