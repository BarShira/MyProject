// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.engine;

import game.characters.PlayerCharacter;
import game.characters.Enemy;
import game.combat.LootDropper;
import game.items.GameItem;
import game.items.Treasure;
import game.map.GameMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the central world state of the game.
 * Stores all active players, enemies, items, and the game map.
 *
 * This class acts as the core model in the MVC architecture.
 * It notifies listeners whenever the state changes using the observer pattern.
 */
public class GameWorld extends GameObservable {

    /** List of all player characters currently in the game */
    private List<PlayerCharacter> players;

    /** List of all enemies currently active in the game */
    private List<Enemy> enemies;

    /** List of all collectible or interactive items in the world */
    private List<GameItem> items;

    /** The map that defines the layout of the world and object positions */
    private GameMap map;

    /**
     * Constructs a new GameWorld with empty lists and the given map.
     *
     * @param map the GameMap to associate with the world
     */
    public GameWorld(GameMap map) {
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.map = map;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return list of PlayerCharacter objects
     */
    public List<PlayerCharacter> getPlayers() {
        return players;
    }

    /**
     * Returns the list of enemies in the game.
     *
     * @return list of Enemy objects
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the list of items currently present in the game.
     *
     * @return list of GameItem objects
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Returns the map associated with the game world.
     *
     * @return the GameMap object
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Adds a player to the world. Notifies listeners if successful.
     *
     * @param p the player to add
     * @return true if added successfully, false otherwise
     */
    public boolean addPlayer(PlayerCharacter p) {
        if (p == null) return false;
        boolean added = players.add(p);
        if (added) notifyGameStateChanged();
        return added;
    }

    /**
     * Adds an enemy to the world. Notifies listeners if successful.
     *
     * @param e the enemy to add
     * @return true if added successfully, false otherwise
     */
    public boolean addEnemy(Enemy e) {
        if (e == null) return false;
        boolean added = enemies.add(e);
        if (added) notifyGameStateChanged();
        return added;
    }

    /**
     * Adds an item to the world. Notifies listeners if successful.
     *
     * @param item the item to add
     * @return true if added successfully, false otherwise
     */
    public boolean addItem(GameItem item) {
        if (item == null) return false;
        boolean added = items.add(item);
        if (added) notifyGameStateChanged();
        return added;
    }

    /**
     * Removes an enemy from the world. Notifies listeners if successful.
     *
     * @param e the enemy to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeEnemy(Enemy e) {
        boolean removed = enemies.remove(e);
        if (removed) notifyGameStateChanged();
        return removed;
    }

    /**
     * Removes an item from the world. Notifies listeners if successful.
     *
     * @param item the item to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeItem(GameItem item) {
        boolean removed = items.remove(item);
        if (removed) notifyGameStateChanged();
        return removed;
    }

    /**
     * Handles loot drop generation from a lootable source (like a defeated enemy).
     * If loot is generated, it is added to the world and map.
     *
     * @param source the entity capable of dropping loot
     * @return true if loot was generated and added, false otherwise
     */
    public boolean handleLootDrop(LootDropper source) {
        if (source == null) return false;
        Treasure t = source.generateLoot();
        if (t == null) return false;

        boolean added = addItem(t);
        map.addEntity(t);
        return added;
    }

    /**
     * Returns a string summary of the current state of the world.
     *
     * @return summary string with player/enemy/item counts
     */
    @Override
    public String toString() {
        return "GameWorld with " + players.size() + " player(s), " +
                enemies.size() + " enemy(ies), " +
                items.size() + " item(s).";
    }
}
