import java.net.*;
import java.io.*;
import java.util.*;

//어떠한 클래스에서도 접근가능한 클래스.
public class SServer {
	ArrayList<User> userArray;
	ArrayList<Room> roomArray;

	
	SServer() {
		userArray=new ArrayList<User>();
		roomArray=new ArrayList<Room>();
		Collections.synchronizedList(userArray);
		Collections.synchronizedList(roomArray);
		/*
	Collections.synchronizedMap(clients);
  	syn·chron·ize  
   	동시 발생(움직)이다.
   	동시에 발생(움직)이게 하다.

	오브젝트의 존재여부를 모른다면, 맵은  wrapped (자동행갈이) 하여야 한다 . 생성시간을 위한 최고의 방법이며 , 맵에 있어서 불시에 동시에 동작하지 않는 권한을 막는데에 사용된다.
 	If no such object exists,the map should be "wrapped" using the Collections.synchronizedMap method. This is best done at creation time, to prevent accidental unsynchronized access to the map:
		 */
 
	}
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");
			while(true) {
				socket = serverSocket.accept();
				
				// serverSocket.accept();
				//위 소캣과 권한을 만들어지기위한 접속을 기다린다. 위 메소드는 접속이 완료될때가지 블락이 될것이다.
				System.out.println("["+socket.getInetAddress() + ":"+socket.getPort()+"]"+"에서 접속하였습니다.");
				
				DataInputStream dis= new DataInputStream(socket.getInputStream());
				DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
				User user=new User(dis, dos);
				ServerReceiver thread = new ServerReceiver(socket,user,userArray,roomArray);
				thread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // start()

	public static void main(String args[]) {
		new SServer().start();
	}
}