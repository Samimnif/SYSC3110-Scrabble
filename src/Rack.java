import java.util.ArrayList;

public class Rack {
    private final char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private ArrayList<Character> rack;

    public Rack() {
        rack = new ArrayList<Character>(7);
    }

    public void addLetter(char letter){
        rack.add(letter);
    }

    public void removeLetter(Character letter){
        rack.remove(letter);
    }

    public int getSize(){
        return rack.size();
    }

    public void printRack(){
        System.out.print("Rack Contents: ");
        for (Character letter: rack) {
            System.out.printf(" %c ",letter);
        }
        System.out.println();
    }

    public Boolean hasLetter(char letter){
        for (Character userLetter: rack) {
            if(userLetter.equals(letter)){
                return true;
            }
        }
        return false;
    }
}
