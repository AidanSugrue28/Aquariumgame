// ======================= BasicGameApp.java =======================
// Rock Paper Scissors FINAL GAME
// - Sword is player controlled (WASD)
// - Mouse deletes enemies
// - 3 lives system
// - 10 second survival win condition
// - Arrays + collisions + full interaction

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

    // Images
    public Image rockPic, paperPic, scissorsPic, swordPic, backgroundPic, swordWinPic;

    // ARRAYS
    public Rock[] rocks = new Rock[5];
    public Paper[] papers = new Paper[5];
    public Scissors[] scissors = new Scissors[5];

    // PLAYER
    public Sword sword;

    // GAME STATE
    public int swordLives = 3;
    public final int FPS = 50;
    public int timer = 10 * FPS;

    public boolean gameOver = false;
    public boolean swordWin = false;

    public static void main(String[] args) {
        new Thread(new BasicGameApp()).start();
    }

    public BasicGameApp() {
        setUpGraphics();

        // Load images
        rockPic = Toolkit.getDefaultToolkit().getImage("rock.jpg");
        paperPic = Toolkit.getDefaultToolkit().getImage("paper.png");
        scissorsPic = Toolkit.getDefaultToolkit().getImage("scissors.jpg");
        swordPic = Toolkit.getDefaultToolkit().getImage("sword.jpg");
        backgroundPic = Toolkit.getDefaultToolkit().getImage("Table.jpg");
        swordWinPic = Toolkit.getDefaultToolkit().getImage("SwordWinScreen.png");

        // Create enemies
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

    public void run() {
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

    // ================= MOVE =================
    public void moveThings() {
        sword.move();

        for (Rock r : rocks) r.move();
        for (Paper p : papers) p.move();
        for (Scissors s : scissors) s.move();
    }

    // ================= COLLISIONS =================
    public void collisions() {

        // Sword loses lives on contact
        for (Rock r : rocks) {
            if (r.isAlive && sword.hitbox.intersects(r.hitbox)) {
                r.isAlive = false;
                swordLives--;
            }
        }

        for (Paper p : papers) {
            if (p.isAlive && sword.hitbox.intersects(p.hitbox)) {
                p.isAlive = false;
                swordLives--;
            }
        }

        for (Scissors s : scissors) {
            if (s.isAlive && sword.hitbox.intersects(s.hitbox)) {
                s.isAlive = false;
                swordLives--;
            }
        }

        // RPS logic
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

        // Lose condition
        if (swordLives <= 0) {
            gameOver = true;
            sword.isAlive = false;
        }
    }

    // ================= TIMER =================
    public void updateTimer() {
        timer--;

        if (timer <= 0) {
            gameOver = true;
            swordWin = true;
        }
    }

    // ================= GRAPHICS =================
    private void setUpGraphics() {
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

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

        // Sword
        if (sword.isAlive)
            g.drawImage(swordPic, sword.xpos, sword.ypos, sword.width, sword.height, null);

        // Enemies
        for (Rock r : rocks)
            if (r.isAlive)
                g.drawImage(rockPic, r.xpos, r.ypos, r.width, r.height, null);

        for (Paper p : papers)
            if (p.isAlive)
                g.drawImage(paperPic, p.xpos, p.ypos, p.width, p.height, null);

        for (Scissors s : scissors)
            if (s.isAlive)
                g.drawImage(scissorsPic, s.xpos, s.ypos, s.width, s.height, null);

        // UI
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Lives: " + swordLives, 20, 40);
        g.drawString("Time: " + (timer / FPS), 20, 70);

        // END SCREEN
        if (gameOver) {
            if (swordWin) {
                g.drawImage(swordWinPic, 250, 200, 500, 200, null);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("GAME OVER", 331, 350);
            }
        }

        g.dispose();
        bufferStrategy.show();
    }

    // ================= INPUT =================
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) sword.dy = -5;
        if (key == KeyEvent.VK_S) sword.dy = 5;
        if (key == KeyEvent.VK_A) sword.dx = -5;
        if (key == KeyEvent.VK_D) sword.dx = 5;
    }

    public void keyReleased(KeyEvent e) {
        sword.dx = 0;
        sword.dy = 0;
    }

    public void keyTyped(KeyEvent e) {}

    // Mouse deletes enemies
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        for (Rock r : rocks)
            if (r.hitbox.contains(mx, my))
                r.isAlive = false;

        for (Paper p : papers)
            if (p.hitbox.contains(mx, my))
                p.isAlive = false;

        for (Scissors s : scissors)
            if (s.hitbox.contains(mx, my))
                s.isAlive = false;
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void pause(int time) {
        try { Thread.sleep(time); } catch (Exception e) {}
    }
}