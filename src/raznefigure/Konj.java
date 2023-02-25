package raznefigure;

import paketfigure.Figure;
import raznefigure.*;
import framepackage.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

// -------------------------------------------------------------------
public class Konj extends Figure {
    public Konj() {
    }

    public Konj(MyFrame boardFrame, ArrayList<Integer> istorijaPolozaja) {
        super(boardFrame, istorijaPolozaja);
    }

    public Konj(int upisi0zaBelog1zaCrnog, boolean ovoJeLeviKonj, MyFrame boardFrame) {
        super(upisi0zaBelog1zaCrnog, boardFrame);
        rank = (upisi0zaBelog1zaCrnog == 0) ? (byte) 1 : (byte) 8;
        file = (ovoJeLeviKonj) ? (byte) 2 : 7;
        this.setPozicija(boardFrame.isWhitesPerspective(), boardFrame.getSquareLength());
    }


    @Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {
        boolean izlaz = false;

        if((Math.abs((this.rank - rankDestination)) == 2) && ((Math.abs((this.file - fileDestination)) == 1))){
            izlaz = true;
        }
        if((Math.abs((this.file - fileDestination)) == 2) && ((Math.abs((this.rank - rankDestination)) == 1))) {
            izlaz = true;
        }

        return izlaz;

    }

    @Override
    public boolean Move(int rank, int file, MyFrame boardFrame) {
        boolean output = false;
        int startRank = this.rank;
        int startFile = this.file;

        if((NaturalMovement(rank, file, boardFrame)) && (neJedemSopstvenog(rank, file, boardFrame)) && nisamUsahuNakonPoteza(rank, file, boardFrame)){
            if(jedemTudjuFiguru(rank, file, boardFrame)){
                skloniFiguruSaTable(rank, file, boardFrame);
            }
            this.setRank(rank);
            this.setFile(file);
            this.setPozicija(boardFrame.isWhitesPerspective(), boardFrame.getSquareLength());
            output = true;

        }
        return output;
    }
}
