//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

public class BasicGameApp implements Runnable {

   //Variable Definition Section
   //Declare the variables used in the program 
   //You can set their initial values too
   
   //Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

   //Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
   public JPanel panel;
   
	public BufferStrategy bufferStrategy;
	public Image rockPic;
    public Image paperPic;
    public Image scissorsPic;
    public Image backgroundPic;
    public Image rockWinPic;
    public Image paperWinPic;
    public Image scissorsWinPic;
    public boolean gameOver = false;
    public String winner  = "";


    //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	public Rock rock1;
    public Paper paper1;
    public Scissors scissors1;



   // Main method definition
   // This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method  
	}


   // Constructor Method
   // This has the same name as the class
   // This section is the setup portion of the program
   // Initialize your variables and construct your program objects here.
	public BasicGameApp() {
      
      setUpGraphics();
       
      //variable and objects
      //create (construct) the objects needed for the game and load up
		rockPic = Toolkit.getDefaultToolkit().getImage("rock.jpg"); //load the picture
		rock1 = new Rock((int)(Math.random()*900)+1,(int)(Math.random()*600)+1);
        paperPic = Toolkit.getDefaultToolkit().getImage("paper.png"); //load the picture
        paper1 = new Paper((int)(Math.random()*900)+1,(int)(Math.random()*600)+1);
        scissorsPic = Toolkit.getDefaultToolkit().getImage("scissors.jpg"); //load the picture
        scissors1 = new Scissors((int)(Math.random()*900)+1,(int)(Math.random()*600)+1);
        backgroundPic = Toolkit.getDefaultToolkit().getImage("Table.jpg");//load the picture
        rockWinPic = Toolkit.getDefaultToolkit().getImage("rockWins.png");
        paperWinPic = Toolkit.getDefaultToolkit().getImage("paperWins.png");
        scissorsWinPic = Toolkit.getDefaultToolkit().getImage("scissorsWins.png");



    }// BasicGameApp()

   
//*******************************************************************************
//User Method Section
//
// put your code to do things here.

   // main thread
   // this is the code that plays the game after you set things up
	public void run() {

      //for the moment we will loop things forever.
		while (true) {

         moveThings();  //move all the game objects
         render();  // paint the graphics
         pause(20); // sleep for 10 ms
		}
	}


	public void moveThings()
	{
      //calls the move( ) code in the objects
        if (!gameOver) {
            rock1.move();
            paper1.move();
            scissors1.move();
            crashing();
        }

	}
    public void crashing() {

        if (paper1.hitbox.intersects(rock1.hitbox)&& rock1.isAlive == true) {
            System.out.println("Rock/paper hit");
            rock1.isAlive = false;
            paper1.isAlive = true;
            paper1.height = 200;
            paper1.width = 200;
        }
        if (rock1.hitbox.intersects(scissors1.hitbox)&& scissors1.isAlive == true) {
            System.out.println("Rock/scissors hit");
            scissors1.isAlive = false;
            rock1.isAlive = true;
            rock1.height = 200;
            rock1.width = 200;
        }
        if (scissors1.hitbox.intersects(paper1.hitbox)&& paper1.isAlive == true) {
            System.out.println("scissors/paper hit");
            paper1.isAlive = false;
            scissors1.isAlive = true;
            scissors1.height = 200;
            scissors1.width = 200;
        }

        int aliveCount = 0;
        if (rock1.isAlive) aliveCount++;
        if (paper1.isAlive) aliveCount++;
        if (scissors1.isAlive) aliveCount++;

        if (aliveCount == 1) {
            gameOver = true;
            if (rock1.isAlive) winner = "ROCK";
            if (paper1.isAlive) winner = "PAPER";
            if (scissors1.isAlive) winner = "SCISSORS";
        }

    }


	
   //Pauses or sleeps the computer for the amount specified in milliseconds
   public void pause(int time ){
   		//sleep
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {

			}
   }


   //Graphics setup method
   private void setUpGraphics() {
      frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.
   
      panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
      panel.setLayout(null);   //set the layout
   
      // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
      // and trap input events (Mouse and Keyboard events)
      canvas = new Canvas();  
      canvas.setBounds(0, 0, WIDTH, HEIGHT);
      canvas.setIgnoreRepaint(true);
   
      panel.add(canvas);  // adds the canvas to the panel.
   
      // frame operations
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
      frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
      frame.setResizable(false);   //makes it so the frame cannot be resized
      frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!
      
      // sets up things so the screen displays images nicely.
      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      canvas.requestFocus();
      System.out.println("DONE graphic setup");
   
   }



	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(backgroundPic, 0, 0, WIDTH,HEIGHT , null);


        //draw the image of the astronaut
        if (rock1.isAlive == true) {
            g.drawImage(rockPic, rock1.xpos, rock1.ypos, rock1.width, rock1.height, null);
        }
        if (paper1.isAlive == true) {
            g.drawImage(paperPic, paper1.xpos, paper1.ypos, paper1.width, paper1.height, null);
        }
        if (scissors1.isAlive == true) {
            g.drawImage(scissorsPic, scissors1.xpos, scissors1.ypos, scissors1.width, scissors1.height, null);
        }
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
        }



        g.dispose();

		bufferStrategy.show();
	}
}