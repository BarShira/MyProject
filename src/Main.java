// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.engine.EnemyManager;
import game.engine.GameLogger;
import game.engine.GameWorld;
import game.engine.MapPopulator;
import game.gui.*;
import game.items.GameItem;
import game.map.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    private static void clearLogFile() {
        try (FileWriter fw = new FileWriter("log.txt", false)) {
            fw.write(""); // ריקון תוכן הקובץ
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        clearLogFile();
        GameLogger.log("=== Game Started ===");


        SwingUtilities.invokeLater(() -> {
            // choose name
            String name = promptForName();

            // choose character
            PlayerCharacter player = promptForCharacter(name);

            // choose map size
            int mapSize = promptForMapSize();

            // create game world
            GameMap map = new GameMap();
            GameWorld world = new GameWorld(map);

            // randomize player position
            Random rand = new Random();
            Position playerPos = new Position(rand.nextInt(mapSize), rand.nextInt(mapSize));
            player.setPosition(playerPos);
            world.addPlayer(player);
            map.addEntity(player);

            // create enemies
            MapPopulator.populateMap(world, mapSize, playerPos);
            EnemyManager enemyManager = new EnemyManager(world.getEnemies(), player, map, world);
            enemyManager.startAll();


            // 7. GUI
            MapPanel mapPanel = new MapPanel(mapSize, map);
            StatusPanel statusPanel = new StatusPanel();
            GameController controller = new GameController(world, map, player, mapPanel, statusPanel);
            GameFrame frame = new GameFrame(mapPanel, statusPanel);
            frame.setVisible(true);
        });
    }

    private static String promptForName() {
        JDialog dialog = new JDialog();
        BackgroundPanel panel = new BackgroundPanel("images/choose_name_character.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JLabel label = new JLabel("Enter your name:");
        label.setForeground(Color.WHITE);
        JTextField field = new JTextField(15);
        JButton ok = new JButton("OK");

        final String[] nameHolder = {null};

        ok.addActionListener(e -> {
            String input = field.getText().trim();
            if (!input.isEmpty()) {
                nameHolder[0] = input;
                dialog.dispose();
            }
        });

        panel.add(label);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ok);

        dialog.setContentPane(panel);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setVisible(true);

        return nameHolder[0];
    }

    private static PlayerCharacter promptForCharacter(String name) {
        JDialog dialog = new JDialog();
        BackgroundPanel panel = new BackgroundPanel("images/choose_name_character.png");
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Choose your hero:");
        label.setForeground(Color.WHITE);
        panel.add(label);

        final PlayerCharacter[] selected = {null};
        Position dummyPos = new Position(0, 0); // to avoid null pointer exception

        JButton warrior = new JButton("Warrior");
        warrior.addActionListener(e -> {
            selected[0] = new Warrior(name, dummyPos);
            dialog.dispose();
        });

        JButton mage = new JButton("Mage");
        mage.addActionListener(e -> {
            selected[0] = new Mage(name, dummyPos);
            dialog.dispose();
        });

        JButton archer = new JButton("Archer");
        archer.addActionListener(e -> {
            selected[0] = new Archer(name, dummyPos);
            dialog.dispose();
        });

        panel.add(warrior);
        panel.add(mage);
        panel.add(archer);

        dialog.setContentPane(panel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setVisible(true);

        return selected[0];
    }

    private static int promptForMapSize() {
        JDialog dialog = new JDialog();
        BackgroundPanel panel = new BackgroundPanel("images/choose_map.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("Enter map size (min 10):");
        label.setForeground(Color.WHITE);
        JTextField field = new JTextField(5);
        JButton ok = new JButton("Start");

        final int[] mapSize = {0};

        ok.addActionListener(e -> {
            try {
                int size = Integer.parseInt(field.getText());
                if (size >= 10) {
                    mapSize[0] = size;
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Minimum is 10.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Enter a valid number.");
            }
        });

        panel.add(label);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ok);

        dialog.setContentPane(panel);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setVisible(true);

        return mapSize[0];
    }
}
