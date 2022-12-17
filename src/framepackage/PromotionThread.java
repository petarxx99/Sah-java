package src.framepackage;

import src.communication.MoveSender;
import src.communication.Promotion;

public class PromotionThread implements Runnable {
    final byte START_RANK, START_FILE, END_RANK, END_FILE;
    MoveSender moveSender;
    Promotion promotion;
    private boolean stopThread = false;


    private boolean promotionHasOccured = false;
    public void promotionHasOccured(Promotion promotion){
        promotionHasOccured = true;
        this.promotion = promotion;
    }


    public PromotionThread(int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion, MoveSender moveSender){
        START_RANK = (byte) startRank;
        START_FILE = (byte) startFile;
        END_RANK = (byte) destinationRank;
        END_FILE = (byte) destinationFile;
        this.promotion = promotion;
        this.moveSender = moveSender;

    }
    @Override
    public void run(){

        try{
            while(!promotionHasOccured){
                Thread.sleep(50);
                if(stopThread) return;
            }
            moveSender.getAndSendMove(START_RANK, START_FILE, END_RANK, END_FILE, promotion);
        } catch(Exception error){
            error.printStackTrace();
            error.getMessage();
        }
    }

    public void stopThread(){
       stopThread = true;
    }

}
