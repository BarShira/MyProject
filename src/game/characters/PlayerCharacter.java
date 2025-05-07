// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.core.Inventory;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import game.map.Position;

/**
 * Represents a player-controlled character in the game.
 * Holds inventory and treasure points, and can use items.
 */
public class PlayerCharacter extends AbstractCharacter {

    private String name;
    private Inventory inventory;
    private int treasurePoints;

    /**
     * Constructs a new PlayerCharacter with the given name and position.
     *
     * @param name     the player's name
     * @param position the starting position on the map
     */
    public PlayerCharacter(String name, Position position) {
        super(position);
        this.name = name;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an item to the player's inventory.
     *
     * @param item the item to add
     * @return true if item is not null and added, false otherwise
     */
    public boolean addToInventory(GameItem item) {
        if (item == null) return false;
        inventory.addItem(item);
        return true;
    }

    /**
     * Attempts to use a healing potion from the inventory.
     *
     * @return true if a potion was used, false otherwise
     */
    public boolean usePotion() {
        for (GameItem item : inventory.getItems()) {
            if (item instanceof Potion p && !(p instanceof PowerPotion)) {
                p.interact(this);
                inventory.removeItem(p);
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to use a power potion from the inventory.
     *
     * @return true if a power potion was used, false otherwise
     */
    public boolean usePowerPotion() {
        for (GameItem item : inventory.getItems()) {
            if (item instanceof PowerPotion p) {
                p.interact(this);
                inventory.removeItem(p);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the treasure points of the player.
     *
     * @param amount amount to add (can be negative)
     * @return true if amount is valid (non-zero), false otherwise
     */
    public boolean updateTreasurePoint(int amount) {
        if (amount == 0) return false;
        this.treasurePoints += amount;
        return true;
    }

    /**
     * Returns the current treasure points collected by the player.
     *
     * @return treasure points value
     */
    public int getTreasurePoints() {
        return treasurePoints;
    }

    /**
     * Increases the player's power by the given amount.
     *
     * @param amount the amount to increase
     * @return true if the new power was set successfully, false otherwise
     */
    public boolean increasePower(int amount) {
        if (amount <= 0) return false;
        return setPower(getPower() + amount);
    }

    /**
     * Handles receiving damage from a combatant.
     * If the player evades, no damage is taken.
     *
     * @param amount the amount of damage to receive
     * @param source the source of the damage
     */
    public void receiveDamage(int amount, Combatant source) {
        if (tryEvade()) return;
        int finalDamage = Math.max(1, amount); // Always take at least 1 damage
        setHealth(getHealth() - finalDamage);
    }

    @Override
    public String getDisplaySymbol() {
        return name.substring(0, 1).toUpperCase(); // First letter of name
    }

    @Override
    public String toString() {
        return super.toString() + " | Player: " + name + " | Treasure: " + treasurePoints;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PlayerCharacter)) return false;
        PlayerCharacter other = (PlayerCharacter) obj;
        return super.equals(other) &&
                this.name.equals(other.name) &&
                this.treasurePoints == other.treasurePoints;
    }
    /**
     * Defines the behavior when the player is defeated.
     * Currently, this method is empty and can be overridden.
     */
    @Override
    public void defeat() {
    }
    /**
     * Returns the player's inventory.
     *
     * @return the inventory object
     */
    public Inventory getInventory() {
        return inventory;
    }
}
