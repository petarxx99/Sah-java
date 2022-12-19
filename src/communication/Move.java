package src.communication;

public class Move {

    public final byte START_RANK;
    public final byte START_FILE;
    public final byte END_RANK;
    public final byte END_FILE;
    public final Promotion PROMOTION;

    public Move(int startRank, int startFile, int endRank, int endFile, Promotion promotion) throws Exception {
        if(isItLessThan8BiggerThan1(startRank)){
            START_RANK = (byte) startRank;
        } else {
            throw new WrongRankFileException();
        }

        if(isItLessThan8BiggerThan1(endRank)){
            END_RANK = (byte) endRank;
        } else {
            throw new WrongRankFileException();
        }

        if(isItLessThan8BiggerThan1(startFile)){
            START_FILE = (byte) startFile;
        } else {
            throw new WrongRankFileException();
        }

        if(isItLessThan8BiggerThan1(endFile)){
            END_FILE = (byte) endFile;
        } else {
            throw new WrongRankFileException();
        }

        if(promotion != null){
            this.PROMOTION = promotion;
        } else{
            throw new Exception("Promotion is null.");
        }

    }

    private boolean isItLessThan8BiggerThan1(int number){
        return number>=1 && number<=8;
    }

    private static byte LETTER_a = 97;
    private static char fileAsLetter(byte rank){
        return (char) (rank + LETTER_a-1);
    }
    @Override
    public String toString(){
        return "start rank: " + START_RANK +"\n"+
                "start file: " + fileAsLetter(START_FILE) + "\n"+
                "end rank: " + END_RANK + "\n"+
                "end file: " + fileAsLetter(END_FILE);
    }
}
