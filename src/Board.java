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
import java.util.Stack;

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
    private Stack<String> edits;
    private Bag lettersBag;

    /**
     * Constructor of the Board object
     * Creates a 10x10 board
     * fill the board spots with Box objects.
     * Initializing important attributes
     *
     */
    public Board() {
        lettersBag = new Bag();
        this.edits = new Stack<>();
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

    /**
     * The editBoard is a method that takes a letter and its specific coordinates
     * on board and then edit the board by adding the letter in teh specific box/
     *
     * @param column takes the specific column index for the edit
     * @param row takes the specific row index for the edit
     * @param letter the letter to be added in the board
     */
    private void editBoard(int column, int row, char letter){
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (r == row && c == column){
                    this.board.get(r).get(c).setLetter(letter);
                }
            }
        }
    }

    /**
     * the isEmpty method takes the coordinates and checks if the box with that specific coordinates in board is empty
     * or contains a letter. True if the box is empty and contains '□' character, false otherwise.
     *
     * @param column the column index on the board
     * @param row the row index on the board
     * @return boolean if the specific box is empty in the board
     */
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

    /**
     * the processCommand method processes the command submitted by the user and then
     * direct the game to teh appropriate method for the appropriate command.
     * The method returns false if the game will continue and returns true if the user
     * wants to end the game.
     *
     * @param command the command object that contains the users command from the terminal
     * @param user the user object that submitted the command
     * @return boolean if the user wants to end the game or not
     */
    private boolean processCommand(Command command, User user)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println(YELLOW+"I don't know what you mean..."+RESET);
            return false;
        }

        String commandWord = command.getCommandWord();
        if ((commandWord.toLowerCase()).equals("help")) {
            printHelp();
        }
        else if ((commandWord.toLowerCase()).equals("place")) {
            if (command.hasThirdWord() && command.hasSecondWord()){
                place(command, user);
            }
            else{
                System.out.println(RED+"Sorry, I don't understand please include the letters and the coordinates."+RESET);
            }
        }
        else if ((commandWord.toLowerCase()).equals("rack")) {
            printRack(user);
        }
        else if ((commandWord.toLowerCase()).equals("submit")) {
            if (checkBoard(user)){
                switchTurn(user);
            }
        }
        else if ((commandWord.toLowerCase()).equals("pass")) {
            pass(user);
        }
        else if ((commandWord.toLowerCase()).equals("back")) {
            charBack(user);
        }
        else if ((commandWord.toLowerCase()).equals("exit")) {
            wantToQuit = true;
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * the checkBoard method checks the letters the user placed if it forms a valid word.
     * If the word is valid then it return true, otherwise it returns false
     *
     * @param user the user object that was placing the letters on board
     * @return boolean if the letters the user places on board is valid or not
     */
    public boolean checkBoard(User user){
        Stack<String> temp = new Stack<>();
        coordinates(edits.get(0));
        int column = columnSelect;
        int row = rowSelect;
        boolean hDirection = false;
        String word = "";
        int majorityV = 0;
        int majorityH = 0;

        for (int i = 1; i < edits.size(); i++) {
            if (coordinates(edits.get(i))){
                if (column == columnSelect){
                    //System.out.println("Same column"+Integer.toString(column));
                    //hDirection = false;
                    majorityV++;
                }
                else if (row == rowSelect){
                    //System.out.println("Same row"+Integer.toString(row));
                    //hDirection = true;
                    majorityH++;
                }
                column = columnSelect;
                row = rowSelect;
            }
        }
        if (majorityV > majorityH){
            hDirection = false;
        }
        else hDirection = true;
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (hDirection && r == row) {
                    if (this.board.get(r).get(c).getLetter() != '□'){
                        word += this.board.get(r).get(c).getLetter();
                        for (int i = 1; i < edits.size(); i++) {
                            if (coordinates(edits.get(i))){
                                if (columnSelect == c && rowSelect == r){
                                    temp.push(edits.remove(i));
                                }
                            }
                        }
                    }
                    else if (this.board.get(r).get(c).getLetter() == '□' && c < column){
                        //System.out.println("Word so far: "+word);
                        if (!(word.equals(""))){
                            word = "";
                        }
                    }
                }
                else if (!hDirection && c == column){
                    if (this.board.get(r).get(c).getLetter() != '□'){
                        word += this.board.get(r).get(c).getLetter();
                        for (int i = 1; i < edits.size(); i++) {
                            if (coordinates(edits.get(i))){
                                if (columnSelect == c && rowSelect == r){
                                    temp.push(edits.remove(i));
                                }
                            }
                        }
                    }
                    else if (this.board.get(r).get(c).getLetter() == '□' && r < row){
                        //System.out.println("Word so far: "+word);
                        if (!(word.equals(""))){
                            word = "";
                        }
                    }
                }
            }
        }
        WordList checker = new WordList();
        //System.out.println(word +" "+hDirection+temp);
        if (word.length()<2){
            System.out.println(RED+"Sorry you have one lonely letter"+RESET);
        }
        else if (checker.isWord(word.toLowerCase())){
            System.out.println(CYAN+word+" is a word! Good Job!"+RESET);
            for (int i = 0; i < word.length(); i++) {
                user.addScore(word.charAt(i));
            }
            System.out.print(PURPLE);
            user.showScore();
            System.out.print(RESET);
            return true;
        }
        else{
            System.out.println(RED+"Sorry the word doesn't exist in out dictionary"+RESET);
        }
        if (edits.size() > 0){
            System.out.println(RED+"Your characters are not forming a word"+RESET);
        }
        for (int i = 0; i < temp.size(); i++) {
            edits.push(temp.pop());
        }
        return false;
    }

    /**
     * the charBack method takes the letters that the user placed on board back to their rack.
     *
     * @param user the user object that placed the letters on board
     */
    private void charBack(User user){
        if (edits.size() > 0){
            if(coordinates(edits.pop())){
                user.addLetter(this.board.get(rowSelect).get(columnSelect).getLetter());
                this.board.get(rowSelect).get(columnSelect).setLetter('□');
            }
        }
        else System.out.println(RED+"You didn't place any letters"+RESET);
    }

    /**
     * the switchTurn method switches users turns. It makes sure that the current playing user
     * gets its rack filled before switching to th next user.
     *
     * @param user the user object that was currently playing
     */
    private void switchTurn(User user){
        if (user.getRackSize() < 7){
            for (int i = 0; i < 7-user.getRackSize(); i++) {
                user.addLetter(lettersBag.getRandom());
            }
        }
        edits.clear();
        if (turn1){
            turn1 = false;
        }
        else turn1 = true;
    }

    /**
     * the pass method passes the user turn to the next one if the user doesn't want to play his turn.
     * The method makes sure that all characters placed by the user gets retrieved and put back to the rack.
     * Then switches turns to next player.
     *
     * @param user the user object that was currently playing
     */
    private void pass(User user){
        int size = edits.size();
        for (int i = 0; i < size; i++) {
            charBack(user);
        }
        if (turn1){
            turn1 = false;
        }
        else turn1 = true;
    }

    /**
     * The printRack method prints the specific user's rack
     *
     * @param user the user object that holds the rack
     */
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
            System.out.println(RED+"Sorry the coordinates are wrong"+RESET);
        }
        else{
            for (int i = 0; i < command.getSecondWord().length(); i++) {
                if(user.hasLetter(command.getSecondWord().toUpperCase().charAt(i)) && isEmpty(columnSelect, rowSelect)){
                    editBoard(columnSelect, rowSelect, command.getSecondWord().toUpperCase().charAt(i));
                    edits.push(Integer.toString(rowSelect)+column[columnSelect]);
                    if (horizontal){
                        columnSelect++;
                    }
                    else rowSelect++;
                    user.removeLetter(command.getSecondWord().toUpperCase().charAt(i));
                }
                else if (!(user.hasLetter(command.getSecondWord().toUpperCase().charAt(i)))){
                    System.out.println(YELLOW+"You don't have letter "+ RED+command.getSecondWord().toUpperCase().charAt(i)+RESET);
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
                user1.showScore();
                user1.printRack();
                System.out.print(RESET);
                exit = processCommand(getInput(user1), user1);
            }
            else {
                for (int i = 0; i< 7-user1.getRackSize(); i++){
                    user1.addLetter(lettersBag.getRandom());
                }
                System.out.println(RED+"User2 turn:");
                user2.showScore();
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
