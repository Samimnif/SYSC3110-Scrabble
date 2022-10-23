/**
 * Board class
 * Board contains boxes objects.
 * MAIN class
 *
 * @author Sami Mnif
 * @version 2022-10-16
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
public class Board {
    private ArrayList<ArrayList<Box>> boardList;
    private final int boardSize = 11;
    private  static int rowValue;
    private  static int colValue;

    private static int firstUserScore;
    private static int secondUserScore;
    private static final int[] LETTER_VALUES = {
            1, 3, 3, 2, 1, 4, 2, 4, 1, 8,
            5, 1, 3, 1, 1, 3, 10, 1, 1, 1,
            1, 4, 4, 8, 4, 10
    };
    /**
     * Constructor of the Board object
     * Creates a 10x10 board
     * fill the board spots with Box objects.
     *
     */
    public Board() {
        this.boardList = new ArrayList<>(boardSize);
        Box newBox;
        int ascii = 63;
        for(int r = 0; r < boardSize; r++)  {
            boardList.add(new ArrayList<Box>(boardSize)); //adds an arraylist 0f 11 Box objects to main arraylist - board

            for(int c = 0; c < boardSize; c++){
                ascii++;
                if(r==0 && c >0){
                    boardList.get(r).add(newBox = new Box((char) ascii));
                }else if(c==0 && r>0){
                    boardList.get(r).add(newBox = new Box(Character.forDigit(r-1, 10)));
                }else {
                    boardList.get(r).add(newBox = new Box('-'));
                }
            }
        }
    }

    /**
     * Adds a new word to the board object.
     * @author Nikita Sara Vijay
     */
    public void updateBoard(String word, int row, int col, int orientation) {
        ArrayList<Box> boardRowList = new ArrayList<>();
        boardRowList = boardList.get(row);
        char [] letters = word.toCharArray();
        char currentLetter;
        if (orientation == 1) {

            for (int i = 0; i < letters.length; i++) {
                //System.out.println(boardRowList.get(col).getLetter());
                currentLetter = boardRowList.get(col).getLetter();
                if(currentLetter != '-'){
                    System.out.println("Sorry, Location selected overlaps with an existing word. Please try again.");
                    break;
                }
                boardRowList.set(col, new Box(letters[i]));
                col++;
            }
            boardList.set(row, boardRowList);
        }else {
            Box box;
            for (int i = 0; i < letters.length; i++) {

                boardRowList = boardList.get(row);
                box = boardRowList.get(col);
                currentLetter = boardRowList.get(col).getLetter();
                if(currentLetter != '-'){
                    System.out.println("Sorry, Location selected overlaps with an existing word. Please try again.");
                    break;
                }
                boardRowList.set(col, new Box(letters[i]));
                row++;
            }
        }
    }

    /**
     * Prints the board contents to the terminal
     *
     */
    public void printBoard() {
        for(int r = 0; r < boardSize; r++) {
            //System.out.println("r:"+r);
            for (int c = 0; c < boardSize; c++) {
                System.out.printf(" %c ",this.boardList.get(r).get(c).getLetter());
            }
            System.out.println();
        }
    }
    /**
     * Gets the value of each letter to calculate the score for a word
     * @author Nikita Sara Vijay
     */
    private static int getLetterValue(char letter) {
        char upperCase = Character.toUpperCase(letter);
        return LETTER_VALUES[upperCase - 'A'];
    }
    /**
     * Calculates the score of each word
     * @author Nikita Sara Vijay
     */
    private static int calculateWordScore(String word) {
        int score = 0;
        for (char letter : word.toCharArray()) {
            score += getLetterValue(letter);
        }
        return score;
    }
    /**
     * Converts arraylist to string to compare letters in rack and the word entered by user
     * @author Nikita Sara Vijay
     */
    private static String convertArrayListToString(ArrayList<Character> rack){
        StringBuilder rackStr = new StringBuilder(rack.size());
        for(Character ch: rack)
        {
            rackStr.append(ch);
        }
        return rackStr.toString();
    }
    /**
     * Compare letters in rack and the word entered by user
     * @author Nikita Sara Vijay
     */
    private static boolean compareStrings(String firstValue, String secondValue){
        char[] first = firstValue.toCharArray();
        char[] second = secondValue.toCharArray();
        if(second.length > first.length)
            return false;
        else if(second.length == first.length) {
            Arrays.sort(first);
            Arrays.sort(second);
            return Arrays.equals(first, second);
        }
        return true;
    }
    /**
     * Validates if the word entered is valid
     * May not be needed as only values in dictionary are allowed.
     * @author Nikita Sara Vijay
     */
    private static String validateWord(Scanner input , String word, ArrayList<Character> rackValues){

        //Compares if the word consists of letters in the rack
        String rackStr =  convertArrayListToString(rackValues);
        while(!compareStrings(rackStr, word)) {
            System.out.println("Invalid word, Word entered contains letters that are not in your rack. Please try again.");

            System.out.println("Enter word: " );
            word = input.nextLine();
            //Compares if the word consists of letters in the rack
            rackStr =  convertArrayListToString(rackValues);
        }

        return word;
    }
    /**
     * Validates if the row value entered by user is greater than 10
     * If it's greater, a message will be displayed and gets a new input.
     * @author Nikita Sara Vijay
     */
    private static int validateRow(Scanner input , int rValue){
        String rowStr = "";
        while(rValue >10) {
            System.out.println("Invalid row location. Please enter values below 10 and try again.");

            System.out.println("Enter row to place word: ");
            rowStr = input.nextLine();
            if((rowStr != null) && !(rowStr.equals(""))){
                rValue = Integer.parseInt(rowStr);
            }
        }
        return rValue;
    }
    /**
     * Validates if the column value entered by user is greater than 10
     * If it's greater, a message will be displayed and gets a new input.
     * @author Nikita Sara Vijay
     */
    private static int validateCol(Scanner input , int cValue){
        String colStr = "";
        while(cValue >10) {
            System.out.println("Invalid column location. Please enter values below 10 and try again.");

            System.out.println("Enter column to place word: ");
            colStr = input.nextLine();
            if((colStr != null) && !(colStr.equals(""))){
                cValue = Integer.parseInt(colStr);
            }
        }
        return cValue;
    }
    /**
     * Validates if the user entered word is valid/exists in file.
     * Returns a message to the user if the word is invalid and allows the user to enter a new word
     * @author Nikita Sara Vijay
     */
    private static String checkWordInFile(Scanner input ,String word){
        while(!validateWordInFile(word)) {
            System.out.println("Invalid word, Word entered is not present in dictionary. Please try again.");
            System.out.println("Enter word: " );
            word = input.nextLine();
        }
        return word.toUpperCase();
    }
    /**
     * Checks if the word entered by the user exists in the dictionary/file
     * @author Nikita Sara Vijay
     */
    private static boolean validateWordInFile(String word){
        File f1=new File("./src/dictionary.txt");
        try{
            BufferedReader buff=new BufferedReader(new FileReader(f1));
            String s=null;
            while((s=buff.readLine())!=null){
                if(s.trim().contains(word)){
                    return true;
                }
            }
            buff.close();
        }catch(Exception e){e.printStackTrace();}
        return false;

    }

    /**
     * Gets User Input - word, row, column and orientation
     * @author Nikita Sara Vijay
     */
    private static String getUserInput(Scanner input, Board table, ArrayList<Character> rackValues){
        int orientValue = 1;
        String rowStr = "";
        String colStr = "";
        System.out.println("Enter word: " );
        String word = input.nextLine();
        word = checkWordInFile(input,word);
        //word = validateWord(input, word, rackValues);
        System.out.println("Enter row to place word: ");
        rowStr = input.nextLine();
        if((rowStr != null) && !(rowStr.equals(""))){
            rowValue = Integer.parseInt(rowStr);
        }
        rowValue = validateRow( input ,  rowValue);

        System.out.println("Enter column to place word: ");
        colStr = input.nextLine();
        if((colStr != null) && !(colStr.equals(""))){
            colValue = Integer.parseInt(colStr);
        }

        colValue = validateCol(input ,  colValue);

        System.out.println("Enter the placement orientation (1 for horizontal & 2 for vertical): " );
        String orientation = input.nextLine();
        if((orientation != null) && !(orientation.equals(""))){
            orientValue = Integer.parseInt(orientation);
        }

        table.updateBoard(word, rowValue,colValue, orientValue);
        table.printBoard();
        return word;
    }

    /**
     * @author Nikita Sara Vijay
     */

    public static void main(String[] args) {

        Board table = new Board();
        Scanner input = new Scanner(System.in);
        String userInput = "";
        System.out.println("Welcome to Scrabble");
        String word = "";
        Rack firstRack = new Rack();
        User firstUser = new User(firstRack);

        Rack secondRack = new Rack();
        User secondUser = new User(secondRack);
        ArrayList<Character> firstRackValues  = new ArrayList<Character>(7);
        ArrayList<Character> secondRackValues  = new ArrayList<Character>(7);
        table.printBoard();
        while(!(userInput.equals("exit"))) {

            //Gets input from First User

            firstRackValues = firstRack.getRack();
            System.out.println("\n\nPlayer 1 rack: "+firstRackValues);
            word = getUserInput(input, table, firstRackValues);
            firstUserScore = firstUserScore + calculateWordScore(word);
            firstUser.setScore(firstUserScore);
            System.out.println("*********************************");
            System.out.println("Player 1 Score is: " + firstUser.getScore());
            System.out.println("*********************************");

            //Gets input from Second User

            secondRackValues = secondRack.getRack();
            System.out.println("\n\nPlayer 2 rack: "+secondRackValues);
            word = getUserInput(input, table, secondRackValues);
            secondUserScore = secondUserScore + calculateWordScore(word);
            firstUser.setScore(secondUserScore);
            secondUser.setScore(calculateWordScore(word));
            System.out.println("*********************************");
            System.out.println("Player 2 Score is: " + secondUser.getScore());
            System.out.println("*********************************");
        }
    }
}