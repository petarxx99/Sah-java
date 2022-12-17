package src.framepackage;

import src.communication.MoveSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class InstanciranjeFrejma implements ActionListener{
    public final static byte DEFAULT_PC = 0, PC_FROM_THE_LIST = 1, CUSTOM_IP_AND_PORT = 2;
    public byte whichPC = DEFAULT_PC;

    public int duzinaPolja;

    public boolean da_li_da_obavestim_da_je_igrac_u_sahu = false;
    private boolean questionAnswered = false, opponentQAnswered, pcQuestion;
    boolean askWhichPCAnswered = false;
    static JButton buttonWhite, buttonBlack, buttonDefaultOption;
    private ArrayList<ChoiceButton> opponentButtons = new ArrayList<>();

    JButton buttonUnesiSamOption;
    JTextField ipTextField;

    public byte opponentsColor;
    public Opponents opponent;
    public boolean whitesPerspective;
    public String opponentIp;
    public int opponentPort = 5000;
    public int myPort = 5000;

    public static HashMap<String, String> opponentsHashMap = new HashMap<>() {
        {
            put("DEFAULT_PC", "111.111.111.1:5000");  
            put("MOJ_KOMPJUTER", "111.111.111.1:5000");  
           // put("CHESS_ENGINE", "localhost:5000");
        }
    };


    public static void instancirajFrejm(int duzinaPolja){

        InstanciranjeFrejma thisClass = new InstanciranjeFrejma();
        thisClass.duzinaPolja = duzinaPolja;

        thisClass.askWhiteOrBlackPieces();

        thisClass.askAgainstWhomYouPlay();

        switch (thisClass.opponent) {
            case CHESS_ENGINE: {
                thisClass.opponentPort = 5003;
                break;
            }
            case PLAYER_ON_ANOTHER_PC: {
                thisClass.askWhichPC();
                if (thisClass.whichPC == CUSTOM_IP_AND_PORT){
                    thisClass.askForOpponentIP();
                }
                break;
            }
        }

        MoveSender moveSender = new MoveSender(thisClass.opponent);
        moveSender.opponentsColor = thisClass.opponentsColor;

        MyFrame frejm1 = new MyFrame(thisClass, moveSender);
    }


    public void askWhiteOrBlackPieces(){

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

        buttonWhite = new JButton("WHITE/BELI");
        buttonWhite.setBounds(20, 300, 200, 50);
        buttonWhite.addActionListener(thisClass);

        buttonBlack = new JButton("BLACK/CRNI");
        buttonBlack.setBounds(280, 300, 200, 50);
        buttonBlack.addActionListener(thisClass);

        framePerspective.add(buttonBlack);
        framePerspective.add(buttonWhite);
        framePerspective.add(labelAsk);
        framePerspective.add(labelPitanje);
        framePerspective.setVisible(true);

        try{
            while(true) {
                if (questionAnswered) {
                    framePerspective.dispose();
                    break;
                }
                Thread.sleep(200);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void askAgainstWhomYouPlay(){
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

        for(int i=0; i<opponentButtons.size(); i++){
            opponentButtons.get(i).setBounds(buttonX, firstButtonY * i + buttonHeight + distanceBetweenButtons, buttonLength, buttonHeight);
            opponentButtons.get(i).addActionListener(thisClass);
            frameOpponent.add(opponentButtons.get(i));
        }

        frameOpponent.add(labelPitanje);
        frameOpponent.add(labelAsk);
        frameOpponent.setVisible(true);

        try{
            while(true){
                if(opponentQAnswered){
                    frameOpponent.dispose();
                    break;
                }
                Thread.sleep(200);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }


    public void askWhichPC(){

        JFrame frameWhichPC = new JFrame();
        frameWhichPC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWhichPC.setLayout(null);
        frameWhichPC.setBounds(0, 0, 500, 500);

        JLabel labelAsk = new JLabel("Choose the default PC, choose from the list, or manually write IP and port");
        labelAsk.setBounds(20, 20, 460, 30);

        JLabel labelPitanje = new JLabel("Izaberite podrazumevani PC, PC sa liste, ili sami upisite IP i port.");
        labelPitanje.setBounds(20, 50, 460, 30);

        InstanciranjeFrejma thisClass = this;

        buttonDefaultOption = new JButton("Default PC/podrazumevani PC");
        buttonDefaultOption.setBounds(20, 100, 460, 30);
        buttonDefaultOption.addActionListener(thisClass);

        thisClass.buttonUnesiSamOption = new JButton("Unesi sam IP adresu. Enter your own IP address");
        thisClass.buttonUnesiSamOption.setBounds(20, 150, 460, 30);
        thisClass.buttonUnesiSamOption.addActionListener((ActionEvent event)->{
            whichPC = CUSTOM_IP_AND_PORT;
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
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        frameWhichPC.dispose();
    }


    public void askForOpponentIP(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        JLabel labelObjasnjenja = new JLabel("Ispod unesite IP adresu protivnika. Ona verovatno pocinje sa 192.168.100. ako je na lokalnoj mrezi.");


        this.ipTextField = new JTextField("Enter opponent's IP address here."); // new JTextField(50);
        //  thisObject.ipTextField.setPreferredSize(new Dimension(300, 30));

        JLabel labelOpponentPort = new JLabel("Ovde upisite protivnikov port. Write opponents' port here.");
        JTextField textFieldOpponentPort = new JTextField("Upisite broj izmedju 2000 i 6000 (savetujem da unesete 5000). Write a number between 2000 and 6000 (for instance 5000)");

        JLabel labelMojPort = new JLabel("Ovde upisite sopstveni port. Choose your own port here.");
        JTextField textFieldMojPort = new JTextField("Upisite broj izmedju 2000 i 6000 (savetujem da unesete 5000). Write a number between 2000 and 6000 (for instance 5000).");

        InstanciranjeFrejma thisObject = this;
        JButton buttonPotvrdi = new JButton("Porvrdi IP adresu/confirm IP");
        buttonPotvrdi.addActionListener((ActionEvent event)-> {
            String unetaIpAdresa = this.ipTextField.getText();
            String unetProtivnikovPort = textFieldOpponentPort.getText();
            String unetMojPort = textFieldMojPort.getText();

            if (unetaIpAdresa.isEmpty()){
                JOptionPane.showMessageDialog(null, "Molim vas unesite IP adresu.");
                return;
            }
            thisObject.opponentIp = unetaIpAdresa;

            try{
                int opponentPortTry = Integer.parseInt(unetProtivnikovPort);
                int mojPortTry = Integer.parseInt(unetMojPort);

                if(opponentPortTry>2000 && opponentPortTry < 6000) thisObject.opponentPort = 5000;
                if (mojPortTry>2000 && mojPortTry < 6000) thisObject.myPort = 5000;

                JOptionPane.showMessageDialog(null, "opponent IP = " + thisObject.opponentIp + ", opponent port = " + thisObject.opponentPort + ", my port = " + thisObject.myPort);
            } catch(NumberFormatException exception){
                JOptionPane.showMessageDialog(null, "Unesite brojeve za vas port i protivnikov port");
            }

            askWhichPCAnswered = true;  // OVO OMOGUCAVA NASTAVAK PROGRAMA
        });

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
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == buttonBlack){
            whitesPerspective = false;
            opponentsColor = MyFrame.WHITE_PIECES;
            //MoveSender.opponentsColor = 0;  // 0 = beli
            questionAnswered = true;
        } else if (e.getSource() == buttonWhite){
            whitesPerspective = true;
            opponentsColor = MyFrame.BLACK_PIECES;
            //MoveSender.opponentsColor = 1; // 1 = crni
            questionAnswered = true;
        }

        for(int i=0; i<opponentButtons.size(); i++){
            if(e.getSource() == opponentButtons.get(i)){
                opponent = opponentButtons.get(i).getOpponent();
                System.out.println("Opponent is: " + opponent);
                opponentQAnswered = true;
            }
        }

        if(e.getSource() == buttonDefaultOption){
            whichPC = DEFAULT_PC;
           // opponentIp = opponentsHashMap.get("DEFAULT_PC").split(":")[0];
            opponentIp = "";
            opponentPort = 5000;
          //  opponentPort = Integer.parseInt(opponentsHashMap.get("DEFAULT_PC").split(":")[1]);
            JOptionPane.showMessageDialog(null, "Podesite default ip adresu u kodu. \n");
            System.exit(0);
            pcQuestion = true;
        }


    }




}


