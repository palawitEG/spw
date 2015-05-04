package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpaceShip extends Sprite{
	
	BufferedImage bg;
	int step = 9;
	private int Hp = 100;
	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		//g.setColor(Color.YELLOW);
    	//g.fillOval(x, y, width, height);
		try{
			bg = ImageIO.read(new File("f2/spw/Image/avion.gif"));
		}
		catch(IOException e){
			// don't
		}
		g.drawImage(bg, x, y, width, height, null);
	}
	public void reduceHP(int damage){
		this.Hp -= damage;
	}
	public void increteHP(int potion){
		this.Hp += potion;
	}
	public void move(int direction){
		x += (step * direction);
		if(x < 0)
			x = 0;
		if(x > 400 - width)
			x = 400 - width;
	}
	public void moveup(int direction){
		y += (step * direction);
		if(y < 0)
			y = 0;
		if(y > 600 - height)
			y = 600 - height;
	}
	public int getHp(){
		return this.Hp;
	}

}
