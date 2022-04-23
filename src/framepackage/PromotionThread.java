package src.framepackage;

public class PromotionThread implements Runnable {
    byte[] moveInfo = new byte[5];
    MyFrame boardFrame;

    public PromotionThread(int startRank, int startFile, int destinationRank, int destinationFile, int promotionButtonNumber, MyFrame boardFrame){
        moveInfo[0] = (byte) startRank;
        moveInfo[1] = (byte) startFile;
        moveInfo[2] = (byte) destinationRank;
        moveInfo[3] = (byte) destinationFile;
        moveInfo[4] = (byte) promotionButtonNumber;
        this.boardFrame = boardFrame;

    }
    @Override
    public void run(){

        try{
            while(!boardFrame.promotionButtonClicked){
                Thread.sleep(50);
            }
            boardFrame.moveSender.getAndSendMove(moveInfo[0], moveInfo[1], moveInfo[2], moveInfo[3], boardFrame.promotionButtonNumber);
        } catch(InterruptedException error){
            error.printStackTrace();
        }
    }


}
