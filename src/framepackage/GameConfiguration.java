package framepackage;

import menu.GameEndedListener;
import framepackage.ChessGame;

import java.util.Set;

public interface GameConfiguration {
    public void startNewGameConfiguration(ChessGame chessGame, Set<GameEndedListener> gameEndedListeners);
}
