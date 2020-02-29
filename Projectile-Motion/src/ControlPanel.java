import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel {
    // PROPERTIES
    private BufferedImage imgProjType; // Projectile Type image
    public int intProjType = 1; // Projectile type; defaults to 1

    // METHODS
    public void paintComponent(Graphics g) { // Override JPanel paintComponent method
        super.paintComponent(g); // Clear previous drawings (Windows only); super JPanel (original) paintComponent method
        try {
            imgProjType = ImageIO.read(new File("Projectile-Motion/images/type" + intProjType + ".png")); // Load projectile type __ image
        } catch (IOException e) { // Catch IOExceptions for loading images
            e.printStackTrace(); // Print error location
        }
        g.drawImage(imgProjType, 435, 20, this); // Draw projectile type image
    }

    // CONSTRUCTOR
    public ControlPanel () { // Construct ControlPanel
        super(); // Super JPanel
    }
}
