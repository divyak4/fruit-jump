import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;

public class Fruit {
	Image fruit;
	String fruitType;
	double upgrav, downgrav;
	int y, starY, endY;
	int x = 312;
	double starJump, endJump;
	double score = 0; 
	final double OVERLAP_THRESHOLD = .9;
	String fruitName;
	Rectangle rect = new Rectangle(312,300,50,50);

	public Fruit(String type) {
		fruitType = type;
		fruit = getFruit();
		y = 300;
		upgrav = 1.5;
		downgrav = 2;
	}

	public Image getFruit() {
		Image img = null;
		img = Toolkit.getDefaultToolkit().createImage("src/Melon.png");
		
		img = img.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		return img;
	}

	public void changeCoor(int xnew, int ynew) {
		x = xnew;
		y = ynew;
		rect.x = xnew;
		rect.y = ynew;
	}

	public void setFruitType(String fruit) {
		fruitType = fruit;
	}

	public void updateFruit(int ticks) {
		if (groundDead()== false) {
			if (ticks < endJump) {
				jumpstart(ticks);
			} else {
				jumpend(ticks);
			}
		}
		rect.y=y;
	}

	public void jumpstart(int ticks) {
		y-=upgrav;
		rect.y-= upgrav;
	}

	public void jumpend(int ticks) {
		y+=downgrav;
		rect.y+=downgrav;
	}

	public boolean groundDead() {
		if (y > 630) {
			return true;
		}
		else {
			return false;
		}
	}

	public void jump(int ticks) {
		starJump = ticks;
		endJump = starJump + 20;
	}

	public void drawFruit(Graphics g) {
		g.drawImage(fruit, x, y, null);
	}

	public boolean hit(Pipes pipe) {
		Rectangle topOne = pipe.getTopRect();
		Rectangle bottomOne = pipe.getBottomRect();
		if(rect.intersects(topOne)||rect.intersects(bottomOne)) {
			return true;
		}
		return false;
	}

	public void resetGameOver() {
		y=300;
	}

	public void setGravity(double gravity) {
		upgrav = gravity;
		downgrav = gravity;
	}

	public void freeze() {
		this.setGravity(0);
	}

	public void unfreeze() {
		this.upgrav = 1.5;
		this.downgrav = 2;
	}
}