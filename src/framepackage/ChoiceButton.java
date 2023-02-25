package framepackage;
import java.awt.*;

public class ChoiceButton extends Button {

    public ChoiceButton(){
        super();
    }
    public ChoiceButton(String title){
        super(title);
    }


    private String text;

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    private Opponents opponent;

    public void setOpponent(Opponents opponent){
        this.opponent = opponent;
    }
    public Opponents getOpponent(){
        return opponent;
    }

}
