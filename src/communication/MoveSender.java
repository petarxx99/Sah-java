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

    PromotionThread promotionThread;

    public static final byte NO_PROMOTION = -1, PROMOTE_QUEEN = 0, PROMOTE_ROOK = 1, PROMOTE_BISHOP = 2, PROMOTE_KNIGHT = 3;
    public static final byte ERROR = Byte.MIN_VALUE;

   /* I chose this design because this way new promotionThread is created for every new promotion move.
   * This way the variable that holds the information about whether promotion has occurred or not is in
   * an object of a class whose new instance gets instantiated every time new promotion move happens.
   * The alternative is to have that variable in this class and reset it after each promotion.
   *  I think the solution that I chose is less messy, there is no reseting variables
   * other than writting promotionThread = new PromotionThread
   * PromotionThread also holds info about the promotion move, such as start rank, end rank etc.*/
    public void waitForPromotionAndThenSendMove(int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion){
        if(promotionThread != null) {
            promotionThread.stopThread();
        }

        promotionThread = new PromotionThread(startRank, startFile, destinationRank, destinationFile, promotion, this);
        Thread threadPromotion = new Thread(promotionThread);
        threadPromotion.start();
    }


    public void promotionHasOccured(Promotion promotion){
        promotionThread.promotionHasOccured(promotion);
    }

    private static byte encodePromotion(Promotion promotion){
        switch (promotion){
            case NO_PROMOTION: return NO_PROMOTION;
            case PROMOTE_QUEEN: return PROMOTE_QUEEN;
            case PROMOTE_ROOK:  return PROMOTE_ROOK;
            case PROMOTE_BISHOP: return PROMOTE_BISHOP;
            case PROMOTE_KNIGHT: return PROMOTE_KNIGHT;
            default: return NO_PROMOTION;
        }
    }
    private static Promotion decodePromotion(byte promotionByte){
        switch(promotionByte){
            case NO_PROMOTION: return Promotion.NO_PROMOTION;
            case PROMOTE_QUEEN: return Promotion.PROMOTE_QUEEN;
            case PROMOTE_ROOK: return Promotion.PROMOTE_ROOK;
            case PROMOTE_BISHOP: return Promotion.PROMOTE_BISHOP;
            case PROMOTE_KNIGHT: return Promotion.PROMOTE_KNIGHT;
            default: return Promotion.NO_PROMOTION;
        }
    }

    public void getAndSendMove(int startRank, int startFile, int endRank, int endFile, Promotion promotion) throws Exception {
        if(promotion == null){
            throw new Exception("Promotion is null.");
        }

        byte[] move = new byte[3];
        move[0] = (byte) (startRank * 10 + startFile);
        move[1] = (byte) (endRank * 10 + endFile);
        move[2] = encodePromotion(promotion);

        switch (opponent){
            case PLAYER_ON_ANOTHER_PC: {
                sendMove(move, opponentIpAddress, opponentPort);
                boardFrame.moveIsSentToOpponent();
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

            int startRank = opponentMove[0] / 10;
            int startFile = opponentMove[0] % 10;
            int endRank = opponentMove[1] / 10;
            int endFile = opponentMove[1] % 10;
            Promotion promotion = decodePromotion(opponentMove[2]);

            try{
                Move aMove = new Move(startRank, startFile, endRank, endFile, promotion);
                boardFrame.receiveOpponentsMove(aMove);
            }catch (Exception e){
                e.getMessage();
                e.printStackTrace();
            }
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
