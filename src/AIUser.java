import java.util.ArrayList;

public class AIUser {
    private Rack userRack;
    private int score;
    private Points pointSystem;

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

    public void play(Board gameBoard) {

    }
}

