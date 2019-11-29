/* -----------------------------------------------
 *
 * 	Program Name: Projectile Motion Simulator
 *	Creator: Henry Wong
 *
 *	Version: 1.0
 *	November 29, 2019
 *
 *	Course Code: ICS4U1
 *	Mr. Cadawas
 *
 * ------------------------------------------------ */

import javax.swing.*;

public class ProjectileMotion {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Modern Look&Feel UI
        } catch (Exception e) {
            e.printStackTrace(); // Print where error is
        }
        new GUI(); // Open the projectile motion gui
    }
}
