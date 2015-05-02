package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Items extends Sprite{
	public static final int Y_TO_FADE = 400;
	public static final int Y_TO_DIE = 600;
	BufferedImage bg;
	private int step = 12;
	private boolean alive = true;
	
	public Items(int x, int y) {
		super(x, y, 35, 35);
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(y < Y_TO_FADE)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		else{
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
					(float)(Y_TO_DIE - y)/(Y_TO_DIE - Y_TO_FADE)));
		}
		try{
			bg = ImageIO.read(new File("f2/spw/Image/potion.png"));
		}
		catch(IOException e){
			// don't
		}
		g.drawImage(bg, x, y, width, height, null);
		//g.setColor(Color.PINK);
		//g.fillRect(x, y, width, height);
		
	}

	public void proceed(){
		y += step;
		if(y > Y_TO_DIE){
			alive = false;
		}
	}	
	public boolean isAlive(){
		return alive;
	}
}
