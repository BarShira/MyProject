package game.gui;

import game.characters.*;
import game.combat.CombatSystem;
import game.combat.MeleeFighter;
import game.combat.RangedFighter;
import game.core.GameEntity;
import game.engine.GameWorld;
import game.items.*;
import game.map.GameMap;
import game.map.Position;

import javax.swing.*;
import java.awt.*;

public class GameController implements MapPanel.MapClickListener {

    private final GameWorld world;
    private final GameMap gameMap;
    private final PlayerCharacter player;
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;

    public GameController(GameWorld world, GameMap gameMap, PlayerCharacter player,
                          MapPanel mapPanel, StatusPanel statusPanel) {
        this.world = world;
        this.gameMap = gameMap;
        this.player = player;
        this.mapPanel = mapPanel;
        this.statusPanel = statusPanel;

        this.mapPanel.setClickListener(this);
        mapPanel.refresh(player.getPosition());
        statusPanel.updateStatus(player);
    }

    @Override
    public void onLeftClick(int row, int col) {
        Position target = new Position(row, col);

        // 1. בדוק אם מותר לזוז
        if (!isWithinMoveRange(player.getPosition(), target)) return;

        // 2. בדוק אם יש חסימה (לדוגמה קיר)
        for (GameEntity entity : gameMap.getEntitiesAt(target)) {
            if (entity instanceof GameItem item && item.isBlocking()) {
                mapPanel.highlightCell(target, Color.ORANGE); // חסום = כתום
                return;
            }
        }

        // 3. עבור על היישויות במשבצת היעד
        for (GameEntity entity : gameMap.getEntitiesAt(target)) {

            // אויב
            if (entity instanceof Enemy enemy) {
                boolean inRange = (player instanceof MeleeFighter mf && mf.isInMeleeRange(player.getPosition(), target)) ||
                        (player instanceof RangedFighter rf && rf.isInRange(player.getPosition(), target));
                if (inRange) {
                    CombatSystem.resolveCombat(player, enemy, world);
                    if (enemy.isDead()) {
                        world.removeEnemy(enemy);
                        gameMap.removeEntity(enemy);
                        mapPanel.highlightCell(target, Color.RED); // הבהוב אדום
                    }
                    updateUI();
                    return;
                }
            }

            // שיקוי חיים / כוח
            if (entity instanceof Potion || entity instanceof PowerPotion) {
                if (player.addToInventory((GameItem) entity)) {
                    world.removeItem((GameItem) entity);
                    gameMap.removeEntity(entity);
                    mapPanel.highlightCell(target, Color.GREEN); // הבהוב ירוק
                }
                updateUI();
                return;
            }

            // אובייקט אינטראקטיבי אחר (לדוג' Treasure)
            if (entity instanceof GameItem item && item instanceof Interactable interactable) {
                interactable.interact(player);
                world.removeItem(item);
                gameMap.removeEntity(item);
                mapPanel.highlightCell(target, Color.GREEN); // אינטראקציה = ירוק
                updateUI();
                return;
            }
        }

        // 4. תנועה רגילה אם אין אויב או פריט אינטראקטיבי
        gameMap.removeEntity(player);
        player.setPosition(target);
        gameMap.addEntity(player);
        updateUI();
    }

    @Override
    public void onRightClick(int row, int col, Component invoker, int x, int y) {
        Position pos = new Position(row, col);
        StringBuilder info = new StringBuilder("<html>");

        for (GameEntity entity : gameMap.getEntitiesAt(pos)) {
            info.append(entity.toString()).append("<br>");
        }

        if (info.toString().equals("<html>")) info.append("Nothing here.");
        info.append("</html>");

        JPopupMenu popup = new JPopupMenu();
        JLabel label = new JLabel(info.toString());
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        popup.add(label);
        popup.show(invoker, x, y);
    }

    private boolean isWithinMoveRange(Position from, Position to) {
        return from.distanceTo(to) <= 1;
    }

    private void updateUI() {
        mapPanel.refresh(player.getPosition());
        statusPanel.updateStatus(player);
    }
}
