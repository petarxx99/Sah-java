package paketpolje;

import javax.swing.*;
import framepackage.*;

public class Polje extends JButton {

    // karakteristike same table
    public int duzinaPolja;
    public  int duzinaTable;
    public boolean whitesPerspective;


    public void setDuzina(MyFrame boardFrame){
        this.duzinaPolja = boardFrame.getSquareLength();
        this.duzinaTable = 8 * duzinaPolja;
    }
    // -------------------------------------------------------------------------------

    private void initDimensions(int squareLength){
        this.duzinaPolja = squareLength;
        this.duzinaTable = 8 * squareLength;
    }
    byte rank;
    byte file;

    // set metode

    public void setPozicija(MyFrame boardFrame) {
        initDimensions(boardFrame.getSquareLength());

        if(boardFrame.isWhitesPerspective()) {
            this.setBounds(duzinaPolja * (file - 1), duzinaTable - duzinaPolja * rank, duzinaPolja, duzinaPolja);
        }else {
            this.setBounds(duzinaTable - duzinaPolja * file, duzinaPolja * (rank - 1), duzinaPolja, duzinaPolja);
        }
    }

    public void setRankFile(int rank, int file) {
        setRank(rank);
        setFile(file);
    }

    public void setRank(int rank) {
        if (rank < 1) {
            this.rank = 1;
        } else if (rank > 8) {
            this.rank = 8;
        } else {
            this.rank = (byte) rank;
        }
    }

    public void setFile(int file) {
        if (file < 1) {
            this.file = 1;
        } else if (file > 8) {
            this.file = 8;
        } else {
            this.file = (byte) file;
        }
    }


    // get metode
    public byte getRank() {
        return rank;
    }

    public byte getFile() {
        return file;
    }

    public int getPozicija() {
        return rank * 10 + file;
    }

}
