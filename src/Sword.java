import java.awt.*;

public class Sword {

    public int xpos, ypos, dx, dy, width, height;
    public boolean isAlive;
    public Rectangle hitbox;

    public Sword(int x, int y) {
        xpos = x;
        ypos = y;
        dx = 0; // controlled by player
        dy = 0;
        width = 101;
        height = 100;
        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);
    }

    public void move() {

        if (isAlive) {

            xpos += dx;
            ypos += dy;

            // Keep inside screen
            if (xpos < 0) xpos = 0;
            if (xpos > 900) xpos = 900;

            if (ypos < 0) ypos = 0;
            if (ypos > 600) ypos = 600;

            hitbox = new Rectangle(xpos, ypos, width, height);

        } else {
            hitbox = new Rectangle(0,0,0,0);
        }
    }
}