
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class SignUpUI extends JFrame{
	private JTextField idFD;
	private JTextField pwFD;
	private JTextField nicknameFD;
	
	Client client;
	
	public SignUpUI(Client cli) {
		this.client=cli;
		initialize();
	}

	
	private void initialize() {
		
		setBounds(100, 100, 429, 362);
		setTitle("회원가입");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\uD68C\uC6D0\uAC00\uC785", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLACK));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		idFD = new JTextField();
		idFD.setBounds(220, 72, 116, 21);
		panel.add(idFD);
		idFD.setColumns(10);
		
		pwFD = new JTextField();
		pwFD.setBounds(220, 126, 116, 21);
		panel.add(pwFD);
		pwFD.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("아이디 :");
		lblNewLabel.setBounds(112, 75, 57, 15);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("비밀번호 :");
		lblNewLabel_1.setBounds(112, 129, 57, 15);
		panel.add(lblNewLabel_1);
		
		nicknameFD = new JTextField();
		nicknameFD.setBounds(220, 184, 116, 21);
		panel.add(nicknameFD);
		nicknameFD.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("별명 :");
		lblNewLabel_2.setBounds(112, 187, 85, 15);
		panel.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("가입");
		btnNewButton.setBounds(239, 268, 97, 23);
		panel.add(btnNewButton);
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				sendSignUPMsg();
			}
		
		});
		
		setVisible(true);
	}
	
	void sendSignUPMsg(){
		String tem;
		try {
			//아이디 칸과 비밀번호 칸이 공백이 아니라면
			if(!(idFD.getText().equals(""))){
				if(!(pwFD.getText().equals(""))){
					if(!(nicknameFD.getText().equals(""))){
						//메세지 보내기
						client.dos.writeUTF(MsgProtocol.SIGNUP+"/"+idFD.getText()+"/"+pwFD.getText()+"/"
								+nicknameFD.getText());
					}else{
						//닉네임 창이 비었을 경우
						JOptionPane.showMessageDialog(null, "닉네임 미입력");
					}
				}else{
					//비밀번호가 비었을 경우
					JOptionPane.showMessageDialog(null, "비밀번호 미입력");
				}
			}else{
				//아이디 창이 비었을경우
				JOptionPane.showMessageDialog(null, "아이디 미입력");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
