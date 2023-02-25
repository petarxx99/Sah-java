package raznefigure;

import paketfigure.Figure;
import raznefigure.*;
import framepackage.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

// ---------------------------------------------------------------------
public class Top extends Figure {
    boolean nijeSePomerao = true;
    public Top() {
    }
    public Top(MyFrame boardFrame, ArrayList<Integer> istorijaPolozaja) {
        super(boardFrame, istorijaPolozaja);
    }

    public Top(int upisi0zaBelog1zaCrnog, boolean ovoJeLeviTop, MyFrame boardFrame) {
        super(upisi0zaBelog1zaCrnog, boardFrame);
        rank = (upisi0zaBelog1zaCrnog == 0) ? (byte) 1 : (byte) 8;
        file = (ovoJeLeviTop) ? (byte) 1 : (byte) 8;
        this.setPozicija(boardFrame.isWhitesPerspective(), boardFrame.getSquareLength());
    }

    @Override
    public boolean getNijeSePomerao() {
        return nijeSePomerao;
    }

    @Override
    public void setNijeSePomerao(boolean nijeSePomerao) {
        this.nijeSePomerao = nijeSePomerao;
    }

    @Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {
        boolean izlaz = false;
        if((this.rank == rankDestination) && (!((this.file)==(fileDestination)))){
            izlaz = true;
        }

        if((this.file == fileDestination) && (!((this.rank)==(rankDestination)))){
            izlaz = true;
        }
        return izlaz;
    }

    @Override
    public boolean nemaNistaIzmedju(int rank, int file, MyFrame boardFrame) {
        boolean izlaz = true;

           if (this.file == file){
               for(int i=(Math.min(this.rank, rank)+1); i<Math.max(this.rank, rank); i++ ){
                   if(!neJedemSopstvenog(i,file, boardFrame) || jedemTudjuFiguru(i,file, boardFrame)) {
                      izlaz = false;
                   }
               }
           } else if (this.rank == rank){
               for(int i=(Math.min(this.file, file)+1); i<Math.max(this.file, file); i++ ){
                   if(!neJedemSopstvenog(rank, i, boardFrame) || jedemTudjuFiguru(rank,i, boardFrame)) {
                       izlaz = false;
                   }
               }

           }

        return izlaz;
    }

    @Override
    public boolean Move(int rank, int file, MyFrame boardFrame) {
        boolean output = false;
        int startRank = this.rank;
        int startFile = this.file;

        if((NaturalMovement(rank, file, boardFrame)) && (neJedemSopstvenog(rank, file, boardFrame))  && nisamUsahuNakonPoteza(rank,file, boardFrame) && (nemaNistaIzmedju(rank,file, boardFrame))){

               if(jedemTudjuFiguru(rank, file, boardFrame)){
                   skloniFiguruSaTable(rank, file, boardFrame);
               }
               this.setRank(rank);
               this.setFile(file);
               this.setPozicija(boardFrame.isWhitesPerspective(), boardFrame.getSquareLength());
               nijeSePomerao = false;
               output = true;
           }
        return output;

        }


}
