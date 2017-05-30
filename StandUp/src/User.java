import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class User {
	String id;
	String pw;
	String nickName;
	int money;
	int win;
	int lose;
	DataInputStream dis;
	DataOutputStream dos;
	boolean isLogin=false;
	
	Room room=null;
	
	public User(DataInputStream dis,DataOutputStream dos) {
		this.dis=dis;
		this.dos=dos;
	}
}
