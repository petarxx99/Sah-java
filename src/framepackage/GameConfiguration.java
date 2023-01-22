package src.framepackage;

import src.menu.GameEndedListener;

import java.util.HashSet;
import java.util.Set;

public interface GameConfiguration {
    public void startNewGameConfiguration(ChessGame chessGame, Set<GameEndedListener> gameEndedListeners);
}
