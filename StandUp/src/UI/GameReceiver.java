package UI;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import Client.Client;
import Client.ClientGUI;
import CODE_KEY;

public class GameReceiver extends Thread {
	Socket socket;
	DataInputStream input;
	String msg;
	int player;
	ClientGUI gui;

	public GameReceiver(Socket socket, ClientGUI gui) {
		this.socket = socket;
		this.gui = gui;
		try {
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
		try {
			player = input.readInt();
			gui.playerNumber = player;
			gui.myMoney = 50000;
			System.out.println("Player" + player + "Client GAME RECEIVER COMPLETE");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			try {
				String temp = input.readUTF();
				String[] str = temp.split("_");
				System.out.println(player + "---------------");
				System.out.println("RECEIVE :" + temp);
				// 내턴일때
				if (str[0].equals(CODE_KEY.CODE_GAME)) {
					// 1. 버튼 활성화
					gui.setEnableButton(true);
					// 2. 판돈 update
					gui.updateGameMoney("판돈 " + str[3]);
					gui.allGameMoney = Integer.parseInt(str[3]);
					// 3. 상대 상태 update
					gui.updateYourState(str[1]);
					// 4. 상대가 가진돈 update
					gui.updateYourMoney(str[4]);
					gui.yourMoney = Integer.parseInt(str[4]);
					// 5. 배팅에 필요한 금액
					gui.updateBettingMoney(str[2]);
					gui.bettingMoney = Integer.parseInt(str[2]);
					// 6.
					gui.updateMyState("마이 턴");
				} else if (str[0].equals(CODE_KEY.CODE_GAMESTART)) {
					gui.bettingMoney = 100;
					gui.allGameMoney = 200;
					gui.updateGameMoney("BET 200");
					gui.updateStartButton(false);
//					gui.updateMyCardImage( gui.card1Num, gui.card2Num);
					gui.updateMyCardImage( -1, -1);
					gui.updateYourCardImage(-1, -1);
					gui.card1Num = Integer.parseInt(str[1]);
					gui.card2Num = Integer.parseInt(str[2]);
					gui.setCombi(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
					if (str[3].equals(CODE_KEY.CODE_TOKEN_ON)) {
						gui.myMoney = gui.myMoney - 100;
						gui.yourMoney = gui.yourMoney - 100;
						gui.updateMyMoney(gui.myMoney + "");
						gui.updateYourMoney(gui.yourMoney + "");
						gui.updateMyState("배팅고고");
						gui.updateAllButton(true);
					} else {
						gui.myMoney = gui.myMoney - 100;
						gui.yourMoney = gui.yourMoney - 100;
						gui.updateMyMoney(gui.myMoney + "");
						gui.updateYourMoney(gui.yourMoney + "");
						gui.updateMyState("기달기달");
						gui.updateAllButton(false);
					}
				} else if (str[0].equals(CODE_KEY.CODE_GAMEEND)) {
					// gui.updateBettingMoney("0");
					gui.updateAllButton(false);
					gui.updateGameMoney("판돈 " + str[2]);
					if (str[1].equals(CODE_KEY.CODE_WIN)) {
						gui.updateMyState("위너너~");
						gui.updateYourState("패배자~");
						gui.myMoney = Integer.parseInt(str[3]);
						gui.yourMoney  = Integer.parseInt(str[4]);
						gui.updateMyMoney(gui.myMoney+"");
						gui.updateYourMoney(gui.yourMoney+"");
						gui.updateStartButton(true);
						gui.updateYourCardImage(Integer.parseInt(str[5]), Integer.parseInt(str[6]));
					} else {
						System.out.println("받자나 받어안그래?");
						gui.updateMyState("패배자~");
						gui.updateYourState("위너너~");
						gui.myMoney = Integer.parseInt(str[3]);
						gui.yourMoney  = Integer.parseInt(str[4]);
						gui.updateMyMoney(gui.myMoney+"");
						gui.updateYourMoney(gui.yourMoney+"");
						gui.updateStartButton(true);
						gui.updateYourCardImage(Integer.parseInt(str[5]), Integer.parseInt(str[6]));
					}
				}
			} catch (IOException e) {
			}
		}
	}
}
