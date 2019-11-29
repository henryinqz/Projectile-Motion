import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HelpPanel extends JPanel {
    // PROPERTIES
    private BufferedImage imgTypes; // Projectile Type help screen
    private BufferedImage imgControl; // ControlPanel help screen
    private BufferedImage imgStat; // StatPanel help screen
    public int intPage = 0; // 0 = proj types, 1 = control, 2 = stats; 0 default

    // METHODS
    public void paintComponent(Graphics g) { // Override JPanel paintComponent method
        super.paintComponent(g); // Clear previous drawings (Windows only); super JPanel (original) paintComponent method
        try {
            imgTypes = ImageIO.read(new File("images/help/helpTypes.png")); // Load projectile type help
            imgControl = ImageIO.read(new File("images/help/helpControl.png")); // Load control panel help
            imgStat = ImageIO.read(new File("images/help/helpStat.png")); // Load stats panel help
        } catch (IOException e) { // catch IOExceptions for loading images
            e.printStackTrace(); // Print where error is
        }
        if (intPage == 0) { // If page/tab is 0, then draw projectile type help image
            g.drawImage(imgTypes, 0, 0, this);
        } else if (intPage == 1) { // If page/tab is 1, then draw controlpanel type help image
            g.drawImage(imgControl, 0, 0, this);
        } else if (intPage == 2) { // If page/tab is 2, then draw statpanel type help image
            g.drawImage(imgStat, 0, 0, this);
        }
    }

    // CONSTRUCTORS
    public HelpPanel () { // Construct HelpPanel
        super(); // Super JPanel

    }
}
