import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Rectangle;

public class SnakeGame implements ActionListener, KeyListener{
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel lable = new JLabel();
	JLabel ele = new JLabel();
	JLabel scoreLabel = new JLabel("Score: 0");
	Timer timer;
	boolean isGameOver = false;
	boolean justAte = false;
	JPanel gameOverPanel = new JPanel();
	JLabel gameOverMessage = new JLabel("", SwingConstants.CENTER);
	JButton restartButton = new JButton("Restart");
	
	ArrayList<JLabel> body = new ArrayList<>();
	
	int score = 0;
	int x = 0;
	int y = 0;
	int prevHeadX = 0;
	int prevHeadY = 0;
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
		// Score label: top-right corner
		scoreLabel.setBounds(375, 5, 120, 25);
		scoreLabel.setForeground(Color.white);
		scoreLabel.setOpaque(true);
		scoreLabel.setBackground(new Color(50,50,50));
		panel.add(scoreLabel);
		
		// Game over overlay (centered)
		gameOverPanel.setBounds(100,150,300,200);
		gameOverPanel.setBackground(new Color(0,0,0,170));
		gameOverPanel.setLayout(null);
		gameOverMessage.setBounds(25,20,250,80);
		gameOverMessage.setForeground(Color.white);
		gameOverMessage.setFont(new Font("Arial", Font.BOLD, 24));
		gameOverPanel.add(gameOverMessage);
		restartButton.setBounds(100,110,100,40);
		gameOverPanel.add(restartButton);
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
		gameOverPanel.setVisible(false);
		panel.add(gameOverPanel);
		// overlay on top, score label just below overlay
		panel.setComponentZOrder(gameOverPanel, 0);
		panel.setComponentZOrder(scoreLabel, 1);
		
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
		
		timer = new Timer(100,this);
		timer.start();
	}
	
//	public void direction() {
//		
//	}
	public void repeat() {
		Random random = new Random();
		int a, b;
		int xPos, yPos;
		while (true) {
			a = random.nextInt(20);
			b = random.nextInt(20);
			xPos = a * 25;
			yPos = b * 25;
			Rectangle candidate = new Rectangle(xPos, yPos, 25, 25);
			// don't place food on the head
			if (candidate.intersects(lable.getBounds())) continue;
			// don't place food on any body segment
			boolean overlap = false;
			for (JLabel seg : body) {
				if (candidate.intersects(seg.getBounds())) { overlap = true; break; }
			}
			if (!overlap) break;
		}
		ele.setLocation(xPos, yPos);
	}
	
public void moveBody(int prevHeadX, int prevHeadY) {
	    
	    for (int i = body.size() - 1; i > 0; i--) {
	        JLabel segment = body.get(i);
	        JLabel previousSegment = body.get(i - 1);
	        segment.setLocation(previousSegment.getX(), previousSegment.getY());
	    }
	    
	   	if (!body.isEmpty()) {
	        JLabel head = body.get(0);
	        head.setLocation(prevHeadX, prevHeadY);
	    }
	}

	public void move() {
		prevHeadX = lable.getX();
		prevHeadY = lable.getY();
		lable.setLocation(lable.getX() + x , lable.getY() + y);
		
		moveBody(prevHeadX, prevHeadY);
		
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
        // place the new segment at the previous head position so it doesn't cause an immediate self-collision
        newSegment.setBounds(prevHeadX, prevHeadY,25, 25);
        newSegment.setOpaque(true);
        newSegment.setBackground(Color.green);
        body.add(newSegment); 
        panel.add(newSegment);

        // increment and update score
        score++;
        scoreLabel.setText("Score: " + score);
        // mark that we just ate so the newly added segment won't trigger a self-collision this tick
        justAte = true;

        repeat();
    }
}

public void resetGame() {
    // reset score
    score = 0;
    scoreLabel.setText("Score: " + score);
    // remove body segments from panel
    for (JLabel seg : body) {
        panel.remove(seg);
    }
    body.clear();
    // reset head position and movement
    lable.setLocation(0, 0);
    x = 0; y = 0;
    repeat();
    panel.revalidate();
    panel.repaint();
}

public void checkSelfCollision() {
    // If we just ate, skip checking collision against the last-added segment for this tick
    int skipIndex = justAte ? body.size() - 1 : -1;
    for (int i = 0; i < body.size(); i++) {
        if (i == skipIndex) continue;
        JLabel seg = body.get(i);
        if (lable.getBounds().intersects(seg.getBounds())) {
            gameOver();
            break;
        }
    }
    // reset the flag so next tick is normal
    justAte = false;
}

public void gameOver() {
    isGameOver = true;
    if (timer != null) timer.stop();
    gameOverMessage.setText("<html><div style='text-align:center'>Game Over<br/>Score: " + score + "</div></html>");
    gameOverPanel.setVisible(true);
    panel.revalidate();
    panel.repaint();
}

public void restartGame() {
    gameOverPanel.setVisible(false);
    resetGame();
    isGameOver = false;
    if (timer != null) timer.start();
    panel.requestFocusInWindow();
}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (isGameOver) return;
		move();
		intersect();
		checkSelfCollision();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (isGameOver) return;
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
		if (isGameOver) return;
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
