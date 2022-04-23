package src.raznefigure;

import src.paketfigure.Figure;
import src.raznefigure.*;
import src.framepackage.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

// ------------------------------------------------------------------
public class Lovac extends Figure {

    public Lovac() {
    }

    public Lovac(MyFrame boardFrame, ArrayList<Integer> istorijaPolozaja) {
        super(boardFrame, istorijaPolozaja);
    }

    public Lovac(int upisi0zaBelog1zaCrnog, boolean ovoJeSvetliLovac, MyFrame boardFrame) {
        super(upisi0zaBelog1zaCrnog, boardFrame);
        rank = (upisi0zaBelog1zaCrnog == 0) ? (byte) 1 : (byte) 8;
        file = (ovoJeSvetliLovac) ? (byte) 3 : (byte) 6;
        this.setPozicija(boardFrame);
    }

    @Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {
        boolean izlaz = false;
        if( (Math.abs((this.rank - rankDestination))) == (Math.abs((this.file - fileDestination))) ){
            if(!(this.rank - rankDestination == 0)) {
                izlaz = true;
            }
        }
        return izlaz;
    }

    @Override
    public boolean nemaNistaIzmedju(int rank, int file, MyFrame boardFrame) {
        if (Math.abs(this.rank - rank) != Math.abs(this.file - file)) return false;
        if (this.rank - rank == 0) return false;
        if (Math.abs(this.rank - rank) > 7) return false;

// posto lovac ide dijagonalno, posmatrao sam ga kao lineranu funkciju y = kx + n
// gde je y rank, x file
// n = y - kx
//kx = y - n
// x = (y - n)/k

        int trenutniFile;
        int k = (rank - this.rank) / (file - this.file);
        int n = rank - k * file;
           for(int trenutniRank = (Math.min(this.rank, rank)+1); trenutniRank<Math.max(this.rank, rank); trenutniRank++ ){
                trenutniFile = (trenutniRank - n)/k;
                for(int i=0; i<16; i++){
                    for(int j=0; j<2; j++){
                        int trenutnaPozicija = 10*trenutniRank + trenutniFile;
                        if(boardFrame.figura[j][i].getPozicija() == trenutnaPozicija){
                            return false;
                       }
                    }
                }
           }

        return true;
    }

    @Override
    public boolean Move(int rank, int file, MyFrame boardFrame) {
        boolean output = false;
        int startRank = this.rank;
        int startFile = this.file;

        if((NaturalMovement(rank, file, boardFrame)) && (neJedemSopstvenog(rank, file, boardFrame)) && nisamUsahuNakonPoteza(rank, file, boardFrame) && (nemaNistaIzmedju(rank,file, boardFrame))){

            if(jedemTudjuFiguru(rank, file, boardFrame)){
                skloniFiguruSaTable(rank, file, boardFrame);
            }
            this.setRank(rank);
            this.setFile(file);
            this.setPozicija(boardFrame);
            output = true;

        }

        return output;
    }
}
