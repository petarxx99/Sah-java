package src.communication.encoding;

import src.communication.Move;
import src.communication.Promotion;

public class MoveEncoder3bytes implements MoveEncoder{
    public static final byte NO_PROMOTION = -1, PROMOTE_QUEEN = 0, PROMOTE_ROOK = 1, PROMOTE_BISHOP = 2, PROMOTE_KNIGHT = 3;
    public static final byte ERROR = Byte.MIN_VALUE;

    @Override
    public byte[] createBufferForTheMove(){
        return new byte[3];
    }
    @Override
    public Move decodeMove(byte[] encodedMove){
        int startRank = encodedMove[0] / 10;
        int startFile = encodedMove[0] % 10;
        int endRank = encodedMove[1] / 10;
        int endFile = encodedMove[1] % 10;
        Promotion promotion = decodePromotion(encodedMove[2]);

        try {
            return new Move(startRank, startFile, endRank, endFile, promotion);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    @Override
    public byte[] encodeMove(Move aMove){
        byte[] move = new byte[3];
        move[0] = (byte) (aMove.START_RANK * 10 + aMove.START_FILE);
        move[1] = (byte) (aMove.END_RANK * 10 + aMove.END_FILE);
        move[2] = encodePromotion(aMove.PROMOTION);
        return move;
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

}
