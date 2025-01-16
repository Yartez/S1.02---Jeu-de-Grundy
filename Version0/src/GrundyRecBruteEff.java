import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Jeu de Grundy avec IA pour la machine
 * Ce programme ne contient que les méthodes permettant de tester jouerGagnant()
 * Cette version est brute sans aucune amélioration
 *
 * @author G.Audebert, Y.Monnot
 */

class GrundyRecBruteEff {

    /**
     * Variables globales
     */
    long cpt; // compteur d'opérations élémentaires
    /**
     * Constructeur de la classe
     */
    GrundyRecBrute monGrundy = new GrundyRecBrute(); // Pour les méthodes de la classe GrundyRecBrute

    /**
     * Méthode principal du programme
     */
    void principal() {
        testEfficacite();
        boucleJeu();
    }

    /**
     * Boucle principale du jeu
     */
    void boucleJeu() {
        boolean game = true;
        // Initialisation du jeu
        int n = SimpleInput.getInt("Saisir la taille du jeu : ");
        ArrayList<Integer> jeu = new ArrayList<>(Collections.singletonList(n));
        
        while (monGrundy.estPossible(jeu)) {
            // Affichage du jeu
            System.out.println("Jeu : " + jeu);
            // Résultat du jeu
            tourIA(jeu);
            System.out.println("Jeu : " + jeu);
            
            if (monGrundy.estPossible(jeu)) {
                int userLigne = SimpleInput.getInt("Sur quelle tas enlever des allumettes? ");
                int userMove = SimpleInput.getInt("Combien d'allumettes à enlever ? ");
                monGrundy.enlever(jeu, userLigne - 1, userMove);
            }
        }
        
        System.out.println("Jeu terminé : " + jeu);
    }
    /**
     * Méthode du tour de l'IA
     */
    void tourIA(ArrayList<Integer> jeu) {
        // Résultat du jeu
        boolean res = monGrundy.jouerGagnant(jeu);
        System.out.println("Résultat : " + res);
        
        if (!res) {
            // Jouer un coup aléatoire
            Random rand = new Random();
            int tas, allumettes;
            do {
                tas = rand.nextInt(jeu.size());
                allumettes = rand.nextInt(jeu.get(tas)) + 1;
            } while (allumettes == jeu.get(tas) || (jeu.get(tas) - allumettes) == allumettes);
            
            System.out.println("Coup aléatoire : enlever " + allumettes + " allumettes du tas " + (tas + 1));
            monGrundy.enlever(jeu, tas, allumettes);
        }
    }

    /**
     * Test de l'efficacité de la méthode estGagnante
     */
    void testEfficacite() {
        System.out.println();
        System.out.println("*** testEfficacite() ***");
        for (int n = 3; n <= 20; n++) {
            ArrayList<Integer> jeu = new ArrayList<>(Collections.singletonList(n));
            monGrundy.estGagnante(jeu);
        }
    }
}
