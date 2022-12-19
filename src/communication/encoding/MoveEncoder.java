package src.communication.encoding;

import src.communication.Move;

public interface MoveEncoder {

    public byte[] encodeMove(Move aMove);
    public Move decodeMove(byte[] encodedMove);
    public byte[] createBufferForTheMove();
}
