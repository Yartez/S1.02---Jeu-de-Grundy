import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Grundy game with AI for the machine
 * This program contains only the methods to test jouerGagnant()
 * This version is raw without any improvement
 *
 * @author G.Audebert, Y.Monnot
 */

class GrundyRecBrute {

    long cpt; // elementary operations counter

    /**
     * Principal function of the program
     */
    void principal() {
        testJouerGagnant();
		testPremier();
		testSuivant();
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
            System.err.println("jouerGagnant(): le paramètre jeu est null");
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();
			
			// A very first decomposition is carried out from the game.
			// This first decomposition of the game is recorded in test.
			// row is the cell number of the ArrayList array (which starts at zero) which
			// memorizes the pile (number of matches) that was broken down
            int ligne = premier(jeu, essai);
			
			// implementation of rule number 2
			// A situation (or position) is said to be winning for the machine, if there exists AT LEAST ONE decomposition
			// (AN action which consists of decomposing a pile into 2 unequal piles) losing for the opponent. It is
			// obviously this losing decomposition which will be chosen by the machine.
            while (ligne != -1 && !gagnant) {
				// estPerdante is recursive
                if (estPerdante(essai)) {
					// estPerdante(for the opponent) to true ===> Bingo test is the decomposition chosen by the machine which is then
					// certain to win!!
                    jeu.clear();
                    gagnant = true;
					// essai is copied into game because trial is the new game situation after the machine has played (winner)
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
					// estPerdante to false ===> the machine tries another decomposition by calling on "suivant".
					// If, after execution of suivant, ligne is at (-1) then there is no longer any possible decomposition from play (and we exit while).
					// In other words: the machine has NOT found a winning decomposition from the game.
                    ligne = suivant(jeu, essai, ligne);
                }
            }
        }
		
