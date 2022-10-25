public class CommandWords {

    // a constant array that holds all valid command words
    private static final String[] validCommands = {
            "help", "place", "rack", "submit", "pass", "back", "exit"
    };

    public CommandWords()
    {
        // nothing to do at the moment...
    }

    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }

        return false;
    }

    public void showAll()
    {
        for(int i = 0; i < validCommands.length; i++) {
            System.out.print(validCommands[i] + "  ");
        }
        System.out.println();
    }
}