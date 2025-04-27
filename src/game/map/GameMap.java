// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.map;

import game.core.GameEntity;

import java.util.*;

/**
 * Represents the grid-based map of the game.
 * Each position on the map can contain one or more GameEntity objects.
 */
public class GameMap {

    /**
     * A mapping from positions to a list of entities occupying that position.
     */
    private Map<Position, List<GameEntity>> grid;

    /**
     * Constructs an empty GameMap.
     */
    public GameMap() {
        this.grid = new HashMap<>();
    }

    /**
     * Adds a GameEntity to the map at its current position.
     *
     * @param entity the entity to add
     * @return true if the entity was added successfully, false if null or position is invalid
     */
    public boolean addEntity(GameEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        Position pos = entity.getPosition();
        grid.computeIfAbsent(pos, k -> new ArrayList<>()).add(entity);
        return true;
    }

    /**
     * Removes a GameEntity from the map.
     *
     * @param entity the entity to remove
     * @return true if the entity was found and removed, false otherwise
     */
    public boolean removeEntity(GameEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        List<GameEntity> entities = grid.get(entity.getPosition());
        return entities != null && entities.remove(entity);
    }

    /**
     * Retrieves the list of entities located at a specific position.
     *
     * @param pos the position to query
     * @return a list of GameEntity objects (empty if none found)
     */
    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    /**
     * Checks whether a specific position is currently occupied by any entities.
     *
     * @param pos the position to check
     * @return true if the position has at least one entity, false otherwise
     */
    public boolean isOccupied(Position pos) {
        return grid.containsKey(pos) && !grid.get(pos).isEmpty();
    }

    /**
     * Returns all positions currently in use on the map.
     *
     * @return a set of positions that have at least one entity
     */
    public Set<Position> getAllOccupiedPositions() {
        return grid.keySet();
    }

    /**
     * Provides direct access to the internal grid map.
     * Use with caution to avoid breaking map integrity.
     *
     * @return the position-to-entity map
     */
    public Map<Position, List<GameEntity>> getGrid() {
        return grid;
    }

    /**
     * Returns a summary of the GameMap.
     *
     * @return a string indicating the number of occupied positions
     */
    @Override
    public String toString() {
        return "GameMap with " + grid.size() + " active positions.";
    }

    /**
     * Checks equality between this GameMap and another object.
     * Two maps are considered equal if their grids are equal.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameMap)) return false;
        GameMap other = (GameMap) obj;
        return Objects.equals(this.grid, other.grid);
    }
}
