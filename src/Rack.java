/**
 * Rack class taht contains the 7 letters for each user
 *
 * @author Sami Mnif
 * @version 2022-10-16
 */

import java.util.ArrayList;

public class Rack {
    private final char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private ArrayList<Character> rack;

    /**
     * Teh constructor just initializes attributes
     *
     */
    public Rack() {
        rack = new ArrayList<Character>(7);
    }

    /**
     * teh addLetter method adds a letter to teh rack list
     *
     * @param letter teh letter to be added to teh list
     */
    public void addLetter(char letter){
        rack.add(letter);
    }

    /**
     * the removeLetter method removes the specific letter from teh rack
     *
     * @param letter teh letter to be removed
     */
    public void removeLetter(Character letter){
        rack.remove(letter);
    }

    /**
     * the getSize method return the size of the rack
     *
     * @return int size of the rack (how many letters it contains)
     */
    public int getSize(){
        return rack.size();
    }

    /**
     * printrack prints the letters in the rack to teh terminal
     */
    public void printRack(){
        System.out.print("Rack Contents: ");
        for (Character letter: rack) {
            System.out.printf(" %c ",letter);
        }
        System.out.println();
    }

    /**
     * teh hasLetter method checks if that specific letter is in the rack
     *
     * @param letter teh letter we want to check
     * @return true if teh letter is in teh rack, false otherwise.
     */
    public Boolean hasLetter(char letter){
        for (Character userLetter: rack) {
            if(userLetter.equals(letter)){
                return true;
            }
        }
        return false;
    }
}
