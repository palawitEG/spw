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
	private ArrayList<Boss> boss = new ArrayList<Boss>();
	private ArrayList<BulletBoss> bulletboss = new ArrayList<BulletBoss>();

	public int i=0,z=0;

	private SpaceShip v;	
	private Timer timer;
	private Hp hpbars;
 	private long score = 0;
	private double difficulty = 0.1;
	private boolean set = true;
	
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
		Items f = new Items((int)(Math.random()*12000), 5);
		gp.sprites.add(f);
		items.add(f);
	}
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*800), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	private void generateBoss(){
		Boss bosser = new Boss(180,40,100);
		gp.sprites.add(bosser);
		boss.add(bosser);
	}
	private void generateBullet(){
		Bullet g = new Bullet(v.x+(v.width)/2,v.y);
		System.out.println(v.x+" "+v.y);
		System.out.println("Shot!");
		gp.sprites.add(g);
		bullet.add(g);
	}
	private void generateEnemyfromBoss(){
		BulletBoss gbs = new BulletBoss(boss.get(i).x, boss.get(i).getY());
		gp.sprites.add(gbs);
		bulletboss.add(gbs);
		
	}
	
	public void process(){
		if(this.set == true){
			generateBoss();
			set = false;
		}
		if(Math.random() < difficulty){
			generateEnemy();
			generateItems();
			generateEnemyfromBoss();
		}
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<Bullet> g_iter = bullet.iterator();
		Iterator<Items> f_iter = items.iterator();
		Iterator<Boss> b_iter = boss.iterator();
		Iterator<BulletBoss> q_iter = bulletboss.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 100;
			}
		}
		while(b_iter.hasNext()){
			Boss b = b_iter.next();
			b.proceed();
			if(!b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
				score += 500;
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
		while(q_iter.hasNext()){
			BulletBoss q = q_iter.next();
			q.proceed();
			if(!q.isAlive()){
				q_iter.remove();
				gp.sprites.remove(q);
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
		Rectangle2D.Double br;
		Rectangle2D.Double brs;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				v.reduceHP(1);
                hpbars.damage();
				gp.sprites.remove(e);
				if(v.getHp()<= 0){
                    die();
                }
			}
			
			for(Bullet g : bullet){
				br = g.getRectangle();
				if(br.intersects(er)){
					gp.sprites.remove(g);
					e.die();
				}
				for(Boss b : boss){
					bs = b.getRectangle();
					if(br.intersects(bs)){
						b.lostHp();
						gp.sprites.remove(g);
						if(b.getHp() <= 0){
							System.out.println("Yes he is die!!");
							gp.sprites.remove(b);
							b.die();
							this.set = true;
						}
					}
				}
				for(BulletBoss h : bulletboss){
				brs = h.getRectangle();
				if(brs.intersects(br)){
					gp.sprites.remove(h);
					gp.sprites.remove(g);
					g.die();
					h.die();
				}
				if(brs.intersects(vr)){
					v.reduceHP(1);
                	hpbars.damage();
					gp.sprites.remove(e);
					if(v.getHp()<= 0){
                    	die();
                	}
					return;
				}
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
			/*for(BulletBoss h : bulletboss){
				brs = h.getRectangle();
				if(brs.intersects(br)){
					gp.sprites.remove(h);
					gp.sprites.remove(g);
				}
				if(brs.intersects(vr)){
					v.reduceHP(1);
                	hpbars.damage();
					gp.sprites.remove(e);
					if(v.getHp()<= 0){
                    	die();
                	}
					return;
				}
			}*/
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
