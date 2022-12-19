package src.communication;

public interface ReceiverOfChessMoves {
    public void receiveOpponentsMove(Move aMove);
    public void moveIsSentToOpponent(Move aMove);
}
