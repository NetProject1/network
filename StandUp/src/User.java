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
	
	int gamePlayerNumber;
	boolean isLogin=false;
	
	Room room=null;
	
	public User(DataInputStream dis,DataOutputStream dos) {
		this.dis=dis;
		this.dos=dos;
	}
	//id nick money win lose 
	public User(String id, String nick,int money,int win, int lose){
		this.id=id;
		this.nickName=nick;
		this.money=money;
		this.win=win;
		this.lose=lose;
	}
	public User(){
		
	}
	//비밀번호를 제외한 유저 정보를 넘겨줍니다.
	public String getUserINFO(){
		String str;
		str="/"+id+"/"+nickName+"/"+money+"/"+win+"/"+lose;
		return str;
	}
}
