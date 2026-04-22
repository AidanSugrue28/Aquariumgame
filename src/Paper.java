import java.awt.*;
public class Paper {


    public String name;
    public int xpos;
    public int ypos;
    public int dx;
    public int dy;
    public int width;
    public int height;
    public boolean isAlive;
    public Rectangle hitbox;








    public Paper(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx =3;
        dy =3;
        width = 75;
        height = 75;
        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);


    }// constructor

    //The move method
    public void move() {

        if (isAlive) {

            if (xpos < 0) { // bounce off left wall
                dx = -dx;
            }
            if (ypos < 0) {
                dy = -dy;
            }

            if (xpos > 900 - width) {
                dx = -dx;
            }
            if (ypos > 600 - height) {
                dy = -dy;
            }

            xpos = xpos + dx;
            ypos = ypos + dy;

            hitbox = new Rectangle(xpos, ypos, width, height);

        } else {

            hitbox = new Rectangle(0, 0, 0, 0);

        }
    }

}
