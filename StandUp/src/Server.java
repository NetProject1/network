import java.net.*;
import java.io.*;
import java.util.*;

//어떠한 클래스에서도 접근가능한 클래스.
public class Server {
	HashMap clients;
	/* Map
 	key (변수) 와 value(값)을 쌍으로 가지고 있으면 key값으로 데이터 접근가능.
 	순서는 유지되지 않는다.
 	key의 중복값은 허용치 않고 value값의 중복은 허용 (덮어씌기)
	 */
	Server() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
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
				/*
    	getInetAddress() 
     	-서버소켓의 local 주소를 리턴한다.
    	getPort() 
     	- 원격포트를 위 소켓이 연결된곳으로 리턴.
    
				 */
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // start()
	void sendToAll(String msg) {
		Iterator it = clients.keySet().iterator();
  
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)clients.get(it.next());
				out.writeUTF(msg);
			} catch(IOException e){}
		} // while
	} // sendToAll
	public static void main(String args[]) {
		new Server().start();
	}
 

	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch(IOException e) {}
		}
		public void run() {
			String name = "";
			try {
				name = in.readUTF();
				sendToAll("#"+name+"님이 들어오셨습니다.");
				clients.put(name, out);
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
				while(in!=null) {
					sendToAll(in.readUTF());
				}
			} catch(IOException e) {
				// ignore
			} finally {
				sendToAll("#"+name+"님이 나가셨습니다.");
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"에서 접속을 종료하였습니다.");
				System.out.println("현재 서버접속자 수는 "+ clients.size()+"입니다.");
			} // try
		} // run
	} // ReceiverThread
} // class