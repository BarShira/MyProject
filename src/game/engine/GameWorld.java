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
 * Stores all active players, enemies, items, and the map.
 */
public class GameWorld {

    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap map;

    /**
     * Constructs a new GameWorld with empty lists and a given game map.
     *
     * @param map the map to associate with the world
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
     * Returns the list of items currently in the game.
     *
     * @return list of GameItem objects
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Returns the game map associated with the world.
     *
     * @return the GameMap object
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Adds a player to the game world.
     *
     * @param p the player to add
     * @return true if added successfully, false otherwise
     */
    public boolean addPlayer(PlayerCharacter p) {
        if (p == null) return false;
        return players.add(p);
    }

    /**
     * Adds an enemy to the game world.
     *
     * @param e the enemy to add
     * @return true if added successfully, false otherwise
     */
    public boolean addEnemy(Enemy e) {
        if (e == null) return false;
        return enemies.add(e);
    }

    /**
     * Adds an item to the game world.
     *
     * @param item the item to add
     * @return true if added successfully, false otherwise
     */
    public boolean addItem(GameItem item) {
        if (item == null) return false;
        return items.add(item);
    }

    /**
     * Removes an enemy from the game world.
     *
     * @param e the enemy to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeEnemy(Enemy e) {
        return enemies.remove(e);
    }

    /**
     * Removes an item from the game world.
     *
     * @param item the item to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeItem(GameItem item) {
        return items.remove(item);
    }


    /**
     * If the source is capable of dropping loot, this generates the loot and adds it to the world and map.
     *
     * @param source the LootDropper entity (e.g., an enemy)
     * @return true if loot was generated and added, false otherwise
     */
    public boolean handleLootDrop(LootDropper source) {
        if (source == null) return false;
        Treasure t = source.generateLoot();
        if (t == null) return false;

        addItem(t);
        map.addEntity(t);
        return true;
    }

    /**
     * Returns a summary string describing the game world's state.
     *
     * @return a string with the number of players, enemies, and items
     */
    @Override
    public String toString() {
        return "GameWorld with " + players.size() + " player(s), " +
                enemies.size() + " enemy(ies), " +
                items.size() + " item(s).";
    }
}
