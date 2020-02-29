import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener, ChangeListener {
    // PROPERTIES
    // Frame
    JFrame theframe = new JFrame("Projectile Motion"); // Create JFrame for program, titled "Projectile Motion"

    // Panels
    JPanel mainPanel; // Main JPanel; contains all other panels
    JSplitPane splitPaneV; // Splits panels horizontally (so there is a panel on top & bottom)
    JSplitPane splitPaneH; // Splits panels vertically (so there is a panel on left & right side)

    // ProjPanel
    ProjPanel projPanel; // Projectile Animation Panel
    JSlider sliderHeight; // Height slider
    JLabel labelHeight; // Height label
    JLabel labelTimer; // Timer label
    JTextField fieldTimer; // Projectile Timer

    // ControlPanel
    ControlPanel controlPanel; // Panel to hold initial velocity, angle, timer, launch & reset button
    JSlider sliderV1; // Initial velocity (v1) slider
    JSlider sliderAngle; // Angle slider
    JLabel labelV1; // Initial velocity label
    JLabel labelAngle; // Angle label
    JButton butLaunch; // Launch button
    JButton butReset; // Reset button
    JLabel labelGridlines; // Gridlines label
    JCheckBox boxGridlines; // Gridlines button
    JLabel labelProjType; // Projectile type label (under pic of projectile)
    boolean blnLaunch = false; // Boolean to enable launch code; default false

    // StatPanel
    JPanel statPanel; // Stats, variables, etc Panel
    JButton butPosition; // Opens projectile's position stats
    JButton butVelocity; // Opens projectile's position stats
    JButton butAcceleration; // Opens projectile's acceleration stats
    int intStatPanel; // Integer to determine which stats to load. 0=position, 1=velocity
    JTextArea txtStats; // Text box for all stats (position/velocity/acceleration

    // HelpPanel
    HelpPanel helpPanel; // Panel to hold all help menus
    JTabbedPane helpPane = new JTabbedPane(); // Tab menu for different help panes
    boolean blnHelp = false; // Boolean to enable help screen; default false
    JButton butHelpExit; // Button to return back to simulator

    // Menu bar
    JMenuBar thebar; // Top Menu bar
    JMenu menuFile = new JMenu("File"); // Menu for  "File"
    JMenu menuHelp = new JMenu("Help"); // Menu for  "File"
    JMenuItem itemHelp = new JMenuItem("Help"); // Menu option under File to open help page
    JMenuItem itemAbout = new JMenuItem("About"); // Menu option under File to open about page
    JMenuItem itemExit = new JMenuItem("Exit"); // Menu option under File exit program

    // Timers
    Timer thetimer = new Timer(1000/48, this); // Timer to animate at 48fps
    long longStartTime; // Start timer for measuring projectile airtime
    long longElapsedTime; // Check timer for elapsed projectile airtime
    double dblElapsedSeconds; // Convert ElapsedTime to seconds

    // Local Projectile Variables
    double[] dblCurrentPos; // Array to store the current X&Y values of projectile
    double dblHeight; // Stores height slider value
    double dblV1; // Stores velocity slider value
    double dblAngle; // Stores angle slider value

    // METHODS
    public void actionPerformed(ActionEvent evt) { // ActionListener method for ActionEvents
        if (evt.getSource() == thetimer) { // Timer ActionEvent triggered
           if (blnLaunch == true) { // Launch loop
               // Initialize projectile
               dblV1 = this.sliderV1.getValue();  // Get velocity slider value
               dblAngle = this.sliderAngle.getValue(); // get angle slider value
               Ball projectile = new Ball(dblV1, dblAngle, dblHeight); // Construct Ball object called projectile using V1, Angle, & Height.

               // Get Time & Position of Projectile
               longElapsedTime = System.currentTimeMillis() - longStartTime; // Gets time elapsed since launch
               dblElapsedSeconds = longElapsedTime / 1000.0000; // Converts milliseconds to seconds
               dblCurrentPos = projectile.getCurrentPos(dblElapsedSeconds); // Get array of x & y values of current position

               // Debug code for projectile position
               /*System.out.println("Current time: " + dblElapsedSeconds);
               System.out.println("Ball current posX: " + dblCurrentPos[0]);
               System.out.println("Ball current posY: " + dblCurrentPos[1]);*/

               // Check if ball lands
               if (dblCurrentPos[1] <= 0) { // If projectile lands (or goes under y=0), needs to set currentPosX, currentPoxY, & timer to exact value (Since 48fps can skip over exact values)
                   dblCurrentPos[0] = Ball.dblDistX; // Set X-axis to exact range
                   dblCurrentPos[1] = 0; // Set Y-axis to exactly 0
                   dblElapsedSeconds = Math.floor(Ball.dblTime * 1000)/1000; // Keeps double value to only 3 decimal places
                   blnLaunch = false; // Disable launch loop
               }
               this.fieldTimer.setText(dblElapsedSeconds + "s"); // Update sidebar live timer

               // Draw ball in updated/current position
               this.projPanel.intBallX = 85 + (int)(dblCurrentPos[0]);
               this.projPanel.intBallY = 340 - (int)(dblCurrentPos[1]);

               loadStatPanel(); // Update stats panels
           }
        } else if (evt.getSource() == butLaunch) {  // Launch button pressed
            this.blnLaunch = true; // Enable launch loop
            this.butLaunch.setEnabled(false); // Disable launch button
            this.sliderHeight.setEnabled(false); // Disable height slider
            this.sliderV1.setEnabled(false); // Disable V1 slider
            this.sliderAngle.setEnabled(false); // Disable angle slider

            longStartTime = System.currentTimeMillis(); // Get start time for projectile
        } else if (evt.getSource() == butReset) { // Reset button pressed
            blnLaunch = false; // Disable launch loop
            this.butLaunch.setEnabled(true); // Enable launch button
            this.sliderHeight.setEnabled(true); // Enable height slider
            this.sliderV1.setEnabled(true); // Enable V1 slider
            this.sliderAngle.setEnabled(true); // Enable angle slider

            // Reset projectile position to (0,0)
            this.projPanel.intBallX = 85;
            this.projPanel.intBallY = 340 - (int)(dblHeight);

            this.fieldTimer.setText("0.000s"); // Reset timer
            this.fieldTimer.setFont(fieldTimer.getFont().deriveFont(Font.BOLD, 14f)); // Bold timer font
        } else if (evt.getSource() == boxGridlines) { // Gridlines checkbox pressed
            this.projPanel.blnGrid = !this.projPanel.blnGrid; // Changes boolean to opposite value
        } else if (evt.getSource() == butPosition) { // Position stats button pressed
            intStatPanel = 0; // Set panel to 0 (position)
            loadStatPanel(); // Reload stat panel
        } else if (evt.getSource() == butVelocity) { // Velocity stats button pressed
            intStatPanel = 1; // Set panel to 1 (velocity)
            loadStatPanel(); // Reload stat panel
        } else if (evt.getSource() == butAcceleration) { // Acceleration stats button pressed
            intStatPanel = 2; // Set panel to 1 (velocity)
            loadStatPanel(); // Reload stat panel
        } else if (evt.getSource() == itemAbout) { // About dropdown button pressed; open about page
            JOptionPane.showMessageDialog(theframe, "Program: Projectile Motion Simulator\nAuthor: Henry Wong" +
                    "\n\nVersion 1.0\nNovember 19, 2019\n\nSt. Augustine CHS Computer Science", "About This Program", JOptionPane.INFORMATION_MESSAGE); // Opens dialog to display 'About' info
        } else if (evt.getSource() == itemExit) { // Exit dropdown button pressed; exit program
            System.exit(0); // Exit program (theframe.dispose() was still running program in background)
        } else if (evt.getSource() == itemHelp) { // Help dropdown button pressed; open help page
            this.theframe.setContentPane(this.helpPanel); // Change frame to help panel
            this.theframe.pack(); // Resize panel
            blnHelp = true; // Enable help screen boolean check
        }

        if (blnHelp == true) { // Run if help page is open
            this.helpPanel.intPage = this.helpPane.getSelectedIndex(); // Set page # (0-2, draws image based on page #)
            if (evt.getSource() == butHelpExit) { // Close help page
                theframe.setContentPane(this.mainPanel); // Reset frame to main panel
                theframe.pack(); // Resize panel
                blnHelp = false; // Disable help screen boolean check; end help screen
            }
            this.helpPanel.repaint(); // Repaint HelpPanel
        }
        this.projPanel.repaint(); // Repaint projectile panel
    }
    public void stateChanged(ChangeEvent evt) { // ChangeListener method for ChangeEvents (sliders). Separated the if statements otherwise V1 would (only) get set when sliderAngle was created
        if (evt.getSource() == this.sliderHeight) { // Height slider moved
            dblHeight = sliderHeight.getValue(); // Get height slider value
            this.projPanel.intBallY = 340 - (int)(dblHeight); // Redraw projectile to match height slider
            this.labelHeight.setText("Height (" + dblHeight + "m):"); // Update height label
        }
        if (evt.getSource() == this.sliderV1) { // V1 slider moved
            dblV1 = sliderV1.getValue(); // Get V1 slider value
            this.labelV1.setText("Initial Velocity (" + dblV1 + "m/s):"); // Update initial velocity label
        }
        if (evt.getSource() == this.sliderAngle) { // ChangeEvent to angle slider
            dblAngle = this.sliderAngle.getValue(); // Get angle slider value
            this.labelAngle.setText("Angle (" + dblAngle + "\u00B0):"); // Update angle label
            if(dblHeight == 0 && dblAngle <= 0) { // If Type 1, prevent user from going past 1 degrees
                dblAngle = 1; // Set angle to 1
                this.sliderAngle.setValue(1);
            }
        }
        /* Show Projectile Type (Diagram)
           Updates intProjType in ControlPanel to draw current projectile type
           Updates ProjType label to properly describe current projectile type
           Repaints control panel to update drawn image */
        if (dblHeight == 0) { // Type 1
            this.controlPanel.intProjType = 1;
            this.labelProjType.setText("Projectile Type: 1");
            this.controlPanel.repaint();
        } else { // Types 1-3
            if (dblAngle == 0) { // Type 2
                this.controlPanel.intProjType = 2;
                this.labelProjType.setText("Projectile Type: 2");
            } else if (dblAngle > 0 && dblAngle <= 90) { // Type 3
                this.controlPanel.intProjType = 3;
                this.labelProjType.setText("Projectile Type: 3");
            } else if (dblAngle < 0 && dblAngle >= -90) { // Type 4
                this.controlPanel.intProjType = 4;
                this.labelProjType.setText("Projectile Type: 4");
            }
            this.controlPanel.repaint();
        }
    }
    public void createHelpPanel() { // Help pane will switch between mainPanel JPanel when called
        this.helpPanel = new HelpPanel(); // Create new panel from HelpPanel
        this.helpPanel.setPreferredSize(new Dimension(960,540)); // Full screen
        this.helpPanel.setLayout(null); // Set LayoutManager to null

        // Add panes to HelpPanel
        this.helpPane.addTab("Projectile Types", null);
        this.helpPane.addTab("Control Panel", null);
        this.helpPane.addTab("Stats Panel", null);
        this.helpPane.setSize(960,25);
        this.helpPane.setLocation(0,0);
        this.helpPanel.add(this.helpPane); // Add tab panes to HelpPanel

        // Exit Button
        this.butHelpExit = new JButton("Return to Simulator");
        this.butHelpExit.addActionListener(this);
        this.helpPanel.add(this.butHelpExit); // Listen for ActionEvents (pressing button)
        this.butHelpExit.setSize(180,30);
        this.butHelpExit.setLocation(410, 500);

    }
    public void createProjPanel() { // Method to create ProjPanel & its JComponents
        this.projPanel = new ProjPanel(); // Create new panel from ProjPanel
        this.projPanel.setPreferredSize(new Dimension(960, 360)); // 2/3 of mainPanel height
        this.projPanel.setLayout(null); // Set LayoutManager to null

        // Height slider
        this.sliderHeight = new JSlider(JSlider.VERTICAL, 0, 250, 0); // Range 0-50, starts at 0
        dblHeight = this.sliderHeight.getValue(); // Get height slider value
        this.labelHeight = new JLabel("Height (" + dblHeight + "m):", SwingConstants.CENTER); // Height label
        this.sliderHeight.setPaintTicks(true); // Enable interval ticks
        this.sliderHeight.setPaintLabels(true); // Enable interval labels
        this.sliderHeight.setSnapToTicks(true); // Enable snapping to interval ticks
        this.sliderHeight.setMinorTickSpacing(10); // Spacing between small interval ticks is 10m
        this.sliderHeight.setMajorTickSpacing(50); // Spacing between big interval ticks is 50m
        this.sliderHeight.addChangeListener(this); // Listen for ChangeEvents
        this.projPanel.add(this.labelHeight); // Add height label to ProjPanel
        this.projPanel.add(this.sliderHeight); // Add height slider to ProjPanel
        this.labelHeight.setSize(86,30);
        this.labelHeight.setLocation(0,65);
        this.sliderHeight.setSize(50,260);
        this.sliderHeight.setLocation(15,94);

        // Projectile timer
        this.labelTimer = new JLabel("Timer:", SwingConstants.CENTER); // Live timer label
        this.fieldTimer = new JTextField("0.000s"); // Live timer
        this.fieldTimer.setFont(fieldTimer.getFont().deriveFont(Font.BOLD, 14f)); // Bold live timer font
        this.fieldTimer.setEditable(false); // Disable typing in timer
        this.projPanel.add(this.labelTimer); // Add timer label to ProjPanel
        this.projPanel.add(this.fieldTimer); // Add timer to ProjPanel
        this.labelTimer.setSize(83,30);
        this.labelTimer.setLocation(0,1);
        this.fieldTimer.setSize(83,40);
        this.fieldTimer.setLocation(0,25);
    }
    public void createControlPanel() { // Method to create ControlPanel & its JComponents
        this.controlPanel = new ControlPanel(); // Create new ControlPanel
        this.controlPanel.setPreferredSize(new Dimension(560,180)); // 1/2 of mainPanel width, 1/3 of height
        this.controlPanel.setLayout(null); // Set LayoutManager to null

        // Projectile Type Label
        this.labelProjType = new JLabel("Projectile Type: 1", SwingConstants.CENTER); // Default value 1
        this.controlPanel.add(labelProjType);
        this.labelProjType.setSize(128,15);
        this.labelProjType.setLocation(435,150);

        // Initial Velocity Slider
        this.sliderV1 = new JSlider(0, 100, 40); // Range of 0-100, starts at 15
        dblV1 = this.sliderV1.getValue();
        this.labelV1 = new JLabel("Initial Velocity (" + dblV1 + "m/s):", SwingConstants.CENTER); // Initial velocity label, center aligned
        this.sliderV1.setPaintTicks(true);
        this.sliderV1.setPaintLabels(true);
        this.sliderV1.setMinorTickSpacing(10);
        this.sliderV1.setMajorTickSpacing(20);
        //this.sliderV1.setSnapToTicks(true);
        this.sliderV1.addChangeListener(this);
        this.controlPanel.add(this.labelV1); // Add velocity label to controlPanel JPanel
        this.controlPanel.add(this.sliderV1); // Add velocity slider to controlPanel JPanel
        this.labelV1.setSize(170,15);
        this.labelV1.setLocation(40,10);
        this.sliderV1.setSize(250,50);
        this.sliderV1.setLocation(5,30);

        // Angle Slider
        this.sliderAngle = new JSlider(-90, 90, 45); // Range of -90-90degrees, starts at 45degrees
        dblAngle = this.sliderAngle.getValue();
        this.labelAngle = new JLabel("Angle (" + dblAngle + "\u00B0):", SwingConstants.CENTER); // Angle label, center aligned
        this.sliderAngle.setPaintTicks(true);
        this.sliderAngle.setPaintLabels(true);
        this.sliderAngle.setMinorTickSpacing(15);
        this.sliderAngle.setMajorTickSpacing(45);
        //this.sliderAngle.setSnapToTicks(true);
        this.sliderAngle.addChangeListener(this);
        this.controlPanel.add(this.labelAngle); // Add angle label controlPanel JPanel
        this.controlPanel.add(this.sliderAngle); // Add angle slider to controlPanel JPanel
        this.labelAngle.setSize(100,15);
        this.labelAngle.setLocation(83,105);
        this.sliderAngle.setSize(250,50);
        this.sliderAngle.setLocation(5,120);

        // Launch Button
        this.butLaunch = new JButton("Launch");
        this.butLaunch.addActionListener(this);
        this.controlPanel.add(this.butLaunch);
        this.butLaunch.setSize(80,30);
        this.butLaunch.setLocation(280+20,40);

        // Reset Button
        this.butReset = new JButton("Reset");
        this.butReset.addActionListener(this);
        this.controlPanel.add(this.butReset);
        this.butReset.setSize(80,30);
        this.butReset.setLocation(280+20,90);

        // Gridlines Checkbox
        this.boxGridlines = new JCheckBox();
        this.boxGridlines.addActionListener(this);
        this.labelGridlines = new JLabel("Enable Gridlines");
        this.controlPanel.add(this.labelGridlines);
        this.controlPanel.add(this.boxGridlines);
        this.boxGridlines.setSize(25,25);
        this.boxGridlines.setLocation(280,140-3);
        this.labelGridlines.setSize(110,15);
        this.labelGridlines.setLocation(280+23, 143);

        this.controlPanel.repaint();
    }
    public void createStatPanel() { // Method to create StatPanel & its JComponents
        this.statPanel = new JPanel(); // Create new JPanel for stats
        this.statPanel.setPreferredSize(new Dimension(400,180)); // 1/2 of mainPanel width, 1/3 of height
		this.statPanel.setLayout(null); // Disable LayoutManager

        // Position Button
        this.butPosition = new JButton("Position");
        this.statPanel.add(this.butPosition);
        this.butPosition.addActionListener(this);
        this.butPosition.setSize(110,30);
        this.butPosition.setLocation(20,0);

        // Velocity Button
        this.butVelocity = new JButton("Velocity");
        this.statPanel.add(this.butVelocity);
        this.butVelocity.addActionListener(this);
        this.butVelocity.setSize(110,30);
        this.butVelocity.setLocation(135,0);

        // Acceleration Button
        this.butAcceleration = new JButton("Acceleration");
        this.statPanel.add(this.butAcceleration);
        this.butAcceleration.addActionListener(this);
        this.butAcceleration.setSize(130,30);
        this.butAcceleration.setLocation(250,0);

        // TextArea to Print Stats
        this.txtStats = new JTextArea(10,40);
        this.txtStats.setEditable(false); // Disable user typing
        this.statPanel.add(this.txtStats);
        this.txtStats.setSize(370,140);
        this.txtStats.setLocation(15,35);
        
        intStatPanel = 0; // Default value, position stats
        loadStatPanel(); // Call method to determine which stats to show
    }
    public void loadStatPanel() { // Method to determine which stats to show in StatsPanel
        if (intStatPanel == 0) { // Default, calls method to load position stats
            loadStatPosition();
        } else if (intStatPanel == 1) { // Calls method to load velocity stats
            loadStatVelocity();
        } else if (intStatPanel == 2) { // Calls method to load acceleration stats
            loadStatAcceleration();
        }
    }
    public void loadStatPosition() { // Load Position Stats
        this.txtStats.setText(""); // Clear TextArea
        try {
            this.txtStats.append("x: " + Math.floor(dblCurrentPos[0]*1000)/1000 + "m"); // Print x-axis value; only show 3 decimal places
        } catch (NullPointerException e) { // If 1st launch hasn't occurred & currentPosX doesn't have a value
            this.txtStats.append("x: 0.0m");
        }
        try {
            this.txtStats.append("\ny: " + Math.floor(dblCurrentPos[1]*1000)/1000 + "m"); // Print y-axis value; only show 3 decimal places
        } catch (NullPointerException e) { // If 1st launch hasn't occurred & currentPosY doesn't have a value
            this.txtStats.append("\ny: 0.0m");
        }
        this.txtStats.append("\n\nRange: " + Math.floor(Ball.dblDistX*1000)/1000 + "m"); // Print range; only show 3 decimal places
        this.txtStats.append("\nMax Height: " + Math.floor(Ball.dblDistYMax*1000)/1000 + "m"); // Print max height; only show 3 decimal places
        this.txtStats.append("\n\nTime: " + Math.floor(Ball.dblTime*1000)/1000 + "s"); // Print time; only show 3 decimal places

    }
    public void loadStatVelocity() { // Load Velocity Stats
        this.txtStats.setText(""); // Clear TextArea
        this.txtStats.append("Magnitude of Velocity");
        this.txtStats.append("\n v: " + Math.floor(Ball.dblVel*10)/10 + "m/s"); // Print current velocity & angle; only show 3 decimal places
        this.txtStats.append("\n\nComponents");
        this.txtStats.append("\n vx = " + Math.floor(Ball.dblVel1X*10)/10 + "m/s"); // Vx remains constant throughout in projectile motion
        this.txtStats.append("\n vy = " + Math.floor(Ball.dblVelY*10)/10 + "m/s");
        this.txtStats.append("\n Angle: " + Math.floor(Ball.dblCurrentAngle*10/10) + "\u00B0");
    }
    public void loadStatAcceleration() {
        this.txtStats.setText(""); // Clear Text Area
        this.txtStats.append("\nAcceleration: " + Ball.dblAcceleration + "m/s^2");
    }

    // CONSTRUCTOR
    public GUI() { // Construct GUI object
        // Main panel to contain ProjPanel, ControlPanel, StatPanel
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout()); // Use BorderLayout for LayoutManager

        createProjPanel(); // Method to create ProjPanel & its JComponents
        createControlPanel(); // Method to create ControlPanel & its JComponents
        createStatPanel(); // Method to create StatPanel & its JComponents
        createHelpPanel(); // Preload help panel; method to create HelpPanel & its JComponents

        // Create split panes
        this.splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Split pane horizontally
        this.splitPaneH.setLeftComponent(this.controlPanel); // Left pane is control panel
        this.splitPaneH.setRightComponent(this.statPanel); // Right pane is stats panel

        this.splitPaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Split pane vertically
        this.mainPanel.add(splitPaneV, BorderLayout.CENTER); // Add vertical split pane to mainPanel
        this.splitPaneV.setLeftComponent(this.projPanel); // Top pane is projectile panel
        this.splitPaneV.setRightComponent(this.splitPaneH); // Bottom pane is control & stat panels

        this.splitPaneH.setDividerSize(0); // Prevent resizing
        this.splitPaneV.setDividerSize(0); // Prevent resizing
       
        // Top Menu Bar
        this.thebar = new JMenuBar(); // Create new menu bar
        this.mainPanel.add(this.thebar, BorderLayout.NORTH); // Add menubar to top of mainPanel
        this.thebar.add(this.menuFile); // File dropdown in menu bar
        this.thebar.add(this.menuHelp); // Help dropdown in menu bar
        this.menuFile.add(this.itemAbout);  // About button in file dropdown
        this.menuFile.add(this.itemExit); // Exit button in file dropdown
        this.menuHelp.add(this.itemHelp); // Help button in file dropdown
        this.itemAbout.addActionListener(this); // Listen for ActionEvent on About Button
        this.itemExit.addActionListener(this); // Listen for ActionEvent on Exit Button
        this.itemHelp.addActionListener(this); // Listen for ActionEvent on Help Button

        // JFrame
        this.theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program on close button
        this.theframe.setContentPane(mainPanel); // Set content of the frame to the mainPanel JPanel
        this.theframe.pack(); // Adjust frame size to mainPanel dimensions
        this.theframe.setVisible(true); // Make frame visible
        this.theframe.setResizable(false); // Disable panel resizing
        this.theframe.setLocationRelativeTo(null); // Open frame in center of screen

        this.thetimer.start(); // Start timer for 48fps animation
    }
}
