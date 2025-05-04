package game.gui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private final Image background;

    public BackgroundPanel(String imagePath) {
        var url = getClass().getClassLoader().getResource(imagePath);
        if (url == null) {
            throw new IllegalArgumentException("Image not found: " + imagePath);
        }
        background = new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
