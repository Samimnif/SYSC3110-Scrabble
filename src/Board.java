/**
 * Board class
 * Board contains boxes objects.
 * MAIN class
 *
 * @author Sami Mnif
 * @version 2022-10-16
 */


import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.util.*;

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
    private User user1;
    private User user2;
    private AIUser aiUser;
    private String selectedRackLetter;
    private List<ScrabbleView> views;
    private Boolean firstPlay;
    private Boolean aiMode;
    private LinkedHashMap<String, Character> redoMap; //Stores the coordinates and letters that were removed from the board (back command)

    /**
     * Constructor of the Board object
     * Creates a 10x10 board
     * fill the board spots with Box objects.
     * Initializing important attributes
     *
     */
    public Board() {
        aiMode = false;
        firstPlay = true;
        selectedRackLetter = "";
        redoMap = new LinkedHashMap<String, Character>(7);
        user1 = new User();
        user2 = new User();
        aiUser = new AIUser();
        lettersBag = new Bag();
        for (int i = 0; i<7; i++){
            user1.addLetter(lettersBag.getRandom());
            user2.addLetter(lettersBag.getRandom());
        }
        for (Character c: user2.getRack()) {
            aiUser.addLetter(c.charValue());
        }
        views = new ArrayList<>();
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
    public void editBoard(int column, int row, char letter){
        for(int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (r == row && c == column){
                    this.board.get(r).get(c).setLetter(letter);
                    for(ScrabbleView v : views) {v.update(new ScrabbleEvent (this, r, c, letter));}
                }
            }
        }
    }

    public ArrayList<ArrayList<Box>> getBoard(){
        return this.board;
    }

    public void AIMode(){
        aiMode = true;
        user2 = aiUser;
    }

    public HashSet getBestChar(){
        HashSet<Character> bestChar = new HashSet<>();
        for(int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (this.board.get(r).get(c).getLetter() != '□'){
                    if ((this.board.get(r+1).get(c).getLetter() == '□' && this.board.get(r-1).get(c).getLetter() == '□')){
                        bestChar.add(Character.toLowerCase(this.board.get(r).get(c).getLetter()));
                    }
                    else if (this.board.get(r).get(c+1).getLetter() == '□' && this.board.get(r).get(c-1).getLetter() == '□'){
                        bestChar.add(Character.toLowerCase(this.board.get(r).get(c).getLetter()));
                    }
                }
            }
        }
        return bestChar;
    }

    public String getCoordChar(char letter){
        int col = 0, row = 0, hv = 0;
        for(int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                System.out.println(this.board.get(r).get(c).getLetter());
                if (this.board.get(r).get(c).getLetter() != '□' && this.board.get(r).get(c).getLetter() == letter){
                    System.out.println("found "+r+" "+c);
                    if ((this.board.get(r+1).get(c).getLetter() == '□' && this.board.get(r-1).get(c).getLetter() == '□')){
                        row = r;
                        col = c;
                        hv = 1;
                    }
                    else if (this.board.get(r).get(c+1).getLetter() == '□' && this.board.get(r).get(c-1).getLetter() == '□'){
                        row = r;
                        col = c;
                        hv = 0;
                    }
                }
            }
        }
        System.out.println("coord: "+row + " "+col);
        return Integer.toString(row)+Integer.toString(col)+Integer.toString(hv);
    }

    /**
     * the isEmpty method takes the coordinates and checks if the box with that specific coordinates in board is empty
     * or contains a letter. True if the box is empty and contains '□' character, false otherwise.
     *
     * @param column the column index on the board
     * @param row the row index on the board
     * @return boolean if the specific box is empty in the board
     */
    public boolean isEmpty(int column, int row){
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
    public boolean processCommand(Command command, User user)
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

    public boolean checkBoard(){
        User user;
        if (turn1) user = user1;
        else user = user2;
        Stack<String> temp = new Stack<>();
        coordinates(edits.get(0));
        int column = columnSelect;
        int row = rowSelect;
        boolean hDirection = false;
        String word = "";
        int majorityV = 0;
        int majorityH = 0;
        WordList checker = new WordList();
        Boolean wordFound =false;
        int totalEdits = edits.size();
        int wordSize = 0;

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

        int count = 0;
        while(true){
            word = "";
            for(int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    System.out.println("WORD: "+word);
                    System.out.println("EDITS: "+edits);
                    if (edits.size() == 0) break;
                    else if (coordinates(edits.get(count))){
                        if (columnSelect == c && rowSelect == r){
                            temp.push(edits.remove(count));
                            for(int i = 0; i < boardSize; i++) {
                                if (!(word.equals("")) && this.board.get(i).get(columnSelect).getLetter() == '□'){
                                    break;
                                }
                                else if (this.board.get(i).get(columnSelect).getLetter() != '□') {
                                    word += this.board.get(i).get(columnSelect).getLetter();
                                    int tempCol =  columnSelect;
                                    for (int j = 0; j < edits.size(); j++) {
                                        if(coordinates(edits.get(j))){
                                            if (rowSelect == i && columnSelect == tempCol){
                                                temp.push(edits.remove(j));
                                            }
                                        }
                                    }
                                    columnSelect = tempCol;
                                }
                                System.out.println("WORD HERE: "+word);
                            }
                            if(edits.size() == 0) break;
                            if (word.length()<2){
                                System.out.println(RED+"Sorry you have one lonely letter"+RESET);
                                //for (String letterCoor : temp) {
                                //    edits.push(temp.pop());
                                //}
                                //break;
                            }
                            else if (checker.isWord(word.toLowerCase())){
                                System.out.println(CYAN+word+" is a word! Good Job!"+RESET);
                                for (int i = 0; i < word.length(); i++) {
                                    user.addScore(word.charAt(i));
                                }
                                System.out.print(PURPLE);
                                user.showScore();
                                System.out.print(RESET);
                                wordFound = true;
                                wordSize = word.length();
                            }
                            word = "";
                            for (int n = 0; n < boardSize; n++) {
                                if (!(word.equals("")) && this.board.get(rowSelect).get(n).getLetter() == '□'){
                                    break;
                                }
                                else if (this.board.get(rowSelect).get(n).getLetter() != '□') {
                                    word += this.board.get(rowSelect).get(n).getLetter();
                                    int tempRo =  rowSelect;
                                    for (int i = 0; i < edits.size(); i++) {
                                        if(coordinates(edits.get(i))){
                                            if (rowSelect == tempRo && columnSelect == n){
                                                temp.push(edits.remove(i));
                                            }
                                        }
                                    }
                                    rowSelect = tempRo;
                                }
                                System.out.println("WORD HERE: "+word);
                            }
                            if(edits.size() == 0) break;
                            if (word.length()<2){
                                System.out.println(RED+"Sorry you have one lonely letter"+RESET);
                                //for (String letterCoor : temp) {
                                //    edits.push(temp.pop());
                                //}
                                //break;
                            }
                            else if (checker.isWord(word.toLowerCase())){
                                System.out.println(CYAN+word+" is a word! Good Job!"+RESET);
                                for (int i = 0; i < word.length(); i++) {
                                    user.addScore(word.charAt(i));
                                }
                                System.out.print(PURPLE);
                                user.showScore();
                                System.out.print(RESET);
                                wordFound = true;
                                wordSize = word.length();
                            }
                        }
                    }

                }
            }
            if (word.length()<2){
                System.out.println(RED+"Sorry you have one lonely letter"+RESET);
                //for (String letterCoor : temp) {
                //    edits.push(temp.pop());
                //}
                break;
            }
            else if (checker.isWord(word.toLowerCase())){
                System.out.println(CYAN+word+" is a word! Good Job!"+RESET);
                for (int i = 0; i < word.length(); i++) {
                    user.addScore(word.charAt(i));
                }
                System.out.print(PURPLE);
                user.showScore();
                System.out.print(RESET);
                wordFound = true;
                wordSize = word.length();
            }
            else if(edits.size() == 0) break;
            else{
                System.out.println(RED+"Sorry the word doesn't exist in out dictionary"+RESET);
                break;
            }
            count++;
        }

        /*
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
        */
        if (edits.size() > 0){
            System.out.println(RED+"Your characters are not forming a word"+RESET);
        }
        for (int i = 0; i < temp.size(); i++) {
            edits.push(temp.pop());
        }
        System.out.println("Word length: "+wordSize+" totaledits: "+totalEdits);
        if (firstPlay && wordFound){
            firstPlay = false;
            return true;
        }
        else if (wordSize > totalEdits & wordFound){
            return true;
        }
        return false;
    }

    /**
     * the charBack method takes the letters that the user placed on board back to their rack.
     *
     * @param user the user object that placed the letters on board
     */
    private void charBack(User user){
        redoMap = new LinkedHashMap<String, Character>(7);
        if (edits.size() > 0){
            if(coordinates(edits.pop())){
                user.addLetter(this.board.get(rowSelect).get(columnSelect).getLetter());
                this.board.get(rowSelect).get(columnSelect).setLetter('□');
            }
        }
        else System.out.println(RED+"You didn't place any letters"+RESET);
    }

    public void charBack(){
        User user;
        if (turn1) user = user1;
        else user = user2;
        System.out.println("Edits size: "+edits.size());

        if (edits.size() > 0){
            if(coordinates(edits.pop())){
                System.out.println(this.board.get(rowSelect).get(columnSelect).getLetter());
                user.addLetter(this.board.get(rowSelect).get(columnSelect).getLetter());
                this.board.get(rowSelect).get(columnSelect).setLetter('□');
                for(ScrabbleView v : views) {v.update(new ScrabbleEvent (this, rowSelect, columnSelect, ' '));}
                updateRack(user);
            }
        }
        else System.out.println(RED+"You didn't place any letters"+RESET);
    }

    public void redo(){

        //Currently LinkedHashMap redoMap contains all the letters that were moved back to the rack
        String [] redoKeySetArray = new String[3];
        Set<String> redoKeySet = redoMap.keySet();
        redoKeySetArray = redoKeySet.toArray(new String[0]); //Gets all the keys in redoMap, and converts the Set to array
        // to iterate in reverse order, ie to put back the last removed item first
        String key  = "";
        char letterValue;
        String[] result;
        System.out.print( "key set---- " + redoKeySetArray);


        for( int i = redoKeySetArray.length -1; i >= 0 ; i-- ) { // Iterates from last item in array to the first
            key= redoKeySetArray[i]; //i is the last item now, so gets the last item in the array

            letterValue = redoMap.get(key); //Gets the letter corresponding to the key
            System.out.print(key + "key---- " + letterValue);
            result = key.split(":"); //Splits the key as its formed by concatenating the row:col
            rowSelect = Integer.parseInt(result[0]);
            columnSelect = Integer.parseInt(result[1]);

            for(ScrabbleView v : views) {
                if(turn1) {
                    if (user1.hasLetter(letterValue)) {
                        user1.removeLetter(letterValue);
                    }
                    updateRack(user1);
                }
                else{
                    if (user2.hasLetter(letterValue)) {
                        user2.removeLetter(letterValue);
                    }
                    updateRack(user2);
                }
                v.update(new ScrabbleEvent (this, rowSelect, columnSelect, letterValue));
            }
            break;
        }
        redoMap.remove(key);

    }

    /**
     * the switchTurn method switches users turns. It makes sure that the current playing user
     * gets its rack filled before switching to th next user.
     *
     * @param user the user object that was currently playing
     */
    private void switchTurn(User user){
        redoMap = new LinkedHashMap<String, Character>(7);
        if (user.getRackSize() < 7){
            for (int i = 0; i < 7-user.getRackSize(); i++) {
                user.addLetter(lettersBag.getRandom());
            }
        }
        edits.clear();
        if (turn1) turn1 = false;
        else turn1 = true;
    }

    public void switchTurn(){
        redoMap = new LinkedHashMap<String, Character>(7);
        User user;
        if (turn1) user = user1;
        else user = user2;
        if (user.getRackSize() < 7){
            for (int i = 0; i < 7-user.getRackSize(); i++) {
                user.addLetter(lettersBag.getRandom());
            }
        }
        edits.clear();
        if (turn1){
            for (int i = 0; i < 7 - user1.getRackSize(); i++) {
                user1.addLetter(lettersBag.getRandom());
            }
            turn1 = false;
            if (!aiMode) {
                for (ScrabbleView v : views) {
                    v.updateTurn("Player 2", String.valueOf(user2.getScore()));
                }
                updateRack(user2);
            }
            else{
                for (ScrabbleView v : views) {
                    v.updateTurn("AI Player", String.valueOf(aiUser.getScore()));
                }
                updateRack(aiUser);
                aiUser.play(this);
                printBoard();
            }

        }
        else{
            for (int i = 0; i< 7-user2.getRackSize(); i++){
                user2.addLetter(lettersBag.getRandom());
            }
            turn1 = true;
            for(ScrabbleView v : views) {v.updateTurn("Player 1", String.valueOf(user1.getScore()));}
            updateRack(user1);
        }
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
        redoMap = new LinkedHashMap<String, Character>(7);
        for (int i = 0; i < size; i++) {
            charBack(user);
        }
        if (turn1){
            turn1 = false;
        }
        else turn1 = true;
    }

    public void pass(){
        redoMap = new LinkedHashMap<String, Character>(7);
        int size = edits.size();
        for (int i = 0; i < size; i++) {
            charBack();
        }
        //System.out.println("Edits size: "+edits.size());
        if (turn1){
            turn1 = false;
            //printRack(user1);
            for(ScrabbleView v : views) {v.updateTurn("Player 2", String.valueOf(user2.getScore()));}
            updateRack(user2);
        }
        else{
            turn1 = true;
            //printRack(user2);
            for(ScrabbleView v : views) {v.updateTurn("Player 1",String.valueOf(user1.getScore()));}
            updateRack(user1);
        }

    }

    /**
     * The printRack method prints the specific user's rack
     *
     * @param user the user object that holds the rack
     */
    private void printRack(User user){
        user.printRack();
    }

    public void updateRack(User user){
        int index = 0;
        System.out.println("size: "+user.getRack().size());
        user.printRack();
        for (Character letter: user.getRack()) {
            for(ScrabbleView v : views) {v.update(new ScrabbleEvent (this, index, -1, letter));}
            System.out.println(index);
            index++;
        }
        if (index < 7){
            for (int i = 0; i < 7-index; i++) {
                for(ScrabbleView v : views) {v.update(new ScrabbleEvent (this, index+i, -1, ' '));}
            }
        }
    }

    public ArrayList<Character> getRack(){
        if (turn1){
            return user1.getRack();
        }
        return user2.getRack();
    }

    public boolean getTurn(){
        return turn1;
    }

    private void printHelp(){
        System.out.println();
        System.out.print(GREEN+"For this game you have 7 letter rack \nand you have to generate a word using those letters\nCommands: "+BLUE);
        CommandWords commandWords = new CommandWords();
        commandWords.showAll();
        System.out.println(RESET);
    }

    /**
     * The coordinates method takes a string of coordinates and then converts it into two int.
     * One is the column index and the other is row index.
     * The method also detects if the coordinates is meant to be horizontal vertical direction.
     * Note: the column string is in characters.
     * Example: A0
     *
     * @param coordinates a string that has the coordinates in row and column
     * @return boolean, true if the coordinates are valid, false otherwise.
     */
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

    public boolean coordinates(int row, int col){
        if (row<0 || col<0){
            return false;
        }
        columnSelect = col;
        rowSelect = row;
        return true;
    }

    /**
     * The place method will place the specific letter into the board at a specific coordinates (index).
     * If it is multiple characters, then it will add it sequentially in the direction teh coordinates intend.
     *
     * @param command command submitted by the user
     * @param user the user who submitted the command
     */
    public void place(Command command, User user){
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

    public boolean place(int row, int col, String letter, User user){
        user.printRack();
        if (!coordinates(row, col)){
            System.out.println(RED+"Sorry the coordinates are wrong"+RESET);
        }
        else{
            for (int i = 0; i < letter.length(); i++) {
                if(user.hasLetter(letter.toUpperCase().charAt(i)) && isEmpty(col, row)){
                    editBoard(col, row, letter.charAt(i));
                    edits.push(Integer.toString(row)+column[col]);
                    user.removeLetter(letter.toUpperCase().charAt(i));
                }
                else if (!(user.hasLetter(letter.toUpperCase().charAt(i)))){
                    return false;
                }
                else{
                    break;
                }
            }
        }
        return true;
    }
    public boolean AIplace(int row, int col, String letter){
        System.out.println(row + " "+ col);
        if (!coordinates(row, col)){
            System.out.println(RED+"Sorry the coordinates are wrong"+RESET);
        }
        else{
            for (int i = 0; i < letter.length(); i++) {
                if(true){
                    editBoard(col, row, letter.charAt(i));
                    edits.push(Integer.toString(row)+column[col]);
                    aiUser.removeLetter(letter.toUpperCase().charAt(i));
                }
                else if (!(aiUser.hasLetter(letter.toUpperCase().charAt(i)))){
                    return false;
                }
                else{
                    break;
                }
            }
        }
        return true;
    }

    /**
     * The getInput method get Input from the user using the command object which later uses the parse the input.
     *
     * @param user the user who we will get the input from
     * @return the Command we received from the user (to parse later)
     */
    private Command getInput(User user){
        Command command = user.getInput();
        return command;
    }

    /**
     * The play method is the main method that initializes the users, adds their racks and fill them.
     * and then start playing., Swicthing turns and printing important information about their score and printing board.
     *
     */
    public void play(){
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

    public void play(int row, int col){
        if(turn1){
            for (int i = 0; i< 7-user2.getRackSize(); i++){
                user2.addLetter(lettersBag.getRandom());
            }
            place(row, col, selectedRackLetter, user1);
            updateRack(user1);
            printRack(user1);
        }
        else{
            for (int i = 0; i< 7-user1.getRackSize(); i++){
                user1.addLetter(lettersBag.getRandom());
            }
            place(row, col, selectedRackLetter, user2);
            updateRack(user2);
            printRack(user2);
        }
        printBoard();
        selectedRackLetter = "";
    }

    public void selectRackLetter(String letter){
        //if(selectedRackLetter.equals("")){
            selectedRackLetter = letter;
            System.out.println(letter + " is selected!");
        //}
    }

    public String getSelectedRackLetter(){

        return selectedRackLetter;
    }

    public void addScrabbleView(ScrabbleView v) {
        views.add(v);
    }

    public boolean exportScrabbleBoard(String fileName) {

        String file = "src\\" + fileName;
        String fullPath = file + fileName;
        char[][] grid = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j] = this.board.get(i).get(j).getLetter(); //Gets the character/letter in each square of the board and forms a 2D char array
            }
        }


        try {
            File newFile = new File(fullPath);
            newFile.createNewFile();            // creates a new file automatically everytime user is prompted
            FileOutputStream fout = new FileOutputStream(newFile);

            ObjectOutputStream out = new ObjectOutputStream(fout);
            String gridStr="a"; //Initialized to a random value
            for (int i = 0; i < boardSize; i++) {
                gridStr = gridStr + "\n";       //Line break after each row of the board
                for (int j = 0; j < boardSize; j++) {
                    gridStr = gridStr + grid[i][j]+ "#"; //Adds delimiter # after each item in a row
                }
            }

            out.writeObject(gridStr);      // writing contents of grid array into the file
            out.flush();
            out.close();        //closing the stream
            System.out.println("gridStr exported to file  :"+gridStr);
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public char[][] importScrabbleBoard(String fileName) {

        String path = "src\\" + fileName;
        File f1 = new File(path + fileName);
        char[][] grid = new char[boardSize][boardSize];
        try {
            BufferedReader buff = new BufferedReader(new FileReader(f1));
            String s = buff.readLine(); // Reads the first line in the file and ignores it
            int row = 0;
            String[] result = null;
            while ((s = buff.readLine()) != null) {     //Buffered reader reads from the exported file and assigns each row to String 's'
                result = s.split("#"); //Splits the string using delimiter '#' and stores to String array 'result'
                for(int i=0;i<boardSize;i++){
                    grid[row][i] = result[i].charAt(0); //Converts the string in result array to character and
                    // Recreates the Grid array
                }
                row++;
            }
            buff.close();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if(grid[i][j] == '□') { //No need to update empty squares
                    }else{
                        editBoard(j, i, grid[i][j]); // Updates only those squares with a letter on it
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
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
