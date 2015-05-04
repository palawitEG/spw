package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Boss extends Sprite{
	private boolean moveR = true;
	private boolean moveL = false;
	private int Hp;
	private int step = 3;
	private boolean alive = true;
	
	public Boss(int x, int y, int Hp) {
		super(x, y, 30, 30);
		this.Hp = Hp;
	}

	@Override
	public void draw(Graphics2D g){
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	
	}

	public void proceed(){
		if(this.x <= 380 && moveR == true )
		{
			this.x += this.step;
			if(this.x > 380){
				moveR = false;
				moveL = true;
			}
		}
		if(this.x >= 0 && moveL == true)
		{
			this.x -= this.step;
			if(this.x < 0)
			{
				moveL = false;
				moveR = true;
			}
		}
	}
	public void lostHp(){
		this.Hp -= 1;
	}
	public int getHp(){
		return this.Hp;
	}
	public void die(){
		this.alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
}
