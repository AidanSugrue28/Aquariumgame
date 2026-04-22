import java.awt.*;

public class Sword {

    public int xpos, ypos, dx, dy, width, height;
    public boolean isAlive;
    public Rectangle hitbox;


    public int speed = 3; // slower starting speed

    public Sword(int x, int y) {
        xpos = x;
        ypos = y;
        dx = 0;
        dy = 0;

        width = 90;   // bigger starting size
        height = 90;

        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);
    }

    public void move() {

        if (isAlive) {

            xpos += dx;
            ypos += dy;

            // Keep inside screen
            if (xpos < 0) xpos = 0;
            if (xpos > 1000 - width) xpos = 1000 - width;

            if (ypos < 0) ypos = 0;
            if (ypos > 700 - height) ypos = 700 - height;

            hitbox = new Rectangle(xpos, ypos, width, height);

        } else {
            hitbox = new Rectangle(0,0,0,0);
        }
    }

    // called when sword gets hit
    public void gotHit() {
        width -= 10;
        height -= 10;


        if (width < 20) width = 20;
        if (height < 20) height = 20;

        speed += 1; // gets faster every hit
    }
}