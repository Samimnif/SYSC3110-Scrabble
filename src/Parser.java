import java.util.Scanner;

public class Parser {
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // returns user input.

    public Parser()
    {
        commands = new CommandWords();
        // Scan standard input.
        reader = new Scanner(System.in);
    }


    public Command getCommand()
    {
        String inputLine = "";   // will hold the full input line
        String word1;
        String word2;
        String word3;

        System.out.print("> ");     // print prompt

        String line = reader.nextLine();
        Scanner scan = new Scanner(line);

        if(scan.hasNext())
            word1 = scan.next();      // get first word
        else
            word1 = null;
        if(scan.hasNext())
            word2 = scan.next();      // get second word
        else
            word2 = null;

        if(scan.hasNext())
            word3 = scan.next();      // get third word
        else
            word3 = null;

        return new Command(word1, word2, word3);
    }

    public void showCommands()
    {
        commands.showAll();
    }
}