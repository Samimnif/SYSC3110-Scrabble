public class User {
    private Rack userRack;
    private Parser parser;
    private CommandWords commands;
    private int score;
    private Points pointSystem;

    public User() {
        pointSystem = new Points();
        score = 0;
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

    public void showScore(){
        System.out.println("Your Score is "+Integer.toString(score));
    }

    public void addScore(char letter){
        score += pointSystem.getPoint(letter);
    }

}
