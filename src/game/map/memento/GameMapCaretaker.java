package game.map.memento;

import game.map.GameMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameMapCaretaker {

    private final Deque<GameMapMemento> history = new ArrayDeque<>();

    public void save(GameMap map) {
        history.push(map.saveState());
    }

    public void undo(GameMap map) {
        if (!history.isEmpty()) {
            GameMapMemento previous = history.pop();
            map.restoreState(previous);
        }
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}

