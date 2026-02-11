// ======================= Sword.java =======================
// IMPORTANT: this makes the hitbox disappear when isAlive == false

import java.awt.*;

public class Sword {

    public int xpos;
    public int ypos;
    public int dx;
    public int dy;
    public int width;
    public int height;
    public boolean isAlive;
    public Rectangle hitbox;

    public Sword(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx = 5;
        dy = 5;
        width = 100;
        height = 100;
        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);
    }

    public void move() {
        if (isAlive) {

            // simple bounce movement (same style as Rock/Paper)
            if (xpos < 0) dx = -dx;
            if (ypos < 0) dy = -dy;

            if (xpos > 900 - width) dx = -dx;
            if (ypos > 600 - height) dy = -dy;

            xpos += dx;
            ypos += dy;

            hitbox = new Rectangle(xpos, ypos, width, height);

        } else {
            // hitbox goes away when dead
            hitbox = new Rectangle(0, 0, 0, 0);
        }
    }
}