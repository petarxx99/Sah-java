package src.communication;

import src.paketfigure.*;
import src.framepackage.InstanciranjeFrejma.*;
import src.framepackage.MyFrame.*;
import src.framepackage.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class MoveSender {
    public MyFrame boardFrame;
    public Opponents opponent;
    public String opponentIpAddress, myIpAddress;
    public int opponentPort, myPort;
    public byte opponentsColor = 1;
    public ServerSocket serverSocket;

    public void getAndSendMove(int startRank, int startFile, int endRank, int endFile, int promotionButtonNumber){

        byte[] move = new byte[3];
        move[0] = (byte) (startRank * 10 + startFile);
        move[1] = (byte) (endRank * 10 + endFile);
        move[2] = (byte) promotionButtonNumber;

        switch (opponent){
            case PLAYER_ON_ANOTHER_PC: {
                sendMove(move, opponentIpAddress, opponentPort);
                boardFrame.labelCijiPotez.setText("Na potezu je protivnik.");
                receiveMove();
                break;
            }
            case CHESS_ENGINE: {
                System.out.println("Ovo cu napraviti u buducnosti");
                break;
            }
            case HUMAN_ON_THIS_PC: {

                break;
            }
            default: {

            }
        }

    }

    public void activatePort(){
        try{
            this.serverSocket = new ServerSocket(myPort);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setInfo(String opponentIpAddress, int opponentPort, String myIpAddress, int myPort, MyFrame boardFrame){
        this.opponentIpAddress = opponentIpAddress;
        this.opponentPort = opponentPort;
        this.myIpAddress = myIpAddress;
        this.myPort = myPort;
        this.boardFrame = boardFrame;
    }

    public void setOpponentIpAddress(String opponentIpAddress){
        this.opponentIpAddress = opponentIpAddress;
    }

    public void setOpponentPort(int opponentPort){
        this.opponentPort = opponentPort;
    }


    private void odigrajProtivnikovPotez(byte[] opponentMove){
            int startRank = opponentMove[0] / 10;
            int startFile = opponentMove[0] % 10;
            int endRank = opponentMove[1] / 10;
            int endFile = opponentMove[1] % 10;
            byte promotion = opponentMove[2];

            int whoseTurnItIs = boardFrame.getKoJeNaPotezu();
            int pieceIndex = findPieceIndex(startRank, startFile, whoseTurnItIs);
            
            if(promotion == -1){
                boardFrame.pokusajOdigratiPotez(endRank, endFile, pieceIndex);

            } else {
                (new Figure()).skloniFiguruSaTable(endRank, endFile, boardFrame);

                boardFrame.figura[whoseTurnItIs][pieceIndex].setPozicija(endFile, endRank, boardFrame);
                boardFrame.filePijunaKojiSePomerio2Polja = MyFrame.INVALID_FILE;

                switch (promotion){
                    case 0 : {
                        boardFrame.promoteQueen();
                        break;
                    }
                    case 1 : {
                        boardFrame.promoteRook();
                        break;
                    }
                    case 2 : {
                        boardFrame.promoteBishop();
                        break;
                    }
                    case 3 : {
                        boardFrame.promoteKnight();
                        break;
                    }
                    default : {

                    }
                }
            }

            boardFrame.labelObavestenja();
    }


    public void receiveMove(){
        byte[] opponentMove = new byte[3];
        try {

            System.out.println("Waiting for opponent's move.");
            Socket socketReceive = serverSocket.accept();
            System.out.println("Player has sent a move.");

            BufferedInputStream bis = new BufferedInputStream(socketReceive.getInputStream());
            bis.read(opponentMove);

            bis.close();
            socketReceive.close();
            System.out.printf("Received move: %d-%d-%d \n", opponentMove[0], opponentMove[1], opponentMove[2]);

            odigrajProtivnikovPotez(opponentMove);
            
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public void sendMove(byte[] move, String ipAddress, int port){
        //InetSocketAddress isa = new InetSocketAddress("192.138.100.3", 5003);
       // InetSocketAddress isa = new InetSocketAddress(opponentIpAddress, opponentPort);

        System.out.println("ipAddress: " + ipAddress);
        System.out.println("port: " + port);

        try{
            System.out.println("Pre socketSend");

            Socket socketSend = new Socket(ipAddress, port);
            System.out.println("Posle socketSend");
            BufferedOutputStream bos = new BufferedOutputStream(socketSend.getOutputStream());
            bos.write(move);
            bos.close();
           // socketSend.close();
            System.out.println("Slanje zavrseno.");
            System.out.printf("Sent move: %d-%d-%d \n", move[0], move[1], move[2]);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public int findPieceIndex(int rank, int file, int whoseTurnItIs){
        int position = rank * 10 + file;

        for(int i=0; i<16; i++){
            if(boardFrame.figura[whoseTurnItIs][i].getPozicija() == position){
                return i;
            }
        }
        return -1; // Nesto nije bilo kako treba.
    }

    public MoveSender(){}
    public MoveSender(Opponents opponent){
        this.opponent = opponent;
    }


}
