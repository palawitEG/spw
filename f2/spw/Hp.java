package f2.spw;
import java.awt.Color;
import java.awt.Graphics2D;
public class Hp extends Sprite{
    public int hp; 
    public Hp(int x,int y, int hp){
        super(x,y,hp,2);
        this.hp = hp;
    }
    @Override
    public void draw(Graphics2D g){
        g.setColor(Color.RED);
        g.fillRect(x,y,this.hp,20);
    }
    public void damage(){
        this.hp -= 1;
    }
}
