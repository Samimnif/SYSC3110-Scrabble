import java.util.ArrayList;
import java.util.Random;

public class Rack {
    private final char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private ArrayList<Character> rack;
    private Random rnd = new Random();
    public Rack() {

        rack = new ArrayList<Character>(7);
        for (int i = 0; i < 7; i++) {
            rack.add(alphabet[rnd.nextInt(27)]);
        }
    }
    //Author: @Keya Patel
    public void checkRack(String userInput){
        for (int i = 0; i < rack.size(); i++) {
            for(int j=0; j<userInput.length(); j++) {
                if (Character.compare(rack.get(i), userInput.charAt(j)) == 0) {
                    rack.remove(i);
                    rack.add(alphabet[rnd.nextInt(27)]);
                }
            }
        }
    }
    //Author: @ Keya Patel
    public void printRack() {
        for (int i = 0; i < 7; i++) {
            System.out.print(rack.get(i));
        }
    }
}

