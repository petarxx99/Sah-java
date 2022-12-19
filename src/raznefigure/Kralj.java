package src.raznefigure;


import src.paketfigure.Figure;
import src.raznefigure.*;
import src.framepackage.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Kralj extends Figure {

    boolean nijeSePomerao = true;

    // konstruktori
    public Kralj() {
    }

    public Kralj(MyFrame boardFrame) {
        this.duzinaPolja = boardFrame.duzinaPolja;
        initDimenzije();
    }

    public Kralj(int upisi0zaBelog1zaCrnog, MyFrame boardFrame) {
        super(upisi0zaBelog1zaCrnog, boardFrame);
        rank = (upisi0zaBelog1zaCrnog == 0) ? (byte) 1 : (byte) 8;
        file = 5;
        this.setPozicija(boardFrame);
        updateIstorijaPolozaja();
    }



    @Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {
        boolean izlaz = false;
         if((Math.abs((this.rank - rankDestination)) == 1) && ( (Math.abs(fileDestination - this.file)==1) || (Math.abs(this.file - fileDestination)==0) )){
             izlaz = true;
        }

        if((Math.abs((this.file - fileDestination)) == 1) && ( (Math.abs(rankDestination - this.rank)==1) || (Math.abs(this.rank - rankDestination)==0) )){
            izlaz = true;
        }
        return izlaz;
    }

    @Override
    public boolean Move(int rank, int file, MyFrame boardFrame) {
        boolean output = false;
        int startRank = this.rank;
        int startFile = this.file;

        if((NaturalMovement(rank, file, boardFrame)) && (neJedemSopstvenog(rank, file, boardFrame)) && (nisamUsahuNakonPoteza(rank, file, boardFrame))){
            if(jedemTudjuFiguru(rank, file, boardFrame)){
                skloniFiguruSaTable(rank, file, boardFrame);
            }
            this.setRank(rank);
            this.setFile(file);
            this.setPozicija(boardFrame);
            nijeSePomerao = false;
            output = true;

        }

        // rokada ka a file-u, queenside, velika rokada
        if((this.rank == rank) && (file == 3)){
           if(nijeSePomerao && boardFrame.figura[boardFrame.getKoJeNaPotezu()][2].getNijeSePomerao()){
                if(this.nisamUsahu(boardFrame) && this.nisamUsahuNakonPoteza(this.rank, 4, boardFrame) && this.nisamUsahuNakonPoteza(this.rank, 3, boardFrame)){
                    this.setFile(file);
                    this.setPozicija(boardFrame);
                    nijeSePomerao = false;
                    boardFrame.figura[boardFrame.getKoJeNaPotezu()][2].setFile(4);
                    boardFrame.figura[boardFrame.getKoJeNaPotezu()][2].setPozicija(boardFrame);
                    boardFrame.figura[boardFrame.getKoJeNaPotezu()][2].setNijeSePomerao(false);
                    output = true;

                }
           }
        }

        // rokada ka h file-u, kingside, mala rokada
        if((this.rank == rank) && (file == 7)){
            if(nijeSePomerao && boardFrame.figura[boardFrame.getKoJeNaPotezu()][3].getNijeSePomerao()){
                if(this.nisamUsahu(boardFrame) && this.nisamUsahuNakonPoteza(this.rank, 6, boardFrame) && this.nisamUsahuNakonPoteza(this.rank, 7, boardFrame)){
                    this.setFile(file);
                    this.setPozicija(boardFrame);
                    nijeSePomerao = false;
                    boardFrame.figura[boardFrame.getKoJeNaPotezu()][3].setFile(6);
                    boardFrame.figura[boardFrame.getKoJeNaPotezu()][3].setPozicija(boardFrame);
                    boardFrame.figura[boardFrame.getKoJeNaPotezu()][3].setNijeSePomerao(false);
                    output = true;

                }
            }
        }

        return output;
    }
    // ovde se zavrsava Move metoda



}

