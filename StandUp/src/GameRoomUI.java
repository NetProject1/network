
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import oracle.jrockit.jfr.JFR;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;

public class GameRoomUI extends JFrame{
	Client client;
	JLabel cardHeapLB;
	JLabel cardBackLB1;
	JLabel cardBackLB2;
	JLabel cardBackLB3;
	JLabel cardBackLB4;
	private JTextField textField;
	
	ImageIcon[] images= new ImageIcon[21];
	//배팅 버튼을 붙여놓는 panel
	JPanel bettingButtonPN;
	//player panels
	JPanel player0PN,player1PN,player2PN,player3PN;
	//player info panels
	JPanel p0Info, p1Info, p2Info, p3Info;
	//배팅금액
	JLabel bet,amoutMoney;
	JTextArea textArea;
	//myPlayer
	JLabel p0Nick,p0Money,p0Card1,p0Card3,p0Card2;
	//player 1
	JLabel p1Nick,p1Money,p1Card1,p1Card3,p1Card2;
	//player2
	JLabel p2Nick,p2Money,p2Card1,p2Card3,p2Card2;
	//player3
	JLabel p3Nick,p3Money,p3Card1,p3Card3,p3Card2;
	
	//플레이어들의 카드 결과 보여줌.
	JLabel p1Cardset, p0Cardset, p3Cardset, p2Cardset;
	JLabel label_3;
	private JLabel lblBetting;
	private JButton btnGameStart;
	private JLabel cardSelectLB;
	private JPanel roomInfo;
	private JLabel winLoseLB;
	private JLabel p1Die;
	private JLabel p0Die;
	private JLabel p2Die;
	private JLabel p3Die;
	private JLabel lblNewLabel_2;
	private JLabel ROOMnumber;
	private JLabel lblNewLabel_4;
	private JLabel RoomNAME;
	
	//말풍선
	JLabel p0State, p2State,p1State, p3State;
	ImageIcon doubleimg;
	ImageIcon halfimg;
	ImageIcon callimg;
	
	JButton callBTN,doubleBTN, halfBTN, dieBTN;
	
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
		//이미지 삽입
		doubleimg=new ImageIcon("img/double.png");
		callimg=new ImageIcon("img/call.png");
		halfimg=new ImageIcon("img/half.png");

		images[0]=new ImageIcon("img/CardBack.png");
		images[1]=new ImageIcon("img/01.jpg");
		images[2]=new ImageIcon("img/02.jpg");
		images[3]=new ImageIcon("img/03.jpg");
		images[4]=new ImageIcon("img/04.jpg");
		images[5]=new ImageIcon("img/05.jpg");
		images[6]=new ImageIcon("img/06.jpg");
		images[7]=new ImageIcon("img/07.jpg");
		images[8]=new ImageIcon("img/08.jpg");
		images[9]=new ImageIcon("img/09.jpg");
		images[10]=new ImageIcon("img/10.jpg");
		images[11]=new ImageIcon("img/11.jpg");
		images[12]=new ImageIcon("img/12.jpg");
		images[13]=new ImageIcon("img/13.jpg");
		images[14]=new ImageIcon("img/14.jpg");
		images[15]=new ImageIcon("img/15.jpg");
		images[16]=new ImageIcon("img/16.jpg");
		images[17]=new ImageIcon("img/17.jpg");
		images[18]=new ImageIcon("img/18.jpg");
		images[19]=new ImageIcon("img/19.jpg");
		images[20]=new ImageIcon("img/20.jpg");
		
		
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
		
		p3State = new JLabel("");
		p3State.setBounds(633, 161, 160, 160);
		getContentPane().add(p3State);
		
		p0State = new JLabel("");
		p0State.setBounds(284, 510, 160, 160);
		getContentPane().add(p0State);
		
		p1State = new JLabel("");
		p1State.setBounds(5, 203, 160, 160);
		getContentPane().add(p1State);
		
		p2State = new JLabel("");
		p2State.setBounds(184, 34, 160, 160);
		getContentPane().add(p2State);
		
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
		
		p0Die = new JLabel("다이!");
		p0Die.setHorizontalAlignment(SwingConstants.CENTER);
		p0Die.setForeground(new Color(204, 0, 0));
		p0Die.setFont(new Font("Gulim", Font.PLAIN, 53));
		p0Die.setBounds(0, 0, 293, 203);
		player0PN.add(p0Die);
		
		 p0Die.setVisible(false);
		
		p0Card1 = new JLabel("");
		p0Card1.setBounds(10, 100, 80, 100);
		player0PN.add(p0Card1);
		
