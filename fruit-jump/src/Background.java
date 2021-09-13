import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Background {
	double x;
	double speed = .8;
	Image background = getBack();

	public Background(int num) {
		x=800*num;
	}

	public Image getBack() {
		try {
			Image img = ImageIO.read(new File("src/beachBlocksCont.png"));
			img = img.getScaledInstance(800, 600, Image.SCALE_DEFAULT);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void reset() {
		x=800;
	}

	public boolean atEnd() {
		if(x <= -800) {
			return true;
		}
		return false;
	}

	public void updateBackground() {
		x-=speed;
	}

	public void drawBackground(Graphics g) {
		g.drawImage(background, (int)x, 0, null);
	}
	
	public void resetGameOver(int x) {
		this.x = 800*x;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void freeze() {
		this.setSpeed(0);
	}
	
	public void unfreeze() {
		this.setSpeed(.8);
	}
}
