package src.communication.movesender;

import src.communication.Move;
import src.communication.Promotion;
import src.communication.ReceiverOfChessMoves;
import src.communication.encoding.MoveEncoder;
import src.framepackage.Opponents;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface MoveSender {
    public static final byte WHITE_COLOR = 0;
    public static final byte BLACK_COLOR = 1;

    public void waitForPromotionAndThenSendMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int destinationRank, int destinationFile, Promotion promotion);

    public void promotionHasOccured(Promotion promotion);

    public void sendAndReceiveMove(ReceiverOfChessMoves receiverOfMoves, int startRank, int startFile, int endRank, int endFile, Promotion promotion) throws Exception;


    public void receiveMove(ReceiverOfChessMoves receiverOfMoves);


}
