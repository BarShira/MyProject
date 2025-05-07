package game.gui;

import game.core.GameEntity;
import game.engine.GameWorld;
import game.map.GameMap;
import game.map.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel implements GameEventListener {

    private final int size;
    private final JLabel[][] cells;
    private final GameMap gameMap;
    private final Map<String, ImageIcon> iconMap = new HashMap<>();
    private final Map<Position, Color> highlights = new HashMap<>();
    private final int cellSize = 64;
    private MapClickListener clickListener;
    private Position currentPlayerPosition;
    private Position lastPlayerPosition = null;


    public MapPanel(int size, GameMap gameMap) {
        this.size = size;
        this.gameMap = gameMap;
        this.setLayout(new GridLayout(size, size));
        this.cells = new JLabel[size][size];
        loadIcons();
        initializeGrid();
    }

    public void setPlayerPosition(Position pos) {
        this.currentPlayerPosition = pos;
        this.lastPlayerPosition = null; // בכוונה! כדי לא לדלג על refresh
    }

    public void setClickListener(MapClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onGameStateChanged(GameWorld world) {
        if (!world.getPlayers().isEmpty()) {
            Position pos = world.getPlayers().get(0).getPosition(); // השחקן הראשון
            refresh(pos); // עדכון לפי מיקום עדכני מהמודל
        }
    }


    private void loadIcons() {
        iconMap.put("Warrior", loadIcon("figther.png"));
        iconMap.put("Mage", loadIcon("Mage.png"));
        iconMap.put("Archer", loadIcon("archer.png"));
        iconMap.put("Goblin", loadIcon("goblin.png"));
        iconMap.put("Orc", loadIcon("orc.png"));
        iconMap.put("Dragon", loadIcon("dragon.png"));
        iconMap.put("Potion", loadIcon("life_potion.png"));
        iconMap.put("PowerPotion", loadIcon("power_potion.png"));
        iconMap.put("Wall", loadIcon("wall.png"));
        iconMap.put("Treasure", loadIcon("treasure.png"));
        iconMap.put("GameBackground", loadIcon("game_background.png"));
        iconMap.put("Unknown", loadIcon("unknown.png"));
        iconMap.put("Empty", loadIcon("game_background.png"));
    }

    private ImageIcon loadIcon(String filename) {
        var url = getClass().getClassLoader().getResource("images/" + filename);
        if (url == null) {
            System.err.println("Missing icon: " + filename);
            return new ImageIcon();
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void initializeGrid() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JLabel cell = new JLabel();
                cell.setOpaque(true);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);
                cell.setBackground(Color.BLACK);

                final int r = row, c = col;
                cell.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (clickListener == null) return;
                        if (SwingUtilities.isLeftMouseButton(e))
                            clickListener.onLeftClick(r, c);
                        else if (SwingUtilities.isRightMouseButton(e))
                            clickListener.onRightClick(r, c, e.getComponent(), e.getX(), e.getY());
                    }
                });

                cells[row][col] = cell;
                this.add(cell);
            }
        }
    }

    public void refresh(Position playerPos) {
        if (playerPos == null || playerPos.equals(lastPlayerPosition)) return;

        this.currentPlayerPosition = playerPos;
        this.lastPlayerPosition = playerPos;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position pos = new Position(row, col);
                JLabel cell = cells[row][col];

                cell.setIcon(null);


                int distance = playerPos.distanceTo(pos);
                List<GameEntity> entities = gameMap.getEntitiesAt(pos);

                GameEntity visible = null;
                if (distance <= 2) {
                    for (GameEntity e : entities) {
                        if (e instanceof game.characters.Enemy en && en.isDead()) continue;
                        e.setVisible(true);
                        visible = e;
                        break;
                    }
                }

                if (distance > 2) {
                    cell.setIcon(iconMap.get("Unknown"));
                } else if (visible != null) {
                    cell.setIcon(iconMap.getOrDefault(visible.getClass().getSimpleName(), iconMap.get("Unknown")));
                } else if (!highlights.containsKey(pos)) {
                    cell.setIcon(iconMap.get("Empty"));
                }

                // Apply highlight or default background
                if (highlights.containsKey(pos)) {
                    cell.setBackground(highlights.get(pos));
                } else {
                    cell.setBackground(Color.BLACK);
                }
            }
        }
    }

    public void highlightCell(Position pos, Color color) {
        if (pos == null) return;
        highlights.put(pos, color);
        refresh(currentPlayerPosition);

        Timer t = new Timer(3000, e -> {
            highlights.remove(pos);
            refresh(currentPlayerPosition);
        });
        t.setRepeats(false);
        t.start();
    }

    public int getCellSize() {
        return cellSize;
    }

    public interface MapClickListener {
        void onLeftClick(int row, int col);
        void onRightClick(int row, int col, Component invoker, int mouseX, int mouseY);
    }
}