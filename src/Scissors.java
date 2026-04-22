import java.awt.*;
public class Scissors {


    public String name;
    public int xpos;
    public int ypos;
    public int dx;
    public int dy;
    public int width;
    public int height;
    public boolean isAlive;
    public Rectangle hitbox;

    public Scissors(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx = 3;
        dy = 3;
        width = 75;
        height = 75;
        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);


    }// constructor


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



