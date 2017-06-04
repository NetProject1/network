
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import jdk.internal.org.objectweb.asm.Handle;

public class Client implements Runnable {
	static int PORT=9977; //서버 포트
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
	//최종 카드 구성시 카드 번호 저장
	boolean card1select=false;
	boolean card2select=false;
	boolean card3select=false;
	
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
					if(gameroom!=null){
						gameroom.dispose();
					}
					if(waitRoom!=null){
						waitRoom.dispose();
					}
					if(login!=null){
						login.serverAccessFailed();
					}
					
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
		 System.out.println(receiveMsg);
		 StringTokenizer token=new StringTokenizer(receiveMsg, "/"); //토큰
		 String protocol= token.nextToken();//토큰으로 분리된 스트링
		 String id,pw, nick;
		 String rNum, rName, rUsers;
		 String result;
		
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
		case MsgProtocol.GAMEROOM_CHAT:
			result=token.nextToken()+"\n";
			if(gameroom!=null){
			gameRoomChatRecieve(result);
			}
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
			}else{
				result=token.nextToken();
				JOptionPane.showMessageDialog(null, result);
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
		case MsgProtocol.CODE_GAMESTART:
			result=token.nextToken();
			if(result.equals("OK")){
			//게임이 시작되었다. 카드 배부 애니메이션을 실행한다. 요건 나중에 수정예정
			gameroom.hand(2);
			user.dos.writeUTF(MsgProtocol.ROOM_UPDATE);
			}else{
				JOptionPane.showMessageDialog(null,"인원이 부족합니다.");
			}
			//그 후 정보를 갱신 받는다. 갱신 받고 카드 선택 화면으로 넘어간다.
			
			break;
		case MsgProtocol.CODE_CARDSET:
			//카드 뿌려주는거 수정
			gameroom.hand(1);
			user.dos.writeUTF(MsgProtocol.ROOM_UPDATE);
			break;
		case MsgProtocol.CODE_GAMEEND:
			result=token.nextToken();
			if(result.equals("WIN")){
				//gameroom 이긴거 애니매이션
				gameroom.winAnimate();
				Thread.sleep(4000);
				user.userReset();
			}else{
				//gameroom 진거 애니메이션
				gameroom.loseAnimate();
				Thread.sleep(4000);
				user.userReset();
			}
			user.dos.writeUTF(MsgProtocol.ROOM_UPDATE);
			
			break;
		case MsgProtocol.USERUPDATE:
			user.id=token.nextToken();
			user.pw=token.nextToken();
			user.nickName=token.nextToken();
			user.money=Integer.parseInt(token.nextToken());
			user.win=Integer.parseInt(token.nextToken());
			user.lose=Integer.parseInt(token.nextToken());
			break;
		case MsgProtocol.CODESTOPGAME:
			result=token.nextToken();
			JOptionPane.showMessageDialog(null, result);
			user.dos.writeUTF(MsgProtocol.ROOM_UPDATE);
		case MsgProtocol.CODE_CALL:
			result=token.nextToken();
			if(result.equals("FAIL")){
				result=token.nextToken();
				JOptionPane.showMessageDialog(null, result);
			}
			break;
		case MsgProtocol.CODE_HALF:
			result=token.nextToken();
			if(result.equals("FAIL")){
				result=token.nextToken();
				JOptionPane.showMessageDialog(null, result);
			}
			break;
		case MsgProtocol.CODE_DOUBLE:
			result=token.nextToken();
			if(result.equals("FAIL")){
				result=token.nextToken();
				JOptionPane.showMessageDialog(null, result);
			}
			break;
		case MsgProtocol.GETOUTFOOR:
	
			ExitRoom();
			JOptionPane.showMessageDialog(null, "돈이 부족하여 게임에 참가 할 수 없습니다.");
			break;
		case MsgProtocol.CODE_GAMEWAIT:
			gameroom.setWaitLabel(true);
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
	
	void gameRoomChatRecieve(String str){
		gameroom.textArea.append(str);
		gameroom.textArea.setCaretPosition(waitRoom.chatArea.getDocument().getLength());
	}
	//게임 방정보 룸마스터정보 유저정보순으로 받아온다
	//수정요함
	void roomUpdate(StringTokenizer token){
		if(user.room!=null){
		String isGameStart=token.nextToken();
		if(isGameStart.equals("true")){
			user.room.isGameStart=true;
		}else{
			user.room.isGameStart=false;
		}
		String gameState=token.nextToken();
		user.room.gameState=gameState;
		String msg=token.nextToken();
		user.room.bet=Integer.parseInt(msg);
		msg=token.nextToken();
		user.room.amountMoney=Integer.parseInt(msg);
		msg=token.nextToken();
		user.room.playerturn=Integer.parseInt(msg);
		
		User newuser;
		String isReady=token.nextToken();
		String state=token.nextToken();
		String id=token.nextToken();
		String nick=token.nextToken();
		int money=Integer.parseInt(token.nextToken());
		int win=Integer.parseInt(token.nextToken());
		int lose=Integer.parseInt(token.nextToken());
		int playernum=Integer.parseInt(token.nextToken());
		int card1=Integer.parseInt(token.nextToken());
		int card2=Integer.parseInt(token.nextToken());
		int card3=Integer.parseInt(token.nextToken());
		
		if(id.equals(user.id)){
			user.room.roomMaster=user;
			user.playerNumber=playernum;
			if(isReady.equals("true")){
				user.isReady=true;
			}else{
				user.isReady=false;
			}
			user.money=money;
			user.state=state;
			user.card1=card1;
			user.card2=card2;
			user.card3=card3;
		}else{
			newuser=new User(id, nick, money, win, lose);
			if(isReady.equals("true")){
				newuser.isReady=true;
			}else{
				newuser.isReady=false;
			}
			newuser.state=state;
			newuser.playerNumber=playernum;
			newuser.card1=card1;
			newuser.card2=card2;
			newuser.card3=card3;
			user.room.roomMaster=newuser;
		}
		
		user.room.userArray.clear();
		while(token.hasMoreTokens()){
			 isReady=token.nextToken();
			 state=token.nextToken();
			 id=token.nextToken();
			 nick=token.nextToken();
			 money=Integer.parseInt(token.nextToken());
			 win=Integer.parseInt(token.nextToken());
			 lose=Integer.parseInt(token.nextToken());
			 playernum=Integer.parseInt(token.nextToken());
			 card1=Integer.parseInt(token.nextToken());
			 card2=Integer.parseInt(token.nextToken());
			 card3=Integer.parseInt(token.nextToken());
			 
			 if(user.room.roomMaster.id.equals(id)){
				 user.room.userArray.add(user.room.roomMaster);
			 }else{
				 if(id.equals(user.id)){
						user.room.userArray.add(user);
						if(isReady.equals("true")){
							user.isReady=true;
						}else{
							user.isReady=false;
						}
						user.money=money;
						user.state=state;
						user.playerNumber=playernum;
						user.card1=card1;
						user.card2=card2;
						user.card3=card3;
					}else{
						 newuser=new User(id, nick, money, win, lose);
						 if(isReady.equals("true")){
							 newuser.isReady=true;
							}else{
								newuser.isReady=false;
							}
						 newuser.state=state;
						 newuser.playerNumber=playernum;
						newuser.card1=card1;
						newuser.card2=card2;
						newuser.card3=card3;
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
	public void choseCard1(){
		try {
			System.out.println(user.room.gameState);
			if(user.room.gameState.equals("start") && !user.isReady&& !user.state.equals("die")){
			user.dos.writeUTF(MsgProtocol.CODE_CARDOPEN+"/CARD1");
			gameroom.setSelectedCard1(true);
			}
			if(user.room.gameState.equals("cardset") && !user.isReady&& !user.state.equals("die")){
				if(!card1select){
				gameroom.setSelectedCard1(true);
				card1select=true;
				}else{
					gameroom.setSelectedCard1(false);
					card1select=false;
				}
				int i=0;
				String card1="no";
				String card2="no";
				String card3="no";
				if(card1select){
					i++;
					card1="card1";
				}
				if(card2select){
					i++;
					card2="card2";
				}
				if(card3select){
					i++;
					card3="card3";
				}
				if(i==2){
					int q=JOptionPane.showConfirmDialog(null, "이렇게 최종 패를 결정하시겠습니까?");
					if(q==0){
						try {
							
							user.dos.writeUTF(MsgProtocol.CODE_CARDSET+"/"+card1
									+"/"+card2+"/"+card3);
							if(card1select){
								if(card2select){
									user.selectedCard1=user.card1;
									user.selectedCard2=user.card2;
								}else{
									user.selectedCard1=user.card1;
									user.selectedCard2=user.card3;
								}
							}else{
								user.selectedCard1=user.card2;
								user.selectedCard2=user.card3;
							}
							user.isReady=true;
							card1select=false;
							card2select=false;
							card3select=false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void choseCard2(){
		try {
			System.out.println(user.room.gameState);
			if(user.room.gameState.equals("start") && !user.isReady && !user.state.equals("die")){
				user.dos.writeUTF(MsgProtocol.CODE_CARDOPEN+"/CARD2");
				gameroom.setSelectedCard2(true);
			}
			if(user.room.gameState.equals("cardset") && !user.isReady && !user.state.equals("die")){
				if(!card2select){
				gameroom.setSelectedCard2(true);
				card2select=true;
				}else{
					gameroom.setSelectedCard2(false);
					card2select=false;
				}
				int i=0;
				String card1="no";
				String card2="no";
				String card3="no";
				if(card1select){
					i++;
					card1="card1";
				}
				if(card2select){
					i++;
					card2="card2";
				}
				if(card3select){
					i++;
					card3="card3";
				}
				if(i==2){
					int q=JOptionPane.showConfirmDialog(null, "이렇게 최종 패를 결정하시겠습니까?");
					if(q==0){
						try {
							
							user.dos.writeUTF(MsgProtocol.CODE_CARDSET+"/"+card1
									+"/"+card2+"/"+card3);
							if(card1select){
								if(card2select){
									user.selectedCard1=user.card1;
									user.selectedCard2=user.card2;
								}else{
									user.selectedCard1=user.card1;
									user.selectedCard2=user.card3;
								}
							}else{
								user.selectedCard1=user.card2;
								user.selectedCard2=user.card3;
							}
							user.isReady=true;
							card1select=false;
							card2select=false;
							card3select=false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void choseCard3(){
		if(user.room.gameState.equals("cardset") && !user.isReady && !user.state.equals("die")){
			if(!card3select){
			gameroom.setSelectedCard3(true);
			card3select=true;
			}else{
				gameroom.setSelectedCard3(false);
				card3select=false;
			}
			int i=0;
			String card1="no";
			String card2="no";
			String card3="no";
			if(card1select){
				i++;
				card1="card1";
			}
			if(card2select){
				i++;
				card2="card2";
			}
			if(card3select){
				i++;
				card3="card3";
			}
			if(i==2){
				int q=JOptionPane.showConfirmDialog(null, "이렇게 최종 패를 결정하시겠습니까?");
				if(q==0){
					try {
						
						user.dos.writeUTF(MsgProtocol.CODE_CARDSET+"/"+card1
								+"/"+card2+"/"+card3);
						if(card1select){
							if(card2select){
								user.selectedCard1=user.card1;
								user.selectedCard2=user.card2;
							}else{
								user.selectedCard1=user.card1;
								user.selectedCard2=user.card3;
							}
						}else{
							user.selectedCard1=user.card2;
							user.selectedCard2=user.card3;
						}
						user.isReady=true;
						card1select=false;
						card2select=false;
						card3select=false;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
	}
	public void betMoney(String bet){
		try{
		if(bet.equals("call")){
			user.dos.writeUTF(MsgProtocol.CODE_CALL);
		}else if(bet.equals("double")){
			user.dos.writeUTF(MsgProtocol.CODE_DOUBLE);
		}else if(bet.equals("half")){
			user.dos.writeUTF(MsgProtocol.CODE_HALF);
		}else if(bet.equals("die")){
			user.dos.writeUTF(MsgProtocol.CODE_DIE);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
