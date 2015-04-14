package f2.spw;

import java.awt.*;

import javax.swing.*;
import java.applet.Applet;

public class Main {
	public static void main(String[] args){
		JFrame frame = new JFrame("Space War By B'Bat");
		JButton button = new JButton("START GAME");

		frame.setSize(400, 650);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpaceShip v = new SpaceShip(180, 550, 30, 30);
		GamePanel gp = new GamePanel();
		GameEngine engine = new GameEngine(gp, v);
		frame.addKeyListener(engine);
		frame.getContentPane().add(gp, BorderLayout.CENTER);
		frame.setVisible(true);
		
		engine.start();

	}
}