		p0Card1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				//게임 상황이 카드선택일때.
				client.choseCard1();
			}
		});
		
		p0Card2 = new JLabel("");
		p0Card2.setBounds(100, 100, 80, 100);
		player0PN.add(p0Card2);
		
		p0Card2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				//게임 상황이 카드선택일때.
				client.choseCard2();
			}
		});
		
		p0Card3 = new JLabel("");
		p0Card3.setBounds(190, 100, 80, 100);
		player0PN.add(p0Card3);
		
		p0Card3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				//게임 상황이 카드선택일때.
				client.choseCard3();
			}
		});
		
		
		player1PN = new JPanel();
		player1PN.setLayout(null);
		player1PN.setBounds(5, 300, 280, 210);
		getContentPane().add(player1PN);
		
		p1Die = new JLabel("다이!");
		p1Die.setForeground(new Color(204, 0, 0));
		p1Die.setHorizontalAlignment(SwingConstants.CENTER);
		p1Die.setFont(new Font("Gulim", Font.PLAIN, 53));
		p1Die.setBounds(0, 0, 293, 203);
		player1PN.add(p1Die);
		p1Die.setVisible(false);
		
		p1Card1 = new JLabel("");
		p1Card1.setBounds(10, 100, 80, 100);
		player1PN.add(p1Card1);
		
		p1Card2 = new JLabel("");
		p1Card2.setBounds(100, 100, 80, 100);
		player1PN.add(p1Card2);
		
		player2PN = new JPanel();
		player2PN.setLayout(null);
		player2PN.setBounds(300, 10, 280, 210);
		getContentPane().add(player2PN);
		
		p2Die = new JLabel("다이!");
		p2Die.setHorizontalAlignment(SwingConstants.CENTER);
		p2Die.setForeground(new Color(204, 0, 0));
		p2Die.setFont(new Font("Gulim", Font.PLAIN, 53));
		p2Die.setBounds(0, 0, 293, 203);
		player2PN.add(p2Die);
		p2Die.setVisible(false);
		
		p2Card1 = new JLabel("");
		p2Card1.setBounds(10, 100, 80, 100);
		player2PN.add(p2Card1);
		
		p2Card2 = new JLabel("");
		p2Card2.setBounds(100, 100, 80, 100);
		player2PN.add(p2Card2);
		
		player3PN = new JPanel();
		player3PN.setLayout(null);
		player3PN.setBounds(600, 300, 280, 210);
		getContentPane().add(player3PN);
		 
		 p3Die = new JLabel("다이!");
		 p3Die.setHorizontalAlignment(SwingConstants.CENTER);
		 p3Die.setForeground(new Color(204, 0, 0));
		 p3Die.setFont(new Font("Gulim", Font.PLAIN, 53));
		 p3Die.setBounds(0, 0, 293, 203);
		 player3PN.add(p3Die);
		 p3Die.setVisible(false);
		
		 p3Card1 = new JLabel("");
		p3Card1.setBounds(10, 100, 80, 100);
		player3PN.add(p3Card1);
		
		 p3Card2 = new JLabel("");
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
		
		p1Card3 = new JLabel("");
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
		
		p2Card3 = new JLabel("");
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
		
		 p3Card3 = new JLabel("");
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
		textField.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				msgSubmit();
			}
		});
		
		JButton btnNewButton = new JButton("발신");
		panel_1.add(btnNewButton, BorderLayout.EAST);
		
		JScrollPane scrollPane = new JScrollPane();
		roomChatPN.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		btnNewButton.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {				
				msgSubmit();

			 }
		});
		
		bettingButtonPN = new JPanel();
		bettingButtonPN.setBounds(640, 685, 230, 114);
		getContentPane().add(bettingButtonPN);
		bettingButtonPN.setLayout(null);
		
		callBTN = new JButton("콜");
	
		callBTN.setToolTipText("전 사람이 배팅한 만큼 배팅합니다.");
		callBTN.setBounds(12, 48, 97, 23);
		bettingButtonPN.add(callBTN);
		
		doubleBTN = new JButton("더블");
		doubleBTN.setToolTipText("전 사람이 배팅한 금액의 두배를 배팅합니다.");
		doubleBTN.setBounds(121, 48, 97, 23);
		bettingButtonPN.add(doubleBTN);
		
		halfBTN = new JButton("하프");
		halfBTN.setToolTipText("현재 걸린 금액의 절반을 배팅합니다.");
		halfBTN.setBounds(12, 81, 97, 23);
		bettingButtonPN.add(halfBTN);
		
		dieBTN = new JButton("다이");
		dieBTN.setToolTipText("이번 판 배팅을 포기합니다.");
		dieBTN.setBounds(121, 81, 97, 23);
		bettingButtonPN.add(dieBTN);
		
		//버튼들에 달린 이벤트 리스너들추가
		
		callBTN.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {				
					client.betMoney("call");
				 }
		});
		doubleBTN.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {				
				 client.betMoney("double");
				 }
		});
		halfBTN.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {				
				 client.betMoney("half");
				 }
		});
		dieBTN.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {				
				 client.betMoney("die");
				 }
		});
		
		
		
		
		lblBetting = new JLabel("betting");
		lblBetting.setBounds(90, 10, 57, 15);
		bettingButtonPN.add(lblBetting);
		
		bettingButtonPN.setVisible(false);
		
		btnGameStart = new JButton("Game Start");
		btnGameStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					client.user.dos.writeUTF(MsgProtocol.CODE_GAMESTART);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnGameStart.setBounds(650, 647, 97, 23);
		btnGameStart.setVisible(false);
		getContentPane().add(btnGameStart);
		
		JLabel lblNewLabel = new JLabel("배팅 액수:");
		lblNewLabel.setBounds(326, 269, 69, 23);
		getContentPane().add(lblNewLabel);
		
		bet = new JLabel("0");
		bet.setBounds(400, 273, 57, 15);
		getContentPane().add(bet);
		
		JLabel lblNewLabel_1 = new JLabel("총 금액 :");
		lblNewLabel_1.setBounds(326, 306, 57, 15);
		getContentPane().add(lblNewLabel_1);
		
		amoutMoney = new JLabel("0");
		amoutMoney.setBounds(400, 306, 57, 15);
		getContentPane().add(amoutMoney);
		
		cardSelectLB = new JLabel("공개할 카드를 선택하세요!");
		cardSelectLB.setBounds(300, 541, 280, 47);
		getContentPane().add(cardSelectLB);
		cardSelectLB.setFont(new Font("Gulim", Font.BOLD, 14));
		cardSelectLB.setForeground(Color.RED);
		
		roomInfo = new JPanel();
		roomInfo.setBounds(12, 30, 241, 65);
		getContentPane().add(roomInfo);
		roomInfo.setLayout(null);
		
		lblNewLabel_2 = new JLabel("방 번호:");
		lblNewLabel_2.setBounds(12, 10, 57, 15);
		roomInfo.add(lblNewLabel_2);
		
		ROOMnumber = new JLabel("New label");
		ROOMnumber.setBounds(81, 10, 148, 15);
		roomInfo.add(ROOMnumber);
		
		lblNewLabel_4 = new JLabel("방 제목:");
		lblNewLabel_4.setBounds(12, 35, 57, 15);
		roomInfo.add(lblNewLabel_4);
		
		RoomNAME = new JLabel("New label");
		RoomNAME.setBounds(81, 35, 148, 15);
		roomInfo.add(RoomNAME);
		
		winLoseLB = new JLabel("승리!!");
		winLoseLB.setForeground(new Color(255, 204, 0));
		winLoseLB.setHorizontalAlignment(SwingConstants.CENTER);
		winLoseLB.setFont(new Font("Gulim", Font.BOLD | Font.ITALIC, 47));
		winLoseLB.setBounds(326, 245, 230, 177);
		getContentPane().add(winLoseLB);
		
		p1Cardset = new JLabel("");
		p1Cardset.setHorizontalAlignment(SwingConstants.CENTER);
		p1Cardset.setForeground(new Color(255, 20, 147));
		p1Cardset.setFont(new Font("Gulim", Font.BOLD, 20));
		p1Cardset.setBounds(284, 385, 160, 72);
		getContentPane().add(p1Cardset);
		
		p0Cardset = new JLabel("");
		p0Cardset.setHorizontalAlignment(SwingConstants.CENTER);
		p0Cardset.setForeground(new Color(255, 20, 147));
		p0Cardset.setFont(new Font("Gulim", Font.BOLD, 20));
		p0Cardset.setBounds(326, 500, 209, 72);
		getContentPane().add(p0Cardset);
		
		
		p3Cardset = new JLabel("");
		p3Cardset.setHorizontalAlignment(SwingConstants.CENTER);
		p3Cardset.setForeground(new Color(255, 20, 147));
		p3Cardset.setFont(new Font("Gulim", Font.BOLD, 20));
		p3Cardset.setBounds(435, 385, 160, 72);
		getContentPane().add(p3Cardset);
		
		p2Cardset = new JLabel("");
		p2Cardset.setHorizontalAlignment(SwingConstants.CENTER);
		p2Cardset.setForeground(new Color(255, 20, 147));
		p2Cardset.setFont(new Font("Gulim", Font.BOLD, 20));
		p2Cardset.setBounds(337, 220, 198, 72);
		getContentPane().add(p2Cardset);
		
		winLoseLB.setVisible(false);
		cardSelectLB.setVisible(false);
		
		setVisible(true);
		
		repaint();
		
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	public void roomUpdate(){
		setWaitLabel(false);
		RoomNAME.setText(client.user.room.roomName);
		ROOMnumber.setText(Integer.toString(client.user.room.roomNumber));
		bet.setText(Integer.toString(client.user.room.bet));
		amoutMoney.setText(Integer.toString(client.user.room.amountMoney));
		int n = client.user.playerNumber;
		
		//정보를 지웠다가 다시 그림
		p0Nick.setText(" ");
		p0Money.setText("");
		p0Card1.setIcon(null);	
		p0Card2.setIcon(null);
		p0Card3.setIcon(null);
		
		p1Nick.setText(" ");
		p1Money.setText("");
		p1Card1.setIcon(null);
		p1Card2.setIcon(null);
		p1Card3.setIcon(null);
		
		p2Nick.setText(" ");
		p2Money.setText("");
		p2Card1.setIcon(null);
		p2Card2.setIcon(null);
		p2Card3.setIcon(null);	
		p3Nick.setText(" ");
		p3Money.setText("");
		p3Card1.setIcon(null);
		p3Card2.setIcon(null);
		p3Card3.setIcon(null);
		
		p0Die.setVisible(false);
		p1Die.setVisible(false);
		p2Die.setVisible(false);
		p3Die.setVisible(false);
		
		p0State.setIcon(null);
		p1State.setIcon(null);
		p2State.setIcon(null);
		p3State.setIcon(null);
		
		p0State.setVisible(true);
		p1State.setVisible(true);
		p2State.setVisible(true);
		p3State.setVisible(true);
		
		p0Cardset.setText("");
		p1Cardset.setText("");
		p2Cardset.setText("");
		p3Cardset.setText("");
		//유저가 n번째일때 오른쪽에는 n+3 %4 번이 위치 왼쪽에는 n+1 %4번이 위치
		
		
		
		//게임 시작하기 전만 보인다.
		btnGameStart.setVisible(false);
		//게임 시작시
		if(client.user.room.gameState.equals("start")){
			if(!client.user.isReady){

			cardSelectLB.setText("공개할 카드를 선택하세요!");
			cardSelectLB.setVisible(true);
			}else{
				cardSelectLB.setVisible(false);
			}
			bettingButtonPN.setVisible(false);
			//배팅시
		}else if(client.user.room.gameState.equals("bet")){
			//배팅시만 콜과 다이를 계속 보여준다
			for(int i=0; i< client.user.room.userArray.size();i++){			
				if(client.user.room.userArray.get(i).playerNumber == n){
					if(client.user.room.userArray.get(i).state.equals("call")){
						p0State.setIcon(callimg);
					}else if(client.user.room.userArray.get(i).state.equals("double")){
						p0State.setIcon(doubleimg);
					}else if(client.user.room.userArray.get(i).state.equals("half")){
						p0State.setIcon(halfimg);
					}
			
				}else if(client.user.room.userArray.get(i).playerNumber == ((n+1)%4)){
					
					if(client.user.room.userArray.get(i).state.equals("call")){
						p1State.setIcon(callimg);
					}else if(client.user.room.userArray.get(i).state.equals("double")){
						p1State.setIcon(doubleimg);
					}else if(client.user.room.userArray.get(i).state.equals("half")){
						p1State.setIcon(halfimg);
					}
			
				}else if(client.user.room.userArray.get(i).playerNumber == ((n+2)%4)){
					
					if(client.user.room.userArray.get(i).state.equals("call")){
						p2State.setIcon(callimg);
					}else if(client.user.room.userArray.get(i).state.equals("double")){
						p2State.setIcon(doubleimg);
					}else if(client.user.room.userArray.get(i).state.equals("half")){
						p2State.setIcon(halfimg);
					}
				
				}else if(client.user.room.userArray.get(i).playerNumber == ((n+3)%4)){
					
					if(client.user.room.userArray.get(i).state.equals("call")){
						p3State.setIcon(callimg);
					}else if(client.user.room.userArray.get(i).state.equals("double")){
						p3State.setIcon(doubleimg);
					}else if(client.user.room.userArray.get(i).state.equals("half")){
						p3State.setIcon(halfimg);
				
				}
				}		
			}
			
			cardSelectLB.setVisible(false);
			doubleBTN.setVisible(true);
			bettingButtonPN.setVisible(true);
			halfBTN.setVisible(true);
			callBTN.setVisible(true);
			if(client.user.room.bet*2 > client.user.money){
				doubleBTN.setVisible(false);
			}
			int half=client.user.room.amountMoney+client.user.room.bet;
			if(half> client.user.money ){
				halfBTN.setVisible(false);
			}
			if(client.user.room.bet > client.user.money){
				callBTN.setVisible(false);
			}
			
				if(client.user.room.playerturn== n){
					player0PN.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
					player1PN.setBorder(null);
					player2PN.setBorder(null);
					player3PN.setBorder(null);
				}else if(client.user.room.playerturn== (n+1)%4 ){
					bettingButtonPN.setVisible(false);
					player1PN.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
					player0PN.setBorder(null);
					player2PN.setBorder(null);
					player3PN.setBorder(null);
				}else if(client.user.room.playerturn== (n+2)%4 ){
					bettingButtonPN.setVisible(false);
					player2PN.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
					player1PN.setBorder(null);
					player0PN.setBorder(null);
					player3PN.setBorder(null);
				}else if(client.user.room.playerturn== (n+3)%4 ){
					bettingButtonPN.setVisible(false);
					player3PN.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
					player1PN.setBorder(null);
					player2PN.setBorder(null);
					player0PN.setBorder(null);
				}
				//최종카드 선택시
			}else if(client.user.room.gameState.equals("cardset")){
				if(!client.user.state.equals("die")){
				//최종패를 선택하세요
				
				if(!client.user.isReady){
				cardSelectLB.setText("최종 패 두 장을 선택하세요!");
				cardSelectLB.setVisible(true);
				}
				bettingButtonPN.setVisible(false);
				player3PN.setBorder(null);
				player1PN.setBorder(null);
				player2PN.setBorder(null);
				player0PN.setBorder(null);
				p0Card1.setBorder(null);
				p0Card2.setBorder(null);
				p0Card3.setBorder(null);
				}
				
			}else if(client.user.room.gameState.equals("end")){
				bettingButtonPN.setVisible(false);
				cardSelectLB.setVisible(false);
				player3PN.setBorder(null);
				player1PN.setBorder(null);
				player2PN.setBorder(null);
				player0PN.setBorder(null);
				p0Card1.setBorder(null);
				p0Card2.setBorder(null);
				p0Card3.setBorder(null);
			}else{
				if(client.user.equals(client.user.room.roomMaster)){
					btnGameStart.setVisible(true);
				}
				bettingButtonPN.setVisible(false);
				cardSelectLB.setVisible(false);
				player3PN.setBorder(null);
				player1PN.setBorder(null);
				player2PN.setBorder(null);
				player0PN.setBorder(null);
				p0Card1.setBorder(null);
				p0Card2.setBorder(null);
				p0Card3.setBorder(null);
				p1Card1.setBorder(null);
				p1Card2.setBorder(null);
				p1Card3.setBorder(null);
				p2Card1.setBorder(null);
				p2Card2.setBorder(null);
				p2Card3.setBorder(null);
				p3Card1.setBorder(null);
				p3Card2.setBorder(null);
				p3Card3.setBorder(null);
			}
		if(client.user.state.equals("die")){
			bettingButtonPN.setVisible(false);
		}
		int cardsetValue=0;
		// 룸정보를 계속 업데이트한다.
		for(int i=0; i< client.user.room.userArray.size();i++){
			
			if(client.user.room.userArray.get(i).playerNumber == n){

				p0Nick.setText(client.user.room.userArray.get(i).nickName);		
				p0Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
				if(client.user.room.userArray.get(i).card1 != 99999){
					p0Card1.setIcon(images[client.user.room.userArray.get(i).card1]);
				}else{
					p0Card1.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card2 != 99999){
					p0Card2.setIcon(images[client.user.room.userArray.get(i).card2]);
				}else{
					p0Card2.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card3 != 99999){
					p0Card3.setIcon(images[client.user.room.userArray.get(i).card3]);
				}else{
					p0Card3.setIcon(null);
				}
				if(client.user.room.userArray.get(i).state.equals("die")){
					p0Die.setVisible(true);
				}
				if(client.user.selectedCard1 != 99999 && client.user.selectedCard2 != 99999 ){
					if(client.user.selectedCard1 < client.user.selectedCard2){
					cardsetValue=client.user.room.priority[client.user.selectedCard1][client.user.selectedCard2];
					}else{
						cardsetValue=client.user.room.priority[client.user.selectedCard2][client.user.selectedCard1];
					}
					if(cardsetValue==Integer.MAX_VALUE){
						p0Cardset.setText("삼 팔 광 땡!");
					}else if(cardsetValue==10000){
						if(client.user.selectedCard2==18 || client.user.selectedCard1==18){
							p0Cardset.setText("일 팔 광 땡!");
						}else{
							p0Cardset.setText("일 삼 광 땡!");
						}
					}else if(cardsetValue>=1000 && cardsetValue<10000){
						int tang=client.user.selectedCard1%10;
						p0Cardset.setText((tang)+"땡 ");
					}else if(cardsetValue>=900 && cardsetValue<1000){
						p0Cardset.setText("알리");
					}else if(cardsetValue>=800 && cardsetValue<900){
						p0Cardset.setText("구삥");
					}else if(cardsetValue>=700 && cardsetValue<800){
						p0Cardset.setText("장삥");
					}else if(cardsetValue>=600 && cardsetValue<700){
						p0Cardset.setText("세륙");
					}else if(cardsetValue>=500 && cardsetValue<600){
						p0Cardset.setText("갑오");
					}else if(cardsetValue>=400 && cardsetValue<500){
						p0Cardset.setText("땡 잡이");
					}else if(cardsetValue>=300 && cardsetValue<400){
						p0Cardset.setText("암행어사");
					}else if(cardsetValue>=200 && cardsetValue<300){
						p0Cardset.setText("구사");
					}else{
						p0Cardset.setText(cardsetValue+"끗");
					}
					
				}
			}else if(client.user.room.userArray.get(i).playerNumber == ((n+1)%4)){
			
				p1Nick.setText(client.user.room.userArray.get(i).nickName);
				p1Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
				if(client.user.room.userArray.get(i).card1 != 99999){
					p1Card1.setIcon(images[client.user.room.userArray.get(i).card1]);
				}else{
					p1Card1.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card2 != 99999){
					p1Card2.setIcon(images[client.user.room.userArray.get(i).card2]);
				}else{
					p1Card2.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card3 != 99999){
					p1Card3.setIcon(images[client.user.room.userArray.get(i).card3]);
				}else{
					p1Card3.setIcon(null);
				}
				if(client.user.room.userArray.get(i).state.equals("die")){
					p1Die.setVisible(true);
				}
				if(client.user.room.userArray.get(i).card2 != 99999 && client.user.room.userArray.get(i).card3 != 99999 
						&& client.user.room.userArray.get(i).card2 != 0 && client.user.room.userArray.get(i).card3 != 0){
					if(client.user.room.userArray.get(i).card2 < client.user.room.userArray.get(i).card3){
					cardsetValue=client.user.room.priority[client.user.room.userArray.get(i).card2][client.user.room.userArray.get(i).card3];
					}else{
						cardsetValue=client.user.room.priority[client.user.room.userArray.get(i).card3][client.user.room.userArray.get(i).card2];
					}
					if(cardsetValue==Integer.MAX_VALUE){
						p1Cardset.setText("삼 팔 광 땡!");
					}else if(cardsetValue==10000){
						if(client.user.selectedCard2==18 || client.user.selectedCard1==18){
							p1Cardset.setText("일 팔 광 땡!");
						}else{
							p1Cardset.setText("일 삼 광 땡!");
						}
					}else if(cardsetValue>=1000 && cardsetValue<10000){
						p1Cardset.setText((client.user.selectedCard1%10)+"땡 ");
					}else if(cardsetValue>=900 && cardsetValue<1000){
						p1Cardset.setText("알리");
					}else if(cardsetValue>=800 && cardsetValue<900){
						p1Cardset.setText("구삥");
					}else if(cardsetValue>=700 && cardsetValue<800){
						p1Cardset.setText("장삥");
					}else if(cardsetValue>=600 && cardsetValue<700){
						p1Cardset.setText("세륙");
					}else if(cardsetValue>=500 && cardsetValue<600){
						p1Cardset.setText("갑오");
					}else if(cardsetValue>=400 && cardsetValue<500){
						p1Cardset.setText("땡 잡이");
					}else if(cardsetValue>=300 && cardsetValue<400){
						p1Cardset.setText("암행어사");
					}else if(cardsetValue>=200 && cardsetValue<300){
						p1Cardset.setText("구사");
					}else{
						p1Cardset.setText(cardsetValue+"끗");
					}
					
				}
			}else if(client.user.room.userArray.get(i).playerNumber == ((n+2)%4)){
				
			
				p2Nick.setText(client.user.room.userArray.get(i).nickName);
				p2Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
				if(client.user.room.userArray.get(i).card1 != 99999){
					p2Card1.setIcon(images[client.user.room.userArray.get(i).card1]);
				}else{
					p2Card1.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card2 != 99999){
					p2Card2.setIcon(images[client.user.room.userArray.get(i).card2]);
				}else{
					p2Card2.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card3 != 99999){
					p2Card3.setIcon(images[client.user.room.userArray.get(i).card3]);
				}else{
					p2Card3.setIcon(null);
				}
				if(client.user.room.userArray.get(i).state.equals("die")){
					p2Die.setVisible(true);
				}
				if(client.user.room.userArray.get(i).card2 != 99999 && client.user.room.userArray.get(i).card3 != 99999 
						&& client.user.room.userArray.get(i).card2 != 0 && client.user.room.userArray.get(i).card3 != 0){
					if(client.user.room.userArray.get(i).card2 < client.user.room.userArray.get(i).card3){
					cardsetValue=client.user.room.priority[client.user.room.userArray.get(i).card2][client.user.room.userArray.get(i).card3];
					}else{
						cardsetValue=client.user.room.priority[client.user.room.userArray.get(i).card3][client.user.room.userArray.get(i).card2];
					}
					if(cardsetValue==Integer.MAX_VALUE){
						p2Cardset.setText("삼 팔 광 땡!");
					}else if(cardsetValue==10000){
						if(client.user.selectedCard2==18 || client.user.selectedCard1==18){
							p2Cardset.setText("일 팔 광 땡!");
						}else{
							p2Cardset.setText("일 삼 광 땡!");
						}
					}else if(cardsetValue>=1000 && cardsetValue<10000){
						p2Cardset.setText((client.user.selectedCard1%10)+"땡 ");
					}else if(cardsetValue>=900 && cardsetValue<1000){
						p2Cardset.setText("알리");
					}else if(cardsetValue>=800 && cardsetValue<900){
						p2Cardset.setText("구삥");
					}else if(cardsetValue>=700 && cardsetValue<800){
						p2Cardset.setText("장삥");
					}else if(cardsetValue>=600 && cardsetValue<700){
						p2Cardset.setText("세륙");
					}else if(cardsetValue>=500 && cardsetValue<600){
						p2Cardset.setText("갑오");
					}else if(cardsetValue>=400 && cardsetValue<500){
						p2Cardset.setText("땡 잡이");
					}else if(cardsetValue>=300 && cardsetValue<400){
						p2Cardset.setText("암행어사");
					}else if(cardsetValue>=200 && cardsetValue<300){
						p2Cardset.setText("구사");
					}else{
						p2Cardset.setText(cardsetValue+"끗");
					}
					
				}
			}else if(client.user.room.userArray.get(i).playerNumber == ((n+3)%4)){
				
			
				p3Nick.setText(client.user.room.userArray.get(i).nickName);
				p3Money.setText(Integer.toString(client.user.room.userArray.get(i).money));
				if(client.user.room.userArray.get(i).card1 != 99999){
					p3Card1.setIcon(images[client.user.room.userArray.get(i).card1]);
				}else{
					p3Card1.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card2 != 99999){
					p3Card2.setIcon(images[client.user.room.userArray.get(i).card2]);
				}else{
					p3Card2.setIcon(null);
				}
				if(client.user.room.userArray.get(i).card3 != 99999){
					p3Card3.setIcon(images[client.user.room.userArray.get(i).card3]);
				}else{
					p3Card3.setIcon(null);
				}
				if(client.user.room.userArray.get(i).state.equals("die")){
					p3Die.setVisible(true);
				}
				if(client.user.room.userArray.get(i).card2 != 99999 && client.user.room.userArray.get(i).card3 != 99999 
						&& client.user.room.userArray.get(i).card2 != 0 && client.user.room.userArray.get(i).card3 != 0){
					if(client.user.room.userArray.get(i).card2 < client.user.room.userArray.get(i).card3){
					cardsetValue=client.user.room.priority[client.user.room.userArray.get(i).card2][client.user.room.userArray.get(i).card3];
					}else{
						cardsetValue=client.user.room.priority[client.user.room.userArray.get(i).card3][client.user.room.userArray.get(i).card2];
					}
					if(cardsetValue==Integer.MAX_VALUE){
						p3Cardset.setText("삼 팔 광 땡!");
					}else if(cardsetValue==10000){
						if(client.user.selectedCard2==18 || client.user.selectedCard1==18){
							p3Cardset.setText("일 팔 광 땡!");
						}else{
							p3Cardset.setText("일 삼 광 땡!");
						}
					}else if(cardsetValue>=1000 && cardsetValue<10000){
						p3Cardset.setText((client.user.selectedCard1%10)+"땡 ");
					}else if(cardsetValue>=900 && cardsetValue<1000){
						p3Cardset.setText("알리");
					}else if(cardsetValue>=800 && cardsetValue<900){
						p3Cardset.setText("구삥");
					}else if(cardsetValue>=700 && cardsetValue<800){
						p3Cardset.setText("장삥");
					}else if(cardsetValue>=600 && cardsetValue<700){
						p3Cardset.setText("세륙");
					}else if(cardsetValue>=500 && cardsetValue<600){
						p3Cardset.setText("갑오");
					}else if(cardsetValue>=400 && cardsetValue<500){
						p3Cardset.setText("땡 잡이");
					}else if(cardsetValue>=300 && cardsetValue<400){
						p3Cardset.setText("암행어사");
					}else if(cardsetValue>=200 && cardsetValue<300){
						p3Cardset.setText("구사");
					}else{
						p3Cardset.setText(cardsetValue+"끗");
					}
					
				}
			}
					
		}
	}
	//카드를 나눠주는 애니메이션 재생 (playerNumber 나눠줄 플레이어번호, numcard 몇장 나눠줄지)
	void HandOutCard(int playerNumber, int numCard){
		new Thread(){
			public void run(){
				
				int repeat=0;
				while(true){
					if(playerNumber==3){
						cardBackLB1.setLocation(cardBackLB1.getLocation().x+10, cardBackLB1.getLocation().y);
						if(cardBackLB1.getLocation().x >=800){
							cardBackLB1.setLocation(400, 350);
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
						cardBackLB2.setLocation(cardBackLB2.getLocation().x, (cardBackLB2.getLocation().y)-10);
						if(cardBackLB2.getLocation().y <=10){
							cardBackLB2.setLocation(400, 350);
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
						cardBackLB3.setLocation(cardBackLB3.getLocation().x-10, cardBackLB3.getLocation().y);
						if(cardBackLB3.getLocation().x <=10){
							cardBackLB3.setLocation(400, 350);
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
					}else if(playerNumber==0){
						cardBackLB4.setLocation(cardBackLB4.getLocation().x, (cardBackLB4.getLocation().y)+10);
						if(cardBackLB4.getLocation().y >=700){
							cardBackLB4.setLocation(400, 350);
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
	
	void hand(int card){
		int n = client.user.playerNumber;
		try{
		for(int i=0; i< client.user.room.userArray.size();i++){
			if(!client.user.room.userArray.get(i).state.equals("die")){
			if(client.user.room.userArray.get(i).playerNumber == ((n)%4)){
				HandOutCard(0, card);
				Thread.sleep(100);
			}else if(client.user.room.userArray.get(i).playerNumber == ((n+1)%4)){
				HandOutCard(1, card);
				Thread.sleep(100);
			}else if(client.user.room.userArray.get(i).playerNumber == ((n+2)%4)){
				HandOutCard(2, card);
				Thread.sleep(100);
			}else if(client.user.room.userArray.get(i).playerNumber == ((n+3)%4)){
				HandOutCard(3, card);
				Thread.sleep(100);
			}
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	void msgSubmit(){
		try {
			client.dos.writeUTF(MsgProtocol.GAMEROOM_CHAT+"/"+textField.getText());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			textField.setText("");
		}
		
		textField.setText("");
	}
	void gameStart(){
		try {
			client.dos.writeUTF(MsgProtocol.CODE_GAMESTART);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void DrawTwoCard(){
		int n=client.user.room.userArray.size();
		for(int i=0;i < n;i++){
			//다이인놈은 안주게해야함;
			//수정
			HandOutCard(i, 2);
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void setSelectedCard1(boolean t){
		if(t){
			p0Card1.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
		}else{
			p0Card1.setBorder(null);
		}
	}
	void setSelectedCard2(boolean t){
		if(t){
			p0Card2.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
		}else{
			p0Card2.setBorder(null);
		}
	}
	void setSelectedCard3(boolean t){
		if(t){
			p0Card3.setBorder(new LineBorder(new Color(204, 255, 0), 4, true));
		}else{
			p0Card3.setBorder(null);
		}
	}
	void winAnimate(){
		new Thread(){	
			public void run() {
				winLoseLB.setVisible(true);
				winLoseLB.setText("승리!!!");
				try {
					Thread.sleep(4000);
					winLoseLB.setVisible(false);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	
	}
	void loseAnimate(){
		new Thread(){	
			public void run() {
				winLoseLB.setVisible(true);
				winLoseLB.setText("패배!!!");
				try {
					Thread.sleep(4000);
					winLoseLB.setVisible(false);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	
	}
	void setWaitLabel(boolean t){
		if(true){
		cardSelectLB.setText("다른 사람의 선택을 기다리는 중입니다.");
		}else{
			cardSelectLB.setText("공개할 카드를 선택하세요!");
		}
	}
}
