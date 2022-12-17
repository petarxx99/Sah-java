package src;
import src.paketfigure.*;
import src.paketpolje.Polje;
import src.framepackage.*;

import java.util.*;

/*
    From the main menu, select File | Project Structure | Project Settings | Project.
    If the necessary JDK is already defined in IntelliJ IDEA, select it from the SDK list. ...
    Apply the changes and close the dialog.

    Language level File>ProjectStructure>Modules>language level
 */

public class Mainclass {

    public static void main(String[] args){

        int duzinaPolja = 60;
        InstanciranjeFrejma.instancirajFrejm(duzinaPolja);
    }
}




