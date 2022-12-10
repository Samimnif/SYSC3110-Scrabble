import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class AIUser extends User{
    private Rack userRack;
    private int score;
    private Points pointSystem;
    private int col, row;
    private char chosenChar;
    private ScriptObjectMirror replica;

    /**
     * The constructor will set important attributes.
     */
    public AIUser() {
        pointSystem = new Points();
        score = 0;
        this.userRack = new Rack();
    }

    /**
     * teh addLetter add the letter to teh rack of the user.
     *
     * @param letter teh letter to be added to the rack.
     */
    public void addLetter(char letter){
        userRack.addLetter(letter);
    }

    /**
     * teh remove letter removes the character from teh rack
     *
     * @param letter the letter to be removed from teh rack
     */
    public void removeLetter(char letter){
        userRack.removeLetter((Character) letter);
    }

    /**
     * the printRack prints the user's rack into teh terminal
     *
     */
    public void printRack() {
        userRack.printRack();
    }

    public ArrayList<Character> getRack(){
        return userRack.getRack();
    }

    /**
     * the hasLetter method checks if the user has that specific letter in the rack
     *
     * @param letter the letter we have to check in the rack
     * @return true if the letter exists in te rack, false otherwise
     */
    public boolean hasLetter(char letter){
        return userRack.hasLetter(letter);
    }

    /**
     * the getRackSize method returns the size of the user's rack
     *
     * @return int the size of user's rack.
     */
    public int getRackSize(){
        return userRack.getSize();
    }

    /**
     * the showScore prints the user's score in the terminal.
     *
     */
    public void showScore(){
        System.out.println("Your Score is "+Integer.toString(score));
    }

    /**
     * the addScore method will add the score of a letter to teh score of the user
     *
     * @param letter the letter to add its score to user's score
     */
    public void addScore(char letter){
        score += pointSystem.getPoint(letter);
    }

    public int getScore() {
        return score;
    }

    private String findBestWord(HashSet letters){
        String s = "";
        String maxS = "";
        Boolean valid = false;
        char chosen = ' ';
        System.out.println("here");
        try {
            File myObj = new File("src/wordlist-10000.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                for (int j = 0; j < data.length(); j++) {
                    if (letters.contains(data.charAt(j))) {
                        chosen = data.charAt(j);
                        for (int i = 0; i < data.length(); i++) {
                            if (userRack.hasLetter(data.toUpperCase().charAt(i)) && i != j) {
                                System.out.println("Found! "+data);
                                valid = true;
                                System.out.println(data);
                            } else if (i != j) {
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            chosenChar = chosen;
                            s = data;
                        }
                        if (s.length() > maxS.length()) {
                            maxS = s;
                        }
                    }
                }
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return maxS;
    }

    private void place(int x, int y, String word){
    }

    public void play(Board mainBoard){
        HashSet<Character> bestChar = mainBoard.getBestChar();
        ArrayList<ArrayList<Box>> replica = new ArrayList<>();
        replica = mainBoard.getBoard();
        printRack();
        System.out.println(bestChar);
        String word = findBestWord(bestChar);
        String coord = mainBoard.getCoordChar(chosenChar);
        System.out.println("AI chose: "+ word);
        System.out.println(coord);
        if (word.equals("")){
            mainBoard.pass();
            return;
        }
        for (int i = 0; i < word.length(); i++) {
            mainBoard.place(Integer.parseInt(coord.substring(0,1))+i, Integer.parseInt(coord.substring(1,2)), String.valueOf(word.charAt(i)).toUpperCase(),this);
        }

    }

    public static void main(String[] args) {
        String longestWod = "";
        //while (longestWod.length() == 0) {
            AIUser user = new AIUser();
            HashSet<Character> hash = new HashSet<>();
            hash.add('x');
            hash.add('j');
            hash.add('m');
            hash.add('n');
            hash.add('o');
        System.out.println(hash);
            Bag lettersBag = new Bag();
            for (int i = 0; i < 7; i++) {
                user.addLetter(lettersBag.getRandom());
            }
            longestWod = user.findBestWord(hash);
            user.printRack();
            System.out.println("Longest word: " + longestWod);
       // }
    }
}

