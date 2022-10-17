import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Box>> board;
    private final int boardSize = 10;

    public Board() {
        this.board = new ArrayList<>(boardSize);
        Box newBox;// = new Box('-');
        for(int r = 0; r < boardSize; r++)  {
            board.add(new ArrayList<Box>(boardSize));
            for(int c = 0; c < boardSize; c++){
                board.get(r).add(newBox = new Box('-'));
            }
        }
    }
    public void printBoard() {
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                System.out.printf(" %c ",this.board.get(r).get(c).getLetter());
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {

        Board table = new Board();
        System.out.println("Welcome to Scrabble");
        table.printBoard();

    }
}
