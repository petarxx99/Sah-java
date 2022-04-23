package src.paketfigure;


import src.raznefigure.*;
import src.framepackage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;


public class Figure extends JLabel {

    public final byte NIJEDNU_FIGURU = 100;
    public final byte NISAM_SE_MENJAO = -1;
    
    // karakteristike same table
    public final float ODNOS_FIGURE_I_POLJA = 0.6f;
    public int duzinaPolja;
    public int duzinaTable;
    public int duzinaFigure;
    public int odIvice; // rastojanje figure od ivice polja
    
    private ArrayList<Integer> istorijaPolozaja = new ArrayList<>();
    public int bioSamPijunKogFajla = NISAM_SE_MENJAO;
    public int naKomPomeranjuSamSePromovisao = NISAM_SE_MENJAO;
    
    public static boolean whitesPerspective;

    // -------------------------------------------------------------------------------

    protected byte rank;
    protected byte file;
    boolean figuraJeBela;
    protected byte bojaFigure; // 0 je bela, 1 je crna

    public void initDimenzije(){
        duzinaFigure = (int) Math.round(duzinaPolja * ODNOS_FIGURE_I_POLJA);
        odIvice = (int) Math.round((double)(duzinaPolja - duzinaFigure) / 2);
        duzinaTable = 8 * duzinaPolja;
    }

    // konstruktori
    public Figure(){

    }

    public Figure(MyFrame boardFrame, ArrayList<Integer> istorijaPolozaja) {
        this.duzinaPolja = boardFrame.instanciranjeFrejma.duzinaPolja;
        initDimenzije();
        this.istorijaPolozaja = istorijaPolozaja;
        bioSamPijunKogFajla = istorijaPolozaja.get(0) % 10;
        naKomPomeranjuSamSePromovisao = istorijaPolozaja.size();
    }

    public Figure(int upisi0zaBelog1zaCrnog, MyFrame boardFrame) {
        if (upisi0zaBelog1zaCrnog == 1) {
            this.bojaFigure = 1;
        } else {
            this.bojaFigure = 0;
        }
        this.duzinaPolja = boardFrame.instanciranjeFrejma.duzinaPolja;
        initDimenzije();
        this.setOpaque(true);
        this.setVisible(true);
    }
    // set metode --------------------------------------------------------------------------


   public void updateIstorijaPolozaja(){
       this.istorijaPolozaja.add(this.rank * 10 + this.file);
   } 

   
   public ArrayList<Integer> getIstorijaPolozaja(){
       return this.istorijaPolozaja;
   }
    
   /* void setBojaFigure(boolean figuraJeBela){
        this.figuraJeBela = figuraJeBela;
    } */

    public void setBojaFigure(int upisi0zaBeluFiguruUpisi1zaCrnuFiguru) {
        if (upisi0zaBeluFiguruUpisi1zaCrnuFiguru == 1) {
            bojaFigure = 1; // figura je crna
        } else {
            bojaFigure = 0; // figura je bela
        }
    }

    public void setPozicija(MyFrame boardFrame) {
        //this.setBounds(getXkoordinata(), getYkoordinata(), duzinaFigure, duzinaFigure);
        if(boardFrame.instanciranjeFrejma.whitesPerspective)
            this.setBounds(duzinaPolja * (file -1) + odIvice, duzinaTable - rank * duzinaPolja + odIvice, duzinaFigure, duzinaFigure);
        else
            this.setBounds(duzinaTable - duzinaPolja * file + odIvice, duzinaPolja * (rank-1) + odIvice, duzinaFigure, duzinaFigure);
    }

    public void setPozicija(int file, int rank, MyFrame boardFrame) {
        this.setFile(file);
        this.setRank(rank);
        this.setPozicija(boardFrame);
        //this.setBounds(getXkoordinata(), getYkoordinata(), duzinaFigure, duzinaFigure);
    }

    public void setRankFile(int rank, int file) {
        setRank(rank);
        setFile(file);
    }

    public void setRank(int rank) {
            this.rank = (byte)rank;
    }

    public void setFile(int file) {

        this.file = (byte)file;
    }

    // get metode -------------------------------------------------------------------------------

   public  boolean getDaLiJeBelaFigura() {
        return figuraJeBela;
    }

   public byte getBojaFigure() {
        return bojaFigure;
    }

   public byte getRank() {
        return rank;
    }

   public byte getFile() {
        return file;
    }

   public int getPozicija(){
        return  rank*10 + file;
   }



   public static byte invert(int koJeNaPotezu){
       byte izlaz =1;
       if(koJeNaPotezu==0){
           izlaz = 1;
        }
       if(koJeNaPotezu==1){
           izlaz = 0;
       }
       return izlaz;
   }


    // kretanje figura sa jednog mesta na drugo ------------------------------------------------
    // --------------------------------------------------------------------------------------------







