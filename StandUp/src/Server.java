import java.net.*;
import java.io.*;
import java.util.*;

//��� Ŭ���������� ���ٰ����� Ŭ����.
public class Server {
	HashMap clients;
	/* Map
 	key (����) �� value(��)�� ������ ������ ������ key������ ������ ���ٰ���.
 	������ �������� �ʴ´�.
 	key�� �ߺ����� ���ġ �ʰ� value���� �ߺ��� ��� (�����)
	 */
	Server() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
		/*
	Collections.synchronizedMap(clients);
  	syn��chron��ize  
   	���� �߻�(����)�̴�.
   	���ÿ� �߻�(����)�̰� �ϴ�.

	������Ʈ�� ���翩�θ� �𸥴ٸ�, ����  wrapped (�ڵ��థ��) �Ͽ��� �Ѵ� . �����ð��� ���� �ְ��� ����̸� , �ʿ� �־ �ҽÿ� ���ÿ� �������� �ʴ� ������ ���µ��� ���ȴ�.
 	If no such object exists,the map should be "wrapped" using the Collections.synchronizedMap method. This is best done at creation time, to prevent accidental unsynchronized access to the map:
		 */
 
	}
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("������ ���۵Ǿ����ϴ�.");
			while(true) {
				socket = serverSocket.accept();
    
				// serverSocket.accept();
				//�� ��Ĺ�� ������ ������������� ������ ��ٸ���. �� �޼ҵ�� ������ �Ϸ�ɶ����� ����� �ɰ��̴�.
				System.out.println("["+socket.getInetAddress() + ":"+socket.getPort()+"]"+"���� �����Ͽ����ϴ�.");
				/*
    	getInetAddress() 
     	-���������� local �ּҸ� �����Ѵ�.
    	getPort() 
     	- ������Ʈ�� �� ������ ����Ȱ����� ����.
    
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
				sendToAll("#"+name+"���� �����̽��ϴ�.");
				clients.put(name, out);
				System.out.println("���� ���������� ���� " + clients.size() + "�Դϴ�.");
				while(in!=null) {
					sendToAll(in.readUTF());
				}
			} catch(IOException e) {
				// ignore
			} finally {
				sendToAll("#"+name+"���� �����̽��ϴ�.");
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"���� ������ �����Ͽ����ϴ�.");
				System.out.println("���� ���������� ���� "+ clients.size()+"�Դϴ�.");
			} // try
		} // run
	} // ReceiverThread
} // class