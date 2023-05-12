package main;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import entity.Player;

public class GamePanel extends JPanel implements Runnable {
	
	public final int sprite_size = 64;								// height and width need to be the same
	boolean running = false;
	final int screen_height = 900;
	final int screen_width = 1400;
	final int FPS = 500;
	int playerX = 0, playerY = 0, playerSpeed = 1;
	boolean GameExit = false;								// for closing the game
	
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	Player player = new Player(this, keyHandler);
	
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screen_width, screen_height));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
		
		
	}
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		
	}


	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		long lastTime = System.nanoTime();
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {
			

			update();
			drawCount++;
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				timer += System.nanoTime() - lastTime;
				lastTime = System.nanoTime();
				
				if (remainingTime < 0){
					remainingTime = 0;
				}
				
				Thread.sleep((long)(remainingTime / 1000000));
				
				nextDrawTime += drawInterval;
				
				} catch(InterruptedException e) {
					e.printStackTrace();				
				}			
			
			repaint();		
			
			if (timer >= 1000000000){
				System.out.println("FPS:" + drawCount);
				timer = 0;
				drawCount = 0;
			}
			
			if (GameExit == true) {
				GameClose();
			}

			
		}
	}

	
	public void update() {
		
		player.update();		

		if(keyHandler.ESCPressed == true) {
			GameExit = true;
		}
	}	
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		player.draw(g2);
		g2.dispose();
		
	}
	public void GameClose() {
		// here we draw the closing screen and wait
		// maybe we wait for an button press of the user

		
		System.out.println("EXIT CODE: '0'");

		System.exit(0);
		
	}
}
