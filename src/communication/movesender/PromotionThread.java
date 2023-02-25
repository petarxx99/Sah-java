package communication.movesender;

import communication.movesender.MoveSender;
import communication.Promotion;
import communication.ReceiverOfChessMoves;

public class PromotionThread implements Runnable {
    final byte START_RANK, START_FILE, END_RANK, END_FILE;
    MoveSender moveSender;
    Promotion promotion;
    ReceiverOfChessMoves receiverOfMoves;
    private boolean stopThread = false;


    private boolean promotionHasOccured = false;
    public void promotionHasOccured(Promotion promotion){
        promotionHasOccured = true;
        this.promotion = promotion;
    }


    public PromotionThread(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion, MoveSender moveSender){
        START_RANK = (byte) startRank;
        START_FILE = (byte) startFile;
        END_RANK = (byte) destinationRank;
        END_FILE = (byte) destinationFile;
        this.promotion = promotion;
        this.receiverOfMoves = receiverOfMoves;
        this.moveSender = moveSender;

    }
    @Override
    public void run(){

        try{
            while(!promotionHasOccured){
                Thread.sleep(50);
                if(stopThread) return;
            }
            moveSender.sendAndReceiveMove(receiverOfMoves, START_RANK, START_FILE, END_RANK, END_FILE, promotion);
        } catch(Exception error){
            error.printStackTrace();
            error.getMessage();
        }
    }

    public void stopThread(){
       stopThread = true;
    }

}
