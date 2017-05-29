import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import oracle.jrockit.jfr.JFR;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class GameRoomUI extends JFrame{

	JLabel cardHeapLB;
	JLabel cardBackLB1;
	JLabel cardBackLB2;
	JLabel cardBackLB3;
	JLabel cardBackLB4;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new GameRoomUI();
	}

	/**
	 * Create the application.
	 */
	public GameRoomUI() {
		setTitle("섰다");
		getContentPane().setBackground(new Color(0, 100, 0));
		getContentPane().setForeground(Color.WHITE);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cardHeapLB= new JLabel(new ImageIcon("img/CardHeap.png"), SwingUtilities.CENTER);
		cardHeapLB.setSize(150, 150);
		cardHeapLB.setLocation(325, 290);
		
		cardBackLB1= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);		
		cardBackLB1.setSize(150, 150);
		cardBackLB1.setLocation(325, 290);
		
		cardBackLB2= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB2.setSize(150, 150);
		cardBackLB2.setLocation(325, 290);
		
		cardBackLB3= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB3.setSize(150, 150);
		cardBackLB3.setLocation(325, 290);
		
		cardBackLB4= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB4.setSize(150, 150);
		cardBackLB4.setLocation(325, 290);
		
		getContentPane().add(cardHeapLB);
		getContentPane().add(cardBackLB1);
		getContentPane().add(cardBackLB2);
		getContentPane().add(cardBackLB3);
		getContentPane().add(cardBackLB4);
		setBounds(100, 100, 800, 800);
		
		getContentPane().setLayout(null);
		
		JButton button1 = new JButton("패 돌리기1");
		button1.setBounds(687, 247, 97, 23);
		getContentPane().add(button1);
		button1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard1();
			}
		
		});
		
		JButton button2 = new JButton("패 돌리기2");
		button2.setBounds(687, 290, 97, 23);
		getContentPane().add(button2);
		button2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard2();
			}
		
		});
		
		JButton button3 = new JButton("패 돌리기3");
		button3.setBounds(687, 337, 97, 23);
		getContentPane().add(button3);
		button3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard3();
			}
		
		});
		
		JButton button4 = new JButton("패 돌리기4");
		button4.setBounds(687, 383, 97, 23);
		getContentPane().add(button4);
		button4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard4();
			}
		
		});
		
		setVisible(true);
		
		repaint();
		
	}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}
	
	void HandOutCard1(){
		
		new Thread(){
			public void run(){
				int repeat=0;
				while(true){
					cardBackLB1.setLocation(cardBackLB1.getLocation().x+4, cardBackLB1.getLocation().y);
					
					if(cardBackLB1.getLocation().x >=600){
						cardBackLB1.setLocation(325, 290);
						repeat+=1;
						if(repeat>=2){
						break;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	void HandOutCard2(){
		
		new Thread(){
			public void run(){
				int repeat=0;
				while(true){
					cardBackLB2.setLocation(cardBackLB2.getLocation().x, (cardBackLB2.getLocation().y)-4);
					if(cardBackLB2.getLocation().y <=10){
						cardBackLB2.setLocation(325, 290);
						repeat+=1;
						if(repeat>=2){
						break;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	void HandOutCard3(){
		
		new Thread(){
			public void run(){
				int repeat=0;
				while(true){
					cardBackLB3.setLocation(cardBackLB3.getLocation().x-4, cardBackLB3.getLocation().y);
					
					if(cardBackLB3.getLocation().x <= 100){
						cardBackLB3.setLocation(325, 290);
						repeat+=1;
						if(repeat>=2){
						break;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	void HandOutCard4(){
		
		new Thread(){
			public void run(){
				int repeat=0;
				while(true){
					cardBackLB4.setLocation(cardBackLB4.getLocation().x, cardBackLB4.getLocation().y+4);
					
					if(cardBackLB4.getLocation().y >=600){
						cardBackLB4.setLocation(325, 290);
						repeat+=1;
						if(repeat>=2){
						break;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
