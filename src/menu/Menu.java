package menu;

import framepackage.ChessGame;
import framepackage.GameConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class Menu implements GameEndedListener {
    private GameConfiguration gameConfiguration;


    @Override
    public void gameHasEnded(ChessGame newChessGame){
        newWindow(500, gameConfiguration, newChessGame);
    }
    public Menu(GameConfiguration gameConfiguration, ChessGame chessGame){
        this.gameConfiguration = gameConfiguration;
        newWindow(500, gameConfiguration, chessGame);
    }

    public JFrame newWindow(int length, GameConfiguration gameConfiguration, ChessGame chessGame){
        var frame = new JFrame();
        frame.setSize(length, length);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var rootPanel = new JPanel();
        rootPanel.setLayout(new FlowLayout());

        var thisObject = this;
        var buttonStart = new JButton("Start game");
        buttonStart.addActionListener(actionEvent -> {
            frame.dispose();
            HashSet<GameEndedListener> listeners = new HashSet<>(){{add(thisObject);}};
            var gameConfig = new Thread(() -> gameConfiguration.startNewGameConfiguration(chessGame, listeners));
            gameConfig.start();
        });
        var quitButton = new JButton("QUIT");
        quitButton.addActionListener(actionEvent -> System.exit(0));

        rootPanel.add(buttonStart);
        rootPanel.add(quitButton);

        frame.getContentPane().add(rootPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        return frame;
    }
}
