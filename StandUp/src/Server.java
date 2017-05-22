import java.io.*;
import java.net.*;
 
public class Server extends Thread {
 
	protected Socket socket;
 
	public Server(Socket socket) {
		this.socket = socket;
	}
 

	// 런메소드 안에서 소켓의 인,아웃 스트림을 연결하고 바이트 배열 선언해서 계속해서 입력을 받아 들인다.
 
	@Override
	public void run() {
		try{
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write("즐겁게 게임하세요~ \r".getBytes("utf-8"));
   
			byte[] buffer = new byte[1024];
			int read;
			while((read=in.read(buffer)) >= 0) out.write(buffer, 0, read);
   
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 /*
	public static void main(String[] args) throws NumberFormatException, IOException {
		if(args.length != 1) throw new IllegalArgumentException("Syntax: Server <port>");
  
		System.out.println("Starting on port" + args[0]);
  
		// 서버소켓 선언 
		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
  
		// 메인의 while문 
		// 반복문이 계속해서 돌면서 서버소켓이 응답을 계속 기다릴 수 있게 한다. 
 
		while(true){
			//  accept()메소드로 클라이언트에게 새로운 소켓을 반환 -> TCP 연결이 만들어짐 
			Socket client = serverSocket.accept();
			//  서버가 스타트 되고 반복문을 빠져 나감  
			Server server = new Server(client);
			server.start();
		}
	}
	*/
}
