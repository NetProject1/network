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
		//userArray.add(user);
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
				//유저 종료시, 유저 목록과 유저가 들어있던 방에서 유저를 제거해야함.
				//방목록에서 유저가 속해있던 방 수정.
				//유저가 종료했을때 유저가 로그인상태였다면
				if(user.isLogin){
					if(user.room!=null){
						//유저가 방에 들어가있었다면
						//게임중이였다면
						//if(user.room.isGameStart)
						if(user.room.roomMaster.equals(user)){
							if(user.room.passMaster()==1){
								user.room.userArray.remove(user);
							}else{
								roomArray.remove(user.room);								
							}
						}else{
							user.room.userArray.remove(user);
						}
					}
				
					userArray.remove(user);
					user.isLogin=false;
					userList();
					roomList();
				}
				
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
			boolean isExistUSER=false;
			for(int i=0;i< userArray.size(); i++){
				if(userArray.get(i).id.equals(id)){					
					isExistUSER=true;
				}
			}

			//아이디 패스워드가 일치하면
			if(dbdao.checkIDPW(id, pw)!=1 && !isExistUSER){
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
				userArray.add(user);
				userList();
				roomList();
			}else{
				if( !isExistUSER){
				user.dos.writeUTF(MsgProtocol.LOGIN+"/Fail");
				}else{
				user.dos.writeUTF(MsgProtocol.LOGIN+"/CONN");
				}
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
		case MsgProtocol.ENTERROOM:
			rNum=token.nextToken();
			EnterRoom(rNum);
			break;
		case MsgProtocol.EXITROOM:
			ExitRoom();
			
			break;
		case MsgProtocol.WAITROOM_UPDATE:
			roomList();
			userList();
		case MsgProtocol.ROOM_UPDATE:
			if(user.room!=null){
			roomUpdate(user.room.roomNumber);
			}
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
		user.room=newRoom;
		roomList();
		//방 만들어졌으니 클라이언트쪽에서 게임방 ui 띄우도록 정보를 보내줌
		try {
			user.dos.writeUTF(MsgProtocol.MAKEROOM+"/OK/"+rnum+"/"+rName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void ExitRoom(){
		try{
			int rnum=user.room.roomNumber;
			if(user.room.isGameStart){
				user.dos.writeUTF(MsgProtocol.EXITROOM+"/FAIL/게임이 시작중입니다.");
			}else{
				if(user.room.roomMaster.equals(user)){
					if(user.room.passMaster()==1){
						user.room.userArray.remove(user);
					
					}else{
						roomArray.remove(user.room);								
					}
					
				}else{
					user.room.userArray.remove(user);
				}
				
			user.dos.writeUTF(MsgProtocol.EXITROOM+"/OK");
			user.room=null;
			userList();
			roomList();
			roomUpdate(rnum);
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	void EnterRoom(String rNum){
		try{
			Room enterRoom=null;
			int num=Integer.parseInt(rNum);
			for(int i=0; i <roomArray.size() ;i++){
				if(roomArray.get(i).roomNumber==num){
					enterRoom=roomArray.get(i);
				}
			}
			if(enterRoom!=null){
				if(!enterRoom.isGameStart){
					if(enterRoom.userArray.size()<4){
						//방에 입장하기위한 조건. 3인이하 일것.
						user.room=enterRoom;
						enterRoom.userArray.add(user);
						//방에 입장 성공적.
						//방 정보를 전달
						//같은 방인 놈들 전원 방정보 갱신해야함.
						user.dos.writeUTF(MsgProtocol.ENTERROOM+"/OK/"+user.room.roomNumber
								+"/"+user.room.roomName);
						
					}else{
						user.dos.writeUTF(MsgProtocol.ENTERROOM+"/FAIL/방이 꽉 찼습니다.");
					}
				}else{
					user.dos.writeUTF(MsgProtocol.ENTERROOM+"/FAIL/게임 중입니다.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	//현재 접속중인 방의 정보를 같은 방의 모든 클라이언트에게 업데이트함.
	void roomUpdate(int rNum){
		Room selectedRoom=null;
		for(int i=0;i< roomArray.size(); i++){
			if(roomArray.get(i).roomNumber==rNum){
				selectedRoom=roomArray.get(i);
			}
		}
		String msg=MsgProtocol.ROOM_UPDATE+selectedRoom.GetRoomINFO();
		//전달할 방을 찾아서
		for(int i=0; i <selectedRoom.userArray.size() ;i++){
			try {
				if(selectedRoom.userArray.get(i).isLogin){
				selectedRoom.userArray.get(i).dos.writeUTF(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
