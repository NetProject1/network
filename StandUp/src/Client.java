import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
	static int PORT=9999; //서버 포트
	static String IP=""; //서버 아이피
	
	Socket socket; //서버와 접속할 소켓
	DataInputStream dis; // 입출력 스트림
	DataOutputStream dos;
	boolean accessReady=false;
	
	//UI 구현시 
	//loginUI 
	//waitingroom
	//gameroom
	
	public Client() {
		//생성과 함께 ui 생성
		//login asd= new loginui
		Thread thread=new Thread(this);
		thread.start();
		
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
	
	@Override
	public void run() {
		//접속이 안되어있다면 계속 대기한다
		while(!accessReady){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//접속이 됬다면
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
	 void msgParsing(String receiveMsg) {
		
	}
	 void uiTerminated(){
		 
	 }
	 //서버에 접속을 요청하고 접속이 되었는지 accessReady를 반환함
	 boolean serverAccess(){
		 //접속이 안되어있을때
		if(!accessReady){
			socket=null;
			//소켓을 접속시킴 
			try {
				socket=new Socket(IP, PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//소켓이 접속되었다면
			if(socket.isBound()){
				try {
					dis=new DataInputStream(socket.getInputStream());
					dos=new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				accessReady=true;
			}
		}
		 
		
		
		return accessReady;
		 
	 }
	
}
