import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import javax.swing.SpringLayout;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class WaitRoomUI extends JFrame{

	private JTextField chatInputFD;
	private JTable roomListTable;
	
	JTextArea chatArea;
	
	JLabel lblNewLabel, lblNewLabel_1;
	JPanel roomPN, chatPN, chatInputPN ,userListPN , playerPN;
	JButton makeRoomBTN, sendMsgBTN;
	
	JList connectUserList;
	
	JScrollPane scrollPane,roomListScroll,userListScroll;
	
	Client client;
	private JPanel panel;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel nicknameLB;
	private JLabel userMoney;
	private JLabel userWin;
	private JLabel userLose;
	
	DefaultTableModel model;
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		new WaitRoomUI();
	}
	
	/**
	 * Create the application.
	 */
	public WaitRoomUI() {
		initialize();
	}
	
	public WaitRoomUI(Client client){
		this.client=client;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBounds(100, 100, 820, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setTitle("섯다 대기실");

		roomPN = new JPanel();
		roomPN.setBounds(12, 10, 560, 258);
		getContentPane().add(roomPN);
		roomPN.setLayout(new BorderLayout(0, 0));
		
		
		makeRoomBTN = new JButton("방 생성");
		roomPN.add(makeRoomBTN, BorderLayout.SOUTH);
		makeRoomBTN.addMouseListener(new MouseAdapter() {
			//방생성 버튼을 누르면
			 public void mouseClicked(MouseEvent evt) {
				 makeRoom();
			 }
		});
		
		roomListScroll = new JScrollPane();
		roomPN.add(roomListScroll, BorderLayout.CENTER);
		
		
		String[] columNames={"방 번호","방 제목 ","방장","방 인원"};
		Object rowData[][]={
		};
		String rowd[][]={};
		model= new DefaultTableModel(rowd, columNames ){
			public boolean isCellEditable(int row,int column){
				return false;
			}
		};
		roomListTable = new JTable(model);
		roomListScroll.setViewportView(roomListTable);
		roomListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomListTable.setBorder(new LineBorder(new Color(0, 0, 0)));


		//테이블이 더블클릭 되었을때
		roomListTable.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {
				 if(evt.getClickCount()==2){
					 client.EnterRoom( roomListTable.getValueAt( roomListTable.getSelectedRow() , 0 ).toString());
			    }
			 }
		});
		
		chatPN = new JPanel();
		chatPN.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		chatPN.setBounds(12, 278, 560, 173);
		getContentPane().add(chatPN);
		chatPN.setLayout(new BorderLayout(0, 0));
		
		chatInputPN = new JPanel();
		chatPN.add(chatInputPN, BorderLayout.SOUTH);
		chatInputPN.setLayout(new BorderLayout(0, 0));
		
		chatInputFD = new JTextField();
		chatInputPN.add(chatInputFD, BorderLayout.CENTER);
		chatInputFD.setColumns(10);
		chatInputFD.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				msgSubmit();
			}
		});
		
		sendMsgBTN = new JButton("발신");
		chatInputPN.add(sendMsgBTN, BorderLayout.EAST);
		//대기실 채팅 메시지 버튼을 눌렀을때
		sendMsgBTN.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {				
				msgSubmit();

			 }
		});
		
		scrollPane = new JScrollPane();
		chatPN.add(scrollPane, BorderLayout.CENTER);
		
		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		scrollPane.setViewportView(chatArea);
		
		userListPN = new JPanel();
		userListPN.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		userListPN.setBounds(584, 278, 208, 173);
		getContentPane().add(userListPN);
		userListPN.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("접속 유저 목록");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userListPN.add(lblNewLabel, BorderLayout.NORTH);
		
		userListScroll = new JScrollPane();
		userListPN.add(userListScroll, BorderLayout.CENTER);
		
		connectUserList = new JList();
		userListScroll.setViewportView(connectUserList);
		String[] usertemplist={"유저1","유저2"};
		connectUserList.setListData(usertemplist);
		//유저 리스트에서 클릭시 
		connectUserList.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent evt) {
				 if(evt.getClickCount()==2){
				
				 }
			 }
		});
		
		playerPN = new JPanel();
		playerPN.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		playerPN.setBounds(584, 10, 208, 258);
		getContentPane().add(playerPN);
		playerPN.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel_1 = new JLabel("플레이어 정보");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		playerPN.add(lblNewLabel_1, BorderLayout.NORTH);
		
		panel = new JPanel();
		playerPN.add(panel, BorderLayout.CENTER);
		
		lblNewLabel_2 = new JLabel("게임 머니:");
		
		lblNewLabel_3 = new JLabel("승리 : ");
		
		lblNewLabel_4 = new JLabel("패배 : ");
		
		nicknameLB = new JLabel("별 명");
		
		userMoney = new JLabel("00000");
		
		userWin = new JLabel("1");
		
		userLose = new JLabel("1");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(24)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel_4)
									.addGap(18)
									.addComponent(userLose))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel_3)
									.addGap(18)
									.addComponent(userWin))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel_2)
									.addGap(18)
									.addComponent(userMoney))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(73)
							.addComponent(nicknameLB)))
					.addContainerGap(52, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(nicknameLB)
					.addGap(28)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(userMoney))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(userWin))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(userLose))
					.addContainerGap(109, Short.MAX_VALUE))
		);
		setUserInfo();
		panel.setLayout(gl_panel);
		
		setVisible(true);
	}
	void msgSubmit(){
		try {
			client.dos.writeUTF(MsgProtocol.WAITROOM_CHAT+"/"+chatInputFD.getText());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			chatInputFD.setText("");
		}
		
		chatInputFD.setText("");
	}
	void makeRoom(){
		try {
			//알림창을 띄워 방제목을 입력받는다.
			String str=(String)JOptionPane.showInputDialog(null, "방 제목 입력:","방 생성",
					 JOptionPane.PLAIN_MESSAGE, null,
					 null, "왜 이렇게 혓바닥이 길어?");
			
			client.dos.writeUTF(MsgProtocol.MAKEROOM+"/"+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void setUserInfo(){
		nicknameLB.setText(client.user.nickName);
		userMoney.setText(Integer.toString(client.user.money));
		userWin.setText(Integer.toString(client.user.win));
		userLose.setText(Integer.toString(client.user.lose));
	}
}
