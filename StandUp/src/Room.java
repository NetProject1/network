import java.util.ArrayList;

public class Room {
	int roomNumber;
	String roomName;
	ArrayList<User> userArray;
	User roomMaster;
	
	public Room(int rNum,String rName, User roomMaster) {
		// TODO Auto-generated constructor stub
		this.roomNumber=rNum;
		this.roomName=rName;
		this.roomMaster=roomMaster;
		userArray=new ArrayList<User>();
		userArray.add(roomMaster);
	}
	
}
