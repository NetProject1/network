import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LoginUI extends JFrame{

	private JTextField idFD;
	private JPasswordField pwFD;
	private JTextField ipFD;
	JLabel ServerAccessLB;
	JButton serverBTN;
	
	//회원가입
	SignUpUI sign=null;
	//클라이언트와 ui가 서로 참조함.
	Client client;

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
	}
	
	// 클라이언트와 붙여줬을때 사용할 생성자.
	public LoginUI(Client client) {
		this.client=client;
		initialize();
	}
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//프레임
		//frame = new JFrame();
		setTitle("로그인");
		setBounds(100, 100, 534, 287);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		//아이디 입력창
		idFD = new JTextField();
		idFD.setBounds(146, 129, 116, 21);
		getContentPane().add(idFD);
		idFD.setColumns(10);
		//비밀번호 입력창
		pwFD = new JPasswordField();
		pwFD.setColumns(10);
		pwFD.setBounds(146, 171, 116, 21);
		getContentPane().add(pwFD);
		
		//로그인 버튼
		JButton loginBTN = new JButton("로그인");
		loginBTN.setBounds(293, 128, 97, 23);
		getContentPane().add(loginBTN);
		
		//로그인 버튼 클릭시 리스너
		loginBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				LoginSubmit();
			}
		
		});
		
		//아이디 페스워드 label
		JLabel idLB = new JLabel("아이디 :");
		idLB.setBounds(58, 132, 76, 15);
		getContentPane().add(idLB);
		
		JLabel pwLB = new JLabel("비밀번호 :");
		pwLB.setBounds(58, 174, 76, 15);
		getContentPane().add(pwLB);
		
		JLabel lblNewLabel_1 = new JLabel("SERVER IP:");
		lblNewLabel_1.setBounds(58, 88, 93, 15);
		getContentPane().add(lblNewLabel_1);
		
		ipFD = new JTextField();
		ipFD.setBounds(146, 85, 116, 21);
		getContentPane().add(ipFD);
		ipFD.setColumns(10);
		//처음시작시 정해져있는 ip를 보여줌
		ipFD.setText(client.IP);
		
		serverBTN = new JButton("서버 접속");
		serverBTN.setBounds(293, 84, 142, 23);
		getContentPane().add(serverBTN);
		
		
		
		serverBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				setIpPort();
			}
			
		});
		
		JButton signupBTN = new JButton("회원가입");
		signupBTN.setBounds(293, 170, 97, 23);
		getContentPane().add(signupBTN);
		
		ServerAccessLB = new JLabel("서버에 접속하지 않았습니다.");
		ServerAccessLB.setBounds(146, 25, 268, 15);
		getContentPane().add(ServerAccessLB);
		
		signupBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(sign==null){
					sign= new SignUpUI(client);
				}else{
					sign.dispose();
					sign=new SignUpUI(client);
				}
			}
		
		});
		//프레임 보이기
		setVisible(true);
	}
	
	void LoginSubmit(){
		System.out.println(idFD.getText()+":아이디 "+pwFD.getText()+":비밀번호 로그인");
		try {
			client.dos.writeUTF(MsgProtocol.LOGIN+"/"+idFD.getText()+"/"+pwFD.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void setIpPort(){
		client.IP=ipFD.getText();
		System.out.println(client.IP+": 서버 IP ");
		client.changeIP();
	}
	void serverAccessConfirm(){
		ServerAccessLB.setText("서버에 접속했습니다.");
		serverBTN.setVisible(false);
	}
	void serverAccessFailed(){
		ServerAccessLB.setText("서버에 접속하지 않았습니다.");
		serverBTN.setVisible(true);
	}
	void closeSignUpUI(){
		if(sign!=null){
			sign.dispose();
		}
	}
}
