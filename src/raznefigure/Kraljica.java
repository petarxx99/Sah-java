package raznefigure;

import paketfigure.Figure;
import raznefigure.*;
import framepackage.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

// --------------------------------------------------------------------
public class Kraljica extends Figure {

    public Kraljica(MyFrame boardFrame, ArrayList<Integer> istorijaPolozaja) {
        super(boardFrame, istorijaPolozaja);
    }

    public Kraljica(int upisi0zaBelog1zaCrnog, MyFrame boardFrame) {
        super(upisi0zaBelog1zaCrnog, boardFrame);
        rank = (upisi0zaBelog1zaCrnog == 0) ? (byte) 1 : (byte) 8;
        file = 4;
        this.setPozicija(boardFrame.isWhitesPerspective(), boardFrame.getSquareLength());
    }

    @Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {

        // kretanje kao top
        if((this.rank == rankDestination) && (this.file !=fileDestination)){
            return true;
        }

        if((this.file == fileDestination) && (this.rank !=rankDestination)){
            return true;
        }
        // kretanje kao lovac
        if((Math.abs((this.rank - rankDestination))) == (Math.abs((this.file - fileDestination))) ){
            if(this.rank - rankDestination != 0) {
                return true;
            }
        }

        return false;
    }

   @Override
    public boolean nemaNistaIzmedju(int rank, int file, MyFrame boardFrame) {

        boolean kaoLovac = (!(this.file==file)) && (!(this.rank==rank)) && ((Math.abs((this.rank - rank))) == (Math.abs((this.file - file))));
        boolean kaoTop = (this.file==file) || (this.rank==rank);

        if ((!kaoLovac) && (!kaoTop)) return false;

        if (kaoLovac){
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
        }


        if (kaoTop){
            if (this.file == file){
                for(int i=(Math.min(this.rank, rank)+1); i<Math.max(this.rank, rank); i++ ){
                    if(!neJedemSopstvenog(i,file, boardFrame) || jedemTudjuFiguru(i,file, boardFrame)) {
                        return false;
                    }
                }
            } else if (this.rank == rank){
                for(int i=(Math.min(this.file, file)+1); i<Math.max(this.file, file); i++ ){
                    if(!neJedemSopstvenog(rank, i, boardFrame) || jedemTudjuFiguru(rank,i, boardFrame)) {
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
            this.setPozicija(boardFrame.isWhitesPerspective(), boardFrame.getSquareLength());
            output = true;
        }
        return output;
    }
}


/*
@Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {
        boolean izlaz = false;
        // kretanje kao top
        if((this.rank == rankDestination) && (!(this.file ==fileDestination)) ){
            izlaz = true;
        }

        if((this.file == fileDestination) && (!(this.rank==rankDestination)) ){
            izlaz = true;
        }
        // kretanje kao lovac
        if((Math.abs((this.rank - rankDestination))) == (Math.abs((this.file - fileDestination))) ){
            if(!(this.rank - rankDestination == 0)) {
                izlaz = true;
            }
        }

        return izlaz;
    }




    // --------------------------------------------------
    @Override
    public boolean nemaNistaIzmedju(int rank, int file, MyFrame boardFrame) {
        boolean izlaz = true;

        boolean kaoLovac = (!(this.file==file)) && (!(this.rank==rank)) && ((Math.abs((this.rank - rank))) == (Math.abs((this.file - file))));
        boolean kaoTop = (this.file==file) || (this.rank==rank);

        if (kaoLovac){
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
                            izlaz = false;
                        }
                    }
                }
            }
        }


        if (kaoTop){
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
        }


        return izlaz;
      //  return true;
    }

 */
