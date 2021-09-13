import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.Timer;


public class Runner {

	Image back;
	Timer timer;
	int ticks;
	Fruit fruit;
	ArrayList<Pipes> pipes;
	ArrayList<Background> backgrounds;
	double score = 0.0;
	static Font font = getFont();
	static JFrame frame = new JFrame("Fruit Jump!");
	Clip clip;
	JPanel panel;
	static JPanel panelBegin;
	boolean playAgain = false;
	int x;
	int y;
	static String fruitType = "Melon";
	int numSpace = 0;
	boolean space = true;

	static Runner runner = new Runner();
	public static void main(String[] args) {
		runner.start();
	}

	// START BEGIN
	private void start() {
		playSound();
		fruit = new Fruit(fruitType);

		/* Making JPanel */
		pipes = new ArrayList<Pipes>();
		backgrounds = new ArrayList<Background>();

		for(int x = 0; x < 4; x++) {
			pipes.add(new Pipes(x));
		}

		for(int x = 0; x < 2; x++) {
			backgrounds.add(new Background(x));
		}
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				for(Background back: backgrounds) {
					back.drawBackground(g);
				}
				for(Pipes pipe: pipes) {
					pipe.drawPipes(g);
				}
				fruit.drawFruit(g);

				if(gameOver()) {
					resetGraphics(g);
				}

				if(!gameOver()) {
					if(space) {
						g.setColor(Color.BLACK);
						Font fontNew4 = font.deriveFont(font.getSize() * 1.1F);
						g.setFont(fontNew4);
						g.drawString("Press Space To Begin", 115, 120);
					} else {
						Font fontNew = font.deriveFont(font.getSize() * 1.3F);
						g.setFont(fontNew);
						g.setColor(Color.BLACK);
						g.drawString("Score: " +((int)score) , 20, 52);
						g.setColor(new Color(75, 198, 153));
						g.drawString("Score: " +((int)score) , 23, 50);
					}
				}
			}


		};
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(800,600));
		frame.pack();
		frame.add(panel);
		mapKeyStrokesToActions(panel);
		frame.setVisible(true);

		/* Timer */
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateGame();
				panel.repaint();
				if(ticks > 1) {
					if(space) {
						timer.stop();
					}
				}
			}
		});
		timer.start();
	}


	//START END

	/* This code resets the GUI when the person clicks play again */
	private void resetGraphics(Graphics g) {
		Font fontNew = font.deriveFont(font.getSize() * 3F);
		g.setFont(fontNew);
		g.drawImage(getBlackBackground(), 0, 0, 800, 600, null);
		g.setColor(Color.WHITE); 
		g.drawString("Game", 255, 210);
		g.drawString("Over!", 228, 290);
		Font font2 = font.deriveFont(font.getSize() * .8F);
		g.setFont(font2);
		g.drawString("Your score:", 300, 368);
		g.setColor(Color.GREEN);
		g.drawString("" + (int)score, 395, 399);
		g.setColor(Color.WHITE);
		Font font3 = font.deriveFont(font.getSize() * 1.2F);
		g.setFont(font3);
		g.drawString("Play Again?", 255, 500);
		Rectangle rect = new Rectangle(255, 500, 320, 35);
		if(rect.contains(MouseInfo.getPointerInfo().getLocation())) {
			g.setColor(Color.GREEN);
			g.drawString("Play Again?", 255, 500);
		}
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(rect.contains(e.getX(), e.getY())) {
					playAgain = true;
				}
			}
		});

	}

	/* Making coconut jump when pressing space */
	private void mapKeyStrokesToActions(JPanel panel) {
		ActionMap map = panel.getActionMap();
		InputMap inMap = panel.getInputMap();
		inMap.put(KeyStroke.getKeyStroke("pressed SPACE"), "space");
		map.put("space", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fruit.y>0) {
					fruit.jump(ticks);
					numSpace++;
				}
				if(numSpace>0) {
					space = false;
					timer.start();
				}
			}
		});
		;

	}

	/* If anything here happens, it's game over */
	private boolean gameOver() {
		boolean hit = false;
		boolean ground = false;
		for(int i=0; i<pipes.size();i++) {
			if(fruit.hit(pipes.get(i))) {
				hit = true;
			}
		}
		if(fruit.groundDead()) {
			ground = true;
		}
		if(hit || ground) {
			return true;
		} else {
			return false;
		}

	}

	/* Everything here updates with ticks */
	protected void updateGame() {
		if(gameOver()) {
			for(int i = 0; i < 4; i++) {
				pipes.get(i).freeze();
			}
			for(int i = 0; i < 2; i++) {
				backgrounds.get(i).freeze();
			}
			fruit.freeze();
			if(playAgain) {
				timer.stop();
				restart();
				for(int i = 0; i < 4; i++) {
					pipes.get(i).unfreeze();
				}
				for(int i = 0; i < 2; i++) {
					backgrounds.get(i).unfreeze();
				}
				fruit.unfreeze();
				playAgain = !playAgain;
				timer.start();
			}
		}

		ticks++;
		fruit.updateFruit(ticks);

		for(int i=0; i<pipes.size();i++) {
			if(pipes.size()>0) {
				pipes.get(i).updatePipes();
			}
			if(pipes.get(i).getX() >= 279.4 && pipes.get(i).getX() <= 280) {
				playPointSound();
				score++;
			}
		}

		for(Background back: backgrounds) {
			if(back.atEnd()) {
				back.reset();
			}
			back.updateBackground();
		}
	}

	private Image getBlackBackground() {
		try {
			Image img = ImageIO.read(new File("src/transparentBlack.png"));
			img = img.getScaledInstance(800, 600, Image.SCALE_DEFAULT);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void restart() {
		for(int x = 0; x < 4; x++) {
			pipes.get(x).resetGameOver(x);
		}
		for(int x=0; x<2; x++) {
			backgrounds.get(x).resetGameOver(x);
		}
		fruit.resetGameOver();
		score=0;
	}

	//public static Font getFont() throws MalformedURLException {
	public static Font getFont() {
		Font ttfBase = null;
		Font bit = null;
		InputStream myStream = null;
		String fontpath = "src/Font.ttf";
		try {
			myStream = new BufferedInputStream(
					new FileInputStream(fontpath));
			
			ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
			bit = ttfBase.deriveFont(Font.PLAIN, 24);               
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Font not loaded.");
		}
		return bit;
	}

	public void playPointSound() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/ding.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch(Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}



	public void playSound() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/lobbyMusic.wav").getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch(Exception ex) {
			System.out.println("Can't play sound");
			ex.printStackTrace();
		}
	}

}