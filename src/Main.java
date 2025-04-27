// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551


import game.characters.*;
import game.combat.*;
import game.core.*;
import game.engine.*;
import game.items.*;
import game.map.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class for launching the game.
 * Handles character creation, map initialization, and the main game loop.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Map size
        System.out.print("Enter map size (min 10): ");
        int size = scanner.nextInt();
        while (size < 10) {
            System.out.print("Size too small. Enter at least 10: ");
            size = scanner.nextInt();
        }

        // 2. Choose character class
        System.out.println("Choose your character class:");
        System.out.println("1. Warrior");
        System.out.println("2. Mage");
        System.out.println("3. Archer");
        int choice = scanner.nextInt();

        System.out.print("Enter your character name: ");
        String name = scanner.next();

        // 3. Create world and map
        GameMap map = new GameMap();
        GameWorld world = new GameWorld(map);

        // 4. Random player position
        Random rand = new Random();
        Position playerPos = new Position(rand.nextInt(size), rand.nextInt(size));
        PlayerCharacter player;

        switch (choice) {
            case 1 -> player = new Warrior(name, playerPos);
            case 2 -> player = new Mage(name, playerPos);
            case 3 -> player = new Archer(name, playerPos);
            default -> {
                System.out.println("Invalid choice. Defaulting to Warrior.");
                player = new Warrior(name, playerPos);
            }
        }

        world.addPlayer(player);
        map.addEntity(player);

        // 5. Populate map with entities
        MapPopulator.populateMap(world, size, player.getPosition());

        System.out.println("Welcome, " + player.getName() + "! You start at " + player.getPosition());

        // 6. Game loop
        boolean gameRunning = true;
        while (gameRunning) {
            System.out.println("\nChoose action: (w/a/s/d to move, i to use potion, p to use power potion, b to view inventory, q to quit)");
            String input = scanner.next().toLowerCase();

            switch (input) {
                case "w", "a", "s", "d" -> {
                    Position newPos = getNewPosition(player.getPosition(), input, size);

                    // Check if position is blocked
                    boolean blocked = false;
                    for (GameEntity entity : map.getEntitiesAt(newPos)) {
                        if (entity instanceof GameItem item && item.isBlocking()) {
                            blocked = true;
                            System.out.println("Movement blocked by: " + item.getDescription());
                            break;
                        }
                    }

                    // Check if position is already occupied by an enemy (Combatant)
                    boolean occupiedByEnemy = false;
                    for (GameEntity entity : map.getEntitiesAt(newPos)) {
                        if (entity instanceof Combatant combatant && combatant != player) {
                            occupiedByEnemy = true;
                            System.out.println("You can't move there – an enemy is already occupying the tile.");
                            break;
                        }
                    }

                    // Cancel movement if blocked or enemy is on the tile
                    if (blocked || occupiedByEnemy) break;

                    player.setPosition(newPos);
                    System.out.println("You moved to: " + newPos);

                    // Check for nearby enemies within range and attack them if possible
                    for (Enemy enemy : new ArrayList<>(world.getEnemies())) {
                        if (enemy.isDead()) continue;
                        if (player instanceof MeleeFighter mf && mf.isInMeleeRange(player.getPosition(), enemy.getPosition())) {
                            System.out.println("An enemy is nearby! Engaging in melee combat with: " + enemy);
                            CombatSystem.resolveCombat(player, enemy, world);
                        } else if (player instanceof RangedFighter rf && rf.isInRange(player.getPosition(), enemy.getPosition())) {
                            System.out.println("A distant enemy is within range! Engaging in ranged combat with: " + enemy);
                            CombatSystem.resolveCombat(player, enemy, world);
                        }
                    }

                    // Interaction with items at new position
                    List<GameEntity> entities = new ArrayList<>(map.getEntitiesAt(newPos));
                    for (GameEntity entity : entities) {
                        if (entity == player) continue;

                        if (entity instanceof Enemy enemy) {
                            System.out.println("An enemy is here: " + enemy);
                            CombatSystem.resolveCombat(player, enemy, world);
                            break;

                        } else if (entity instanceof Potion || entity instanceof PowerPotion) {
                            if (player.addToInventory((GameItem) entity)) {
                                System.out.println("You picked up: " + entity);
                                world.removeItem((GameItem) entity);
                                map.removeEntity(entity);
                            }
                        } else if (entity instanceof GameItem item && item instanceof Interactable interactable) {
                            interactable.interact(player);
                            System.out.println("You interacted with: " + item);
                            world.removeItem(item);
                            map.removeEntity(item);
                            break;
                        }
                    }
                }

                case "i" -> {
                    if (!player.usePotion()) {
                        System.out.println("No healing potion found!");
                    } else {
                        System.out.println("You used a healing potion.");
                    }
                }

                case "p" -> {
                    if (!player.usePowerPotion()) {
                        System.out.println("No power potion found!");
                    } else {
                        System.out.println("You used a power potion.");
                    }
                }

                case "b" -> {
                    System.out.println("Your inventory:");
                    for (GameItem item : player.getInventory().getItems()) {
                        System.out.println("- " + item);
                    }
                    System.out.println("Treasure points: " + player.getTreasurePoints());
                }

                case "q" -> {
                    System.out.println("Thanks for playing! Final treasure points: " + player.getTreasurePoints());
                    gameRunning = false;
                }

                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }

    /**
     * Calculates the next position based on user input.
     *
     * @param current   current player position
     * @param direction "w", "a", "s", or "d"
     * @param size      board boundary limit
     * @return new position after movement (clamped within bounds)
     */
    private static Position getNewPosition(Position current, String direction, int size) {
        int row = current.getRow();
        int col = current.getCol();

        return switch (direction) {
            case "w" -> new Position(Math.max(0, row - 1), col);
            case "s" -> new Position(Math.min(size - 1, row + 1), col);
            case "a" -> new Position(row, Math.max(0, col - 1));
            case "d" -> new Position(row, Math.min(size - 1, col + 1));
            default -> current;
        };
    }
}
