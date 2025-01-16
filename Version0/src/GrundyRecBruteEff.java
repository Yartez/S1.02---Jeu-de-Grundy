import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Grundy game with AI for the machine
 * This program only contains the methods for testing jouerGagnant()
 * This version is raw without any improvements
 *
 * @author G.Audebert, Y.Monnot
 */

class GrundyRecBruteEff {

    /**
     * Global variables
     */
    long cpt; // elementary operations counter
    /**
     * Class constructor
     */
    GrundyRecBrute monGrundy = new GrundyRecBrute(); // For class methods GrundyRecBrute

    /**
     * Méthode principal du programme
     */
    void principal() {
        testEfficacite();
        boucleJeu();
    }

    /**
     * Main game loop
     */
    void boucleJeu() {
        boolean game = true;
        // Initializing the game
        int n = SimpleInput.getInt("Saisir la taille du jeu : ");
        ArrayList<Integer> jeu = new ArrayList<>(Collections.singletonList(n));
        
        while (monGrundy.estPossible(jeu)) {
            // Game display
            System.out.println("Jeu : " + jeu);
            // Game result
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
     * AI trick method
     */
    void tourIA(ArrayList<Integer> jeu) {
        // Game result
        boolean res = monGrundy.jouerGagnant(jeu);
        System.out.println("Résultat : " + res);
        
        if (!res) {
            // Play a random move
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
     * Testing the effectiveness of the method estGagnante
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
