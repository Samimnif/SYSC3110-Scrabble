import java.util.Scanner;

/**
 * @author Nikita Sara Vijay
 * @version 2022-10-22
 */
public class User {
    private Rack userTack;

    private int score;

    public User(Rack userTack) {
        this.userTack = userTack;
    }

    public Rack getUserTack() {
        return userTack;
    }

    public void setUserTack(Rack userTack) {
        this.userTack = userTack;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}