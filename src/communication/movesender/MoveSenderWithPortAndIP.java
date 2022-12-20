package src.communication.movesender;

import src.communication.Move;
import src.communication.Promotion;
import src.communication.ReceiverOfChessMoves;
import src.communication.encoding.MoveEncoder;
import src.framepackage.*;
import java.net.*;
import java.io.*;

public class MoveSenderWithPortAndIP implements MoveSender{
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

    @Override
    public void waitForPromotionAndThenSendMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion){
        if(promotionThread != null) {
            promotionThread.stopThread();
        }

        promotionThread = new PromotionThread(receiverOfMoves, startRank, startFile, destinationRank, destinationFile, promotion, this);
        Thread threadPromotion = new Thread(promotionThread);
        threadPromotion.start();
    }

    @Override
    public void promotionHasOccured(Promotion promotion){
        promotionThread.promotionHasOccured(promotion);
    }

    @Override
    public void sendAndReceiveMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int endRank, int endFile, Promotion promotion) throws Exception {
        if(promotion == null){
            throw new Exception("Promotion is null.");
        }

        Move moveToSend = new Move(startRank, startFile, endRank, endFile, promotion);
        byte[] encodedMoveToSend = moveEncoder.encodeMove(moveToSend);

        sendMove(encodedMoveToSend, opponentIpAddress, opponentPort);
        receiverOfMoves.moveIsSentToOpponent(moveToSend);
        System.out.printf("The move that was sent: %s \n", moveToSend.toString());
        receiveMove(receiverOfMoves);
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


    @Override
    public void receiveMove(ReceiverOfChessMoves receiverOfMoves){
        activatePort();

        try {
            System.out.println("Waiting for opponent's move.");
            Socket socketReceive = serverSocket.accept();
            System.out.println("Player has sent a move.");

            byte[] opponentMoveBuffer = moveEncoder.createBufferForTheMove();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socketReceive.getInputStream());
            bufferedInputStream.read(opponentMoveBuffer);

            bufferedInputStream.close();
            socketReceive.close();

            Move opponentsMove = moveEncoder.decodeMove(opponentMoveBuffer);
            System.out.printf("Received move: %s \n", opponentsMove.toString());
            receiverOfMoves.receiveOpponentsMove(opponentsMove);

        } catch(IOException e){
            e.printStackTrace();
        }
    }


    private void sendMove(byte[] move, String ipAddress, int port){
        try{
            System.out.println("Pre socketSend");
            Socket socketSend = new Socket(ipAddress, port);
            System.out.println("Posle socketSend");

            BufferedOutputStream bos = new BufferedOutputStream(socketSend.getOutputStream());
            bos.write(move);
            bos.close();
           // socketSend.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public MoveSenderWithPortAndIP(Opponents opponent, MoveEncoder moveEncoder, byte opponentsColor,String opponentIpAddress, int opponentPort, String myIpAddress, int myPort){
        this.opponent = opponent;
        this.moveEncoder = moveEncoder;
        this.opponentsColor = opponentsColor;
        this.opponentIpAddress = opponentIpAddress;
        this.myPort = myPort;
        this.opponentPort = opponentPort;
        this.myIpAddress = myIpAddress;
    }


}
