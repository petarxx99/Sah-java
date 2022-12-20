package src.framepackage;

import src.communication.movesender.MoveSenderMock;
import src.communication.movesender.MoveSenderToEngine;
import src.communication.movesender.MoveSenderWithPortAndIP;
import src.constants.ChessConstants;
import src.communication.movesender.MoveSender;
import src.communication.encoding.MoveEncoder;
import src.communication.encoding.MoveEncoder3bytes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;


public class InstanciranjeFrejma {

    private boolean colorQuestionAnswered = false, opponentQAnswered, pcQuestion;
    boolean askWhichPCAnswered = false;
    static JButton buttonWhite, buttonBlack, buttonDefaultOption;
    private ArrayList<ChoiceButton> opponentButtons = new ArrayList<>();

    JButton buttonUnesiSamOption;
    JTextField ipTextField;



    public static HashMap<String, String> opponentsHashMap = new HashMap<>() {
        {
            put("DEFAULT_PC", "111.111.111.1:5000");  
            put("MOJ_KOMPJUTER", "111.111.111.1:5000");  
           // put("CHESS_ENGINE", "localhost:5000");
        }
    };


    public InstanciranjeFrejma(int duzinaPolja){
        Byte opponentsColor = askWhiteOrBlackPieces();
        boolean whitesPerspective = opponentsColor == ChessConstants.BLACK;

        Opponents opponent = askAgainstWhomYouPlay();
        MoveSender moveSender = null;

        switch(opponent){
            case CHESS_ENGINE:{
                moveSender = new MoveSenderToEngine();
                break;
            }
            case PLAYER_ON_ANOTHER_PC:{
                WhichPcEnum whichPc = askWhichPC();
                if(whichPc == WhichPcEnum.CUSTOM_IP_AND_PORT){
                    IpAddressAndPortData ipPort = askForOpponentIPandPort();
                    MoveEncoder moveEncoder = new MoveEncoder3bytes();
                    moveSender = new MoveSenderWithPortAndIP(opponent, moveEncoder, opponentsColor, ipPort.opponentsIpAddress, ipPort.opponentsPort, "localhost", ipPort.myPort);
                }
                break;
            }
            default:{
                moveSender = new MoveSenderMock();
            }
        }

        MyFrame chessGame = new MyFrame(moveSender, opponent, whitesPerspective, opponentsColor, duzinaPolja);
        chessGame.startGame();
    }

   