    public boolean jedemTudjuFiguru(int rankDestination, int fileDestination, MyFrame boardFrame){

        boolean izlaz = false;

        for(int i=0; i<16; i++){
            if ((boardFrame.figura[invert(this.getBojaFigure())] [i].getFile() == fileDestination)
                    && (boardFrame.figura[invert(this.getBojaFigure())] [i].getRank() == rankDestination)){
                izlaz = true;
            }
        }
        return izlaz;

    }



    public void skloniFiguruSaTable(int rank, int file, MyFrame boardFrame){
        for(int i=0; i<2; i++){
            for(int j=0; j<16; j++){
                if(boardFrame.figura[i][j].getRank() == rank){
                    if(boardFrame.figura[i][j].getFile() == file){
                        boardFrame.figura[i][j].setBounds((duzinaTable+odIvice), 0, duzinaFigure, duzinaFigure);
                        boardFrame.figura[i][j].setRankFile(9,99);
                    }
                }
            }
        }
    }



    public boolean mozeDociNaPolje(int pozicijaTj10putaRankPlusFile, MyFrame boardFrame){
        boolean izlaz = false;
        int rank = (int)(pozicijaTj10putaRankPlusFile / 10);
        int file = pozicijaTj10putaRankPlusFile % 10;

        if((this.NaturalMovement(rank, file, boardFrame)) && this.nemaNistaIzmedju(rank, file, boardFrame)){
            izlaz = true;
        }

        return izlaz;
    }

    public boolean nisamUsahu(MyFrame boardFrame){
        boolean izlaz = true;

        int pozicijaKralja = boardFrame.figura[boardFrame.getKoJeNaPotezu()][0].getPozicija();
        int fileKralja = pozicijaKralja % 10;

        // prolazim kroz sve protivnikove figure da vidim da li neka moze doci na poziciju mog kralja
        // pisao bih figura[<protivnikova boja>][<prodji kroz sve figure od 0 do 15>]
        // ali moram da dodam boardFrame. ispred da bih imao pristup figurama
        // zato sto se figure nalaze u MyFrame klasi. Takodje, figure nisu static tipa, te zato
        // moram da pristupim instanci klase MyFrame (instanca klase je frejm1)
        // frejm1 sam deklarisao u klasi InstanciranjeFrejma, zato ide pocetak InstanciranjeFrejma.
        for(int i=0; i<8; i++){
            if(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame)){
                izlaz = false;
            }
        }

// pijuni jedu ukoso, oni ne jedu napred, iako idu napred, zato se moraju tretirati drugacije od ostalih figura.
// od 8 do 15 su pijuni, od 0 do 7 su ostale figure

        for(int i=8; i<16; i++){
            if((boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame))
          && (!(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].getFile() == fileKralja))){
                izlaz = false;
            }
        }

        return izlaz;
    }

    public boolean nisamUsahuNakonPoteza(int rankDestinacija, int fileDestinacija, MyFrame boardFrame){
        boolean izlaz = true;
        byte sacuvajRank = this.getRank();
        byte sacuvajFile = this.getFile();
        byte pozicijaDestinacija = (byte)(rankDestinacija * 10 + fileDestinacija);
        byte kojuFiguruJedem = NIJEDNU_FIGURU; // Ako ne jedem ni jednu figuru, ova promenljiva ce ostati na 100.
        this.setRankFile(rankDestinacija, fileDestinacija);
        // Ukoliko jedem figuru koja daje sah ili indirektno daje sah moram da je izbacim sa table prilikom provere, inace ce biti bag
        // OVO JE JAKO BITAN KORAN KOJI UKLANJA BUG!!!!!
        for(int i=0; i<16; i++){
            if(pozicijaDestinacija == boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].getPozicija()){
                boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].setRankFile(9,99);
                kojuFiguruJedem = (byte) i;
            }
        }
        int pozicijaKralja = boardFrame.figura[boardFrame.getKoJeNaPotezu()][0].getPozicija();
        int fileKralja = pozicijaKralja % 10;

        // prolazim kroz sve protivnikove figure da vidim da li neka moze doci na poziciju mog kralja
        // pisao bih figura[<protivnikova boja>][<prodji kroz sve figure od 0 do 15>]
        // ali moram da dodam boardFrame. ispred da bih imao pristup figurama
        // zato sto se figure nalaze u MyFrame klasi. Takodje, figure nisu static tipa, te zato
        // moram da pristupim instanci klase MyFrame (instanca klase je frejm1)
        // frejm1 sam deklarisao u klasi InstanciranjeFrejma, zato ide pocetak InstanciranjeFrejma.

        for(int i=0; i<8; i++){
            if(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame)){
                izlaz = false;
            }
        }