        return gagnant;
    }
	
	/**
     *RECURSIVE method which indicates if the configuration (of the current game or test game) is losing.
	 * This method is used by the machine to know if the opponent can lose (100%).
     * 
     * @param jeu current game board (the state of the game at a certain point during the game)
     * @return true if the configuration (of the game) is a loser, false otherwise
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
	
        cpt++; // increment counter
        boolean ret = true; // by default the configuration is losing
		
        if (jeu == null) {
            System.err.println("estPerdante(): le paramètre jeu est null");
        }
		
		else {
			// if there are only piles of 1 or 2 matches left on the game board
			// then the situation is necessarily losing (ret=true) = END of recursion
            if ( !estPossible(jeu) ) {
                ret = true;
            }
			
			else {
				// creation of a test set which will examine all the decompositions
				// possible from game
                ArrayList<Integer> essai = new ArrayList<Integer>(); // size = 0 !
				
				// very first decomposition: remove 1 match from the first pile that has
				// at least 3 matches, ligne = -1 means that there are no more piles of at least 3 matches
                int ligne = premier(jeu, essai);
				
                while ( (ligne != -1) && ret) {
				
					// implementation of rule number 1
					// A situation (or position) is said to be losing if and only if ALL its possible decompositions
					// (ALL actions which consist of breaking a pile into 2 unequal piles) are ALL winners 
					// (for the opponent).
					// The call to "estPerdante" is RECURSIVE.
					
					// If "estPerdante(essai)" is true it is equivalent to "estGagnante" is false, decomposition
					// essai is not winning, we exit while and return false.
                    if (estPerdante(essai) == true) {
					
						// If ONLY ONE decomposition (from the game) is a loser (for the opponent) then the game is NOT a loser.
						// We will therefore return false: the situation (jeu) is NOT a loser.
                        ret = false;
						
                    } else {
						// generates the following test configuration (i.e. ONE possible decomposition)
						// from the game, if ligne = -1 there is no longer any possible decomposition
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
            System.err.println("estGagnante(): le paramètre jeu est null");
        } else {
            ret = !estPerdante(jeu);
        }
        long endTime = System.nanoTime(); // end time
        long duration = endTime - startTime; // duration in nanoseconds
        System.out.println("n = " + jeu.get(0) + ", cpt = " + cpt + ", time = " + duration + " ns");
        return ret;
    }

    /**
     * Brief tests of the method jouerGagnant()
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Test des cas normaux");
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
        resJeu1.add(4);
        resJeu1.add(2);
		
        testCasJouerGagnant(jeu1, resJeu1, true);
        
    }

    /**
     * Testing a case of the method jouerGagnant()
	 *
	 * @param jeu game board
	 * @param resJeu the game board after playing winning
	 * @param res the result expected by jouerGagnant
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
            System.err.println("ERREUR\n");
        }
    }	

    /**
     * Divide the matches from a row of games into two piles (1 row = 1 pile).
	 * The new heap is necessarily placed at the end of the table.
	 * The pile that is divided decreases by the number of matches removed.
     * 
     * @param jeu   table of matches by line
     * @param ligne pile for which matches must be separated
     * @param nb    number of matches REMOVED from the pile (ligne) during separation
     */
    void enlever ( ArrayList<Integer> jeu, int ligne, int nb ) {
		// error handling
        if (jeu == null) {
            System.err.println("enlever() : le paramètre jeu est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("enlever() : le numéro de ligne est trop grand");
        } else if (nb >= jeu.get(ligne)) {
            System.err.println("enlever() : le nb d'allumettes à retirer est trop grand");
        } else if (nb <= 0) {
            System.err.println("enlever() : le nb d'allumettes à retirer est trop petit");
        } else if (2 * nb == jeu.get(ligne)) {
            System.err.println("enlever() : le nb d'allumettes à retirer est la moitié");
        } else {
			// new pile added to the game (necessarily at the end of the table)
			// this new pile contains the number of matches removed (nb) from the pile to be separated			
            jeu.add(nb);
			// the remaining pile has "nb" fewer matches
            jeu.set ( ligne, (jeu.get(ligne) - nb) );
        }
    }

    /**
     * Test if it is possible to separate one of the piles
     * 
     * @param jeu      game board
     * @return true if there is at least one pile of 3 or more matches, false otherwise
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): le paramètre jeu est null");
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
     * Create a very first test setup from the game
     * 
     * @param jeu      game board
     * @param jeuEssai new game configuration
     * @return the number of the pile divided in two or (-1) if there is no pile of at least 3 matches
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
	
        int numTas = -1; // no heap to separate by default
		int i;
		
        if (jeu == null) {
            System.err.println("premier(): le paramètre jeu est null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): aucun tas n'est divisible");
        } else if (jeuEssai == null) {
            System.err.println("premier(): le paramètre jeuEssai est null");
        } else {
            // before copying the game into jeuEssai there is a reset of jeuEssai 
            jeuEssai.clear(); // size = 0
            i = 0;
			
			// copy square by square of the game in jeuEssai
			// jeuEssai is the same as the game before the first test setup
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }
			
            i = 0;
			// search for a match pile of at least 3 matches in the game
			// Otherwise numTas = -1
			boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {
				
				// if we find a pile of at least 3 matches
				if ( jeuEssai.get(i) >= 3 ) {
					trouve = true;
					numTas = i;
				}
				
				i = i + 1;
            }
			
			// separate the pile (case numTas) in a new pile of ONE match which is placed at the end of the table 
			// the pile in box numTas decreased by one match (withdrawal of a match)
			// jeuEssai is the game board which makes this separation appear
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }
		
        return numTas;
    }

    /**
     * Brief tests of the method premier()
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
     * Test a case of the method testPremier
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
            System.err.println("ERREUR\n");
        }
    }

    /**
     * Generates the following test configuration (ONE possible decomposition)
     * 
     * @param jeu      game board
     * @param jeuEssai test configuration of the game after separation
     * @param ligne    the number of the pile which was the last to have been separated
     * @return the heap number divided in two for the new configuration, -1 if no further decomposition is possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
	
        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() +
        // ", " + ligne + ") = ");
		
		int numTas = -1; // by default there is no longer any decomposition possible
		
        int i = 0;
		// error handling
        if (jeu == null) {
            System.err.println("suivant(): le paramètre jeu est null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : le paramètre jeuEssai est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("suivant(): le paramètre ligne est trop grand");
        }
		
		else {
		
			int nbAllumEnLigne = jeuEssai.get(ligne);
			int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);
			
			// if on the same line (passed as a parameter) you can still remove matches,
			// if the difference between the number of matches on this line and
			// the number of matches at the end of the table is > 2, so we remove again
			// 1 match on this line and we add 1 match in the last box		
            if ( (nbAllumEnLigne - nbAllDernCase) > 2 ) {
                jeuEssai.set ( ligne, (nbAllumEnLigne - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = ligne;
            } 
			
			// otherwise you have to examine the pile (ligne) next part of the game to possibly break it down
			// we recreate a new test configuration identical to the game board
			else {
                // copy of the game in JeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }
				
                boolean separation = false;
                i = ligne + 1; // next pile
				// if there is still a pile and it contains at least 3 matches
				// then we carry out a first separation by removing 1 match
                while ( i < jeuEssai.size() && !separation ) {
					// the pile must be at least 3 matches
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
     * Brief tests of the method suivant()
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
     * Test a case of the method suivant
	 * 
	 * @param jeu the game board
	 * @param jeuEssai the game board obtained after separating a pile
	 * @param ligne the number of the pile which was the last to have been separated
	 * @param resJeu is the jeuEssai expected after separation
	 * @param resLigne is the expected number of the heap that is separated
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
            System.err.println("ERREUR\n");
        }
    }
}
