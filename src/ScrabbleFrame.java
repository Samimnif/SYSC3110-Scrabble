import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ScrabbleFrame extends JFrame implements ScrabbleView{

    private JButton[] rackButtons;
    private JButton[][] boardButtons;
    private JLabel rackLabel;
    JLabel scoreLabel;
    private ArrayList<JButton> selectedRackButtons;
    Board board;

    public ScrabbleFrame(){
        super("Scrabble");
        this.setResizable(false);

        selectedRackButtons = new ArrayList<>(1);
        board = new Board();
        board.addScrabbleView(this);
        JPanel mainPanel = new JPanel();
        JPanel rackPanel = new JPanel();
        scoreLabel = new JLabel("Score: 0");

        Object[] possibleValues = { "2-players", "AI Mode"};
        Object selectedValue = JOptionPane.showInputDialog(null,
                "Choose one", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        if (selectedValue == possibleValues[1]){
            board.AIMode();
        }


        mainPanel.setLayout(new GridLayout(10,10));

        this.add(mainPanel,BorderLayout.CENTER);
        this.add(rackPanel,BorderLayout.PAGE_END);

        rackLabel = new JLabel("Player 1 rack: ");
        rackPanel.add(scoreLabel, BorderLayout.WEST);
        rackPanel.add(rackLabel);
        rackPanel.setSize(400,400);

        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);
        JMenu menu = new JMenu("Options");

        JMenuItem back = new JMenuItem("Back");
        JMenuItem submit = new JMenuItem("Submit");
        JMenuItem export = new JMenuItem("Export");
        JMenuItem importBoard = new JMenuItem("Import");
        JMenuItem redo = new JMenuItem("Redo");
        JMenuItem pass = new JMenuItem("Pass");
        JMenuItem exit = new JMenuItem("Exit");

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.charBack();
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (board.checkBoard()){
                    board.switchTurn();
                    lockSubmission();
                }
            }
        });

        pass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.pass();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        export.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    exportBoard();}
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        );
        importBoard.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    importBoard();
                    lockSubmission();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        );
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.redo();
            }
        });

        menu.add(back);
        menu.add(submit);
        menu.add(pass);
        menu.add(redo);
        menu.add(export);
        menu.add(importBoard);
        menu.add(exit);

        menubar.add(menu);

        rackButtons = new JButton[7];
        for (int i = 0; i < 7; i++ ){
            JButton b = new JButton(board.getRack().get(i).toString());
            b.setPreferredSize(new Dimension(50, 35));
            b.setBackground(Color.white);
            b.setBorder(BorderFactory.createLineBorder(Color.gray));
            rackButtons[i] = b;
            b.addActionListener(e -> selectedLetter(b.getText(), b));
            rackPanel.add(b);
        }

        boardButtons = new JButton[11][11];
        for (int i = 0; i < 10; i++ ){
            for(int j = 0; j < 10; j++){
                JButton b = new JButton("");
                b.setBackground(Color.white);
                boardButtons[i][j] = b;
                int finalI = i;
                int finalJ = j;
                b.addActionListener(e -> submitLetter(finalI, finalJ));
                mainPanel.add(b);
            }

        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,600);
        //this.setLayout(new GridLayout(3,0));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void selectedLetter(String letter,JButton button){
        if (!selectedRackButtons.contains(button) && selectedRackButtons.size() == 0){
            board.selectRackLetter(letter);
            button.setBorder(BorderFactory.createLineBorder(Color.blue));
            button.setBorderPainted(true);
            selectedRackButtons.add(button);
        }
        else if (selectedRackButtons.size() == 1 && selectedRackButtons.get(0) != button){
            selectedRackButtons.get(0).setBorder(BorderFactory.createLineBorder(Color.gray));
            selectedRackButtons.remove(selectedRackButtons.get(0));
            selectedRackButtons.add(button);
            board.selectRackLetter(letter);
            button.setBorder(BorderFactory.createLineBorder(Color.blue));
            button.setBorderPainted(true);
        }
        else {
            selectedRackButtons.remove(button);
            button.setBorder(BorderFactory.createLineBorder(Color.gray));
        }
    }

    private void submitLetter(int row, int col){
        if(selectedRackButtons.size() == 1) {
            selectedRackButtons.get(0).setBorder(BorderFactory.createLineBorder(Color.gray));
            selectedRackButtons.remove(selectedRackButtons.get(0));
            board.play(row, col);
        }
    }

    private void lockSubmission(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!boardButtons[i][j].getText().equals("")){
                    boardButtons[i][j].setEnabled(false);
                    boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.cyan));
                }
            }
        }

    }

    @Override
    public void update(ScrabbleEvent e) {
        if(e.getCol() < 0){
            if (e.getLetter() == ' '){
                rackButtons[e.getRow()].setText(" ");
                rackButtons[e.getRow()].setEnabled(false);
                rackButtons[e.getRow()].setBorder(BorderFactory.createLineBorder(Color.red));
            }
            else{
                rackButtons[e.getRow()].setText(String.valueOf(e.getLetter()));
                rackButtons[e.getRow()].setBorder(BorderFactory.createLineBorder(Color.gray));
                rackButtons[e.getRow()].setEnabled(true);
            }
        }
        else{
            if (e.getLetter() == ' '){
                boardButtons[e.getRow()][e.getCol()].setText("");
            }
            else boardButtons[e.getRow()][e.getCol()].setText(String.valueOf(e.getLetter()));
            System.out.println("Updated: "+e.getRow()+" "+e.getCol()+" to '"+ e.getLetter()+"'");
        }
    }

    @Override
    public void updateTurn(String turn, String score) {
        scoreLabel.setText("Score: "+score);
        rackLabel.setText(turn+" rack: ");
    }

    public void exportBoard() throws IOException {
        //board = new Board();
        ImageIcon icon = new ImageIcon("/book.gif", "");
        Object[] possibilities = null;  // Drop-down with options

        String fileName = (String)JOptionPane.showInputDialog(this,"Enter the name of file" , "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE, icon, possibilities,"");

        board.exportScrabbleBoard(fileName); //Adds newly added buddy to the JList
    }
    public void importBoard() throws IOException {

        ImageIcon icon = new ImageIcon("/book.gif", "");
        Object[] possibilities = null;  // Drop-down with options

        String fileName = (String)JOptionPane.showInputDialog(this,"Enter the name of file" , "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE, icon, possibilities,"");

        board.importScrabbleBoard(fileName); //Adds newly added buddy to the JList
    }

    public static void main(String[] args) {
        new ScrabbleFrame();

    }


}