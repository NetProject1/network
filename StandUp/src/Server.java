import java.io.*;
import java.net.*;
 
public class Server extends Thread {
 
	protected Socket socket;
 
	public Server(Socket socket) {
		this.socket = socket;
	}
 

	// ���޼ҵ� �ȿ��� ������ ��,�ƿ� ��Ʈ���� �����ϰ� ����Ʈ �迭 �����ؼ� ����ؼ� �Է��� �޾� ���δ�.
 
	@Override
	public void run() {
		try{
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write("��̰� �����ϼ���~ \r".getBytes("euc-kr"));
   
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
  
		// �������� ���� 
		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
  
		// ������ while�� 
		// �ݺ����� ����ؼ� ���鼭 ���������� ������ ��� ��ٸ� �� �ְ� �Ѵ�. 
 
		while(true){
			//  accept()�޼ҵ�� Ŭ���̾�Ʈ���� ���ο� ������ ��ȯ -> TCP ������ ������� 
			Socket client = serverSocket.accept();
			//  ������ ��ŸƮ �ǰ� �ݺ����� ���� ����  
			Server server = new Server(client);
			server.start();
		}
	}
	*/
}
