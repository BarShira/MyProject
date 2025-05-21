// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.map;

import game.characters.PlayerCharacter;
import game.core.GameEntity;
import game.items.GameItem;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Represents the grid-based map of the game.
 * Each position on the map can contain one or more GameEntity objects.
 * Now supports locking of positions for safe concurrent access.
 */
public class GameMap {

    private Map<Position, List<GameEntity>> grid;

    public GameMap() {
        this.grid = new HashMap<>();
    }

    public boolean addEntity(GameEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        Position pos = entity.getPosition();
        grid.computeIfAbsent(pos, k -> new ArrayList<>()).add(entity);
        return true;
    }

    public boolean removeEntity(GameEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        List<GameEntity> entities = grid.get(entity.getPosition());
        return entities != null && entities.remove(entity);
    }

    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    public boolean isOccupied(Position pos) {
        return grid.containsKey(pos) && !grid.get(pos).isEmpty();
    }

    public Set<Position> getAllOccupiedPositions() {
        return grid.keySet();
    }

    public Map<Position, List<GameEntity>> getGrid() {
        return grid;
    }

    public boolean canMoveTo(Position pos, PlayerCharacter player) {
        List<GameEntity> entities = getEntitiesAt(pos);

        for (GameEntity entity : entities) {
            if (entity instanceof GameItem item && item.isBlocking()) {
                return false;
            }

            if (entity instanceof game.combat.Combatant combatant && combatant != player) {
                return false;
            }
        }

        return true;
    }

    /**
     * Attempts to lock a target position for movement or action.
     * @param pos the position to lock
     * @param timeoutMillis the max time to wait
     * @return true if lock acquired
     */
    public boolean tryLockPosition(Position pos, long timeoutMillis) {
        return pos.tryLock(timeoutMillis);
    }

    /**
     * Unlocks the given position.
     * @param pos the position to unlock
     */
    public void unlockPosition(Position pos) {
        pos.unlock();
    }

    @Override
    public String toString() {
        return "GameMap with " + grid.size() + " active positions.";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameMap)) return false;
        GameMap other = (GameMap) obj;
        return Objects.equals(this.grid, other.grid);
    }
}
