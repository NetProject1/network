import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class Client implements Runnable {
	static int PORT=7777; //서버 포트
	static String IP=""; //서버 아이피
	
	Socket socket; //서버와 접속할 소켓
	DataInputStream dis; // 입출력 스트림
	DataOutputStream dos;
	boolean accessReady=false;
	
	//UI 구현시 
	LoginUI login;
	WaitRoomUI waitRoom;
	//gameroom
	
	//유저 정보 
	User user;
	
	
	public Client() {
		//생성과 함께 ui 생성 ui와 연결했을시 
		login= new LoginUI(this);
		Thread thread=new Thread(this);
		thread.start();
		
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
	
	@Override
	public void run() {
		serverAccess();
		
		//계속해서 서버로부터 메세지를 전달받아 해석한다
		while(true){
			try {
				//recieveMsg에 메세지를 받아와
				String receiveMsg= dis.readUTF();
				//해석한다
				msgParsing(receiveMsg);

			} catch (IOException e) {
				//읽어오다 에러가 나면(서버가 접속을 끊으면)
				//소켓을 닫는다.
				e.printStackTrace();
				try{
					System.out.println("서버가 종료되었습니다.");
					dis.close();
					dos.close();
					socket.close();
					//루프를 끝낸다
					break;
				}catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
		//모든 ui 종료
		uiTerminated();
	}
	//메세지를 받아와 해석해 행동함
	 synchronized void msgParsing(String receiveMsg) {
		 StringTokenizer token=new StringTokenizer(receiveMsg, "/"); //토큰
		 String protocol= token.nextToken();//토큰으로 분리된 스트링
		 String id,pw, nick;
		 String rNum, rName, rUsers;
		 String result;
		 System.out.println(protocol);
		 switch (protocol) {
		 //로그인시 ok 이면 user 정보를 갱신하고 대기실ui
		case MsgProtocol.LOGIN:
			result=token.nextToken();
				if(result.equals("OK")){
					user=new User();
					user.id=token.nextToken();
					user.pw=token.nextToken();
					user.nickName=token.nextToken();
					user.Money=Integer.parseInt(token.nextToken());
					user.win=Integer.parseInt(token.nextToken());
					user.lose=Integer.parseInt(token.nextToken());
					
					login();
				}else{
					JOptionPane.showMessageDialog(null, "로그인 실패: 아이디 비밀번호를 확인해주세요.");
				}
			break;
		case MsgProtocol.LOGOUT:
			break;
		case MsgProtocol.SIGNUP:
			break;
		}
		//로그인시
		 	//성공
		 	//실패
		 		//실패시엔 소켓 dis dos close 함.
		//게임진행시
		//채팅 받아왔을시
		
	}
	 void uiTerminated(){
		 
	 }
	 //서버 접속될때까지 기다리면서 계속 접속 수행.
	 void serverAccess(){
		 while(!accessReady){
			 try {
				 socket= new Socket(IP, PORT);
			 } catch (UnknownHostException e) {
				 e.printStackTrace();
			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 if(socket!=null){
				 if(socket.isBound()){
					 try {
						 dis=new DataInputStream(socket.getInputStream());
						 dos=new DataOutputStream(socket.getOutputStream());
					 } catch (IOException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					 accessReady=true;
				 }else{
					 try {
						 JOptionPane.showMessageDialog(null, "서버 접속 오류...");
						 Thread.sleep(100);
					 } catch (InterruptedException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
				 }
			 }else{
				 try {
					 JOptionPane.showMessageDialog(null, "서버 접속 오류...");
					 Thread.sleep(100);
				 } catch (InterruptedException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
		 }
		 
	 }
	//로그인시 로그인 정보를 받아옴. 로그인ui 를 닫고 대기실 ui를 띄움
	  void login(){

		  waitRoom =new WaitRoomUI(this);
		  login.dispose();
		 
	 }
	 //로그 아웃시 모든창을 닫고 소켓 반환
	 private void logout(){
		 
	 }

}
