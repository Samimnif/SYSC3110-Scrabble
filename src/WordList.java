/**
 * WordList class contains all teh valid words we get from the 10000 words txt file
 *
 * @author Sami Mnif
 * @version 2022-10-16
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class WordList {
    private HashSet<String> words;

    /**
     * the constructor reads a word list txt file and adds the words to teh HashSet
     *
     */
    public WordList() {
        words = new HashSet<>();
        try {
            File myObj = new File("src/wordlist-10000.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                words.add(data);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * check if the specific word exists in the hashSet
     *
     * @param word the word we want to check
     * @return true if teh word is valid, false otherwise
     */
    public boolean isWord(String word){
        return words.contains(word);
    }

}
