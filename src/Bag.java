import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Bag {
    private HashMap<Character, Integer> alphabet; //
    private final char[] alphabetChar = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','_'};

    public Bag() {
        alphabet = new HashMap<>(17);
        alphabet.put('A',9);
        alphabet.put('B',2);
        alphabet.put('C',2);
        alphabet.put('D',4);
        alphabet.put('E',12);
        alphabet.put('F',2);
        alphabet.put('G',3);
        alphabet.put('H',2);
        alphabet.put('I',9);
        alphabet.put('J',1);
        alphabet.put('K',1);
        alphabet.put('L',4);
        alphabet.put('M',2);
        alphabet.put('N',6);
        alphabet.put('O',8);
        alphabet.put('P',2);
        alphabet.put('Q',1);
        alphabet.put('R',6);
        alphabet.put('S',4);
        alphabet.put('T',6);
        alphabet.put('U',4);
        alphabet.put('V',2);
        alphabet.put('W',2);
        alphabet.put('X',1);
        alphabet.put('Y',2);
        alphabet.put('Z',1);
        alphabet.put('_',2);
    }

    private boolean removeLetter(char letter){
        int temp = alphabet.get(letter);
        if (temp > 0){
            alphabet.remove(letter);
            alphabet.put(letter, temp-1);
            return true;
        }
        return false;
    }

    public Character getRandom(){
        Random rnd = new Random();
        int randomLetter;
        for (int i = 0; i<27; i++){
            randomLetter = rnd.nextInt(27);
            if(removeLetter(alphabetChar[randomLetter])) {
                return alphabetChar[randomLetter];
            }
        }
        return null;
    }
}
