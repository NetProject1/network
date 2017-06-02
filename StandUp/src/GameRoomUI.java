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
import java.awt.Font;

public class GameRoomUI extends JFrame{
	Client client;
	JLabel cardHeapLB;
	JLabel cardBackLB1;
	JLabel cardBackLB2;
	JLabel cardBackLB3;
	JLabel cardBackLB4;
	private JTextField textField;
	
	//player panels
	JPanel player0PN,player1PN,player2PN,player3PN;
	//player info panels
	JPanel p0Info, p1Info, p2Info, p3Info;
	
	//myPlayer
	JLabel p0Nick,p0Money,p0Card1,p0Card3,p0Card2;
	//player 1
	JLabel p1Nick,p1Money,p1Card1,p1Card3,p1Card2;
	//player2
	JLabel p2Nick,p2Money,p2Card1,p2Card3,p2Card2;
	//player3
	JLabel p3Nick,p3Money,p3Card1,p3Card3,p3Card2;
	
	JLabel label_3;
	private JLabel lblBetting;
	private JButton btnGameStart;
	
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
		cardHeapLB.setLocation(400, 350);
		
		cardBackLB1= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);		
		cardBackLB1.setSize(80, 100);
		cardBackLB1.setLocation(400, 350);
		
		cardBackLB2= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB2.setSize(80, 100);
		cardBackLB2.setLocation(400, 350);
		
		cardBackLB3= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB3.setSize(80, 100);
		cardBackLB3.setLocation(400, 350);
		
		cardBackLB4= new JLabel(new ImageIcon("img/CardBack.png"), SwingUtilities.CENTER);
		cardBackLB4.setSize(80, 100);
		cardBackLB4.setLocation(400, 350);
		
		getContentPane().add(cardHeapLB);
		getContentPane().add(cardBackLB1);
		getContentPane().add(cardBackLB2);
		getContentPane().add(cardBackLB3);
		getContentPane().add(cardBackLB4);
		setBounds(100, 100, 900, 850);
		
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
		goWaitRoomBTN.setBounds(759, 647, 97, 23);
		getContentPane().add(goWaitRoomBTN);
		goWaitRoomBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				client.ExitRoom();
			}
		});
		
		player0PN = new JPanel();
		player0PN.setBounds(300, 595, 280, 210);
		getContentPane().add(player0PN);
		player0PN.setLayout(null);
		
		p0Card1 = new JLabel("카드1");
		p0Card1.setBounds(10, 100, 80, 100);
		player0PN.add(p0Card1);
		
		p0Card2 = new JLabel("카드2");
		p0Card2.setBounds(100, 100, 80, 100);
		player0PN.add(p0Card2);
		
		p0Card3 = new JLabel("카드3");
		p0Card3.setBounds(190, 100, 80, 100);
		player0PN.add(p0Card3);
		
		player1PN = new JPanel();
		player1PN.setLayout(null);
		player1PN.setBounds(5, 300, 280, 210);
		getContentPane().add(player1PN);
		
		p1Card1 = new JLabel("카드1");
		p1Card1.setBounds(10, 100, 80, 100);
		player1PN.add(p1Card1);
		
		p1Card2 = new JLabel("카드2");
		p1Card2.setBounds(100, 100, 80, 100);
		player1PN.add(p1Card2);
		
		player2PN = new JPanel();
		player2PN.setLayout(null);
		player2PN.setBounds(300, 10, 280, 210);
		getContentPane().add(player2PN);
		
		p2Card1 = new JLabel("카드1");
		p2Card1.setBounds(10, 100, 80, 100);
		player2PN.add(p2Card1);
		
		p2Card2 = new JLabel("카드2");
		p2Card2.setBounds(100, 100, 80, 100);
		player2PN.add(p2Card2);
		
		player3PN = new JPanel();
		player3PN.setLayout(null);
		player3PN.setBounds(600, 300, 280, 210);
		getContentPane().add(player3PN);
		
		 p3Card1 = new JLabel("카드1");
		p3Card1.setBounds(10, 100, 80, 100);
		player3PN.add(p3Card1);
		
		 p3Card2 = new JLabel("카드2");
		p3Card2.setBounds(100, 100, 80, 100);
		player3PN.add(p3Card2);
		
		player0PN.setOpaque(false);
		
		p0Info = new JPanel();
		p0Info.setLayout(null);
		p0Info.setBounds(0, 0, 280, 72);
		player0PN.add(p0Info);
		
		p0Nick = new JLabel("");
		p0Nick.setFont(new Font("Gulim", Font.BOLD, 13));
		p0Nick.setBounds(12, 10, 132, 20);
		p0Info.add(p0Nick);
		
		JLabel mlb0 = new JLabel("소지금 : ");
		mlb0.setBounds(12, 40, 57, 22);
		p0Info.add(mlb0);
		
		p0Money = new JLabel("");
		p0Money.setBounds(87, 44, 181, 15);
		p0Info.add(p0Money);
		player1PN.setOpaque(false);
		
		p1Card3 = new JLabel("카드3");
		p1Card3.setBounds(190, 100, 80, 100);
		player1PN.add(p1Card3);
		
		p1Info = new JPanel();
		p1Info.setBounds(0, 0, 280, 72);
		player1PN.add(p1Info);
		p1Info.setLayout(null);
		
		p1Nick = new JLabel("");
		p1Nick.setFont(new Font("Gulim", Font.BOLD, 13));
		p1Nick.setBounds(12, 10, 132, 20);
		p1Info.add(p1Nick);
		
		JLabel mlb1 = new JLabel("소지금 : ");
		mlb1.setBounds(12, 40, 57, 22);
		p1Info.add(mlb1);
		
		p1Money = new JLabel("");
		p1Money.setBounds(87, 44, 181, 15);
		p1Info.add(p1Money);
		player2PN.setOpaque(false);
		
		p2Card3 = new JLabel("카드3");
		p2Card3.setBounds(190, 100, 80, 100);
		player2PN.add(p2Card3);
		
		p2Info = new JPanel();
		p2Info.setLayout(null);
		p2Info.setBounds(0, 0, 280, 72);
		player2PN.add(p2Info);
		
		p2Nick = new JLabel("");
		p2Nick.setFont(new Font("Gulim", Font.BOLD, 13));
		p2Nick.setBounds(12, 10, 132, 20);
		p2Info.add(p2Nick);
		
		JLabel mlb2 = new JLabel("소지금 : ");
		mlb2.setBounds(12, 40, 57, 22);
		p2Info.add(mlb2);
		
		p2Money = new JLabel("");
		p2Money.setBounds(87, 44, 181, 15);
		p2Info.add(p2Money);
		player3PN.setOpaque(false);
		
		 p3Card3 = new JLabel("카드3");
		 p3Card3.setBounds(190, 100, 80, 100);
		 player3PN.add(p3Card3);
		 
		 p3Info = new JPanel();
		 p3Info.setLayout(null);
		 p3Info.setBounds(0, 0, 280, 72);
		 player3PN.add(p3Info);
		 
		 p3Nick = new JLabel("");
		 p3Nick.setFont(new Font("Gulim", Font.BOLD, 13));
		 p3Nick.setBounds(12, 10, 132, 20);
		 p3Info.add(p3Nick);
		 
		 JLabel label_5 = new JLabel("소지금 : ");
		 label_5.setBounds(12, 40, 57, 22);
		 p3Info.add(label_5);
		 
		 p3Money = new JLabel("");
		 p3Money.setBounds(87, 44, 181, 15);
		 p3Info.add(p3Money);
		
		JPanel roomChatPN = new JPanel();
		roomChatPN.setBounds(0, 600, 241, 210);
		getContentPane().add(roomChatPN);
		roomChatPN.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		roomChatPN.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		panel_1.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("발신");
		panel_1.add(btnNewButton, BorderLayout.EAST);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		roomChatPN.add(textArea, BorderLayout.CENTER);
		
		JPanel bettingButtonPN = new JPanel();
		bettingButtonPN.setBounds(640, 685, 230, 114);
		getContentPane().add(bettingButtonPN);
		bettingButtonPN.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("콜");
		btnNewButton_1.setToolTipText("전 사람이 배팅한 만큼 배팅합니다.");
		btnNewButton_1.setBounds(12, 48, 97, 23);
		bettingButtonPN.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("더블");
		btnNewButton_2.setToolTipText("전 사람이 배팅한 금액의 두배를 배팅합니다.");
		btnNewButton_2.setBounds(121, 48, 97, 23);
		bettingButtonPN.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("하프");
		btnNewButton_3.setToolTipText("현재 걸린 금액의 절반을 배팅합니다.");
		btnNewButton_3.setBounds(12, 81, 97, 23);
		bettingButtonPN.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("다이");
		btnNewButton_4.setToolTipText("이번 판 배팅을 포기합니다.");
		btnNewButton_4.setBounds(121, 81, 97, 23);
		bettingButtonPN.add(btnNewButton_4);
		
		lblBetting = new JLabel("betting");
		lblBetting.setBounds(90, 10, 57, 15);
		bettingButtonPN.add(lblBetting);
		
		btnGameStart = new JButton("Game Start");
		btnGameStart.setBounds(650, 647, 97, 23);
		getContentPane().add(btnGameStart);
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

		p0Nick.setText(" ");
		p0Money.setText("");
		
		p1Nick.setText(" ");
		p1Money.setText("");
		
		p2Nick.setText(" ");
		p2Money.setText("");
		
		p3Nick.setText(" ");
		p3Money.setText("");

		//수정 요함. 룸정보를 계속 업데이트한다.
		for(int i=0; i< client.user.room.userArray.size();i++){
			if(i==0){
				p0Nick.setText(client.user.room.userArray.get(i).nickName);		
				p0Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
			}else if(i==1){
				p1Nick.setText(client.user.room.userArray.get(i).nickName);
				p1Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
			}else if(i==2){
				p2Nick.setText(client.user.room.userArray.get(i).nickName);
				p2Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
			}else if(i==3){
				p3Nick.setText(client.user.room.userArray.get(i).nickName);
				p3Money.setText(Integer.toString(client.user.room.userArray.get(i).money));;
			}
		}
	}
	//카드를 나눠주는 애니메이션 재생 (playerNumber 나눠줄 플레이어번호, numcard 몇장 나눠줄지)
	void HandOutCard(int playerNumber, int numCard){
		new Thread(){
			public void run(){
				
				int repeat=0;
				while(true){
					if(playerNumber==4){
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
					}else if(playerNumber==3){
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
					}else if(playerNumber==2){
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
					}else if(playerNumber==1){
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