    public Byte askWhiteOrBlackPieces(){

        JFrame framePerspective = new JFrame();
        framePerspective.setLayout(null);
        framePerspective.setBounds(0, 0, 500, 500);
        framePerspective.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel labelAsk = new JLabel("Do you want to play with white or black pieces?");
        labelAsk.setBounds(20, 100, 460, 20);
        labelAsk.setOpaque(true);

        JLabel labelPitanje = new JLabel("Da li zelite igrati kao crni ili kao beli?");
        labelPitanje.setBounds(20, 120, 460, 20);
        labelPitanje.setOpaque(true);

        InstanciranjeFrejma thisClass = this;
        OpponentsColor colorOfTheOpponent = new OpponentsColor();

        buttonWhite = new JButton("WHITE/BELI");
        buttonWhite.setBounds(20, 300, 200, 50);
        buttonWhite.addActionListener(actionEvent ->{
                        colorOfTheOpponent.colorOfTheOpponent = ChessConstants.BLACK_TO_MOVE;
                        colorQuestionAnswered = true;
        });

        buttonBlack = new JButton("BLACK/CRNI");
        buttonBlack.setBounds(280, 300, 200, 50);
        buttonBlack.addActionListener(actionEvent -> {
            colorOfTheOpponent.colorOfTheOpponent = ChessConstants.WHITE_TO_MOVE;
            colorQuestionAnswered = true;
        });
        
        framePerspective.add(buttonBlack);
        framePerspective.add(buttonWhite);
        framePerspective.add(labelAsk);
        framePerspective.add(labelPitanje);
        framePerspective.setVisible(true);

        try{
            while(true) {
                if (colorQuestionAnswered) {
                    framePerspective.dispose();
                    return colorOfTheOpponent.colorOfTheOpponent;
                }
                Thread.sleep(200);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }

    static class OpponentsColor{
        Byte colorOfTheOpponent;
    }
    private Opponents askAgainstWhomYouPlay(){
        int buttonHeight = 30;
        int buttonLength = 460;
        int distanceBetweenButtons = 20;
        int firstButtonY = 150;
        int buttonX = 20;


        JFrame frameOpponent = new JFrame();
        frameOpponent.setLayout(null);
        frameOpponent.setBounds(0, 0, 500, 500);
        frameOpponent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel labelAsk = new JLabel("Who do you want to play against?");
        labelAsk.setBounds(20, 100, 460, 20);
        labelAsk.setOpaque(true);

        JLabel labelPitanje = new JLabel("Protiv koga zelite da igrate?");
        labelPitanje.setBounds(20, 120, 460, 20);
        labelPitanje.setOpaque(true);

        ChoiceButton thisPC = new ChoiceButton("Igraca na ovom kompjuteru/player on this PC");
        thisPC.setOpponent(Opponents.HUMAN_ON_THIS_PC);
        ChoiceButton anotherPC = new ChoiceButton("Igraca na drugom kompjuteru/player on another PC");
        anotherPC.setOpponent(Opponents.PLAYER_ON_ANOTHER_PC);
        ChoiceButton chessEngine = new ChoiceButton("kompjutera/chess engine");
        chessEngine.setOpponent(Opponents.CHESS_ENGINE);

        opponentButtons.add(thisPC);
        opponentButtons.add(anotherPC);
        opponentButtons.add(chessEngine);

        InstanciranjeFrejma thisClass = this;

        ChosenButtonData buttonThatWasChosen = new ChosenButtonData();
        for(int i=0; i<opponentButtons.size(); i++){
            final ChoiceButton opponentButton = opponentButtons.get(i);
            opponentButton.setBounds(buttonX, firstButtonY * i + buttonHeight + distanceBetweenButtons, buttonLength, buttonHeight);
            opponentButton.addActionListener(e -> {
                buttonThatWasChosen.choiceButton = opponentButton;
                opponentQAnswered = true;
            });
            frameOpponent.add(opponentButton);
        }

        frameOpponent.add(labelPitanje);
        frameOpponent.add(labelAsk);
        frameOpponent.setVisible(true);

        try{
            while(true){
                if(opponentQAnswered){
                    frameOpponent.dispose();
                    return buttonThatWasChosen.choiceButton.getOpponent();
                }
                Thread.sleep(200);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }


    static class ChosenButtonData{
        public ChoiceButton choiceButton;
    }
    public WhichPcEnum askWhichPC(){

        JFrame frameWhichPC = new JFrame();
        frameWhichPC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWhichPC.setLayout(null);
        frameWhichPC.setBounds(0, 0, 500, 500);

        JLabel labelAsk = new JLabel("Choose the default PC, choose from the list, or manually write IP and port");
        labelAsk.setBounds(20, 20, 460, 30);

        JLabel labelPitanje = new JLabel("Izaberite podrazumevani PC, PC sa liste, ili sami upisite IP i port.");
        labelPitanje.setBounds(20, 50, 460, 30);

        InstanciranjeFrejma thisClass = this;
        WhereOpponentPlaysData whereOpponentPlays = new WhereOpponentPlaysData();

        buttonDefaultOption = new JButton("Default PC/podrazumevani PC");
        buttonDefaultOption.setBounds(20, 100, 460, 30);
        buttonDefaultOption.addActionListener(e -> {
            whereOpponentPlays.whichPc = WhichPcEnum.DEFAULT_PC;
            pcQuestion = true;
        });

        thisClass.buttonUnesiSamOption = new JButton("Unesi sam IP adresu. Enter your own IP address");
        thisClass.buttonUnesiSamOption.setBounds(20, 150, 460, 30);
        thisClass.buttonUnesiSamOption.addActionListener((ActionEvent event)->{
            whereOpponentPlays.whichPc = WhichPcEnum.CUSTOM_IP_AND_PORT;
            pcQuestion = true;
        });


        frameWhichPC.add(buttonDefaultOption);
        frameWhichPC.add(labelAsk);
        frameWhichPC.add(labelPitanje);
        frameWhichPC.add(thisClass.buttonUnesiSamOption);
        frameWhichPC.setVisible(true);

        pcQuestion = false;
        try{
            while(!pcQuestion){  // OVAJ DEO ZAUSTAVLJA PROGRAM DOK SE NE IZABERE ODKLE IGRA PROTIVNIK!!!!
                Thread.sleep(200);
            }
            frameWhichPC.dispose();
            return whereOpponentPlays.whichPc;
        } catch(InterruptedException e){
            e.printStackTrace();
            return null;
        }


    }

    static class WhereOpponentPlaysData{
        WhichPcEnum whichPc;
    }

    static enum WhichPcEnum{
        DEFAULT_PC, CUSTOM_IP_AND_PORT;
    }


    private IpAddressAndPortData askForOpponentIPandPort(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        JLabel labelObjasnjenja = new JLabel("Ispod unesite IP adresu protivnika. Ona verovatno pocinje sa 192.168.100. ako je na lokalnoj mrezi.");
        this.ipTextField = new JTextField("Enter opponent's IP address here."); // new JTextField(50);
        //  thisObject.ipTextField.setPreferredSize(new Dimension(300, 30));
        labelObjasnjenja.setLabelFor(ipTextField);

        JLabel labelOpponentPort = new JLabel("Ovde upisite protivnikov port. Write opponents' port here.");
        JTextField textFieldOpponentPort = new JTextField("Upisite broj izmedju 2000 i 6000 (savetujem da unesete 5000). Write a number between 2000 and 6000 (for instance 5000)");
        labelOpponentPort.setLabelFor(textFieldOpponentPort);

        JLabel labelMojPort = new JLabel("Ovde upisite sopstveni port. Choose your own port here.");
        JTextField textFieldMojPort = new JTextField("Upisite broj izmedju 2000 i 6000 (savetujem da unesete 5000). Write a number between 2000 and 6000 (for instance 5000).");
        labelMojPort.setLabelFor(textFieldMojPort);

        InstanciranjeFrejma thisObject = this;
        JButton buttonPotvrdi = new JButton("Porvrdi IP adresu/confirm IP");
        IpAddressAndPortData dataToReturn = new IpAddressAndPortData();
        buttonPotvrdi.addActionListener((ActionEvent event)-> ipAddressAndPortWereEntered(
                frame,
                ipTextField,
                textFieldOpponentPort,
                textFieldMojPort,
                dataToReturn));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1; gbc.gridy = 1;
        frame.getContentPane().add(labelObjasnjenja, gbc);

        gbc.gridy = 2;
        frame.getContentPane().add(this.ipTextField, gbc);

        gbc.gridy = 3;
        frame.getContentPane().add(labelOpponentPort, gbc);

        gbc.gridy = 4;
        frame.getContentPane().add(textFieldOpponentPort, gbc);

        gbc.gridy = 5;
        frame.getContentPane().add(labelMojPort, gbc);

        gbc.gridy = 6;
        frame.getContentPane().add(textFieldMojPort, gbc);

        gbc.gridy = 7;
        frame.getContentPane().add(buttonPotvrdi, gbc);
        frame.setVisible(true);

        try{
            while(!askWhichPCAnswered){
                Thread.sleep(200);
            }
        } catch(InterruptedException exception){
            exception.printStackTrace();
        }

        frame.dispose();
        return dataToReturn;
    }

    private void ipAddressAndPortWereEntered(
            JFrame frameThatAskedQuestion,
            JTextField opponentIPtextField,
            JTextField textFieldOpponentPort,
            JTextField textFieldMojPort,
            IpAddressAndPortData dataToReturn){
        frameThatAskedQuestion.setEnabled(false);

        String unetaIpAdresa = opponentIPtextField.getText();
        String unetProtivnikovPort = textFieldOpponentPort.getText();
        String unetMojPort = textFieldMojPort.getText();

        if (unetaIpAdresa.isEmpty()){
            JOptionPane.showMessageDialog(null, "Molim vas unesite IP adresu.");
            frameThatAskedQuestion.setEnabled(true);
            return;
        }
        final String OPPONENTS_IP = unetaIpAdresa;

        try{
            int opponentPortTry = Integer.parseInt(unetProtivnikovPort);
            int mojPortTry = Integer.parseInt(unetMojPort);

            if(opponentPortTry>2000 && opponentPortTry < 6000) opponentPortTry = 5000;
            if (mojPortTry>2000 && mojPortTry < 6000) mojPortTry = 5000;

            dataToReturn.init(mojPortTry, opponentPortTry, OPPONENTS_IP);
            JOptionPane.showMessageDialog(null, "opponent IP = " + OPPONENTS_IP + ", opponent port = " + opponentPortTry + ", my port = " + mojPortTry);
        } catch(NumberFormatException exception){
            JOptionPane.showMessageDialog(null, "Unesite brojeve za vas port i protivnikov port");
            frameThatAskedQuestion.setEnabled(true);
            return;
        }

        askWhichPCAnswered = true;  // OVO OMOGUCAVA NASTAVAK PROGRAMA
    }

    static class IpAddressAndPortData{
        public Integer myPort;
        public Integer opponentsPort;
        public String opponentsIpAddress;

        public void init(int myPort, int opponentsPort, String opponentsIpAddress){
            this.myPort = myPort;
            this.opponentsPort = opponentsPort;
            this.opponentsIpAddress = opponentsIpAddress;
        }
    }

}


