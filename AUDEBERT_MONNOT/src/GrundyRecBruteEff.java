import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class implements the Grundy game with an AI opponent. The game involves
 * splitting piles of matches into two unequal piles, and the player who cannot
 * make a move loses. The AI uses a brute-force recursive approach to determine
 * winning and losing positions.
 * 
 * The class provides methods to:
 * - Play the game with an AI opponent.
 * - Determine if a game configuration is winning or losing.
 * - Test the efficiency of the game logic.
 * - Perform various game operations such as splitting piles and checking possible moves.
 * 
 * The main method of the program initializes the game and runs a loop where the
 * player and AI take turns until the game ends.
 * 
 * The AI's strategy is to always play a winning move if one exists, otherwise,
 * it plays a random move.
 * 
 * The class also includes several test methods to verify the correctness of the
 * game logic.
 * 
 * @author G.Audebert, Y.Monnot
 * @version 7.2.7
 * @since 2025-01-17
 */

class GrundyRecBruteEff {

    /**
     * Global variables
     */
    long cpt; // counter of elementary operations

    /**
     * Main method of the program
     */
    void principal() {
        System.out.println("\r\n" + //
                        "  _______        _     _____              __  __   __ _   _               _           \r\n" + //
                        " |__   __|      | |   |  __ \\            |  \\/  | /_/| | | |             | |          \r\n" + //
                        "    | | ___  ___| |_  | |  | | ___  ___  | \\  / | ___| |_| |__   ___   __| | ___  ___ \r\n" + //
                        "    | |/ _ \\/ __| __| | |  | |/ _ \\/ __| | |\\/| |/ _ \\ __| '_ \\ / _ \\ / _` |/ _ \\/ __|\r\n" + //
                        "    | |  __/\\__ \\ |_  | |__| |  __/\\__ \\ | |  | |  __/ |_| | | | (_) | (_| |  __/\\__ \\\r\n" + //
                        "    |_|\\___||___/\\__| |_____/ \\___||___/ |_|  |_|\\___|\\__|_| |_|\\___/ \\__,_|\\___||___/\r\n" + //
                        "                                                                                      \r\n" + //
                        "                                                                                      \r\n" + //
                        "");
        testJouerGagnant();
        testPremier();
        testSuivant();
        testEstGagnanteEfficacite();
        System.out.println(" ");
        System.out.println("\r\n" + //
                        "       _                  _         _____                      _       \r\n" + //
                        "      | |                | |       / ____|                    | |      \r\n" + //
                        "      | | ___ _   _    __| | ___  | |  __ _ __ _   _ _ __   __| |_   _ \r\n" + //
                        "  _   | |/ _ \\ | | |  / _` |/ _ \\ | | |_ | '__| | | | '_ \\ / _` | | | |\r\n" + //
                        " | |__| |  __/ |_| | | (_| |  __/ | |__| | |  | |_| | | | | (_| | |_| |\r\n" + //
                        "  \\____/ \\___|\\__,_|  \\__,_|\\___|  \\_____|_|   \\__,_|_| |_|\\__,_|\\__, |\r\n" + //
                        "                                                                  __/ |\r\n" + //
                        "                                                                 |___/ \r\n" + //
                        "");
        boucleJeu();
    }

    /**
     * Main game loop
     */
    void boucleJeu() {
        boolean game = true;
        int player = 0; // O = player, 1 = AI
        // Initializing the game
        int n = SimpleInput.getInt("Saisir la taille du jeu : ");
        ArrayList<Integer> jeu = new ArrayList<>(Collections.singletonList(n));
        
        while (estPossible(jeu)) {
            player = 0;
            // Game display
            System.out.println("\nJeu : " + jeu);
            // Game result
            tourIA(jeu);
            System.out.println("\nJeu : " + jeu);
            
            if (estPossible(jeu)) {
                player = 1;
                int userLigne;
                int userMove;
                do {
                    userLigne = SimpleInput.getInt("Sur quelle tas enlever des allumettes? ");
                } while (userLigne < 1 || userLigne > jeu.size() || jeu.get(userLigne - 1) <= 2);

                do {
                    userMove = SimpleInput.getInt("Combien d'allumettes à enlever ? ");
                } while (userMove < 1 || userMove >= jeu.get(userLigne - 1) || 2 * userMove == jeu.get(userLigne - 1) || (jeu.get(userLigne - 1) - userMove) == userMove);

                enlever(jeu, userLigne - 1, userMove);
            }
        }
        System.out.println("\nJeu terminé : " + jeu);
        if(player == 1 ) {
            System.out.println("\nVous avez gagné !");
        } else {
            System.out.println("\nVictoire de l'IA !");
        }
    }
    /**
     * AI turn method
     * @param jeu game board
     */
    void tourIA(ArrayList<Integer> jeu) {
        // Game result
        boolean res = jouerGagnant(jeu);

        if (!res) {
             // Play a random move
            Random rand = new Random();
            int tas, allumettes;
            do {
                tas = rand.nextInt(jeu.size());
                allumettes = rand.nextInt(jeu.get(tas)) + 1;
            } while (allumettes == jeu.get(tas) || (jeu.get(tas) - allumettes) == allumettes);
            
            System.out.println("Coup aléatoire fait par l'IA : enlever " + allumettes + " allumettes du tas " + (tas + 1));
            enlever(jeu, tas, allumettes);
        } else {
            System.out.println("Tour de l'IA selon jouerGagnant : ");
        }
    }

