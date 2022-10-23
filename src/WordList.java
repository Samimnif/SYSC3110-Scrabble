import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class WordList {
    private HashSet<String> words;

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

    public boolean isWord(String word){
        return words.contains(word);
    }

}
