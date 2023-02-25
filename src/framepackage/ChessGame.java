package framepackage;

import communication.movesender.MoveSender;
import menu.GameEndedListener;

import java.util.Set;

public interface ChessGame {
    public void startGame();
    public void addGameEndedListener(GameEndedListener listener);
    public void addGameEndedListeners(Set<GameEndedListener> listeners);
    public boolean removeGameEndedListener(GameEndedListener listenerToRemove);
    public void initializeGame(MoveSender moveSender, Opponents opponent, boolean whitesPerspective, byte opponentsColor, int duzinaPolja, Set<GameEndedListener> gameEndedListeners);
}
