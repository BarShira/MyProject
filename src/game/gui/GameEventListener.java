package game.gui;

import game.engine.GameWorld;

public interface GameEventListener {
    void onGameStateChanged(GameWorld world);
}
