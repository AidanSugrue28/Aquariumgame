import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class BasicGameApp implements Runnable, KeyListener, MouseListener {

    final int WIDTH = 1000;
    final int HEIGHT = 700;

    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    public Image rockPic, paperPic, scissorsPic, swordPic, backgroundPic, swordWinPic;

    public Rock[] rocks;
    public Paper[] papers;
    public Scissors[] scissors;

    public Sword sword;

    public int swordLives = 3;
    public final int FPS = 50;
    public int timer = 10 * FPS;

    public boolean gameOver = false;
    public boolean swordWin = false;

    Rectangle restartButton = new Rectangle(780, 20, 200, 100);

    public static void main(String[] args) {
        new Thread(new BasicGameApp()).start();
    }

    public BasicGameApp() {
        setUpGraphics(); //all images used

        rockPic = Toolkit.getDefaultToolkit().getImage("rock.jpg");
        paperPic = Toolkit.getDefaultToolkit().getImage("paper.png");
        scissorsPic = Toolkit.getDefaultToolkit().getImage("scissors.jpg");
        swordPic = Toolkit.getDefaultToolkit().getImage("sword.jpg");
        backgroundPic = Toolkit.getDefaultToolkit().getImage("Table.jpg");
        swordWinPic = Toolkit.getDefaultToolkit().getImage("SwordWinScreen.png");

        rocks = new Rock[5];   // arrays
        papers = new Paper[5];
        scissors = new Scissors[5];

        for (int i = 0; i < rocks.length; i++) {
            rocks[i] = new Rock((int)(Math.random()*900), (int)(Math.random()*600));
        }

        for (int i = 0; i < papers.length; i++) {
            papers[i] = new Paper((int)(Math.random()*900), (int)(Math.random()*600));
        }

        for (int i = 0; i < scissors.length; i++) {
            scissors[i] = new Scissors((int)(Math.random()*900), (int)(Math.random()*600));
        }

        sword = new Sword(400, 300);

        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
    }

    public void run() { //make it run
        while (true) {
            if (!gameOver) {
                moveThings();
                collisions();
                updateTimer();
            }
            render();
            pause(20);
        }
    }

    public void moveThings() { //make things move
        sword.move();

        for (Rock r : rocks) r.move();
        for (Paper p : papers) p.move();
        for (Scissors s : scissors) s.move();
    }

    public void collisions() { //what happens when things hit?

        for (Rock r : rocks) { //take away sword life
            if (r.isAlive && sword.hitbox.intersects(r.hitbox)) {
                r.isAlive = false;
                swordLives--;
                sword.gotHit();
            }
        }

        for (Paper p : papers) { //take away sword life
            if (p.isAlive && sword.hitbox.intersects(p.hitbox)) {
                p.isAlive = false;
                swordLives--;
                sword.gotHit();
            }
        }

        for (Scissors s : scissors) { //take away sword life
            if (s.isAlive && sword.hitbox.intersects(s.hitbox)) {
                s.isAlive = false;
                swordLives--;
                sword.gotHit();
            }
        }

        for (Rock r : rocks)
            for (Scissors s : scissors)
                if (r.isAlive && s.isAlive && r.hitbox.intersects(s.hitbox))
                    s.isAlive = false;

        for (Scissors s : scissors)
            for (Paper p : papers)
                if (s.isAlive && p.isAlive && s.hitbox.intersects(p.hitbox))
                    p.isAlive = false;

        for (Paper p : papers)
            for (Rock r : rocks)
                if (p.isAlive && r.isAlive && p.hitbox.intersects(r.hitbox))
                    r.isAlive = false;

        if (swordLives <= 0) { //what happens when sword dies?
            gameOver = true;  //game ends
            sword.isAlive = false; // sword dead
        }
    }

    public void updateTimer() { //timer for sword win
        timer--;

        if (timer <= 0) { //when timer hits 0 game ends and sword wins
            gameOver = true;
            swordWin = true;
        }
    }

    public void resetGame() { //set up the basics of the sword

        swordLives = 3; //lives
        timer = 10 * FPS; //timer
        gameOver = false;
        swordWin = false;

        sword = new Sword(400, 300); //make sword

        rocks = new Rock[5];
        papers = new Paper[5];
        scissors = new Scissors[5]; //more arrays

        for (int i = 0; i < rocks.length; i++) {
            rocks[i] = new Rock((int)(Math.random()*900), (int)(Math.random()*600));
        }

        for (int i = 0; i < papers.length; i++) {
            papers[i] = new Paper((int)(Math.random()*900), (int)(Math.random()*600));
        }

        for (int i = 0; i < scissors.length; i++) {
            scissors[i] = new Scissors((int)(Math.random()*900), (int)(Math.random()*600));
        }
    }

    private void setUpGraphics() { //graphics
        frame = new JFrame("Rock Paper Scissors");

        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
    }

    private void render() { //printing the images
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

        if (sword.isAlive)
            g.drawImage(swordPic, sword.xpos, sword.ypos, sword.width, sword.height, null);

        for (Rock r : rocks)
            if (r.isAlive)
                g.drawImage(rockPic, r.xpos, r.ypos, r.width, r.height, null);

        for (Paper p : papers)
            if (p.isAlive)
                g.drawImage(paperPic, p.xpos, p.ypos, p.width, p.height, null);

        for (Scissors s : scissors)
            if (s.isAlive)
                g.drawImage(scissorsPic, s.xpos, s.ypos, s.width, s.height, null);

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Lives: " + swordLives, 20, 40);
        g.drawString("Time: " + (timer / FPS), 20, 70);

        if (gameOver) { //win screens and losing screens
            if (swordWin) {
                g.drawImage(swordWinPic, 250, 200, 500, 200, null);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("GAME OVER", 330, 350);
            }

            g.setColor(Color.WHITE); //restart button
            g.fillRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);

            g.setColor(Color.BLACK);
            g.drawRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Click to Restart", restartButton.x + 40, restartButton.y + 50);
        }

        g.dispose();
        bufferStrategy.show();
    }

    public void keyPressed(KeyEvent e) { //control where the sword goes
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) sword.dy = -sword.speed;
        if (key == KeyEvent.VK_S) sword.dy = sword.speed;
        if (key == KeyEvent.VK_A) sword.dx = -sword.speed;
        if (key == KeyEvent.VK_D) sword.dx = sword.speed;
    }

    public void keyReleased(KeyEvent e) {
        sword.dx = 0;
        sword.dy = 0;
    }

    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) { //teleports to where you press and adds two seconds to time

        if (gameOver && restartButton.contains(e.getX(), e.getY())) {
            resetGame();
            return;
        }

        int mx = e.getX();
        int my = e.getY();

        sword.xpos = mx - sword.width / 2;
        sword.ypos = my - sword.height / 2;

        if (sword.xpos < 0) sword.xpos = 0;
        if (sword.xpos > WIDTH - sword.width) sword.xpos = WIDTH - sword.width;

        if (sword.ypos < 0) sword.ypos = 0;
        if (sword.ypos > HEIGHT - sword.height) sword.ypos = HEIGHT - sword.height;

        sword.dx = 0;
        sword.dy = 0;

        sword.hitbox = new Rectangle(sword.xpos, sword.ypos, sword.width, sword.height);

        timer += 2 * FPS; //add two seconds
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void pause(int time) {
        try { Thread.sleep(time); } catch (Exception e) {}
    }
}