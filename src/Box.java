/**
 *Box class that contains the letter
 *
 * @author Sami Mnif
 * @version 2022-10-16
 */
public class Box {
    private char letter;

    /**
     * Constructor of the Box object
     *
     * @param letter
     */
    public Box(char letter) {
        this.letter = letter;
    }

    /**
     * Sets a new letter in teh Box
     * To change the letter to a new one
     *
     * @param letter
     */
    public void setLetter(char letter) {
        this.letter = letter;
    }

    /**
     * Getter method to get the cvontents of the box
     *
     * @return char the letter in the box
     */
    public char getLetter() {
        return this.letter;
    }
}