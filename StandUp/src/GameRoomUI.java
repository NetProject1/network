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
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class GameRoomUI extends JFrame{
	Client client;
	JLabel cardHeapLB;
	JLabel cardBackLB1;
	JLabel cardBackLB2;
	JLabel cardBackLB3;
	JLabel cardBackLB4;
	private JTextField textField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new GameRoomUI(new Client());
	}

	/**
	 * Create the application.
	 */
	public GameRoomUI(Client client) {
		this.client=client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		cardHeapLB.setSize(80, 100);
		cardHeapLB.setLocation(345, 330);
		
		cardBackLB1= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);		
		cardBackLB1.setSize(80, 100);
		cardBackLB1.setLocation(345, 330);
		
		cardBackLB2= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB2.setSize(80, 100);
		cardBackLB2.setLocation(345, 330);
		
		cardBackLB3= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB3.setSize(80, 100);
		cardBackLB3.setLocation(345, 330);
		
		cardBackLB4= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB4.setSize(80, 100);
		cardBackLB4.setLocation(345, 330);
		
		getContentPane().add(cardHeapLB);
		getContentPane().add(cardBackLB1);
		getContentPane().add(cardBackLB2);
		getContentPane().add(cardBackLB3);
		getContentPane().add(cardBackLB4);
		setBounds(100, 100, 800, 800);
		
		getContentPane().setLayout(null);
		
		JButton button1 = new JButton("패 돌리기1");
		button1.setBounds(620, 10, 97, 23);
		getContentPane().add(button1);
		button1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard(1,3);
			}
		
		});
		
		JButton button2 = new JButton("패 돌리기2");
		button2.setBounds(620, 43, 97, 23);
		getContentPane().add(button2);
		button2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard(2,3);
			}
		
		});
		
		JButton button3 = new JButton("패 돌리기3");
		button3.setBounds(620, 76, 97, 23);
		getContentPane().add(button3);
		button3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard(3,3);
			}
		
		});
		
		JButton button4 = new JButton("패 돌리기4");
		button4.setBounds(620, 109, 97, 23);
		getContentPane().add(button4);
		
		JButton goWaitRoomBTN = new JButton("대기실로 나가기");
		goWaitRoomBTN.setBounds(620, 142, 97, 23);
		getContentPane().add(goWaitRoomBTN);
		goWaitRoomBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				client.ExitRoom();
			}
		});
		
		JPanel player1PN = new JPanel();
		player1PN.setBounds(504, 273, 280, 220);
		getContentPane().add(player1PN);
		player1PN.setLayout(null);
		
		JLabel player1nick = new JLabel("New label");
		player1nick.setBounds(115, 10, 57, 15);
		player1PN.add(player1nick);
		
		JPanel player2PN = new JPanel();
		player2PN.setBounds(250, 10, 280, 220);
		getContentPane().add(player2PN);
		player2PN.setLayout(null);
		
		JLabel player2nick = new JLabel("New label");
		player2nick.setBounds(110, 10, 57, 15);
		player2PN.add(player2nick);
		
		JPanel player3PN = new JPanel();
		player3PN.setBounds(0, 273, 280, 220);
		getContentPane().add(player3PN);
		player3PN.setLayout(null);
		
		JLabel player3nick = new JLabel("New label");
		player3nick.setBounds(110, 10, 57, 15);
		player3PN.add(player3nick);
		
		JPanel player4PN = new JPanel();
		player4PN.setBounds(250, 541, 280, 220);
		getContentPane().add(player4PN);
		player4PN.setLayout(null);
		
		JLabel player4nick = new JLabel("New label");
		player4nick.setBounds(110, 10, 57, 15);
		player4PN.add(player4nick);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 541, 241, 210);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		panel_1.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		panel_1.add(btnNewButton, BorderLayout.EAST);
		
		JTextArea textArea = new JTextArea();
		panel.add(textArea, BorderLayout.CENTER);
		button4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				HandOutCard(4,3);
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
	public void roomUpdate(){
		
	}
	//카드를 나눠주는 애니메이션 재생 (playerNumber 나눠줄 플레이어번호, numcard 몇장 나눠줄지)
	void HandOutCard(int playerNumber, int numCard){
		new Thread(){
			public void run(){
				
				int repeat=0;
				while(true){
					if(playerNumber==1){
						cardBackLB1.setLocation(cardBackLB1.getLocation().x+7, cardBackLB1.getLocation().y);
						if(cardBackLB1.getLocation().x >=600){
							cardBackLB1.setLocation(345, 330);
							repeat+=1;
							if(repeat>=numCard){
								break;
							}
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(playerNumber==2){
						cardBackLB2.setLocation(cardBackLB2.getLocation().x, (cardBackLB2.getLocation().y)-7);
						if(cardBackLB2.getLocation().y <=10){
							cardBackLB2.setLocation(345, 330);
							repeat+=1;
							if(repeat>=numCard){
							break;
							}
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(playerNumber==3){
						cardBackLB3.setLocation(cardBackLB3.getLocation().x-7, cardBackLB3.getLocation().y);
						if(cardBackLB3.getLocation().x <=10){
							cardBackLB3.setLocation(345, 330);
							repeat+=1;
							if(repeat>=numCard){
							break;
							}
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(playerNumber==4){
						cardBackLB4.setLocation(cardBackLB4.getLocation().x, (cardBackLB4.getLocation().y)+7);
						if(cardBackLB4.getLocation().y >=600){
							cardBackLB4.setLocation(345, 330);
							repeat+=1;
							if(repeat>=numCard){
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
			}
		}.start();
	}
}
