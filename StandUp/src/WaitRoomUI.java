import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		new WaitRoomUI();
	}
	*/
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
		
		roomListScroll = new JScrollPane();
		roomPN.add(roomListScroll, BorderLayout.CENTER);
		
		roomListTable = new JTable();
		roomListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomListTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		roomListTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"\uBC29 \uBC88\uD638", "\uBC29 \uC81C\uBAA9", "\uBC29\uC7A5", "\uC778\uC6D0"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		roomListTable.getColumnModel().getColumn(0).setResizable(false);
		roomListTable.getColumnModel().getColumn(0).setPreferredWidth(57);
		roomListTable.getColumnModel().getColumn(1).setResizable(false);
		roomListTable.getColumnModel().getColumn(1).setPreferredWidth(167);
		roomListTable.getColumnModel().getColumn(2).setResizable(false);
		roomListTable.getColumnModel().getColumn(2).setPreferredWidth(90);
		roomListTable.getColumnModel().getColumn(3).setResizable(false);
		roomListScroll.setViewportView(roomListTable);
		
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
		
		sendMsgBTN = new JButton("발신");
		chatInputPN.add(sendMsgBTN, BorderLayout.EAST);
		
		scrollPane = new JScrollPane();
		chatPN.add(scrollPane, BorderLayout.CENTER);
		
		chatArea = new JTextArea();
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
		
		playerPN = new JPanel();
		playerPN.setBounds(584, 10, 208, 258);
		getContentPane().add(playerPN);
		playerPN.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel_1 = new JLabel("플레이어 정보");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		playerPN.add(lblNewLabel_1, BorderLayout.NORTH);
		setVisible(true);
	}
}
