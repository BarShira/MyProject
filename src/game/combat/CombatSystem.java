// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.items.Treasure;
import game.map.Position;

/**
 * Handles combat logic between two entities in the game world.
 * This class supports melee and ranged attacks, physical and magical,
 * and handles evasion, damage calculation, and post-combat events
 * such as enemy defeat or player death.
 *
 * <p>
 * No downcasting is used in this class – only instanceof with safe pattern matching.
 * </p>
 */
public class CombatSystem {

    /**
     * Resolves a single combat action from an attacker to a defender.
     * The method performs the following steps:
     * <ol>
     *     <li>Checks if attacker is in range based on attack type (melee or ranged)</li>
     *     <li>Checks if defender evades the attack</li>
     *     <li>Delegates the actual damage via attack or spell casting</li>
     *     <li>Handles post-combat logic: defeat, death, and treasure drop</li>
     *     <li>If defender survives, it may counterattack</li>
     * </ol>
     *
     * @param attacker the entity performing the attack
     * @param defender the entity being attacked
     */
    public static void resolveCombat(Combatant attacker, Combatant defender, GameWorld world) {
        Position attackerPos = attacker.getPosition();
        Position defenderPos = defender.getPosition();

        boolean inRange = false;

        // 1. Check if the attacker is in range
        if (attacker instanceof MeleeFighter mf) {
            inRange = mf.isInMeleeRange(attackerPos, defenderPos);
        } else if (attacker instanceof RangedFighter rf) {
            inRange = rf.isInRange(attackerPos, defenderPos);
        }

        if (!inRange) {
            System.out.println("Target is out of range.");
            return;
        }

        // 2. Check if the defender evades the attack
        if (defender.tryEvade()) {
            System.out.println("The enemy evaded the attack!");
            return;
        }

        int preHP = defender.getHealth();

        // 3. Perform attack based on attacker type
        if (attacker instanceof MagicAttacker ma) {
            ma.castSpell(defender);
        } else if (attacker instanceof PhysicalAttacker pa) {
            pa.attack(defender);
        } else {
            System.out.println("Attacker cannot perform any type of attack.");
            return;
        }

        int postHP = defender.getHealth();
        int damageDealt = preHP - postHP;
        System.out.println(attacker.getDisplaySymbol()
                + " dealt " + damageDealt + " damage. "
                + defender.getDisplaySymbol() + " now has " + postHP + " HP.");

        // 4. Handle death and post-combat events
        if (defender.isDead()) {
            defender.defeat();

            if (defender instanceof LootDropper dropper) {
                Treasure loot = dropper.generateLoot();
                if (loot != null) {
                    world.addItem(loot);
                    world.getMap().addEntity(loot);

                    if (attacker instanceof PlayerCharacter pc) {
                        pc.updateTreasurePoint(loot.getValue());
                        System.out.println("You found treasure worth " + loot.getValue() + " points!");
                    } else {
                        System.out.println("You found treasure!");
                    }
                } else {
                    System.out.println("No treasure dropped.");
                }
            }


            if (defender instanceof Enemy) {
                world.removeEnemy((Enemy) defender);
            }

            if (defender instanceof PlayerCharacter pc) {
                System.out.println("Game Over! Your treasure points: " + pc.getTreasurePoints());
            }

        } else if (defender instanceof PhysicalAttacker counter && defender instanceof Combatant) {
            System.out.println("The enemy fights back!");

            int attackerHPBefore = attacker.getHealth();
            counter.attack(attacker);
            int attackerHPAfter = attacker.getHealth();
            int damageTaken = attackerHPBefore - attackerHPAfter;

            System.out.println(defender.getDisplaySymbol() + " dealt " + damageTaken +
                    " damage in retaliation. " + attacker.getDisplaySymbol() +
                    " now has " + attackerHPAfter + " HP.");
        }
    }
}
