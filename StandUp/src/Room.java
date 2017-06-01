import java.util.ArrayList;

public class Room {
	int roomNumber;
	String roomName;
	ArrayList<User> userArray;
	User roomMaster;
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
			roomMaster=userArray.get(1);
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
}