    /**
     * Test the efficiency of the estGagnante method
     */
    void testEstGagnateEfficacite() {
        System.out.println();
        System.out.println("*** testEstGagnanteEfficacite() ***");
        for (int n = 3; n <= 50; n++) {
            ArrayList<Integer> jeu = new ArrayList<>(Collections.singletonList(n));
            estGagnante(jeu);
        }
    }

    /**
     * Play the winning move if it exists
     * 
     * @param jeu game board
     * @return true if there is a winning move, false otherwise
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
    
        boolean gagnant = false;
        
        if (jeu == null) {
            System.err.println("jouerGagnant(): the parameter jeu is null");
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();
            
            // A very first decomposition is performed from jeu.
            // This first decomposition of the game is recorded in essai.
            // ligne is the number of the ArrayList cell (which starts at zero) that
            // stores the pile (number of matches) that has been decomposed
            int ligne = premier(jeu, essai);
            
            // implementation of rule number 2
            // A situation (or position) is said to be winning for the machine if there is AT LEAST ONE decomposition
            // (i.e. ONE action that consists of decomposing a pile into 2 unequal piles) losing for the opponent. It is
            // obviously this losing decomposition that will be chosen by the machine.
            while (ligne != -1 && !gagnant) {
                // estPerdante is recursive
                if (estPerdante(essai)) {
                    // estPerdante (for the opponent) to true ===> Bingo essai is the decomposition chosen by the machine which is then
                    // sure to win!!
                    jeu.clear();
                    gagnant = true;
                    // essai is copied into jeu because essai is the new game situation after the machine has played (winning)
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
                    // estPerdante to false ===> the machine tries another decomposition by calling "suivant".
                    // If, after executing suivant, ligne is (-1) then there is no more possible decomposition from jeu (and we exit the while).
                    // In other words: the machine has NOT found a winning decomposition from jeu.
                    ligne = suivant(jeu, essai, ligne);
                }
            }
        }
        
        return gagnant;
    }
    
    /**
     * RECURSIVE method that indicates if the configuration (of the current game or test game) is losing.
     * This method is used by the machine to know if the opponent can lose (100%).
     * 
     * @param jeu current game board (the state of the game at a certain moment during the game)
     * @return true if the configuration (of the game) is losing, false otherwise
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
    
        cpt++; // increment counter
        boolean ret = true; // by default the configuration is losing
        
        if (jeu == null) {
            System.err.println("estPerdante(): the parameter jeu is null");
        }
        
        else {
            // if there are only piles of 1 or 2 matches left in the game board
            // then the situation is necessarily losing (ret=true) = END of recursion
            if ( !estPossible(jeu) ) {
                ret = true;
            }
            
            else {
                // creation of a test game that will examine all possible decompositions
                // from jeu
                ArrayList<Integer> essai = new ArrayList<Integer>(); // size = 0 !
                
                // very first decomposition: remove 1 match from the first pile that has
                // at least 3 matches, ligne = -1 means there are no more piles with at least 3 matches
                int ligne = premier(jeu, essai);
                
                while ( (ligne != -1) && ret) {
                
                    // implementation of rule number 1
                    // A situation (or position) is said to be losing if and only if ALL its possible decompositions
                    // (i.e. ALL actions that consist of decomposing a pile into 2 unequal piles) are ALL winning 
                    // (for the opponent).
                    // The call to "estPerdante" is RECURSIVE.
                    
                    // If "estPerdante(essai)" is true it is equivalent to "estGagnante" is false, the decomposition
                    // essai is therefore not winning, we exit the while and return false.
                    if (estPerdante(essai) == true) {
                    
                        // If ONLY ONE decomposition (from the game) is losing (for the opponent) then the game is NOT losing.
                        // We will therefore return false: the situation (game) is NOT losing.
                        ret = false;
                        
                    } else {
                        // generate the next test configuration (i.e. ONE possible decomposition)
                        // from the game, if ligne = -1 there are no more possible decompositions
                        ligne = suivant(jeu, essai, ligne);
                    }
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Indicates if the configuration is winning.
     * Method that simply calls "estPerdante".
     * 
     * @param jeu game board
     * @return true if the configuration is winning, false otherwise
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        cpt = 0; // reset counter
        long startTime = System.nanoTime(); // start time
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estGagnante(): the parameter jeu is null");
        } else {
            ret = !estPerdante(jeu);
        }
        long endTime = System.nanoTime(); // end time
        long duration = endTime - startTime; // duration in nanoseconds
        System.out.println("n = " + jeu.get(0) + ", cpt = " + cpt + ", time = " + duration + " ns");
        return ret;
    }

    /**
     * Brief tests of the joueurGagnant() method
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Test of normal cases");
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
        resJeu1.add(4);
        resJeu1.add(2);
        
        testCasJouerGagnant(jeu1, resJeu1, true);
        
    }

    /**
     * Test a case of the jouerGagnant() method
     *
     * @param jeu the game board
     * @param resJeu the game board after playing winning
     * @param res the expected result of jouerGagnant
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.print("jouerGagnant (" + jeu.toString() + ") : ");

        // Act
        boolean resExec = jouerGagnant(jeu);

        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
        boolean egaliteJeux = jeu.equals(resJeu);
        if (  egaliteJeux && (res == resExec) ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }    

    /**
     * Divide the matches of a game line into two piles (1 line = 1 pile).
     * The new pile is necessarily placed at the end of the array.
     * The pile that is divided decreases by the number of matches removed.
     * 
     * @param jeu   array of matches per line
     * @param ligne pile for which the matches must be separated
     * @param nb    number of matches REMOVED from the pile (line) during separation
     */
    void enlever ( ArrayList<Integer> jeu, int ligne, int nb ) {
        // error handling
        if (jeu == null) {
            System.err.println("enlever() : the parameter jeu is null");
        } else if (ligne >= jeu.size()) {
            System.err.println("enlever() : the line number is too large");
        } else if (nb >= jeu.get(ligne)) {
            System.err.println("enlever() : the number of matches to remove is too large");
        } else if (nb <= 0) {
            System.err.println("enlever() : the number of matches to remove is too small");
        } else if (2 * nb == jeu.get(ligne)) {
            System.err.println("enlever() : the number of matches to remove is half");
        } else {
            // new pile added to the game (necessarily at the end of the array)
            // this new pile contains the number of matches removed (nb) from the pile to be separated            
            jeu.add(nb);
            // the remaining pile has "nb" matches less
            jeu.set ( ligne, (jeu.get(ligne) - nb) );
        }
    }

