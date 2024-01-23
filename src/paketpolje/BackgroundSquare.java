package paketpolje;

import javax.swing.*;
import java.awt.*;

public class BackgroundSquare extends JLabel {

    private Color backgroundColor;

    private JLabel square;

    public BackgroundSquare(){}

    public BackgroundSquare(Color backgroundColor, JLabel square){
        this.backgroundColor = backgroundColor;
        this.square = square;
    }

    public void returnDefaultColor(){
        square.setBackground(backgroundColor);
    }

}
