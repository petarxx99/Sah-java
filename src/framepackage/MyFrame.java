package src.framepackage;

import src.paketfigure.Figure;
import src.paketpolje.Polje;
import src.raznefigure.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import src.framepackage.InstanciranjeFrejma.*;

public class MyFrame extends JFrame implements ActionListener {

    // karakteristike table
    public int duzinaPolja;
    public int duzinaTable;

    //------------------------------------------------------------------------------------------------
    public final static byte BROJ_FIGURA_JEDNE_BOJE = 16;
    public final static byte RESET_EN_PASSANT = 10;
    public final static byte INVALID_FILE = 100;
    public final static byte WHITES_TURN = 0, WHITE_PIECES = 0, WHITE = 0;
    public final static byte BLACKS_TURN = 1, BLACK_PIECES = 1, BLACK = 1;
    public final static byte NO_PROMOTION = -1, PROMOTE_QUEEN = 0, PROMOTE_ROOK = 1, PROMOTE_BISHOP = 2, PROMOTE_KNIGHT = 3;

    // promenljive
    public boolean ovoJePoljeDestinacije = false;
    public boolean kliknuoSamNaPoljeSaFigurom = false;
    public byte koJeNaPotezu = 0; // 0 = beli je na potezu, 1 = crni je na potezu
    public static byte rankKliknuteFigure, fileKliknuteFigure, pozicijaKliknuteFigure, indeksKliknuteFigure;
    public static String beli = "beli", crni = "crni";
    public JButton dugmeZaSah;
    public static boolean dugmeZaSahJeOn = false;
    public int brojPoteza = 0;
    public byte  brojPromocija = 0;
    public byte pozicijaPijunaKojiSePomerio2Polja = 0;
    public byte filePijunaKojiSePomerio2Polja = 100;
    public boolean pijun2PoljaNapredUovomPotezu = false;
    //public boolean pomerioSamPesaka2Polja = false;
    public  boolean choosingPromotionPiece = false;

    // ------------------------------------------------------
    public JLayeredPane layeredPane;
    public JLabel[][] poljePozadina = new JLabel[9][9];
    public Polje[][] polje = new Polje[9][9];
    public JLabel labelCijiPotez, labelBrojPoteza, labelPrikaziSahMat, labelPrikaziSahMat2, labelObavestavamOsahu;

    //----------------------------------------------------------------------
    public JButton promocijaKraljica = new JButton("kraljica");
    public JButton promocijaTop = new JButton("top");
    public JButton promocijaLovac = new JButton("lovac");
    public JButton promocijaKonj = new JButton("konj");
    public JButton[] dugmiciPromocije = {promocijaKraljica, promocijaTop, promocijaLovac, promocijaKonj};


    public ImageIcon slikaBelogKralja = new ImageIcon("beliKralj.png");
    public ImageIcon slikaBeleKraljice = new ImageIcon("belaKraljica.png");
    public ImageIcon slikaBelogKonja = new ImageIcon("beliKonj.png");
    public ImageIcon slikaBelogLovca = new ImageIcon("beliLovac.png");
    public ImageIcon slikaBelogTopa = new ImageIcon("beliTop.png");
    public ImageIcon slikaBelogPijuna = new ImageIcon("beliPijun.png");
    public ImageIcon slikaCrnogKralja = new ImageIcon("crniKralj.png");
    public ImageIcon slikaCrneKraljice = new ImageIcon("crnaKraljica.png");
    public ImageIcon slikaCrnogKonja = new ImageIcon("crniKonj.png");
    public ImageIcon slikaCrnogLovca = new ImageIcon("crniLovac.png");
    public ImageIcon slikaCrnogTopa = new ImageIcon("crniTop.png");
    public ImageIcon slikaCrnogPijuna = new ImageIcon("crniPijun.png");


    // instanciranje figura ----------------------------------------------------------------------

    public Figure[][] figura = new Figure[2][16];

