package src.communication.movesender;

import src.communication.Promotion;
import src.communication.ReceiverOfChessMoves;

public class MoveSenderMock implements MoveSender{

    @Override
    public void waitForPromotionAndThenSendMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion) {

    }

    @Override
    public void promotionHasOccured(Promotion promotion) {

    }

    @Override
    public void noLongerNeeded(ReceiverOfChessMoves receiverOfChessMoves){}
    @Override
    public void sendAndReceiveMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int endRank, int endFile, Promotion promotion) throws Exception {

    }

    @Override
    public void receiveMove(ReceiverOfChessMoves receiverOfMoves) {

    }
}
