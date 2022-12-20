package src.communication.movesender;

import src.communication.Move;
import src.communication.Promotion;
import src.communication.ReceiverOfChessMoves;
import src.communication.encoding.MoveEncoder;
import src.framepackage.*;
import java.net.*;
import java.io.*;

public class MoveSender {
    public static final byte WHITE_COLOR = 0;
    public static final byte BLACK_COLOR = 1;
    private Opponents opponent;
    private MoveEncoder moveEncoder;
    private String opponentIpAddress, myIpAddress;
    private Integer opponentPort, myPort;
    private byte opponentsColor = BLACK_COLOR;
    private ServerSocket serverSocket;

    private PromotionThread promotionThread;


   /* I chose this design because this way new promotionThread is created for every new promotion move.
   * This way the variable that holds the information about whether promotion has occurred or not is in
   * an object of a class whose new instance gets instantiated every time new promotion move happens.
   * The alternative is to have that variable in this class and reset it after each promotion.
   *  I think the solution that I chose is less messy, there is no reseting variables
   * other than writting promotionThread = new PromotionThread
   * PromotionThread also holds info about the promotion move, such as start rank, end rank etc.*/
    public void waitForPromotionAndThenSendMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion){
        if(promotionThread != null) {
            promotionThread.stopThread();
        }

        promotionThread = new PromotionThread(receiverOfMoves, startRank, startFile, destinationRank, destinationFile, promotion, this);
        Thread threadPromotion = new Thread(promotionThread);
        threadPromotion.start();
    }

    public void promotionHasOccured(Promotion promotion){
        promotionThread.promotionHasOccured(promotion);
    }

    public void getAndSendMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int endRank, int endFile, Promotion promotion) throws Exception {
        if(promotion == null){
            throw new Exception("Promotion is null.");
        }

        Move moveToSend = new Move(startRank, startFile, endRank, endFile, promotion);
        byte[] encodedMoveToSend = moveEncoder.encodeMove(moveToSend);

        switch (opponent){
            case PLAYER_ON_ANOTHER_PC: {
                sendMove(encodedMoveToSend, opponentIpAddress, opponentPort);
                receiverOfMoves.moveIsSentToOpponent(moveToSend);
                System.out.printf("The move that was sent: %s \n", moveToSend.toString());
                receiveMove(receiverOfMoves);
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

    private void activatePort(){
        try{
            if(this.serverSocket == null) {
                this.serverSocket = new ServerSocket(myPort);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void init(){
        activatePort();
    }

    public void receiveMove(ReceiverOfChessMoves receiverOfMoves){
        byte[] opponentMoveBuffer = moveEncoder.createBufferForTheMove();
        try {

            System.out.println("Waiting for opponent's move.");
            Socket socketReceive = serverSocket.accept();
            System.out.println("Player has sent a move.");

            BufferedInputStream bis = new BufferedInputStream(socketReceive.getInputStream());
            bis.read(opponentMoveBuffer);

            bis.close();
            socketReceive.close();

            Move opponentsMove = moveEncoder.decodeMove(opponentMoveBuffer);
            System.out.printf("Received move: %s \n", opponentsMove.toString());
            receiverOfMoves.receiveOpponentsMove(opponentsMove);

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
            System.out.println("The move is sent.");

        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public MoveSender(){}
    public MoveSender(Opponents opponent){
        this.opponent = opponent;
    }

    public MoveSender(Opponents opponent, MoveEncoder moveEncoder, byte opponentsColor,String opponentIpAddress, int opponentPort, String myIpAddress, int myPort){
        this.opponent = opponent;
        this.moveEncoder = moveEncoder;
        this.opponentsColor = opponentsColor;
        this.opponentIpAddress = opponentIpAddress;
        this.myPort = myPort;
        this.opponentPort = opponentPort;
        this.myIpAddress = myIpAddress;
    }


}