    public InstanciranjeFrejma instanciranjeFrejma;
    public MoveSender moveSender;
    public String myOpponentIp;
    public int  myOpponentPort, thisProgramPort;
    public Opponents myOpponent;
    public byte startRank, startFile, destinationRank, destinationFile;
    public boolean moveWasPlayed = false;
    public boolean promotionHappened = false;
    public boolean promotionButtonClicked = false;  // SKLONI DUGMICE PROMOCIJE METODA GA MENJA
    public byte promotionButtonNumber = -1;

    // konstruktor
    public MyFrame(InstanciranjeFrejma instanciranjeFrejma, MoveSender moveSender) {
        this.instanciranjeFrejma = instanciranjeFrejma;
        this.moveSender = moveSender;
        this.duzinaPolja = instanciranjeFrejma.duzinaPolja;
        this.duzinaTable = 8 * duzinaPolja;

        this.setSize(700, 700);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 700, 700);
        layeredPane.setOpaque(true);
        layeredPane.setBackground(new Color(100, 130, 220));

        addCosmeticsToTheLayeredPane(layeredPane);
        addPromButtonsToTheLayeredPane(layeredPane);
        addBoardBackgroundAndButtonsToTheLayeredPane(layeredPane);
        
        initFigure();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                layeredPane.add(figura[i][j], Integer.valueOf(1));
            }
        }

        // ako hocu da igram protiv drugog kompjutera 
        initMoveSender(moveSender);
        
        this.add(layeredPane);
        this.setVisible(true);

    }
    // ovde prestaje konstruktor

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
// prolazim kroz sva polja (dugmice) da vidim koje je pritisnuto
        for (int rank = 1; rank < 9; rank++) {
            for (int file = 1; file < 9; file++) {
                if (actionEvent.getSource() == polje[rank][file]) {
                    Polje kliknutoPolje = polje[rank][file];

                     if (shouldIbreakTheLoop()) {
                         break;
                     }
                                         
  /* Da bih pomerio figuru moram da kliknem na 2 dugmeta, 1. dugme je dugme sa figurom,
 2. dugme je dugme destinacije, tj. dugme/polje gde zelim da ta figura ode.
 Sada cu napisati kod za 2 slucaja, PRVI SLUCAJ: kliknuo sam na polje na kojoj se nalazi figura koju zelim da pomerim.
 DRUGI SLUCAJ: kliknuo sam na polje gde zelim da figura ode. */
// Sada obradjujem slucaj da smo kliknuli na polje s kojeg zelimo da pomerimo figuru (1. SLUCAJ).

                    if (!ovoJePoljeDestinacije) {
                        resetBoardPromenljive();                         
                        showWhoseTurnItIs();
                        
 /* Proveravam da li je igrac kliknuo na figuru, ako jeste, pamtim na koju je kliknuo. */
                        for (int i = 0; i<BROJ_FIGURA_JEDNE_BOJE; i++) {
                            if (figura[koJeNaPotezu][i].getPozicija() == kliknutoPolje.getPozicija()) {
                                ovoJePoljeDestinacije = true;
                                indeksKliknuteFigure = (byte) i;
                                startRank = (byte) rank;
                                startFile = (byte) file;
                                break;
                            }
                        }
                    }
/* Sada obradjujem slucaj da smo kliknuli na polje na koje zelimo da pomerimo figuru (2. SLUCAJ). */
                   else if (ovoJePoljeDestinacije) {
                          if (jedemSopstvenuFiguru(rank, file, koJeNaPotezu)){
                              indeksKliknuteFigure = indeksIgraceveFigureNaPolju(rank, file, koJeNaPotezu);
                              break;
                          }
                          boolean moveWasPlayed = pokusajOdigratiPotez(rank, file, indeksKliknuteFigure);
                          labelObavestenja();
                          if (myOpponent != Opponents.HUMAN_ON_THIS_PC){
                                sendAndReceiveMove(moveWasPlayed, promotionHappened);
                          }
                   }
                }
            }
        }

        // ovde zadajem komande sta se desava ako igraci kliknu na dugme za sah
        // dugme moze da bude on ili off. Ako je dugme on, igraci ce se obavestavati o tome da li su u sahu.
        if(actionEvent.getSource() == dugmeZaSah){
            if(dugmeZaSahJeOn){
                labelObavestavamOsahu.setVisible(false);
                dugmeZaSahJeOn = false;
            } else {
                labelObavestavamOsahu.setVisible(true);
                dugmeZaSahJeOn = true;
            }
        }


        // SLEDI KOD ZA PROMOCIJU PESAKA -----------------------------------------------------------
        
        if(actionEvent.getSource() == dugmiciPromocije[0]){
            promoteQueen();
        }
        
        if(actionEvent.getSource() == dugmiciPromocije[1]){
            promoteRook();
        }
        
        if(actionEvent.getSource() == dugmiciPromocije[2]){
            promoteBishop();
        }
        
        if(actionEvent.getSource() == dugmiciPromocije[3]){
            promoteKnight();
        }

    }

    // ACTION PERFORMED KONTROLISANJE DUGMICA SE ZAVRSAVA OVDE ---------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void obavestiDaJePromocijaGotova(){
        promotionButtonClicked = true;
        choosingPromotionPiece = false;
    }

    public void skloniDugmicePromocije(){
        for(int i=0; i<4; i++){
            dugmiciPromocije[i].setVisible(false);
        }
    }

    public void promoteQueen(){
        promotionButtonNumber = 0;
        for(int i=8; i<16; i++){
            if(figura[koJeNaPotezu][i].getRank() == (8 - 7*koJeNaPotezu)){
                int cuvajRank = figura[koJeNaPotezu][i].getRank();
                int cuvajFile = figura[koJeNaPotezu][i].getFile();
                ArrayList<Integer> istorijaPolozaja = figura[koJeNaPotezu][i].getIstorijaPolozaja();
                
                skiniPijunaZaPromocijuSaTable(i);

                figura[koJeNaPotezu][i] = new Kraljica(this, istorijaPolozaja);
                staviSlikuPromovisanojFiguri(i, slikaBeleKraljice, slikaCrneKraljice);
                staviPromovisanuFiguruNaTablu(cuvajRank, cuvajFile, i);
            }
        }
        
        krajPromocije();
    }

    public void promoteRook(){
        promotionButtonNumber = 1;
        for(int i=8; i<16; i++){
            if(figura[koJeNaPotezu][i].getRank() == (8 - 7*(koJeNaPotezu))){
                int cuvajRank = figura[koJeNaPotezu][i].getRank();
                int cuvajFile = figura[koJeNaPotezu][i].getFile();
                ArrayList<Integer> istorijaPolozaja = figura[koJeNaPotezu][i].getIstorijaPolozaja();
                
                skiniPijunaZaPromocijuSaTable(i);

                figura[koJeNaPotezu][i] = new Top(this, istorijaPolozaja);
                staviSlikuPromovisanojFiguri(i, slikaBelogTopa, slikaCrnogTopa);
                staviPromovisanuFiguruNaTablu(cuvajRank, cuvajFile, i);
            }
        }
        
        krajPromocije();
    }

    public void promoteBishop(){
        promotionButtonNumber = 2;
        for(int i=8; i<16; i++){
            if(figura[koJeNaPotezu][i].getRank() == (8 - 7*(koJeNaPotezu))){
                int cuvajRank = figura[koJeNaPotezu][i].getRank();
                int cuvajFile = figura[koJeNaPotezu][i].getFile();
                ArrayList<Integer> istorijaPolozaja = figura[koJeNaPotezu][i].getIstorijaPolozaja();
                
                skiniPijunaZaPromocijuSaTable(i);

                figura[koJeNaPotezu][i] = new Lovac(this, istorijaPolozaja);
                staviSlikuPromovisanojFiguri(i, slikaBelogLovca, slikaCrnogLovca);
                
                staviPromovisanuFiguruNaTablu(cuvajRank, cuvajFile, i);
            }
        }
        
        krajPromocije();
    }

    public void promoteKnight(){
        promotionButtonNumber = 3;
        for(int i=8; i<16; i++){
            if(figura[koJeNaPotezu][i].getRank() == (8 - 7*(koJeNaPotezu))){
                int cuvajRank = figura[koJeNaPotezu][i].getRank();
                int cuvajFile = figura[koJeNaPotezu][i].getFile();
                ArrayList<Integer> istorijaPolozaja = figura[koJeNaPotezu][i].getIstorijaPolozaja();
                
                skiniPijunaZaPromocijuSaTable(i);

                figura[koJeNaPotezu][i] = new Konj(this, istorijaPolozaja);
                staviSlikuPromovisanojFiguri(i, slikaBelogKonja, slikaCrnogKonja);
                
                staviPromovisanuFiguruNaTablu(cuvajRank, cuvajFile, i);
            }
        }
        
        krajPromocije();  
    }


    private void krajPromocije(){
        updateIstorijaPolozajaFigura();
        koJeNaPotezu = Figure.invert(koJeNaPotezu);
        brojPoteza++;
        skloniDugmicePromocije();
        obavestiDaJePromocijaGotova();
        labelObavestenja();
    }
    
    private void staviPromovisanuFiguruNaTablu(int cuvajRank, int cuvajFile, int indeksFigure){
        figura[koJeNaPotezu][indeksFigure].setRankFile(cuvajRank, cuvajFile);
        figura[koJeNaPotezu][indeksFigure].setPozicija(this);
        figura[koJeNaPotezu][indeksFigure].setBojaFigure(koJeNaPotezu);
        layeredPane.add(figura[koJeNaPotezu][indeksFigure], Integer.valueOf(1));
    }
    
    private void staviSlikuPromovisanojFiguri(int indeksFigure, ImageIcon belaSlika, ImageIcon crnaSlika){
        if(koJeNaPotezu == 0){
                figura[koJeNaPotezu][indeksFigure].setIcon(belaSlika);
        } else {
                figura[koJeNaPotezu][indeksFigure].setIcon(crnaSlika);
        }
    }

   private void skiniPijunaZaPromocijuSaTable(int i){  // i je indeksFigure
 figura[koJeNaPotezu][i].setRankFile(8+brojPromocija,9+brojPromocija);
 figura[koJeNaPotezu][i].setBounds(duzinaTable+figura[0][0].odIvice, duzinaTable, figura[0][0].duzinaFigure, figura[0][0].duzinaFigure);
   } 
   
   public boolean pokusajOdigratiPotez(int rank, int file, int indeksKliknuteFigure){
                                    pijun2PoljaNapredUovomPotezu = false; /* RESETUJEM ZBOG KODA ISPOD moveWasPlayed linije koda, zbog ovog if statement-a. */
                                    ovoJePoljeDestinacije = false;

                                    destinationRank = (byte)rank;
                                    destinationFile = (byte)file;

                                    moveWasPlayed = figura[koJeNaPotezu][indeksKliknuteFigure].Move(rank, file, this);
                                    if (moveWasPlayed && !promotionHappened){
                                        updateIstorijaPolozajaFigura();
                                        koJeNaPotezu = Figure.invert(koJeNaPotezu);
                                        brojPoteza++;
                                    } 
                                    
//SLEDI KOD KOJI SE BRINE ZA EN PASSANT. UKOLIKO U PRETHODNOM POTEZU NISMO POMERILI PESAKA 2 POLJA NAPRED,
// ONDA ZELIMO DA filePijunaKojiSePomerio2Polja BUDE NEPOSTOJECI.
// KAKO SAHOVSKA TABLA IMA 8 FILE-OVA, FILE 100 JE NEPOSTOJECI.
// UKOLIKO SMO U PRETHODNOM POTEZU POMERILI PIJUNA 2 POLJA NAPRED,
// ONDA filePijunaKojiSePomerio2Polja NECE BITI RESETOVAN NA 100.
                                    if(!pijun2PoljaNapredUovomPotezu){
                                        filePijunaKojiSePomerio2Polja = INVALID_FILE;
                                    }
                                    
                                    return moveWasPlayed;
   }
   
   
   public void resetBoardPromenljive(){
        moveWasPlayed = false; // Resetujem ovu promenljivu.
        promotionHappened = false; // Resetujem ovu promenljivu. obavestiDaJePromocija gotova metoda menja ovu promenljivu. Nekad ju je menjala metoda Skloni dugmice promocije.
        promotionButtonClicked = false; // Resetujem ovu promenljivu. obavestiDaJePromocija gotova metoda menja ovu promenljivu. Nekad ju je menjala metoda Skloni dugmice promocije.
        promotionButtonNumber = -1;  //Resetujem ovu promenljivu. promoteQueen/Rook/Bishop/Knight metode menjaju ovu promenljivu.          
        pijun2PoljaNapredUovomPotezu = false;
        choosingPromotionPiece = false;
   }
   
    public void sendAndReceiveMove(boolean moveWasPlayed, boolean promotionHappened){
         if(promotionHappened){
                 Thread threadPromotion = new Thread(new PromotionThread(startRank, startFile, destinationRank, destinationFile, promotionButtonNumber, this));
                 threadPromotion.start();
         
         
         } else if (moveWasPlayed) {
                 Thread threadGetAndSendMove = new Thread(()->moveSender.getAndSendMove(startRank, startFile, destinationRank, destinationFile, -1));
                 threadGetAndSendMove.start();
        }
                                                
    }
    
    
    
    private boolean shouldIbreakTheLoop(){
         // Ukoliko igram protiv nekog sa drugog kompjutera, onda ne mogu ja da povucem njegov potez.
// Za to se brine sledeca linija koda.
                    boolean output = false;
                    
                    if((myOpponent != Opponents.HUMAN_ON_THIS_PC)  && (koJeNaPotezu == moveSender.opponentsColor)) {
                        System.out.println("Sada je protivnikov potez.");
                        System.out.println("Na potezu je = " + koJeNaPotezu);
                        System.out.println("Protivnik ima boju = " + moveSender.opponentsColor);
                        output = true;
                    }
// Ukoliko trenutno biram u sta cu da promovisem pesaka i dalje je moj potez, ali ja ne bih smeo da
// pomerim nijednu figuru (ja samo mogu da biram koju novu figuru cu da izvucem).
                    if (choosingPromotionPiece) {
                        output = true;
                    }
                    
                    return output;
    }
    
    
    public void updateIstorijaPolozajaFigura(){
        for(int i=0; i<2; i++){
            for(int j=0; j<16; j++){
                figura[i][j].updateIstorijaPolozaja();
            }
        }
    }
    
    
     public void labelObavestenja(){
       // SADA SLEDE FINESE KOJE SU NEBITNE ZA SAMO FUNKCIONISANJE SAHA
                                    labelBrojPoteza.setText("Broj odigranih poteza: "+String.valueOf(brojPoteza));
                                    showWhoseTurnItIs();

                                   // metoda Move je promenila koJeNaPotezu
                                    // hocu da proverim da li je sah mat.

                                    if(Figure.NemamPotez(this)) {
                                        if (Figure.SadaJeSahMat(this)) {
                                            labelObavestavamOsahu.setText("Sah mat");
                                            labelPrikaziSahMat.setText("Partija je zavrsena matom.");
                                            if (koJeNaPotezu == 1) {
                                                labelPrikaziSahMat2.setText("Beli je pobedio.");
                                            } else {
                                                labelPrikaziSahMat2.setText("Crni je pobedio.");
                                            }

                                        } else if (figura[koJeNaPotezu][0].nisamUsahu(this)){
                                            labelObavestavamOsahu.setText("PAT");
                                            labelPrikaziSahMat.setText("Partija je zavrsena patom.");
                                            labelPrikaziSahMat2.setText("NERESENO");
                                        }

                       // ukoliko nije sah mat, zelim da proverim da li je sah, te o tome obavestim igraca.
                                    }else if(!(figura[koJeNaPotezu][0].nisamUsahu(this))){
                                        labelObavestavamOsahu.setText("U sahu ste.");
                                    } else if (figura[koJeNaPotezu][0].nisamUsahu(this)){
                                        labelObavestavamOsahu.setText("Niste u sahu.");
                                    }
                                    System.out.println("moveWasPlayed: " + moveWasPlayed);
   }
   
    public void showWhoseTurnItIs(){
        if(koJeNaPotezu == 0){
                            labelCijiPotez.setText("Na potezu je: "+ beli);
                        } else {
                            labelCijiPotez.setText("Na potezu je: "+ crni);
                        }
    
    }

    private void initFigure(){
        for(int i=0; i<2; i++){
            figura[i][0] = new Kralj(i, this);
            figura[i][1] = new Kraljica(i, this);
            figura[i][2] = new Top(i,true, this);
            figura[i][3] = new Top(i, false, this);
            figura[i][4] = new Lovac(i,true, this);
            figura[i][5] = new Lovac(i, false, this);
            figura[i][6] = new Konj(i,true, this);
            figura[i][7] = new Konj(i, false, this);

            for(int j=1; j<9; j++){
                figura[i][(7+j)] = new Pijun(i,j, this);
            }
        }

        // dodavanje slika na figure
        figura[0][0].setIcon(slikaBelogKralja);
        figura[0][1].setIcon(slikaBeleKraljice);
        figura[0][2].setIcon(slikaBelogTopa);
        figura[0][3].setIcon(slikaBelogTopa);
        figura[0][4].setIcon(slikaBelogLovca);
        figura[0][5].setIcon(slikaBelogLovca);
        figura[0][6].setIcon(slikaBelogKonja);
        figura[0][7].setIcon(slikaBelogKonja);

        figura[1][0].setIcon(slikaCrnogKralja);
        figura[1][1].setIcon(slikaCrneKraljice);
        figura[1][2].setIcon(slikaCrnogTopa);
        figura[1][3].setIcon(slikaCrnogTopa);
        figura[1][4].setIcon(slikaCrnogLovca);
        figura[1][5].setIcon(slikaCrnogLovca);
        figura[1][6].setIcon(slikaCrnogKonja);
        figura[1][7].setIcon(slikaCrnogKonja);

        for (int i = 8; i < 16; i++) {
            figura[0][i].setIcon(slikaBelogPijuna);
        }

        for (int i = 8; i < 16; i++) {
            figura[1][i].setIcon(slikaCrnogPijuna);
        }
    }

    private void addBoardBackgroundAndButtonsToTheLayeredPane(JLayeredPane layeredPane){
        for (int rank = 1; rank < 9; rank++) {
            for (int file = 1; file < 9; file++) {
                poljePozadina[rank][file] = new JLabel();
                polje[rank][file] = new Polje();

                if ((rank + file) % 2 == 0) {
                    poljePozadina[rank][file].setBackground(new Color(50, 100, 50));
                } else {
                    poljePozadina[rank][file].setBackground(Color.WHITE);
                }
                poljePozadina[rank][file].setOpaque(true);

                if(this.instanciranjeFrejma.whitesPerspective) {
                    poljePozadina[rank][file].setBounds(duzinaPolja * (file - 1), duzinaTable - duzinaPolja * rank, duzinaPolja, duzinaPolja);
                }else {
                    poljePozadina[rank][file].setBounds(duzinaTable - file * duzinaPolja, (rank - 1) * duzinaPolja, duzinaPolja, duzinaPolja);
                }
                layeredPane.add(poljePozadina[rank][file], Integer.valueOf(0));

                polje[rank][file].setOpaque(false);
                polje[rank][file].setContentAreaFilled(false);
                polje[rank][file].setDuzina(this);
                //polje[rank][file].setBounds(duzinaPolja * (file - 1), duzinaTable - duzinaPolja * rank, duzinaPolja, duzinaPolja);
                polje[rank][file].setRankFile(rank, file);
                polje[rank][file].setPozicija(this);
                polje[rank][file].addActionListener(this); // BITAN KORAK! OVIM KORAKOM GOVORIM KOMPJUTERU DA NESTO TREBA DA SE DESI KAD KLIKNEM NA POLJE
                layeredPane.add(polje[rank][file], Integer.valueOf(2));
            }
        }
    }
    
    private void addPromButtonsToTheLayeredPane(JLayeredPane layeredPane){
        dugmiciPromocije[0].setText("kraljica");
        dugmiciPromocije[1].setText("top");
        dugmiciPromocije[2].setText("lovac");
        dugmiciPromocije[3].setText("konj");

        for(int i=0; i<4; i++){
            dugmiciPromocije[i].setBounds(2*i*duzinaPolja, duzinaPolja*3, 2*duzinaPolja,duzinaPolja);
            dugmiciPromocije[i].addActionListener(this);
            dugmiciPromocije[i].setVisible(false);
            layeredPane.add(dugmiciPromocije[i], Integer.valueOf(3));
        }
    }

 
    private void initMoveSender(MoveSender moveSender){
        myOpponent = instanciranjeFrejma.opponent;
        myOpponentIp = instanciranjeFrejma.opponentIp;
        myOpponentPort = instanciranjeFrejma.opponentPort;
        thisProgramPort = instanciranjeFrejma.myPort;

        if(myOpponent != Opponents.HUMAN_ON_THIS_PC){
            moveSender.setInfo(myOpponentIp, myOpponentPort, "localhost", thisProgramPort, this);
            moveSender.activatePort();

// Ako igram crnog, onda moram da dobijem prvi potez od igraca sa drugog kompjutera.
            if(moveSender.opponentsColor == 0){ //Ako je protivnik beli onda primam prvi potez od njega i to u novom thread-u
                Thread threadFirstMove = new Thread(moveSender::receiveMove);
                threadFirstMove.start();
            }
        }
    }
    
    
    private void addCosmeticsToTheLayeredPane(JLayeredPane layeredPane){
        labelCijiPotez = new JLabel();
        labelCijiPotez.setBounds(duzinaTable+10, 100, 300,25);
        labelCijiPotez.setVisible(true);
        labelCijiPotez.setText("Na potezu je: beli");
        layeredPane.add(labelCijiPotez);

        labelBrojPoteza = new JLabel();
        labelBrojPoteza.setBounds(duzinaTable+10, 150, 300,25);
        labelBrojPoteza.setVisible(true);
        labelBrojPoteza.setText("Broj odigranih poteza: 0");
        layeredPane.add(labelBrojPoteza);

        labelPrikaziSahMat = new JLabel();
        labelPrikaziSahMat.setBounds(duzinaTable+10, 200, 300,25);
        labelPrikaziSahMat.setVisible(true);
        labelPrikaziSahMat.setText("");
        layeredPane.add(labelPrikaziSahMat);

        labelPrikaziSahMat2 = new JLabel();
        labelPrikaziSahMat2.setBounds(duzinaTable+10, 225, 300,25);
        labelPrikaziSahMat2.setVisible(true);
        labelPrikaziSahMat2.setText("");
        layeredPane.add(labelPrikaziSahMat2);

        // ovaj label omogucava igracima da vide da li su u sahu ili ne
        labelObavestavamOsahu = new JLabel();
        labelObavestavamOsahu.setBounds(duzinaTable+10, 250, 300,25);
        labelObavestavamOsahu.setVisible(false);
        labelObavestavamOsahu.setText("Niste u sahu.");
        if(instanciranjeFrejma.da_li_da_obavestim_da_je_igrac_u_sahu){
            labelObavestavamOsahu.setVisible(true);
        }
        layeredPane.add(labelObavestavamOsahu);

        // ovo dugme omogucava igracima da kontrolisu da li ce im se prikazivati da li su u sahu ili ne
        dugmeZaSah = new JButton("Kliknite da se prikaze kada ste u sahu");
        dugmeZaSah.setBounds(duzinaPolja, duzinaTable + 20, 400, 25);
        dugmeZaSah.addActionListener(this); // JAKO BITAN KORAK! BEZ OVOG KORAKA DUGME NECE RADITI NISTA
        dugmeZaSah.setVisible(true);
        layeredPane.add(dugmeZaSah);

    }
    
    public boolean nalaziSeFigura(int rank, int file) {
        boolean izlaz = false;

        int pozicija = rank * 10 + file;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                if (this.figura[i][j].getPozicija() == pozicija) {
                    izlaz = true;
                }
            }
        }
        return izlaz;
    }

    public boolean nalaziSeFigura(int pozicija) {
        boolean izlaz = false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                if (this.figura[i][j].getPozicija() == pozicija) {
                    izlaz = true;
                }
            }
        }
        return izlaz;
    }

    public boolean nalaziSeFiguraBoje(int rank, int file, int upisi0zaBele1zaCrneFigure) {
        boolean izlaz = false;

        int pozicija = rank * 10 + file;

            for (int j = 0; j < 16; j++) {
                if (this.figura[upisi0zaBele1zaCrneFigure][j].getPozicija() == pozicija) {
                    izlaz = true;
                }
            }

        return izlaz;
    }

    public boolean jedemSopstvenuFiguru(int rank, int file, int bojaFigura){
        return nalaziSeFiguraBoje(rank, file, bojaFigura);
    }


    public byte indeksIgraceveFigureNaPolju(int rank, int file, byte bojaIgraca){
        byte izlaz = -1;

        for(byte i=0; i<BROJ_FIGURA_JEDNE_BOJE; i++) {
                if (figura[bojaIgraca][i].getPozicija() == (10*rank + file)){
                    izlaz = i;
                }
        }

        return izlaz;
    }

    public boolean nalaziSeFiguraBoje(int pozicija, int upisi0zaBele1zaCrneFigure) {
        boolean izlaz = false;

        for (int j = 0; j < 16; j++) {
            if (this.figura[upisi0zaBele1zaCrneFigure][j].getPozicija() == pozicija) {
                izlaz = true;
            }
        }

        return izlaz;
    }

    // set metode
    public void setKoJeNaPotezu(int pisi0zaBelog1zaCrnog) {
        koJeNaPotezu = (byte) pisi0zaBelog1zaCrnog;
    }

    public void setPozicijaPijunaKojiSePomerio2Polja(int pozicijaPijunaKojiSePomerio2Polja){
        this.pozicijaPijunaKojiSePomerio2Polja = (byte)pozicijaPijunaKojiSePomerio2Polja;
    }

    public void setFilePijunaKojiSePomerio2Polja(int filePijunaKojiSePomerio2Polja){
        this.filePijunaKojiSePomerio2Polja = (byte)filePijunaKojiSePomerio2Polja;
    }


    // get metode
    public  byte getKoJeNaPotezu() {
        return koJeNaPotezu;
    }


    public byte getPozicijaPijunaKojiSePomerio2Polja(){
        return pozicijaPijunaKojiSePomerio2Polja;
    }

    public byte getFilePijunaKojiSePomerio2Polja(){
        return filePijunaKojiSePomerio2Polja;
    }


}
