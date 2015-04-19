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
	private ArrayList<Bullet> bullet = new ArrayList<Bullet>(); // new arry bullet

	private SpaceShip v;	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.1;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
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
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30);
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
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<Bullet> g_iter = bullet.iterator();
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
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				die();
				return;
			}
			Rectangle2D.Double br;
			for(Bullet g : bullet){
				br = g.getRectangle();
				if(br.intersects(er)){
					gp.sprites.remove(g);
					e.die();
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
