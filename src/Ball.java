public class Ball {
    // PROPERTIES
    // Velocity
    public static double dblVel1;
    public static double dblVel1X;
    public static double dblVel1Y;

    public static double dblVel;
    public static double dblVelY;
    public static double dblCurrentAngle;

    // Distance
    public static double dblDistX;
    public static double dblDistY;
    public static double dblDistYMax;

    public static double dblAcceleration;
    public static double dblGravity = 9.8; // 9.8m/s^2
    public static double dblTime;
    public static double dblAngle;
    public double dblType;

    // Current Position Array
    private double[] dblCurrentPos = new double[2];

    // METHODS
    /*  Type 1 = Ideal projectile.projectile
            Type 2 = Straight off cliff
            Type 3 = Up from cliff
            Type 4 = Straight down from cliff
    */
    public void fillVariables(double dblType) { // Calculate missing variables of newly constructed projectile
        if (this.dblType == 1) { // Type 1 (Ideal Projectile, 1 --> 90 degrees)
            this.dblAcceleration = -dblGravity; // Type 1 acceleration is normally (+), but in simulation use (-)
            this.dblTime = (-2 * dblVel1Y) / dblAcceleration; // t = (-2*vy)/a
            this.dblDistX = dblVel1X * dblTime; // dx = v1x*t
        } else if (this.dblType == 2) { // Type 2 (Straight off cliff, 0 degrees)
            this.dblDistY = -this.dblDistY;  // Type 2 dy is normally (+) but in simulation use (-)
            this.dblAcceleration = -dblGravity; // Type 2 acceleration is normally (+), but in simulation use (-)
            this.dblTime = Math.sqrt(dblDistY / (dblAcceleration/2)); // t = sqrt[dy/(a/2)]
            this.dblDistX = dblVel1X * dblTime; // dx = v1x*t
        } else if (this.dblType == 3 || this.dblType == 4) { // Type 3 (Up off cliff, 1 --> 90degrees) & Type 4 (Straight down off cliff, -1 --> -90 degrees)
            this.dblDistY = -this.dblDistY; // Type 3 dy is (-), Type 4 dy is normally (+) but in simulation use (-)
            this.dblAcceleration = -dblGravity; // Type 3 acceleration is (-), Type 4 acceleration is normally (+) but in simulation use (-)
            this.dblTime = (-dblVel1Y-Math.sqrt(Math.pow(dblVel1Y,2) - (4 * (this.dblAcceleration/2) * (-this.dblDistY)))) / (2 * (dblAcceleration/2)); // Quadratic equation to solve for t
            this.dblDistX = dblVel1X * dblTime; // dx = v1x*t
        } else {
            System.out.println("Error! Incorrect type specified!");
        }
    }
    public double[] getCurrentPos(double dblCurrentTime) { // Method for launching projectile; calculates exact position based on time provided
        this.dblCurrentPos[0] = this.dblVel1X * dblCurrentTime; // x value. dx = vx * current time
        this.dblCurrentPos[1] = -this.dblDistY + (this.dblVel1Y * dblCurrentTime) + ((this.dblAcceleration * Math.pow(dblCurrentTime, 2)) / 2); // y value. dy = v1yt + (at^2/2)
        calcStats(dblCurrentPos[0], dblCurrentPos[1], dblCurrentTime); // update all static variables

        return (this.dblCurrentPos); // return x&y coords in array
    }
    public void calcStats(double dblCurrentX, double dblCurrentY, double dblCurrentTime) { // Updates static variables for the stats panel to use
        if (this.dblType == 2 || this.dblType == 4) { // Type 2 & 4 don't go any higher than launch pos
            this.dblDistYMax = -this.dblDistY;
        } else {
            this.dblDistYMax = (Math.pow(this.dblVel1Y, 2) / (2 * this.dblGravity)) + -this.dblDistY;
        }
        //this.dblVelX = this.dblCurrentPos[0] / dblCurrentTime; // Don't need since vx remains constant throughout projectile motion
        this.dblVelY = (this.dblCurrentPos[1] / dblCurrentTime) - (4.9 * dblCurrentTime);
        this.dblVel = Math.sqrt(Math.pow(this.dblVel1X, 2) + Math.pow(this.dblVelY, 2));
        this.dblCurrentAngle = Math.toDegrees(Math.asin((this.dblVelY / this.dblVel))); //asin is inverse sine

        if (this.dblVel > this.dblVel1 && this.dblDistY == 0) { // Ideal projectile; set variables to exact values if past actual end velocity due to 48fps
            this.dblVel = this.dblVel1;
            this.dblVelY = -1 * this.dblVel1Y;
            this.dblCurrentAngle = this.dblAngle;
        }
    }
    // CONSTRUCTOR
    public Ball(double dblV1, double dblAng, double dblHeight) { // Ball constructor requires V1, Angle, & Height
        // Set local variables from constructor variables
        this.dblVel1 = dblV1;
        this.dblVel1X = dblV1 * Math.cos(Math.toRadians(dblAng));
        this.dblVel1Y = dblV1 * Math.sin(Math.toRadians(dblAng));

        this.dblDistY = dblHeight; // Starting height
        this.dblAngle = dblAng;

        // Determine projectile type
        if (this.dblDistY == 0) { // Height = 0, then Type 1
            this.dblType = 1;
        } else { // Height != 0, then Type 2-4
            if (this.dblAngle == 0) { // Type 2
                this.dblType = 2;
            } else if (this.dblAngle > 0 && this.dblAngle <= 90) { // Type 3
                this.dblType = 3;
            } else if (this.dblAngle < 0 && this.dblAngle >= -90) { // Type 4
                this.dblType = 4;
            }
        }
        fillVariables(this.dblType); // Calculate missing variables of projectile
    }
}
