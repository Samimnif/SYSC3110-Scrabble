/**
 * Board class
 * Board contains boxes objects.
 * MAIN class
 *
 * @author Sami Mnif
 * @version 2022-10-16
 */

import com.sun.org.apache.regexp.internal.RE;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;

public class Board {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    private ArrayList<ArrayList<Box>> board;
    private final int boardSize = 10;
    private final char[] column = {'A','B','C','D','E','F','G','H','I','J'};
    private boolean turn1 = true, exit = false, horizontal = true;

    private int columnSelect, rowSelect;

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
                board.get(r).add(newBox = new Box('□'));
            }
        }
    }

    /**
     * prints the board contents to the terminal
     *
     */
    private void printBoard() {
        System.out.print(" "+PURPLE);
        for(int c = 0; c < boardSize; c++) {
            System.out.printf(" %c ",column[c]);
        }
        System.out.println(RESET);
        for(int r = 0; r < boardSize; r++) {
            System.out.print(PURPLE+r+RESET);
            for (int c = 0; c < boardSize; c++) {
                if (this.board.get(r).get(c).getLetter() != '□'){
                    System.out.print(CYAN);
                }
                System.out.printf(" %c ",this.board.get(r).get(c).getLetter());
                System.out.print(RESET);
            }
            System.out.println();
        }
    }

    private void editBoard(int column, int row, char letter){
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (r == row && c == column){
                    this.board.get(r).get(c).setLetter(letter);
                }
            }
        }
    }

    private boolean isEmpty(int column, int row){
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (r == row && c == column){
                    if (this.board.get(r).get(c).getLetter() == ('□')){
                        return true;
                    }
                }
            }
        }
        System.out.println(RED+"You can't place a letter on a occupied tile or outside the board"+RESET);
        return false;
    }

    private boolean processCommand(Command command, User user)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if ((commandWord.toLowerCase()).equals("help")) {
            printHelp();
        }
        else if ((commandWord.toLowerCase()).equals("place")) {
            place(command, user);
        }
        else if ((commandWord.toLowerCase()).equals("rack")) {
            printRack(user);
        }
        else if ((commandWord.toLowerCase()).equals("submit")) {

        }
        else if ((commandWord.toLowerCase()).equals("pass")) {
            switchTurn();
        }
        else if ((commandWord.toLowerCase()).equals("exit")) {
            wantToQuit = true;
        }
        // else command not recognised.
        return wantToQuit;
    }
    private void switchTurn(){
        if (turn1){
            turn1 = false;
        }
        else turn1 = true;
    }
    private void printRack(User user){
        user.printRack();
    }
    private void printHelp(){
        System.out.println();
        System.out.print(GREEN+"For this game you have 7 letter rack \nand you have to generate a word using those letters\nCommands: "+BLUE);
        CommandWords commandWords = new CommandWords();
        commandWords.showAll();
        System.out.println(RESET);
    }

    private boolean coordinates(String coordinates){
        int c = -1;
        int r = -1;
        for (int i = 0; i < coordinates.length(); i++) {
            for (int j = 0; j < column.length; j++) {
                if(column[j] == coordinates.toUpperCase().charAt(i)){
                    c = j;
                    if (i==0){
                        horizontal = false;
                    }
                    else horizontal = true;
                }
                else if((coordinates.substring(i, i+1).toUpperCase()).equals(Integer.toString(j))){
                    r = j;
                }
            }
        }
        if (c<0 || r<0){
            return false;
        }
        columnSelect = c;
        rowSelect = r;
        return true;
    }

    private void place(Command command, User user){
        if (!coordinates(command.getThirdWord())){
            System.out.println("Sorry the coordinates are wrong");
        }
        else{
            for (int i = 0; i < command.getSecondWord().length(); i++) {
                if(user.hasLetter(command.getSecondWord().toUpperCase().charAt(i)) && isEmpty(columnSelect, rowSelect)){
                    editBoard(columnSelect, rowSelect, command.getSecondWord().toUpperCase().charAt(i));
                    if (horizontal){
                        columnSelect++;
                    }
                    else rowSelect++;
                    user.removeLetter(command.getSecondWord().toUpperCase().charAt(i));
                }
                else if (!(user.hasLetter(command.getSecondWord().toUpperCase().charAt(i)))){
                    System.out.println("You don't have letter "+ RED+command.getSecondWord().toUpperCase().charAt(i)+RESET);
                }
                else{
                    break;
                }
            }
        }
    }

    private Command getInput(User user){
        Command command = user.getInput();
        return command;
    }

    public void play(){
        Bag lettersBag = new Bag();

        User user1 = new User();
        User user2 = new User();

        for (int i = 0; i<7; i++){
            user1.addLetter(lettersBag.getRandom());
            user2.addLetter(lettersBag.getRandom());
        }
        while(!(exit)) {
            printBoard();
            if (turn1){
                for (int i = 0; i< 7-user2.getRackSize(); i++){
                    user2.addLetter(lettersBag.getRandom());
                }
                System.out.println(BLUE+"User1 turn:");
                user1.printRack();
                System.out.print(RESET);
                exit = processCommand(getInput(user1), user1);
            }
            else {
                for (int i = 0; i< 7-user1.getRackSize(); i++){
                    user1.addLetter(lettersBag.getRandom());
                }
                System.out.println(RED+"User2 turn:");
                user2.printRack();
                System.out.print(RESET);
                exit = processCommand(getInput(user2), user2);
            }
        }
    }

    public static void main(String[] args) {
        System.out.print(GREEN);
        try {
            File myObj = new File("src/scrabble.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.print(RESET);

        Board table = new Board();


        System.out.println(YELLOW+"Welcome to Scrabble ...\n"+RESET);

        table.play();

    }
}
