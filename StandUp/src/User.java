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
	
	//게임에 필요한 변수들
	int playerNumber=99999;
	public int card1=99999;
	public int card2=99999;
	public int card3=99999;
	public int openCard=99999;
	public int selectedCard1=99999;
	public int selectedCard2=99999;
	//족보 계산 결과
	int cardValue=0;
	
	//
	boolean isReady=false;
	//게임중 다이인지 아닌지 확인한다 상태는 call double half die 둘
	public String state="idle";
	//로그인 여부를 확인한다.
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
	//
	public User(){
		
	}
	
	public String getGameINFO(int playerNum){
		String str;
		if(playerNum==playerNumber){
			str="/"+isReady+"/"+state+"/"+id+"/"+nickName+"/"+money+"/"+win+"/"
				+lose+"/"+playerNumber+"/"+card1+"/"+card2+"/"+card3;
		}else{
			str="/"+isReady+"/"+state+"/"+id+"/"+nickName+"/"+money+"/"+win+"/"
				+lose+"/"+playerNumber+"/"+openCard+"/"+selectedCard1+"/"+selectedCard2;
		}
		
		return str;
	}
	void userReset(){
		//게임에 필요한 변수들
		playerNumber=99999;
		card1=99999;
		card2=99999;
		card3=99999;
		openCard=99999;
		selectedCard1=99999;
		selectedCard2=99999;
		cardValue=0;

		isReady=false;
		 state="idle";
	}
}
