package src.raznefigure;

import src.paketfigure.Figure;
import src.framepackage.*;
import src.raznefigure.*;
import src.framepackage.MoveSender.*;
import src.framepackage.MyFrame.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


// ---------------------------------------------------------------------
public class Pijun extends Figure {
    boolean nijeSePomerao = true;

    public Pijun() {
    }
    public Pijun(MyFrame boardFrame) {
        this.duzinaPolja = boardFrame.instanciranjeFrejma.duzinaPolja;
        initDimenzije();
    }

    public Pijun(int upisi0zaBelog1zaCrnog, int UnetiPocetnifilePijunaOd1Do8, MyFrame boardFrame) {

        super(upisi0zaBelog1zaCrnog, boardFrame);
        rank = (upisi0zaBelog1zaCrnog == 0) ? (byte) 2 : (byte) 7;
        file = (byte) UnetiPocetnifilePijunaOd1Do8;

      /*  if (UnetiPocetnifilePijunaOd1Do8 < 1) {
            file = 1;
        } else if (UnetiPocetnifilePijunaOd1Do8 > 8) {
            file = 8;
        } else {
            file = (byte) UnetiPocetnifilePijunaOd1Do8;
        } */


        this.setPozicija(boardFrame);
    }

    @Override
    public boolean nemaNistaIzmedju(int rank, int file, MyFrame boardFrame) {
            boolean izlaz = true;

            // proveravam samo za slucajeve kad se pijun pokrene dva polja
            if(Math.abs(rank - this.rank) ==2) {
                // Kada se pijun pomeri 2 polja npr. e2-e4, e3 je polje izmedju.
// Proveravam da li se neka figura nalazi na tom polju izmedju (na polju izmedju e2 i e4 u mom primeru)
// treba samo da nadjem rank polja izmedju, posto file ostaje isti, jer pijun ide pravo napred 2 polja
                byte rankIzmedju;
                rankIzmedju = (rank > this.rank)? (byte)(rank-1) : (byte)(this.rank-1);
                byte pozicijaIzmedju = (byte)(10*rankIzmedju + file);
                for (int i = 0; i < 16; i++) {
                    for(int j=0; j<2; j++){
                        if(boardFrame.figura[j][i].getPozicija() == pozicijaIzmedju){
                            izlaz = false;
                        }
                    }
                }
            }

            return izlaz;

    }

   @Override
    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame) {
        boolean izlaz = false;
        boolean jedemTudjuFiguru = boardFrame.nalaziSeFiguraBoje(rankDestination, fileDestination, invert(this.bojaFigure));
        // USLOV SMERA DODAJEM U METODI MOVE.

        if((Math.abs(rankDestination - this.rank) == 1)&&(this.file == fileDestination) && (!jedemTudjuFiguru)){
            izlaz = true;
        }

       if((Math.abs(rankDestination - this.rank) == 1)&&(Math.abs(this.file - fileDestination) ==1) && jedemTudjuFiguru){
            izlaz = true;            
        }

       if ((nijeSePomerao) && (Math.abs(rankDestination - this.rank) == 2)&&(this.file == fileDestination) && (!jedemTudjuFiguru)){
           izlaz = true;
       }


        return izlaz;
    }

    @Override
    public boolean Move(int rank, int file, MyFrame boardFrame) {
        boolean output = false;
        byte pocetniRank = (byte)this.rank;
        byte pocetniFile = (byte)this.file;


        boolean uslovSmera;
        if(boardFrame.getKoJeNaPotezu() == 0){
            uslovSmera = (rank > this.rank);
        } else {
            uslovSmera = (this.rank > rank);
        }

        if(NaturalMovement(rank, file, boardFrame) && neJedemSopstvenog(rank, file, boardFrame) && nisamUsahuNakonPoteza(rank,file, boardFrame) && uslovSmera && nemaNistaIzmedju(rank, file, boardFrame)){
                if(jedemTudjuFiguru(rank, file, boardFrame)){
                   skloniFiguruSaTable(rank, file, boardFrame);
                }
                this.setRank(rank);
                this.setFile(file);
                this.setPozicija(file,rank, boardFrame);
                nijeSePomerao = false;
                output = true;


// SLEDI KOD ZA PROMOCIJU PESAKA 8 - 7 * koJeNaPotezu je nacin da dobijem rank 8 ukoliko je pijun beli i rank 1 ukoliko je pijun crni
// u principu treba nam funkcija f(0) = 8, f(1) = 1. Ovo je najjednostavnija koju sam ja iz proslosti smislio.

                if(this.rank == (8 - 7*(boardFrame.getKoJeNaPotezu()))){
                    boardFrame.choosingPromotionPiece = true;
                    boardFrame.promotionHappened = true;
                  //  boardFrame.filePromovisanogPijuna = (byte)file;
                    for(int i=0; i<4; i++){
                        boardFrame.dugmiciPromocije[i].setVisible(true);
                        System.out.println("Showing promotion buttons.");
                    }
                    return true;
                }

     // SLEDI KOD ZA KOJI GLEDA DA LI SMO SE POMERILI DVA POLJA, DA BI SE OMOGUCIO EN PASSANT PROTIVNKIKU
    // PROTIVNIK IMA MOGUCNOST ZA EN PASSANT AKO SE PESAK POMERI ZA DVA POLJA
     // OVO JE I DALJE KOD KOJI PRIPADA PRVOM IF STATEMENT-U.
               if(Math.abs(this.rank - pocetniRank) == 2){
                   boardFrame.setFilePijunaKojiSePomerio2Polja(this.file);
                   boardFrame.pijun2PoljaNapredUovomPotezu = true;
               }


        } // PRESTAJE BLOK IF STATEMENT-A.

        byte fileEnPassantPijuna = boardFrame.getFilePijunaKojiSePomerio2Polja();
        if (fileEnPassantPijuna == MyFrame.INVALID_FILE) {
            return output;  // EN PASSANT NE MOZE, JER NIJE ODIGRANO 2 POLJA NAPRED PESAKOM U PROSLOM POTEZU, TJ.
        }
        byte rankSaKogMozeEnPassant = (byte)(5 - boardFrame.getKoJeNaPotezu());
        byte rankGdeZavrsavamPosleEnPassant = (boardFrame.getKoJeNaPotezu() == 0)? (byte)6 : (byte)3;
        byte rankEnPassantPijuna = this.rank;

        if((Math.abs(fileEnPassantPijuna - this.file) == 1)
                && (rankSaKogMozeEnPassant == this.rank)
                   && nisamUsahuNakonPoteza(rankGdeZavrsavamPosleEnPassant, fileEnPassantPijuna, boardFrame)) {
                          skloniFiguruSaTable(rankEnPassantPijuna, fileEnPassantPijuna, boardFrame);
                          this.setRank(rankGdeZavrsavamPosleEnPassant);
                          this.setFile(fileEnPassantPijuna);
                          this.setPozicija(boardFrame);
                          nijeSePomerao = false;
                          output = true;

        }

        return output;
    }
}
