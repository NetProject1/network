package Thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameSender extends Thread{
	Socket socket;
	DataOutputStream output;
	String msg = "";

	public GameSender(Socket socket, String serverIp) {
		try {
			this.socket = socket;
			output = new DataOutputStream(socket.getOutputStream());
			System.out.println("대화방에 입장하였습니다.");
		} catch (Exception e) {
		}
	}

	public void send(String msg) {
		this.msg = msg;
	}

	@Override
	public void run() {
		System.out.println("GameSender");
		System.out.println("Client GAME SENDER COMPLETE");
		while (true) {
			try {
				synchronized (this) {
					this.wait();
				}
				output.writeUTF(msg);
				System.out.println("SEND MSG: "+msg);
					

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
