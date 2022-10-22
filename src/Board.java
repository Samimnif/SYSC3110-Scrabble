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

import static java.lang.System.exit;

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

    //Author: @Keya Patel
    public void putLetter(String word, int x, int y, int sel){
        if (x != 0 && y!=0){
            x--;
            y--;
        }
        if(sel == 1) {
            for (int i = 0; i < word.length(); i++) {
                this.board.get(x).get(y).setLetter(word.charAt(i));
                y++;
            }
        }
        else {
            for (int i = 0; i < word.length(); i++) {
                this.board.get(y).get(x).setLetter(word.charAt(i));
                y++;
            }
        }
    }

    public static void main(String[] args) {
        //Author: @Keya Patel
        Board table = new Board();
        Rack rack = new Rack();
        Scanner input = new Scanner(System.in);
        int c = 0;
        String userInput= "";
        while(true){
            int x=0;
            int y=0;
            int userinput=0;
            if(!(userInput.equals(""))){
                rack.checkRack(userInput);
            }
            System.out.println("Welcome to Scrabble");
            System.out.println("Here is a rack of letter you have in hand");
            rack.printRack();
            System.out.println();
            System.out.println("Enter the word you would like to put on the board or enter 'EXIT' to leave the game.");
            userInput = input.next();
            if(userInput.equals("EXIT")){
                exit(0);
            }
            System.out.println("Enter 1 for row and 0 for column ");
            userinput = input.nextInt();
            if (userinput == 1) {
                System.out.println("Enter the position you want to enter the word ");
                x = input.nextInt();
                y = input.nextInt();
            } else {
                System.out.println("Enter the position (x,y) you want to enter the word ");
                x = input.nextInt();
                y = input.nextInt();
            }
            table.putLetter(userInput, x, y, userinput);
            table.printBoard();
        }
    }

}
