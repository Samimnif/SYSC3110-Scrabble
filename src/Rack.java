import java.util.ArrayList;
import java.util.Random;

public class Rack {
    private final char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private ArrayList<Character> rack;

    public Rack() {
        Random rnd = new Random();
        rack = new ArrayList<Character>(7);
        for (int i = 0; i<7; i++){
            rack.add(alphabet[rnd.nextInt(27)]);
        }
    }
}
