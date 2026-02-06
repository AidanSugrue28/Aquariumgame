import java.awt.*;
public class Scissors {

    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use.
    public String name;                //holds the name of the hero
    public int xpos;                //the x position
    public int ypos;                //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;
    public Rectangle hitbox;      //a boolean to denote if the hero is alive or dead.
    //a boolean to denote if the hero is alive or dead.


    // METHOD DEFINITION SECTION

    // Constructor Definition
    // A constructor builds the object when called and sets variable values.


    //This is a SECOND constructor that takes 3 parameters.  This allows us to specify the hero's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.
    public Scissors(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx = 5;
        dy = 5;
        width = 100;
        height = 100;
        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);


    }// constructor

    //The move method.  Everytime this is run (or "called") it wraps and goes back to the opposite side
    public void move() {

        if (isAlive) {

            xpos += dx;
            ypos += dy;

            // wrap horizontally
            if (xpos < -width) {
                xpos = 900;
            }
            if (xpos > 900) {
                xpos = -width;
            }

            // wrap vertically
            if (ypos < -height) {
                ypos = 600;
            }
            if (ypos > 600) {
                ypos = -height;
            }

            hitbox = new Rectangle(xpos, ypos, width, height);

        } else {
            hitbox = new Rectangle(0, 0, 0, 0);
        }
    }
}