    /**
     * Tests if it is possible to separate one of the piles
     * 
     * @param jeu      game board
     * @return true if there is at least one pile of 3 matches or more, false otherwise
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): the parameter jeu is null");
        } else {
            int i = 0;
            while (i < jeu.size() && !ret) {
                if (jeu.get(i) > 2) {
                    ret = true;
                }
                i = i + 1;
            }
        }
        return ret;
    }

    /**
     * Creates a very first test configuration from the game
     * 
     * @param jeu      game board
     * @param jeuEssai new game configuration
     * @return the number of the pile divided into two or (-1) if there is no pile of at least 3 matches
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
    
        int numTas = -1; // no pile to separate by default
        int i;
        
        if (jeu == null) {
            System.err.println("premier(): the parameter jeu is null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): no pile is divisible");
        } else if (jeuEssai == null) {
            System.err.println("premier(): the parameter jeuEssai is null");
        } else {
            // before copying the game into jeuEssai there is a reset of jeuEssai 
            jeuEssai.clear(); // size = 0
            i = 0;
            
            // copy cell by cell from jeu to jeuEssai
            // jeuEssai is the same as the game before the first test configuration
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }
            
            i = 0;
            // search for a pile of at least 3 matches in the game
            // otherwise numTas = -1
            boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {
                
                // if we find a pile of at least 3 matches
                if ( jeuEssai.get(i) >= 3 ) {
                    trouve = true;
                    numTas = i;
                }
                
                i = i + 1;
            }
            
            // separate the pile (cell numTas) into a new pile of ONLY ONE match that comes to be placed at the end of the array 
            // the pile in cell numTas has decreased by one match (removal of one match)
            // jeuEssai is the game board that shows this separation
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }
        
        return numTas;
    }

    /**
     * Brief tests of the premier() method
     */
    void testPremier() {
        System.out.println();
        System.out.println("*** testPremier()");

        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        jeu1.add(11);
        int ligne1 = 0;
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(9);
        res1.add(11);
        res1.add(1);
        testCasPremier(jeu1, ligne1, res1);
    }

