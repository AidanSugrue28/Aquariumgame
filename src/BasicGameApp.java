// ======================= BasicGameApp.java =======================
// Basic Game Application
// Version 2
// Rock / Paper / Scissors / Sword with win screen + sword timer (starts at game start)
// Sword lasts 3 seconds, and every kill adds +1 second.

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicGameApp implements Runnable {

    // Window size
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    // Graphics + window stuff
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    // Images
    public Image rockPic;
    public Image paperPic;
    public Image scissorsPic;
    public Image swordPic;
    public Image backgroundPic;
    public Image rockWinPic;
    public Image paperWinPic;
    public Image scissorsWinPic;
    public Image swordWinPic;

    // Game state
    public boolean gameOver = false;
    public String winner   = "";

    // ===== Sword timer settings =====
    // You pause(20), so ~50 frames per second.
    public final int FRAMES_PER_SECOND = 50;
    public int swordTimer; // counts down in frames

    // Game objects
    public Rock rock1;
    public Paper paper1;
    public Scissors scissors1;
    public Sword sword1;

    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();
        new Thread(ex).start();
    }

    public BasicGameApp() {
        setUpGraphics();

        // Load pictures + create objects
        rockPic = Toolkit.getDefaultToolkit().getImage("rock.jpg");
        rock1 = new Rock((int)(Math.random()*700)+1,(int)(Math.random()*500)+1);

        paperPic = Toolkit.getDefaultToolkit().getImage("paper.png");
        paper1 = new Paper((int)(Math.random()*700)+1,(int)(Math.random()*500)+1);

        scissorsPic = Toolkit.getDefaultToolkit().getImage("scissors.jpg");
        scissors1 = new Scissors((int)(Math.random()*700)+1,(int)(Math.random()*500)+1);

        swordPic = Toolkit.getDefaultToolkit().getImage("sword.jpg");
        sword1 = new Sword((int)(Math.random()*700)+1,(int)(Math.random()*500)+1);

        backgroundPic = Toolkit.getDefaultToolkit().getImage("Table.jpg");

        rockWinPic = Toolkit.getDefaultToolkit().getImage("RockWinScreen.png");
        paperWinPic = Toolkit.getDefaultToolkit().getImage("PaperWinScreen.png");
        scissorsWinPic = Toolkit.getDefaultToolkit().getImage("ScissorsWinScreen.png");
        swordWinPic = Toolkit.getDefaultToolkit().getImage("SwordWinScreen.png");

        // Sword timer starts immediately when game starts:
        swordTimer = 3 * FRAMES_PER_SECOND; // 3 seconds
    }

    public void run() {
        while (true) {
            moveThings();
            render();
            pause(20);
        }
    }

    public void moveThings() {
        if (!gameOver) {

            // Move characters
            rock1.move();
            paper1.move();
            scissors1.move();
            sword1.move();

            // Sword timer countdown (starts at game start)
            if (sword1.isAlive) {
                swordTimer--;
                if (swordTimer <= 0) {
                    sword1.isAlive = false; // stops drawing; hitbox should go away in Sword.move()
                }
            }

            // Collisions
            crashing();
        }
    }

    public void crashing() {
        // Rock / Paper / Scissors interactions
        if (paper1.hitbox.intersects(rock1.hitbox) && rock1.isAlive == true) {
            System.out.println("Rock/paper hit");
            rock1.isAlive = false;
            paper1.isAlive = true;
            paper1.height = 150;
            paper1.width = 150;
            paper1.dx = 20;
            paper1.dy = 20;
        }

        if (rock1.hitbox.intersects(scissors1.hitbox) && scissors1.isAlive == true) {
            System.out.println("Rock/scissors hit");
            scissors1.isAlive = false;
            rock1.isAlive = true;
            rock1.height = 150;
            rock1.width = 150;
            rock1.dx = 2;
            rock1.dy = 2;
        }

        if (scissors1.hitbox.intersects(paper1.hitbox) && paper1.isAlive == true) {
            System.out.println("scissors/paper hit");
            paper1.isAlive = false;
            scissors1.isAlive = true;
            scissors1.height = 150;
            scissors1.width = 150;
            scissors1.dx = 30;
            scissors1.dy = 30;
        }

        // Sword kills + each kill adds +1 second to sword lifetime
        if (sword1.hitbox.intersects(rock1.hitbox) && rock1.isAlive == true && sword1.isAlive == true) {
            System.out.println("sword/rock hit");
            rock1.isAlive = false;

            swordTimer += 1 * FRAMES_PER_SECOND; // +1 second per kill

            sword1.height = 150;
            sword1.width = 150;
            sword1.dx = 15;
            sword1.dy = 15;
        }

        if (sword1.hitbox.intersects(scissors1.hitbox) && scissors1.isAlive == true && sword1.isAlive == true) {
            System.out.println("sword/scissors hit");
            scissors1.isAlive = false;

            swordTimer += 1 * FRAMES_PER_SECOND; // +1 second per kill

            sword1.height = 150;
            sword1.width = 150;
            sword1.dx = 15;
            sword1.dy = 15;
        }

        if (sword1.hitbox.intersects(paper1.hitbox) && paper1.isAlive == true && sword1.isAlive == true) {
            System.out.println("sword/paper hit");
            paper1.isAlive = false;

            swordTimer += 1 * FRAMES_PER_SECOND; // +1 second per kill

            sword1.height = 150;
            sword1.width = 150;
            sword1.dx = 15;
            sword1.dy = 15;
        }

        // Win condition
        int aliveCount = 0;
        if (rock1.isAlive) aliveCount++;
        if (paper1.isAlive) aliveCount++;
        if (scissors1.isAlive) aliveCount++;
        if (sword1.isAlive) aliveCount++;

        if (aliveCount == 1) {
            gameOver = true;
            if (rock1.isAlive) winner = "ROCK";
            if (paper1.isAlive) winner = "PAPER";
            if (scissors1.isAlive) winner = "SCISSORS";
            if (sword1.isAlive) winner = "SWORD";
        }
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    private void setUpGraphics() {
        frame = new JFrame("Application Template");

        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();

        System.out.println("DONE graphic setup");
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Background
        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

        // Draw characters if alive
        if (rock1.isAlive == true) {
            g.drawImage(rockPic, rock1.xpos, rock1.ypos, rock1.width, rock1.height, null);
        }
        if (paper1.isAlive == true) {
            g.drawImage(paperPic, paper1.xpos, paper1.ypos, paper1.width, paper1.height, null);
        }
        if (scissors1.isAlive == true) {
            g.drawImage(scissorsPic, scissors1.xpos, scissors1.ypos, scissors1.width, scissors1.height, null);
        }
        if (sword1.isAlive == true) {
            g.drawImage(swordPic, sword1.xpos, sword1.ypos, sword1.width, sword1.height, null);
        }

        // Optional: show sword time left so teacher can SEE it working
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int secondsLeft = Math.max(0, swordTimer / FRAMES_PER_SECOND);
        g.drawString("Sword time: " + secondsLeft, 20, 40);

        // Win screen images
        if (gameOver) {
            if (winner.equals("ROCK")) {
                g.drawImage(rockWinPic, 250, 200, 500, 200, null);
            }
            if (winner.equals("PAPER")) {
                g.drawImage(paperWinPic, 250, 200, 500, 200, null);
            }
            if (winner.equals("SCISSORS")) {
                g.drawImage(scissorsWinPic, 250, 200, 500, 200, null);
            }
            if (winner.equals("SWORD")) {
                g.drawImage(swordWinPic, 250, 200, 500, 200, null);
            }
        }

        g.dispose();
        bufferStrategy.show();
    }
}