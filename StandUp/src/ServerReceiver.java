
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;


import sun.applet.resources.MsgAppletViewer_es;

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
						if(!user.room.isGameStart){
							if(user.room.roomMaster.equals(user)){
								if(user.room.passMaster()==1){
									user.room.userArray.remove(user);
								}else{
									roomArray.remove(user.room);								
								}
							}else{
								user.room.userArray.remove(user);
							}
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
							//게임 초기화 한후 유저 정보를 제거
							for(int i=0;i<user.room.userArray.size();i++){
								user.room.userArray.get(i).userReset();
							}
							user.room.roomInitilize();
							String msg=MsgProtocol.CODESTOPGAME;
							msg+="/"+user.nickName+"님이 강제 종료하셨습니다. 게임을 재시작합니다.";
							GameRoomEcho(msg);
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
			
		} 
	} 
	
	synchronized public void msgParsing(String receiveMsg){
		 StringTokenizer token=new StringTokenizer(receiveMsg, "/"); //토큰
		 String protocol= token.nextToken();//토큰으로 분리된 스트링
		 String id,pw, nick;
		 String rNum, rName, rUsers;
		 String result;
		 
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
			
		case MsgProtocol.GAMEROOM_CHAT:
			result=MsgProtocol.GAMEROOM_CHAT+"/"+user.nickName+": "+token.nextToken();
			GameRoomEcho(result);
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
			break;
		case MsgProtocol.ROOM_UPDATE:
			if(user.room!=null){
			roomUpdate(user.room.roomNumber);
			}
			break;
		case MsgProtocol.CODE_GAMESTART:
			gameStart();
			break;
		case MsgProtocol.CODE_CARDOPEN:
			result=token.nextToken();
			selecteOpenCard(result);
			break;
		 case MsgProtocol.CODE_CALL:
			 betCall();
			 break;
		 case MsgProtocol.CODE_DOUBLE:
			 betDouble();
			 break;
		 case MsgProtocol.CODE_HALF:
			 betHalf();
			 break;
		 case MsgProtocol.CODE_DIE:
			 betDie();
			 break;
		 case MsgProtocol.CODE_CARDSET:
			 cardSetConfirm(token);
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
	void cardSetConfirm(StringTokenizer token){
		String nextcard1=token.nextToken();
		String nextcard2=token.nextToken();
		String nextcard3=token.nextToken();
		if(nextcard1.equals("no")){
			user.selectedCard1=user.card2;
			user.selectedCard2=user.card3;
		}else if(nextcard2.equals("no")){
			user.selectedCard1=user.card1;
			user.selectedCard2=user.card3;
		}else{
			user.selectedCard1=user.card1;
			user.selectedCard2=user.card2;
		}
		user.openCard=99999;
		
		user.isReady=true;
		if(user.room.allPlayerReady()){
			//패확인
			roomUpdate(user.room.roomNumber);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			judge();
			
		}else{
			//대기함.
			try {
				user.dos.writeUTF(MsgProtocol.CODE_GAMEWAIT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	synchronized void makeRoom(String rName){
		if(user.money>=200){
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
		//게임순서 정하기
		user.room.setPlayerOrder();
		roomList();
		//방 만들어졌으니 클라이언트쪽에서 게임방 ui 띄우도록 정보를 보내줌
		try {
			user.dos.writeUTF(MsgProtocol.MAKEROOM+"/OK/"+rnum+"/"+rName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			try {
				user.dos.writeUTF(MsgProtocol.MAKEROOM+"/FAIL/돈이 없어서 게임을 할 수 없습니다.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
						if(user.money>=200){
						user.dos.writeUTF(MsgProtocol.ENTERROOM+"/OK/"+user.room.roomNumber
								+"/"+user.room.roomName);
						}else{
							user.dos.writeUTF(MsgProtocol.ENTERROOM+"/FAIL/돈이 부족해서 게임에 참가 할 수 없습니다.");
						}
						
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
				selectedRoom.setPlayerOrder();
			}
		}
		//방정보와
		String msg=MsgProtocol.ROOM_UPDATE+selectedRoom.GetRoomINFO();
		String SendMsg="";
		//방장 정보
		
		//유저정보들
		for(int i=0; i <selectedRoom.userArray.size() ;i++){
			msg+=selectedRoom.roomMaster.getGameINFO(i);
			for(int j=0; j < selectedRoom.userArray.size();j++){
				msg+=selectedRoom.userArray.get(j).getGameINFO(i);
			}
			
			try {
				if(selectedRoom.userArray.get(i).isLogin){
				selectedRoom.userArray.get(i).dos.writeUTF(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msg=MsgProtocol.ROOM_UPDATE+selectedRoom.GetRoomINFO();
		}
		
		if(selectedRoom.isGameStart!=true){
			//거지를 강퇴시킨다
			GetOutFoor(rNum);
			
		}
		
	}
	void GetOutFoor(int num){
		Room selectedRoom=null;
		for(int i=0;i< roomArray.size(); i++){
			if(roomArray.get(i).roomNumber==num){
				selectedRoom=roomArray.get(i);
				selectedRoom.setPlayerOrder();
			}
		}
		for(int i=0;i< selectedRoom.userArray.size(); i++){
			if(selectedRoom.userArray.get(i).money<200){
				try {
					String msg=MsgProtocol.GETOUTFOOR;
					selectedRoom.userArray.get(i).dos.writeUTF(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	void GameRoomEcho(String msg){
		for(int i=0; i <user.room.userArray.size() ;i++){
			try {
				user.room.userArray.get(i).dos.writeUTF(msg);			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void gameStart(){
		//카드를 섞고 두장씩 배부
		if(user.room.userArray.size()>=2){
		user.room.isGameStart=true;
		user.room.setFirstMoney();
		user.room.setPlayersCardBack1();
		user.room.dealCard();
		
		user.room.drawTwoCards();
		
		user.room.gameState="start";
		String msg=MsgProtocol.CODE_GAMESTART+"/OK";
		GameRoomEcho(msg);
		}else{
			String msg=MsgProtocol.CODE_GAMESTART+"/FAIL";
			try {
				user.dos.writeUTF(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	//카드를 오픈한다.
	 void selecteOpenCard(String msg){
		if(msg.equals("CARD1")){
			user.openCard=user.card1;
		}else if(msg.equals("CARD2")){
			user.openCard=user.card2;
		}
		
		user.isReady=true;
		if(user.room.allPlayerReady()){
			user.room.gameState="bet";
			user.room.SetNotReady();
		}
		roomUpdate(user.room.roomNumber);
	}
	void betCall(){
		if(user.money>= user.room.bet){
		user.state="call";
		user.money-=user.room.bet;
		user.room.amountMoney+=user.room.bet;
		//모두 콜이거나 한명만 남고 die인지 확인한다. 
		//모두 콜이면 2리턴,
		//한명빼고 다이면 1리턴, 계속진행이면 0리턴,
		if(user.room.isEndBetState()==2){
			user.room.gameState="cardset";
			user.room.SetNotReady();
			
			//3번째 카드를 안보이게 세팅한다.
			user.room.setPlayersCardBack2();
			//카드 1장씩 배부
			user.room.drawOneCard();
			//최종카드 선택 
			String msg=MsgProtocol.CODE_CARDSET;
			GameRoomEcho(msg);
			user.room.setNextPlayerTurn();
		}else if(user.room.isEndBetState()==1){
			user.room.gameState="end";
			user.room.isGameStart=false;
			//승자 정보 패자 정보 줌.
			try{
			String msg;
			for(int i=0;i< user.room.userArray.size();i++){
				if(userArray.get(i).state.equals("die")){
					userArray.get(i).lose+=1;
					dbdao.updateLoseMoney(userArray.get(i).id,userArray.get(i).lose,userArray.get(i).money);
					msg=MsgProtocol.CODE_GAMEEND+"/LOSE";
					userArray.get(i).dos.writeUTF(msg);
				}else{
					userArray.get(i).money+=user.room.amountMoney;
					userArray.get(i).win+=1;
					dbdao.updateWinMoney(userArray.get(i).id,userArray.get(i).win,userArray.get(i).money);
					msg=MsgProtocol.CODE_GAMEEND+"/WIN";
					userArray.get(i).dos.writeUTF(msg);
				}
				//유저 게임정보를 리셋합니다
				userArray.get(i).userReset();
				
			}
			//게임방의 정보를 리셋합니다.
			RoomUsersUpdate();	
			user.room.roomInitilize();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}else if(user.room.isEndBetState()==0){
			user.room.setNextPlayerTurn();
			roomUpdate(user.room.roomNumber);
		}
		
		}else{
			try {
				user.dos.writeUTF(MsgProtocol.CODE_CALL+"/FAIL/금액이 부족합니다.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	void betDouble(){
		if(user.money>= user.room.bet*2){
		user.state="double";
		user.room.callReset();
		user.room.bet=user.room.bet*2;
		user.money-=user.room.bet;
		user.room.amountMoney+=user.room.bet;
		user.room.setNextPlayerTurn();
		roomUpdate(user.room.roomNumber);
		}else{
			try {
				user.dos.writeUTF(MsgProtocol.CODE_DOUBLE+"/FAIL/금액이 부족합니다.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void betHalf(){
		if(user.money >= ( user.room.bet + (user.room.amountMoney/2))){
		user.state="half";
		user.room.callReset();
		user.room.bet+=user.room.amountMoney/2;
		user.money-=user.room.bet;
		user.room.amountMoney+=user.room.bet;
		user.room.setNextPlayerTurn();
		roomUpdate(user.room.roomNumber);
		}else{
			try {
				user.dos.writeUTF(MsgProtocol.CODE_HALF+"/FAIL/금액이 부족합니다.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void betDie(){
		user.state="die";
		//모두 콜이거나 한명만 남고 die인지 확인한다.
		if(user.room.isEndBetState()==2){
			user.room.gameState="cardset";
			user.room.SetNotReady();
			
			//3번째 카드를 안보이게 세팅한다.
			user.room.setPlayersCardBack2();
			//카드 1장씩 배부
			user.room.drawOneCard();
			//최종카드 선택 
			String msg=MsgProtocol.CODE_CARDSET;
			GameRoomEcho(msg);
		}else if(user.room.isEndBetState()==1){
			user.room.gameState="end";
			user.room.isGameStart=false;
			//승자 정보 패자 정보 줌.
			try{
			String msg;
			for(int i=0;i< user.room.userArray.size();i++){
				if(userArray.get(i).state.equals("die")){
					userArray.get(i).lose+=1;
					dbdao.updateLoseMoney(userArray.get(i).id,userArray.get(i).lose,userArray.get(i).money);
					msg=MsgProtocol.CODE_GAMEEND+"/LOSE";
					userArray.get(i).dos.writeUTF(msg);
					
				}else{
					userArray.get(i).money+=user.room.amountMoney;
					userArray.get(i).win+=1;
					dbdao.updateWinMoney(userArray.get(i).id,userArray.get(i).win,userArray.get(i).money);
					msg=MsgProtocol.CODE_GAMEEND+"/WIN";
					userArray.get(i).dos.writeUTF(msg);
				}
				//유저 게임정보를 리셋합니다
				userArray.get(i).userReset();
				
			}
			RoomUsersUpdate();	
			user.room.roomInitilize();
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}else if(user.room.isEndBetState()==0){
			user.room.setNextPlayerTurn();
			roomUpdate(user.room.roomNumber);
		}
		
	}
	void judge(){
		int winnumber=user.room.judge();
		System.out.println("승자 번호:"+winnumber);
		//1000번은 재경기이다.
		if(winnumber<1000){
		user.room.gameState="end";
		user.room.isGameStart=false;
		//승자 정보 패자 정보 줌.
		try{
		String msg;
		for(int i=0;i< user.room.userArray.size();i++){
			if(user.room.userArray.get(i).playerNumber !=winnumber){
				userArray.get(i).lose+=1;
				dbdao.updateLoseMoney(userArray.get(i).id,userArray.get(i).lose,userArray.get(i).money);
				msg=MsgProtocol.CODE_GAMEEND+"/LOSE";
				userArray.get(i).dos.writeUTF(msg);
				
			}else{
				userArray.get(i).money+=user.room.amountMoney;
				userArray.get(i).win+=1;
				dbdao.updateWinMoney(userArray.get(i).id,userArray.get(i).win,userArray.get(i).money);
				msg=MsgProtocol.CODE_GAMEEND+"/WIN";
				userArray.get(i).dos.writeUTF(msg);
			}
			//유저 게임정보를 리셋합니다
			userArray.get(i).userReset();
			
		}
		RoomUsersUpdate();		
		user.room.roomInitilize();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		}else if(winnumber==1000){
			//재경기
			GooSaReStart();
		}else if(winnumber==2000){
			SameCardReStart();
		}
	}
	void RoomUsersUpdate(){
		for(int i=0;i <user.room.userArray.size();i++){
			try {
				user.room.userArray.get(i).dos.writeUTF
				(MsgProtocol.USERUPDATE+"/"+dbdao.updateUserINFO(user.room.userArray.get(i).id));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//구사시 재시작
	void GooSaReStart(){
		//카드를 섞고 두장씩 배부
		for(int i=0; i< user.room.userArray.size();i++){
			if(!user.room.userArray.get(i).state.equals("die")){
			  user.room.userArray.get(i).userRestart();
			}
		}
		
		user.room.cardPoint=0;
		user.room.setPlayersCardBack1();
		user.room.dealCard();
		
		user.room.drawTwoCards();
		
		user.room.gameState="start";
		String msg=MsgProtocol.CODE_GAMESTART+"/OK";
		GameRoomEcho(msg);
	
	}
	//같은 크기의 패끼리 재시합
	void SameCardReStart(){
		int maxValue=0;
		for(int i=0; i< user.room.userArray.size();i++){
			if(!user.room.userArray.get(i).state.equals("die")){
			if(user.room.userArray.get(i).cardValue>=maxValue){
				maxValue=user.room.userArray.get(i).cardValue;
			}
			}
		}
		//최고 패가 아닌 사람은 다 죽는다!
		for(int i=0; i< user.room.userArray.size();i++){
			if(!user.room.userArray.get(i).state.equals("die")){
				if(user.room.userArray.get(i).cardValue<maxValue){
					user.room.userArray.get(i).state="die";
				}
			}
		}
		for(int i=0; i< user.room.userArray.size();i++){
			if(!user.room.userArray.get(i).state.equals("die")){
			  user.room.userArray.get(i).userRestart();
			}
		}
		user.room.cardPoint=0;
		user.room.setPlayersCardBack1();
		user.room.dealCard();
		
		user.room.drawTwoCards();
		
		user.room.gameState="start";
		String msg=MsgProtocol.CODE_GAMESTART+"/OK";
		GameRoomEcho(msg);
		
	}
}