    /**
     * Test a case of the testPremier method
     * @param jeu the game board
     * @param ligne the number of the pile separated first
     * @param res the game board after a first separation
     */
    void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<Integer>();
        // Act
        int noLigne = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        boolean egaliteJeux = jeuEssai.equals(res);
        if ( egaliteJeux && noLigne == ligne ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }

    /**
     * Generates the next test configuration (i.e. ONE possible decomposition)
     * 
     * @param jeu      game board
     * @param jeuEssai test configuration of the game after separation
     * @param ligne    the number of the pile that was last separated
     * @return the number of the pile divided into two for the new configuration, -1 if no more decomposition is possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
    
        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() +
        // ", " + ligne + ") = ");
        
        int numTas = -1; // by default there is no more possible decomposition
        
        int i = 0;
        // error handling
        if (jeu == null) {
            System.err.println("suivant(): the parameter jeu is null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : the parameter jeuEssai is null");
        } else if (ligne >= jeu.size()) {
            System.err.println("suivant(): the parameter ligne is too large");
        }
        
        else {
        
            int nbAllumEnLigne = jeuEssai.get(ligne);
            int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);
            
            // if on the same line (passed as a parameter) we can still remove matches,
            // i.e. if the difference between the number of matches on this line and
            // the number of matches at the end of the array is > 2, then we still remove
            // 1 match on this line and add 1 match in the last cell        
            if ( (nbAllumEnLigne - nbAllDernCase) > 2 ) {
                jeuEssai.set ( ligne, (nbAllumEnLigne - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = ligne;
            } 
            
            // otherwise we need to examine the next pile (line) of the game to possibly decompose it
            // we recreate a new test configuration identical to the game board
            else {
                // copy the game into JeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }
                
                boolean separation = false;
                i = ligne + 1; // next pile
                // if there is still a pile and it contains at least 3 matches
                // then we make a first separation by removing 1 match
                while ( i < jeuEssai.size() && !separation ) {
                    // the pile must have at least 3 matches
                    if ( jeu.get(i) > 2 ) {
                        separation = true;
                        // we start by removing 1 match from this pile
                        enlever(jeuEssai, i, 1);
                        numTas = i;
                    } else {
                        i = i + 1;
                    }
                }                
            }
        }
        
        return numTas;
    }

    /**
     * Brief tests of the suivant() method
     */
    void testSuivant() {
        System.out.println();
        System.out.println("*** testSuivant() ****");

        int ligne1 = 0;
        int resLigne1 = 0;
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        ArrayList<Integer> jeuEssai1 = new ArrayList<Integer>();
        jeuEssai1.add(9);
        jeuEssai1.add(1);
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(8);
        res1.add(2);
        testCasSuivant(jeu1, jeuEssai1, ligne1, res1, resLigne1);

        int ligne2 = 0;
        int resLigne2 = -1;
        ArrayList<Integer> jeu2 = new ArrayList<Integer>();
        jeu2.add(10);
        ArrayList<Integer> jeuEssai2 = new ArrayList<Integer>();
        jeuEssai2.add(6);
        jeuEssai2.add(4);
        ArrayList<Integer> res2 = new ArrayList<Integer>();
        res2.add(10);
        testCasSuivant(jeu2, jeuEssai2, ligne2, res2, resLigne2);

        int ligne3 = 1;
        int resLigne3 = 1;
        ArrayList<Integer> jeu3 = new ArrayList<Integer>();
        jeu3.add(4);
        jeu3.add(6);
        jeu3.add(3);
        ArrayList<Integer> jeuEssai3 = new ArrayList<Integer>();
        jeuEssai3.add(4);
        jeuEssai3.add(5);
        jeuEssai3.add(3);
        jeuEssai3.add(1);
        ArrayList<Integer> res3 = new ArrayList<Integer>();
        res3.add(4);
        res3.add(4);
        res3.add(3);
        res3.add(2);
        testCasSuivant(jeu3, jeuEssai3, ligne3, res3, resLigne3);

    }

    /**
     * Test a case of the suivant method
     * 
     * @param jeu the game board
     * @param jeuEssai the game board obtained after separating a pile
     * @param ligne the number of the pile that was last separated
     * @param resJeu is the expected jeuEssai after separation
     * @param resLigne is the expected number of the pile that is separated
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
        // Act
        int noLigne = suivant(jeu, jeuEssai, ligne);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        boolean egaliteJeux = jeuEssai.equals(resJeu);
        if ( egaliteJeux && noLigne == resLigne ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }
}