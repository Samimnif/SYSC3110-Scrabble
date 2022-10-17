/**
 * Board class
 * Board contains boxes objects.
 * MAIN class
 *
 * @author Sami Mnif
 * @version 2022-10-16
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private ArrayList<ArrayList<Box>> board;
    private final int boardSize = 10;

    /**
     * Constructor of the Board object
     * Creates a 10x10 board
     * fill the board spots with Box objects.
     *
     */
    public Board() {
        this.board = new ArrayList<>(boardSize);
        Box newBox;
        for(int r = 0; r < boardSize; r++)  {
            board.add(new ArrayList<Box>(boardSize));
            for(int c = 0; c < boardSize; c++){
                board.get(r).add(newBox = new Box('-'));
            }
        }
    }

    /**
     * prints the board contents to the terminal
     *
     */
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
        Scanner input = new Scanner(System.in);
        String userInput = "";
        System.out.println("Welcome to Scrabble");
        while(!(userInput.equals("exit"))) {
            table.printBoard();
            userInput = input.nextLine();
        }

    }
}
