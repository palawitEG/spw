package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
	
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();	
	private ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	private ArrayList<Items> items = new ArrayList<Items>();
	private SpaceShip v;	
	private Timer timer;
	private Hp hpbars;
	private long score = 0;
	private double difficulty = 0.1;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;

		this.hpbars = new Hp(9,9,v.getHp());		
		gp.sprites.add(this.hpbars);
		gp.sprites.add(v);
		
		timer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	public void generateItems(){
		Items f = new Items((int)(Math.random()*800), 5);
		gp.sprites.add(f);
		items.add(f);
	}
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*800), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBullet(){
		Bullet g = new Bullet(v.x+(v.width)/2,v.y);
		System.out.println(v.x+" "+v.y);
		System.out.println("Shot!");
		gp.sprites.add(g);
		bullet.add(g);
	}
	
	public void process(){
		if(Math.random() < difficulty){
			generateEnemy();
			generateItems();
		}
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<Bullet> g_iter = bullet.iterator();
		Iterator<Items> f_iter = items.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 100;
			}
		}
		while(g_iter.hasNext()){
			Bullet g = g_iter.next();
			g.proceed();
			
			if(!g.isAlive()){
				g_iter.remove();
				gp.sprites.remove(g);
			}
		}
		while(f_iter.hasNext()){
			Items f = f_iter.next();
			f.proceed();

			if(!f.isAlive()){
				f_iter.remove();
				gp.sprites.remove(f);
			}
		}
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double fr;
		Rectangle2D.Double bs;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				v.reduceHP(1);
                hpbars.damage();
				System.out.println("Warning!!!! "+ v.getHp());
				gp.sprites.remove(e);
				if(v.getHp()<= 0){
                    die();
                }
			}
			Rectangle2D.Double br;
			for(Bullet g : bullet){
				br = g.getRectangle();
				if(br.intersects(er)){
					gp.sprites.remove(g);
					e.die();
				}
			}
			for(Items f : items){
				fr = f.getRectangle();
				if(fr.intersects(vr)){
					gp.sprites.remove(f);
					if(v.getHp()<100){
						v.increteHP(1);
						hpbars.potion();
					}
					return;
				}
			}
		}
	}
	public void die(){
		timer.stop();
		JOptionPane.showMessageDialog(null,score + " " + "Points","Total Score : ",JOptionPane.INFORMATION_MESSAGE);
	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			v.move(-1);
			break;
		case KeyEvent.VK_RIGHT:
			v.move(1);
			break;
		case KeyEvent.VK_UP:
			v.moveup(-1);
			break;
		case KeyEvent.VK_DOWN:
			v.moveup(1);
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		case KeyEvent.VK_SPACE:
			generateBullet();
			break;
		}
	}
	public int gethp(){
		return v.getHp();
	}
	public long getScore(){
		return score;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