// pijuni jedu ukoso, oni ne jedu napred, iako idu napred, zato se moraju tretirati drugacije od ostalih figura.
// od 8 do 15 su pijuni, od 0 do 7 su ostale figure. Obratiti paznju na drugi uslov i uzvicnik ispred njega.
// Prvi if u petlji se stara o tome da ako je u pitanju figura koja je promovisana, a ne pijun, onda tu figuru posebno obradjujemo.
        for(int i=8; i<16; i++){
            if (! boardFrame.figura[invert(boardFrame.koJeNaPotezu)][i].getClass().getSimpleName().trim().equals("Pijun")) {
                if(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame)){
                    izlaz = false;  // Ne moze return false; !!!! Zato sto treba da vratim na tablu figuru koju sam eventualno pojeo!
                }
                // Sada obradjujem slucaj da se radi o pijunu.
            } else if  ((boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame))
                    && (!(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].getFile() == fileKralja))){
                izlaz = false;
            }
        }

// vracam figuru na svoju pocetnu poziciju, ako je potez legalan, metoda Move ce ponovo pomeriti figuru
        this.setRankFile(sacuvajRank, sacuvajFile);
// Vracam figuru koju sam pojeo nazad na tablu. Ukoliko je potez legalan, metoda Move ce je ponovo skloniti sa table.
// Ako je kojuFiguruJedem jednaka 100, to onda znaci da nisam pojeo nijednu figuru.
        if(kojuFiguruJedem != NIJEDNU_FIGURU){
            boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][kojuFiguruJedem].setRankFile(rankDestinacija, fileDestinacija);
        }

        return izlaz;
    }

    // Pravim ovo istu metodu nisamUsahu, ali static da biih mogao da je koristim u sah mat metodi.
    public static boolean nisamUsahuStatic(MyFrame boardFrame){
        boolean izlaz = true;

        int pozicijaKralja = boardFrame.figura[boardFrame.getKoJeNaPotezu()][0].getPozicija();
        int fileKralja = pozicijaKralja % 10;

        for(int i=0; i<8; i++){
            if(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame)){
                izlaz = false;
            }
        }
        for(int i=8; i<16; i++){
            if((boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].mozeDociNaPolje(pozicijaKralja, boardFrame))
                    && (!(boardFrame.figura[invert(boardFrame.getKoJeNaPotezu())][i].getFile() == fileKralja))){
                izlaz = false;
            }
        }

        return izlaz;
    }

    public static boolean NemamPotez(MyFrame boardFrame){
       boolean izlaz = true;

       for(int i=0; i<16; i++){
         /*  byte rank = (byte)boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].getRank();
           byte file = (byte)boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].getFile(); */

           for(int rank = 1; rank<9; rank++) {
              for(int file = 1; file<9; file++) {
/* if proverava da li imam legalan potez */ if ((boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].NaturalMovement(rank, file, boardFrame)) && boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].neJedemSopstvenog(rank, file, boardFrame) && boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].nisamUsahuNakonPoteza(rank, file, boardFrame) && boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].nemaNistaIzmedju(rank, file, boardFrame)) {
                       izlaz = false;
                  }
              }
           }
       }
       return izlaz;
    }


    public static boolean SadaJeSahMat(MyFrame boardFrame){
        boolean izlaz = true;

        for(int i=0; i<16; i++){
         /*  byte rank = (byte)boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].getRank();
           byte file = (byte)boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].getFile(); */

            for(int rank = 1; rank<9; rank++) {
                for(int file = 1; file<9; file++) {
                    /* if proverava da li imam legalan potez */ if ( !(nisamUsahuStatic(boardFrame)) && (boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].NaturalMovement(rank, file, boardFrame)) && boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].neJedemSopstvenog(rank, file, boardFrame) && boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].nisamUsahuNakonPoteza(rank, file, boardFrame) && boardFrame.figura[boardFrame.getKoJeNaPotezu()][i].nemaNistaIzmedju(rank, file, boardFrame)) {
                        izlaz = false;
                    }
                }
            }
        }
        return izlaz;
    }

    public boolean neJedemSopstvenog(int rankDestination, int fileDestination, MyFrame boardFrame){
        boolean izlaz = true;

        for(int i=0; i<16; i++){
            if ((boardFrame.figura[this.getBojaFigure()] [i].getFile() == fileDestination)
                    && (boardFrame.figura[this.getBojaFigure()] [i].getRank() == rankDestination)){
                izlaz = false;
            }
        }
        return izlaz;
    }

    public boolean NaturalMovement(int rankDestination, int fileDestination, MyFrame boardFrame){
        return true;
    }

    public boolean nemaNistaIzmedju(int rank, int file, MyFrame boardFrame){
        return true;
    }




    public boolean getNijeSePomerao(){
        return true;
    }

    public void setNijeSePomerao(boolean daLiSePomerao){}


   public boolean Move(int rank, int file, MyFrame boardFrame){
        return true;
   }


}
