import javax.swing.*;
import java.awt.*;

public class ProjPanel extends JPanel {
    // PROPERTIES
    // Projectile/Ball Coordinates
    public int intBallX = 85;
    public int intBallY = 340;

    public boolean blnGrid = false; // default false

    // METHODS
    public void paintComponent(Graphics g) { // Override JPanel paintComponent method
        Graphics2D g2 = (Graphics2D)g; // Use Graphics2D instead of regular Graphics
        super.paintComponent(g2); // Clear previous drawings (Windows only); super JPanel (original) paintComponent method

        // Draw Sidebar
        g2.setColor(new Color(240,240,240)); // Default JComponent colour
        g2.fillRect(0,0,83,360); // Fill sidebar colour

        // Draw Border Lines
        g2.setStroke(new BasicStroke(2)); // Set line thickness to 2px
        g2.setColor(Color.BLACK);
        g2.drawLine(84,0,84,360); // Draw side border
        g2.drawLine(0,360,960,360); // Draw bottom border

        // Grid
        g2.setStroke(new BasicStroke(1)); // Set line thickness to 1px
        g2.setColor(Color.DARK_GRAY);
        if (blnGrid == true) { // Draw full grid; only draws if "Enable Checkbox" is enabled (which sets boolean to true)
           for (int i = 0; i < 18; i++) { // Loop 18 times
               g2.drawLine(85+10+(i*50), 0, 85+10+(i*50), 360); // Vertical lines
               g2.drawLine(85, (i * 50), 960, (i * 50)); // Horizontal lines
               g2.drawString(i*50 + "",85+11+(i*50), 349); // Vertical text
               g2.drawString(i*50 + "",96,349-(i*50)); // Horizontal text
           }
        } else { // Draw small grid ticks
            for (int j = 0; j < 18; j++) { // Loop 18 times
                g2.drawLine(85+10+(j*50), 354, 85+10+(j*50), 360); // Vertical ticks
                g2.drawLine(85, (j*50), 90, (j*50)); // Horizontal ticks
                g2.drawString(j*50 + "",85+10+(j*50), 349); // Vertical text
                g2.drawString(j*50 + "",95,349-(j*50)); // Horizontal text
            }
        }

        // Draw Ball/Projectile
        g2.setColor(Color.RED);
        g2.fillOval(intBallX, intBallY, 20, 20); // Dynamic position, size of ball is 20x20px

    }
    // CONSTRUCTORS
    public ProjPanel () { // Construct ProjPanel
        super(); // Super JPanel
        setBackground(Color.WHITE); // Set ProjPanel background to white
    }
}
