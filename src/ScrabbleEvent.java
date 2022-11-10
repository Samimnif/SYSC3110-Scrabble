import java.util.EventObject;
import java.util.Stack;

public class ScrabbleEvent extends EventObject {
    private int row, col;
    private char letter;
    /**
     *
     */
    public ScrabbleEvent(Board board, int row, int col, char letter) {
        super(board);
        this.row = row;
        this.col = col;
        this.letter = letter;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getLetter() {
        return letter;
    }
}
