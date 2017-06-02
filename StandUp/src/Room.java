import java.util.ArrayList;

public class Room {
	int roomNumber;
	String roomName;
	ArrayList<User> userArray;

	User roomMaster;
	
	//플레이어들. 각 유저가 하나씩 가진다.
	gamePlayer player0,player1,player2,player3;
	ArrayList<Player> players;
	
	int playerturn=0;
	
	String gameState;
	
	boolean isGameStart=false;
	public Room(int rNum,String rName, User roomMaster) {
		// TODO Auto-generated constructor stub
		this.roomNumber=rNum;
		this.roomName=rName;
		this.roomMaster=roomMaster;
		userArray=new ArrayList<User>();
		userArray.add(roomMaster);
	}
	//방장을 넘긴다.
	int passMaster(){
		if(userArray.size()<=1){
			//방에 오직 방장 한명뿐이라면 0을 리턴
			return 0;
		}else{
			//방에 2명이상이면 다음 유저에게 방장을 넘긴다.
			int i=0;
			for( i=0;i<userArray.size();i++){
				if(userArray.get(i).equals(roomMaster)){
					if(i==3){
						i=0;
					}else{
						i=i+1;
					}
					break;
				}
			}
			roomMaster=userArray.get(i);
			return 1;
		}
		
		
	}
	String GetRoomINFO(){
		String str="";
		if(isGameStart){
			str+="/true";
		}else{
			str+="/false";
		}
		str+=roomMaster.getUserINFO();
		for(int i=0;i<userArray.size();i++){
			str+=userArray.get(i).getUserINFO();
		}
		
		return str;
	}
	String getRoomINFO1(){
		String str="";
		if(isGameStart){
			str+="/true";
		}else{
			str+="/false";
		}
		str+=roomMaster.getUserINFO();
		for(int i=0;i<userArray.size();i++){
			str+=userArray.get(i).getUserINFO();
		}
		
		return str;
	}	
}
class gamePlayer{
	public int card1;
	public int card2;
	public int card3;
	public int money;
	public int playerNumber;
	public String state="";
	public String nick;
}
