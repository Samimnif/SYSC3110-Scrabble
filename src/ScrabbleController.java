import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleController implements ActionListener {
    private Board model;
    public ScrabbleController(Board model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
