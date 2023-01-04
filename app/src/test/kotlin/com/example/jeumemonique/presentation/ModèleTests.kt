package com.example.jeumemonique.presentation

import com.example.jeumemonique.accesAuxDonnees.ISourceDeDonnées
import com.example.jeumemonique.domaine.entité.Groupe
import com.example.jeumemonique.domaine.entité.Joueur
import com.example.jeumemonique.domaine.entité.ListeQuestions
import com.example.jeumemonique.domaine.entité.QuestionRéponse
import kotlin.test.Test
import kotlin.test.assertContentEquals

import org.mockito.Mockito
import kotlin.test.assertEquals

class ModèleTests {

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de connecter un joueur avec des identifiants invalides, on obtient une réponse fausse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob", "1234"))
            .thenReturn(false)

        val cobaye = Modèle(sourceDeTest)

        // Action
        val résultat_obtenu = cobaye.connexionJoueur("Bob", "1234")

        // Vérification
        val résultat_attendu = false

        assertEquals(résultat_attendu, résultat_obtenu)
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de connecter un joueur avec des identifiants valides, on obtient le joueur, ses listes et une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))
        Mockito.`when`(sourceDeTest.obtenirListesDuJoueur(Joueur("Bob", "Bob@test.com")))
            .thenReturn(
                mutableListOf(
                    ListeQuestions(1, "Liste test", Joueur("Bob", "Bob@test.com")),
                    ListeQuestions(2, "Liste test 2", Joueur("Bob", "Bob@test.com"))
                )
            )


        val cobaye = Modèle(sourceDeTest)

        // Action
        val résultats_obtenus = cobaye.connexionJoueur("Bob@test.com", "1234")
        val joueur_obtenu = cobaye.getJoueur()
        val listes_obtenues = cobaye.getListesDuJoueur()


        // Vérification
        val résultats_attendus = true
        val joueur_attendu = Joueur("Bob", "Bob@test.com")
        val listes_attendues = mutableListOf(
            ListeQuestions(1, "Liste test", Joueur("Bob", "Bob@test.com")),
            ListeQuestions(2, "Liste test 2", Joueur("Bob", "Bob@test.com"))
        )

        assertEquals(résultats_attendus, résultats_obtenus)
        assertEquals(joueur_attendu, joueur_obtenu)
        assertContentEquals(listes_attendues, listes_obtenues)

    }


    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de récupérer les listes d'un joueur, on obtient ses listes`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.obtenirListesDuJoueur(Joueur("Bob","Bob@test.com")))
            .thenReturn(mutableListOf( ListeQuestions(1, "Liste test",Joueur("Bob","Bob@test.com")),
                ListeQuestions(2, "Liste test 2",Joueur("Bob","Bob@test.com"))))
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")
        // Action
        val résultats_obtenus = cobaye.récupererListesDuJoueur()

        // Vérification
        val résultats_attendus = mutableListOf( ListeQuestions(1, "Liste test",Joueur("Bob","Bob@test.com")),
            ListeQuestions(2, "Liste test 2",Joueur("Bob","Bob@test.com")))

        assertContentEquals( résultats_attendus, résultats_obtenus )
    }


    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de récupérer les questions-réponse d'une ListeQuestion, on obtient une liste de question-réponse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.obtenirQuestionsRéponsesListe(2))
            .thenReturn(mutableListOf( QuestionRéponse(1,"Test?", "oui"), QuestionRéponse(2,"Test 2 ?", "oui 2")))

        val cobaye = Modèle( sourceDeTest )
        cobaye.selectionnerListe(ListeQuestions(2,"Liste test"))
        // Action
        val résultats_obtenus = cobaye.récupererQuestionsRéponsesListe()

        // Vérification
        val résultats_attendus = mutableListOf( QuestionRéponse(1,"Test?", "oui"), QuestionRéponse(2,"Test 2 ?", "oui 2"))

        assertContentEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de créer une ListeQuestion, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.ajouterListe(ListeQuestions(0,"Test",Joueur("Bob", "Bob@test.com"))))
            .thenReturn(true)

        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")

        // Action
        val résultats_obtenus = cobaye.créerListeQuestions("Test")

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de supprimer une ListeQuestion existante, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerListe(3))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )

        // Action
        val résultats_obtenus = cobaye.supprimerListeQuestions(ListeQuestions(3, "Test", Joueur("Test","test@test.com")))

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de supprimer une ListeQuestion invalide, on obtient une réponse fausse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerListe(3))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )

        // Action
        val résultats_obtenus = cobaye.supprimerListeQuestions(ListeQuestions(5, "Test", Joueur("Test","test@test.com")))

        // Vérification
        val résultats_attendus = false

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie d'ajouter une QuestionRéponse, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.ajouterQuestionRéponse(2,QuestionRéponse(0,"Test","Test")))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )
        cobaye.selectionnerListe(ListeQuestions(2,"Liste test"))

        // Action
        val résultats_obtenus = cobaye.ajouterQuestionRéponse(QuestionRéponse(0,"Test","Test"))

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de supprimer une QuestionRéponse existante, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerQuestionRéponse(3))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )

        // Action
        val résultats_obtenus = cobaye.supprimerQuestionRéponse(QuestionRéponse(3,"Test","Test"))

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de supprimer une QuestionRéponse invalide, on obtient une réponse fausse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerQuestionRéponse(3))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )

        // Action
        val résultats_obtenus = cobaye.supprimerQuestionRéponse(QuestionRéponse(4,"Test","Test"))

        // Vérification
        val résultats_attendus = false

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de récupérer les groupes d'un joueur, on obtient ses une liste de ses groupes`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.obtenirGroupesDuJoueur(Joueur("Bob","Bob@test.com")))
            .thenReturn(mutableListOf( Groupe("Groupe test",Joueur("Bob","Bob@test.com"),1),
                Groupe("Groupe test 2",Joueur("Bob","Bob@test.com",),5)))
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")
        // Action
        val résultats_obtenus = cobaye.récupererGroupesDuJoueur()

        // Vérification
        val résultats_attendus = mutableListOf( Groupe("Groupe test",Joueur("Bob","Bob@test.com"),1),
            Groupe("Groupe test 2",Joueur("Bob","Bob@test.com"),5))

        assertContentEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de créer un groupe, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.ajouterGroupe(Groupe("Test", Joueur("Bob","Bob@test.com"))))
            .thenReturn(true)

        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")

        // Action
        val résultats_obtenus = cobaye.créerGroupe("Test")

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de supprimer un groupe existant, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerGroupe(3))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )

        // Action
        val résultats_obtenus = cobaye.supprimerGroupe(Groupe("Test",Joueur("Bob", "Bob@test.com"), 3))

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de supprimer un groupe invalide, on obtient une réponse fausse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerGroupe(3))
            .thenReturn(true)

        val cobaye = Modèle( sourceDeTest )

        // Action
        val résultats_obtenus = cobaye.supprimerGroupe(Groupe("Test",Joueur("Bob", "Bob@test.com"), 5))

        // Vérification
        val résultats_attendus = false

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de quitter un groupe existant dont on n'est pas propriétaire, on obtient une réponse vraie`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerJoueurDuGroupe(3,"Bob@test.com"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")

        // Action
        val résultats_obtenus = cobaye.quitterGroupe(Groupe("Test",Joueur("Rick", "rick@test.com"), 3))

        // Vérification
        val résultats_attendus = true

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de quitter un groupe existant dont on est propriétaire, on obtient une réponse fausse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerJoueurDuGroupe(3,"Bob@test.com"))
            .thenReturn(false)
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")

        // Action
        val résultats_obtenus = cobaye.quitterGroupe(Groupe("Test",Joueur("Bob", "Bob@test.com"), 3))

        // Vérification
        val résultats_attendus = false

        assertEquals( résultats_attendus, résultats_obtenus )
    }

    @Test
    fun `test étant donné une source de données, lorsqu'on essaie de quitter un groupe invalide, on obtient une réponse fausse`() {

        // Mise en place
        val sourceDeTest = Mockito.mock(ISourceDeDonnées::class.java)
        Mockito.`when`(sourceDeTest.supprimerJoueurDuGroupe(3,"Bob@test.com"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.connexionJoueur("Bob@test.com", "1234"))
            .thenReturn(true)
        Mockito.`when`(sourceDeTest.obtenirJoueurParCourriel("Bob@test.com"))
            .thenReturn(Joueur("Bob", "Bob@test.com"))

        val cobaye = Modèle( sourceDeTest )
        cobaye.connexionJoueur("Bob@test.com","1234")

        // Action
        val résultats_obtenus = cobaye.quitterGroupe(Groupe("Test",Joueur("Rick", "rick@test.com"), 7))

        // Vérification
        val résultats_attendus = false

        assertEquals( résultats_attendus, résultats_obtenus )
    }

}
