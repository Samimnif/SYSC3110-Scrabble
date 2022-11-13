import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ScrabbleTest {

    Board ScrabbleGame;


    @Test
    public void testTurnStart(){

        ScrabbleGame = new Board();

        //Testing that turn 1 starts out when game is first created; player 1 is represtented by true, player 2 by false
        assertEquals(true, ScrabbleGame.getTurn());
    }

    @Test
    public void testTurnPass(){
        ScrabbleGame = new Board();

        //player 1 passes their turn so now it is player 2's turn (false)
        ScrabbleGame.pass();

        assertEquals(false, ScrabbleGame.getTurn());
    }

    @Test
    public void testSelectedRackLetter(){
        ScrabbleGame = new Board();

        ScrabbleGame.selectRackLetter("B") ;

        assertEquals("B", ScrabbleGame.getSelectedRackLetter());

        ScrabbleGame.selectRackLetter("!") ;
        assertEquals("!", ScrabbleGame.getSelectedRackLetter());

        ScrabbleGame.selectRackLetter("0") ;
        assertEquals("0", ScrabbleGame.getSelectedRackLetter());
    }

    @Test
    public void testEditBoard(){
        ScrabbleGame = new Board();

        ScrabbleGame.editBoard(2,2,'A');
        ScrabbleGame.editBoard(4,9,'Z');
        ScrabbleGame.editBoard(8,0,'T');

        ArrayList<ArrayList<Box>> grid = ScrabbleGame.getBoard();

        assertEquals('A', grid.get(2).get(2).getLetter());
        assertEquals('Z', grid.get(9).get(4).getLetter());
        assertEquals('T', grid.get(0).get(8).getLetter());
    }

    /**
     * Checks if the player has selected valid coordinates.
     */
    @Test
    public void testCoordinates(){
        ScrabbleGame = new Board();

        assertEquals(false, ScrabbleGame.coordinates(-1,-2));
        assertEquals(false, ScrabbleGame.coordinates(-3,5));
        assertEquals(false, ScrabbleGame.coordinates(2,-5));
        assertNotEquals(true, ScrabbleGame.coordinates(-5,-2));
        assertEquals(true, ScrabbleGame.coordinates(3,7));
    }


    /**
     * Checks if the selected square on the board is empty and valid to place a new letter.
     */
    @Test
    public void testIsEmpty(){
        ScrabbleGame = new Board();
        User user = new User();
        user.addLetter('J');
        user.addLetter('O');
        user.addLetter('Y');
        Command command = new Command("place","JOY", "2B");
        ScrabbleGame.place(command, user);
        assertEquals(false, ScrabbleGame.isEmpty( 2,2)); //Square is not empty
        assertEquals(true, ScrabbleGame.isEmpty( 7,2)); //Empty square
        assertEquals(false, ScrabbleGame.isEmpty( 15,1)); //Coordinate is out of the board
    }

    /**
     * Places the letter in the specified position/coordinates on the board.
     */
    @Test
    public void testPlace(){
        ScrabbleGame = new Board();
        User user = new User();
        user.addLetter('A');
        user.addLetter('E');
        user.addLetter('D');
        user.addLetter('K');
        user.addLetter('R');
        user.addLetter('G');
        user.addLetter('I');

        assertEquals(true, ScrabbleGame.place(3,7, "A", user));
        assertNotEquals(true, ScrabbleGame.place(14,3, "K", user));
        assertEquals(false, ScrabbleGame.place(14,3, "G", user));
    }

    /**
     * Checks if the player is routed based on the option selected.
     */
    @Test
    public void testProcessCommand(){
        Command command = new Command("place","JOY", "2D");
        ScrabbleGame = new Board();
        User user = new User();
        user.addLetter('A');
        user.addLetter('J');
        user.addLetter('D');
        user.addLetter('K');
        user.addLetter('Y');
        user.addLetter('G');
        user.addLetter('I');

        assertEquals(false, ScrabbleGame.processCommand(command, user));

        command = new Command("exit","JOY", "2D");
        assertEquals(true, ScrabbleGame.processCommand(command, user));

        command = new Command("pass","JOY", "2D");
        assertEquals(false, ScrabbleGame.processCommand(command, user));

        command = new Command("help","JOY", "4C");
        assertEquals(false, ScrabbleGame.processCommand(command, user));

        command = new Command("rack","JOY", "7A");
        assertEquals(false, ScrabbleGame.processCommand(command, user));

        command = new Command("back","JOY", "9B");
        assertEquals(false, ScrabbleGame.processCommand(command, user));

        command = new Command("pass","JOY", "3E");
        assertNotEquals(true, ScrabbleGame.processCommand(command, user));
    }
}
