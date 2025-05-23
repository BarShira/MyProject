// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.gui;

import game.map.GameMap;

import javax.swing.*;
import java.awt.*;

/**
 * Main game window containing the map panel and player status panel.
 * This class initializes the game frame and arranges the main UI components.
 */
public class GameFrame extends JFrame{
    public GameFrame(MapPanel mapPanel, StatusPanel statusPanel) {
        super("Dungeons & Dragons");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        add(mapPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

}
