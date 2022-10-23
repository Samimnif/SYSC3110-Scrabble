public class User {
    private Rack userRack;
    private Parser parser;
    private CommandWords commands;

    public User() {
        this.userRack = new Rack();
        commands = new CommandWords();
    }

    public Command getInput(){
        parser = new Parser();
        Command command = parser.getCommand();
        return command;
    }

    public void addLetter(char letter){
        userRack.addLetter(letter);
    }

    public void removeLetter(char letter){
        userRack.removeLetter((Character) letter);
    }

    public void printRack() {
        userRack.printRack();
    }

    public boolean hasLetter(char letter){
        return userRack.hasLetter(letter);
    }

    public int getRackSize(){
        return userRack.getSize();
    }

}
