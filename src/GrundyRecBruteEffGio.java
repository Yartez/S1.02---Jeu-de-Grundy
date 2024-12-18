import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
        boucleJeu();
    }

    /**
     * Boucle principale du jeu
     */
    void boucleJeu() {
        boolean game = true;
        // Initialisation du jeu
        int n = 0;
        ArrayList<Integer> jeu = new ArrayList<>(n);
        // Saisie du jeu    
        n = SimpleInput.getInt("Saisir la taille du jeu : ");
        jeu.add(n);
        while (monGrundy.estPossible(jeu)) {
            // Affichage du jeu
            System.out.println("Jeu : " + jeu);
            // Résultat du jeu
            boolean res = monGrundy.jouerGagnant(jeu);
            System.out.println("Résultat : " + res);
            if (res){
            }else if (!res){
                
                int nbAEnlever = (int)Math.random() * n;
                monGrundy.enlever(jeu, 0, 4);
                System.out.println(jeu);
            }
            int userLigne = SimpleInput.getInt("Sur quelle tas enlever des allumettes? ");
            int userMove = SimpleInput.getInt("Combien d'allumettes à enlever ? ");
            monGrundy.enlever(jeu, userLigne-1, userMove);
        }
        System.out.println(jeu);
    }
}
