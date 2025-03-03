import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame implements ActionListener, KeyListener{
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel lable = new JLabel();
	JLabel ele = new JLabel();
	
	ArrayList<JLabel> body = new ArrayList<>();
	
	int x = 0;
	int y = 0;
	public SnakeGame() {
		ele.setBounds(0, 0 , 25, 25);
		ele.setBackground(Color.yellow);
		ele.setOpaque(true);
		
		lable.setBounds(0, 0, 25, 25);
		lable.setOpaque(true);
		lable.setBackground(Color.red);
		
		panel.setLayout(null);
		panel.add(ele);
		panel.add(lable);
		panel.setPreferredSize(new Dimension(500,500));
		panel.setOpaque(true);
		panel.setBackground(Color.black);
		panel.setFocusable(true);
		panel.addKeyListener(this);
		
		
		frame.setTitle("SnakeGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		
		repeat();
		
		Timer timer = new Timer(100,this);
		timer.start();
	}
	
//	public void direction() {
//		
//	}
	public void repeat() {
		Random random = new Random();
		int a = random.nextInt(20);
		int b = random.nextInt(20);
		ele.setLocation(a * 25,b * 25);
	}
	
	public void moveBody() {
	    
	    for (int i = body.size() - 1; i > 0; i--) {
	        JLabel segment = body.get(i);
	        JLabel previousSegment = body.get(i - 1);
	        segment.setLocation(previousSegment.getX(), previousSegment.getY());
	    }
	    
	   
	    if (!body.isEmpty()) {
	        JLabel head = body.get(0);
	        head.setLocation(lable.getX(), lable.getY());
	    }
	}

	public void move() {
		lable.setLocation(lable.getX() + x , lable.getY() + y);
		
		moveBody();
		
		if(lable.getX() > 475 || lable.getX() < 0) {
			lable.setLocation(0, lable.getY());
		}
		if(lable.getY() > 475 || lable.getY() < 0) {
			lable.setLocation(lable.getX(), 0);
		}
	}
	
//	public void intersect() {
//		if(lable.getBounds().intersects(ele.getBounds())) {
//			lable.setBounds(lable.getX(), lable.getY() , lable.getWidth() + 25 , lable.getHeight());
//			repeat();
//		}
//	}
	public void intersect() {
    if (lable.getBounds().intersects(ele.getBounds())) {
        JLabel newSegment = new JLabel();
        newSegment.setBounds(lable.getX(), lable.getY(),25, 25);
        newSegment.setOpaque(true);
        newSegment.setBackground(Color.green);
        body.add(newSegment); 
        panel.add(newSegment);

        repeat();
    }
}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		move();
		intersect();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyChar()) {
		case 'a' : x = -25;
		y = 0;
		break;
		case 'd' : x = 25;
		y = 0;
		break;
		case 'w' : y = -25;
		x = 0;
		break;
		case 's' : y = 25;
		x =0;
		break;
		}
		
		intersect();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case 37 : x = -25;
		y = 0;
		break;
		case 39 : x = 25;
		y = 0;
		break;
		case 38 : y = -25;
		x = 0;
		break;
		case 40 : y = 25;
		x =0;
		break;
		}
		
		intersect();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyChar());
	}
	
}
