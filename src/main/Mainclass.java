package main;
import framepackage.GameConfiguration;
import framepackage.InstanciranjeFrejma;
import framepackage.MyFrame;
import menu.Menu;
import framepackage.ChessGame;

/*
    From the main menu, select File | Project Structure | Project Settings | Project.
    If the necessary JDK is already defined in IntelliJ IDEA, select it from the SDK list. ...
    Apply the changes and close the dialog.

    Language level File>ProjectStructure>Modules>language level
 */

public class Mainclass {

    public static void main(String[] args){

        int duzinaPolja = 60;
        GameConfiguration configuration = new InstanciranjeFrejma(duzinaPolja);
        ChessGame chessGame = new MyFrame();
        var menu = new Menu(configuration, chessGame);
    }
}




