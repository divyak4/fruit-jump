import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Pipes {
	Image pipetop = getPipe();
	Image pipebottom = getPipe();
	int width = 90;
	int pos, y;
	Random ran;
	boolean dou = false;
	public double x;
	int spacing = 136;
	Rectangle rect;
	Rectangle rect2;
	double speed = .7;

	public Pipes(int num) {
		ran = new Random();
		pos = (int) (ran.nextDouble()*4);
		x = 1024+num*270;
		System.out.println(rect);
	}

	public void drawPipes(Graphics g) {
		g.setColor(new Color(0, 92, 3));
		g.drawImage(pipetop, (int)x, 0, 90, spacing*(pos), null);
		g.fillRect((int)x, spacing*pos, 90, 9);
		rect = new Rectangle((int)x, 0, 90, spacing*(pos)+5);
		g.drawImage(pipebottom, (int)x, (pos+1)*spacing, 90, 683, null);
		g.fillRect((int)x, (pos+1)*spacing-5, 90, 9);
		rect2 = new Rectangle((int)x, (pos+1)*spacing-5, 90, 683);
	}

	public Image getPipe() {
		try {
			Image img = ImageIO.read(new File("src/Pipe.png"));
			//Image img = ImageIO.read(new File("Pipe.png"));
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public void freeze() {
		this.setSpeed(0);
	}
	public void updatePipes() {
		if (!atEdge()) {
			x-=speed;
			rect.x = (int)x;
			rect2.x = (int)x;
		} else
			reset();
	}

	public boolean inX(double xof) {
		if(xof> x && xof < x-50) {
			return true;
		}
		return false;

	}

	public boolean atEdge() {
		if(x < -90) {
			return true;
		}
		return false;
	}

	public void resetPos(){
		pos = (int) (ran.nextDouble()*4);
		rect.height = 68*pos*2;
		rect2.y = (pos+1)*68*2;
	}

	public void reset() {
		x=1024;
		resetPos();
	}
	
	public void resetGameOver(int x) {
		this.x=1024+(x*270);
	}

	public Rectangle getBottomRect() {
		return this.rect2;
	}

	public Rectangle getTopRect() {
		return this.rect;
	}

	public double getX() {
		return x;
	}
	
	public void unfreeze() {
		this.setSpeed(.7);
	}

}