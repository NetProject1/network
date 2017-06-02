import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Client implements Runnable {
	static int PORT=7777; //서버 포트
	static String IP="111.111.111.111"; //서버 아이피
	
	Socket socket; //서버와 접속할 소켓
	DataInputStream dis; // 입출력 스트림
	DataOutputStream dos;
	boolean accessReady=false;
	
	//UI 구현시 
	LoginUI login;
	WaitRoomUI waitRoom;
	GameRoomUI gameroom;
	
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
	//	serverAccess();
	//	login.serverAccessConfirm();
		
		while(!accessReady){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
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
		 try{
		 
		 switch (protocol) {
		 //로그인시 ok 이면 user 정보를 갱신하고 대기실ui
		case MsgProtocol.LOGIN:
			result=token.nextToken();
				if(result.equals("OK")){
					user.id=token.nextToken();
					user.pw=token.nextToken();
					user.nickName=token.nextToken();
					user.money=Integer.parseInt(token.nextToken());
					user.win=Integer.parseInt(token.nextToken());
					user.lose=Integer.parseInt(token.nextToken());
					user.isLogin=true;
					login();
				}else{
					if(result.equals("CONN")){
						JOptionPane.showMessageDialog(null, "로그인 실패: 이미 접속중입니다.");
					}else{
						JOptionPane.showMessageDialog(null, "로그인 실패: 아이디 비밀번호를 확인해주세요.");
					}
				}
			break;
		case MsgProtocol.LOGOUT:
			break;
		case MsgProtocol.SIGNUP:
			result=token.nextToken();
			if(result.equals("OK")){
				JOptionPane.showMessageDialog(null, "회원가입 성공!");
				login.closeSignUpUI();
			}else{
				JOptionPane.showMessageDialog(null, "회원가입 오류!");
			}
			break;
		case MsgProtocol.ROOMLIST_UPDATE:
			roomList(token);
			break;
		case MsgProtocol.WAITROOM_CHAT:
			result=token.nextToken()+"\n";
			chatRecieve(result);
			break;
		case MsgProtocol.USERLIST_UPDATE:
			userList(token);
			break;
		case MsgProtocol.MAKEROOM:
			result=token.nextToken();
			if(result.equals("OK")){
				rNum=token.nextToken();
				rName=token.nextToken();
				Room newroom=new Room(Integer.parseInt(rNum),
						rName, user);
				user.room=newroom;
				gameroom=new GameRoomUI(this);
				waitRoom.dispose();
				//수정
				user.dos.writeUTF(MsgProtocol.ROOM_UPDATE);
			}

			break;
		case MsgProtocol.ENTERROOM:
			result=token.nextToken();
			if(result.equals("OK")){
				rNum=token.nextToken();
				rName=token.nextToken();
				user.room=new Room(Integer.parseInt(rNum),rName,new User());
				gameroom=new GameRoomUI(this);
				waitRoom.dispose();
				
				user.dos.writeUTF(MsgProtocol.ROOM_UPDATE);

			}else{
				result=token.nextToken();
				 JOptionPane.showMessageDialog(null, result);
			}
			gameroom.roomUpdate();
			break;
		case MsgProtocol.EXITROOM:
			result=token.nextToken();
			if(result.equals("OK")){
				user.room=null;
				waitRoom=new WaitRoomUI(this);
				gameroom.dispose();
				try {
					user.dos.writeUTF(MsgProtocol.WAITROOM_UPDATE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			break;
		case MsgProtocol.ROOM_UPDATE:
			roomUpdate(token);
			break;
		}
		 
		 }catch (Exception e) {

		}
		//게임진행시
		
		
	}
	 void uiTerminated(){
		 
	 }
	 //서버 접속
	 void serverAccess(){
		// while(!accessReady){
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
						 user= new User(dis, dos);
					 } catch (IOException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					 login.serverAccessConfirm();
					 accessReady=true;
				 }else{
					 try {
						 JOptionPane.showMessageDialog(null, "서버 접속 오류...");
						 login.serverAccessFailed();
						 Thread.sleep(200);
					 } catch (InterruptedException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
				 }
			 }else{
				 try {
					 JOptionPane.showMessageDialog(null, "서버 접속 오류...");
					 login.serverAccessFailed();
					 Thread.sleep(200);
				 } catch (InterruptedException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
	//	 }
		 
	 }
	//로그인시 로그인 정보를 받아옴. 로그인ui 를 닫고 대기실 ui를 띄움
	  void login(){
		  waitRoom =new WaitRoomUI(this);
		  login.dispose();
		 
	 }
	 //로그 아웃시 모든창을 닫고 소켓 반환
	  void logout(){
		 
	 }
	  void EnterRoom(String str){
		  try {
			  //방번호와 프로토콜을 서버에 보냄.
			user.dos.writeUTF(MsgProtocol.ENTERROOM+"/"+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  void ExitRoom(){
		  //게임중이 아닐때
		  try {
			user.dos.writeUTF(MsgProtocol.EXITROOM);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	synchronized void roomList(StringTokenizer token){
		 String rNum, rName, rMaster,rUsers;
		 if(waitRoom.model!=null){
			 //테이블 모델을 비운다.
			 waitRoom.model.setRowCount(0);
		 }
		 
		 while(token.hasMoreTokens()){
			 rNum=token.nextToken();
			 rName=token.nextToken();
			 rMaster=token.nextToken();
			 rUsers=token.nextToken();
			 String[] rowData={rNum,rName,rMaster,rUsers};
			 waitRoom.model.addRow(rowData);
			 System.out.println(waitRoom.model.getRowCount());
		 }
		 
	 }
	synchronized void userList(StringTokenizer token){
		if(waitRoom.connectUserList!=null){
			waitRoom.connectUserList.removeAll();
		}
		Vector vec=new Vector();
		String str;
		while(token.hasMoreTokens()){
			str=token.nextToken();
			vec.addElement(str);
		}
		waitRoom.connectUserList.setListData(vec);
	}
	void chatRecieve(String str){
		waitRoom.chatArea.append(str);
		waitRoom.chatArea.setCaretPosition(waitRoom.chatArea.getDocument().getLength());
	}
	//게임 시작여부 룸마스터 정보 유저정보순으로 받아온다
	//수정요함
	void roomUpdate(StringTokenizer token){
		if(user.room!=null){
		String isGameStart=token.nextToken();
		if(isGameStart.equals("true")){
			user.room.isGameStart=true;
		}else{
			user.room.isGameStart=false;
		}
		User newuser;
		String id=token.nextToken();
		String nick=token.nextToken();
		int money=Integer.parseInt(token.nextToken());
		int win=Integer.parseInt(token.nextToken());
		int lose=Integer.parseInt(token.nextToken());
		if(id.equals(user.id)){
			user.room.roomMaster=user;
		}else{
			newuser=new User(id, nick, money, win, lose);
			user.room.roomMaster=newuser;
		}
		
		user.room.userArray.clear();
		while(token.hasMoreTokens()){
			 id=token.nextToken();
			 nick=token.nextToken();
			 money=Integer.parseInt(token.nextToken());
			 win=Integer.parseInt(token.nextToken());
			 lose=Integer.parseInt(token.nextToken());
			 if(user.room.roomMaster.id.equals(id)){
				 user.room.userArray.add(user.room.roomMaster);
			 }else{
				 if(id.equals(user.id)){
						user.room.userArray.add(user);
					}else{
						 newuser=new User(id, nick, money, win, lose);
						 user.room.userArray.add(newuser);
					}	
			 }
					
		}
		}else{
			JOptionPane.showMessageDialog(null, "방이없는데 업데이트하라니?");
		}
		gameroom.roomUpdate();
	}
	
	public void changeIP(){
		serverAccess();
	}
	
}
