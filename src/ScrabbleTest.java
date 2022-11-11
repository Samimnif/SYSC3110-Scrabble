import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

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


}
