import java.util.HashMap;

public class Points {
    private HashMap<Character, Integer> alphabet; //
    private final char[] alphabetChar = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','_'};

    public Points() {
        alphabet = new HashMap<>(17);
        alphabet.put('A',1);
        alphabet.put('B',3);
        alphabet.put('C',3);
        alphabet.put('D',2);
        alphabet.put('E',1);
        alphabet.put('F',4);
        alphabet.put('G',2);
        alphabet.put('H',4);
        alphabet.put('I',1);
        alphabet.put('J',8);
        alphabet.put('K',5);
        alphabet.put('L',1);
        alphabet.put('M',3);
        alphabet.put('N',1);
        alphabet.put('O',1);
        alphabet.put('P',3);
        alphabet.put('Q',10);
        alphabet.put('R',1);
        alphabet.put('S',1);
        alphabet.put('T',1);
        alphabet.put('U',1);
        alphabet.put('V',4);
        alphabet.put('W',4);
        alphabet.put('X',8);
        alphabet.put('Y',4);
        alphabet.put('Z',10);
        alphabet.put('_',0);
    }

    public int getPoint(char letter){
        return  alphabet.get(letter);
    }
}
