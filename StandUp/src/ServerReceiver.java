import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class ServerReceiver extends Thread {
	ArrayList<User> userArray;
	ArrayList<Room> roomArray;
	ArrayList<User> WRUserArray;
	User user;
	Socket socket;
	DBDAO dbdao=new DBDAO();
	
	ServerReceiver(Socket socket,User user, ArrayList<User> userArray,ArrayList<Room> roomArray) {
		this.socket = socket;
		this.userArray=userArray;
		this.roomArray=roomArray;
		this.user=user;
		userArray.add(user);
	}
	public void run() {
		try {
			while(true){
			String receiveMsg= user.dis.readUTF();
			//해석한다
			msgParsing(receiveMsg);
			}
		} catch(IOException e) {
			// ignore
		} finally {
			try {
				user.dis.close();
				user.dos.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"에서 접속을 종료하였습니다.");
			//System.out.println("현재 서버접속자 수는 "+ clients.size()+"입니다.");
		} // try
	} // run
	
	synchronized public void msgParsing(String receiveMsg){
		 StringTokenizer token=new StringTokenizer(receiveMsg, "/"); //토큰
		 String protocol= token.nextToken();//토큰으로 분리된 스트링
		 String id,pw, nick;
		 String rNum, rName, rUsers;
		 String result;
		 System.out.println(protocol);
		 try{
		 switch (protocol) {
		
		case MsgProtocol.LOGIN:
			id=token.nextToken();
			pw=token.nextToken();
			if(dbdao.checkIDPW(id, pw)!=1){
				result=dbdao.login(id, pw);
				user.dos.writeUTF(MsgProtocol.LOGIN+"/OK/"+result);
				token=new StringTokenizer(result,"/");
				user.id=token.nextToken();
				user.pw=token.nextToken();
				user.nickName=token.nextToken();
				user.money=Integer.parseInt(token.nextToken());
				user.win=Integer.parseInt(token.nextToken());
				user.lose=Integer.parseInt(token.nextToken());
				user.isLogin=true;
				userList();
			}else{
				user.dos.writeUTF(MsgProtocol.LOGIN+"/Fail");		
			}
			break;
		case MsgProtocol.LOGOUT:
			break;
		case MsgProtocol.SIGNUP:
			id=token.nextToken();
			pw=token.nextToken();
			nick=token.nextToken();
			//아이디가 존재하지않음
			if(dbdao.checkID(id)==0){
				if(dbdao.signUp(id, pw, nick)==0){
					user.dos.writeUTF(MsgProtocol.SIGNUP+"/OK");
					break;
				}else{
					user.dos.writeUTF(MsgProtocol.SIGNUP+"/FAIL");
				}
			}else{
			//존재함
				user.dos.writeUTF(MsgProtocol.SIGNUP+"/FAIL");
			}
			
			break;
		case MsgProtocol.MAKEROOM:
			rName=token.nextToken();
			makeRoom(rName);
			break;
		case MsgProtocol.WAITROOM_CHAT:
			result=MsgProtocol.WAITROOM_CHAT+"/"+user.nickName+": "+token.nextToken();
			loginEchoMsg(result);
			break;

			}
		 }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		//로그인시
		 	//성공
		 	//실패
		 	
		//게임진행시
		//채팅 받아왔을시
		
	}
	synchronized void makeRoom(String rName){
		int rnum=0;
		if(roomArray.size()!=0){
			for(int i=0; i <roomArray.size() ;i++){
				rnum=roomArray.get(i).roomNumber;
			}
		}
		rnum+=1;
		Room newRoom=new Room(rnum,rName,user);
		roomArray.add(newRoom);
		System.out.println("방만들었다");
		roomList();
		
		
	}
	//로그인한 유저들에게 메세지전달 
	void loginEchoMsg(String msg){
		for(int i=0; i <userArray.size() ;i++){
			try {
				if(userArray.get(i).isLogin){
				System.out.println(userArray.get(i).nickName+"에게 메세지를 보냅니다");
				userArray.get(i).dos.writeUTF(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void roomList(){
		String str="";
		for(int i=0; i <roomArray.size() ;i++){
			str+="/"+roomArray.get(i).roomNumber+
					"/"+roomArray.get(i).roomName+
					"/"+roomArray.get(i).roomMaster.nickName+
					"/"+roomArray.get(i).userArray.size();
		}
		String msg=MsgProtocol.ROOMLIST_UPDATE+str;
		loginEchoMsg(msg);
	}
	void userList(){
		String str="";
		for(int i=0; i <userArray.size() ;i++){
			str+="/"+userArray.get(i).nickName;
		}
		String msg=MsgProtocol.USERLIST_UPDATE+str;
		loginEchoMsg(msg);
	}
	
}
