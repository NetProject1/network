package UI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import CODE_KEY;
import UI.GameServer.GameServerReceiver;

class Player {
	public Player() {
		// TODO Auto-generated constructor stub
	}

	public Player(GameServerReceiver thread, DataOutputStream dos, int playerNumber) {
		this.thread = thread;
		this.dos = dos;
		this.playerNumber = playerNumber;
	}

	public GameServerReceiver thread;
	public DataOutputStream dos;
	public String state = "";
	public int money = 50000;
	public int playerNumber;
	public int card1;
	public int card2;
}

public class GameServer {
	private ServerSocket serverSocket;
	int winner = -1;
	int[][] priority;
	public Player player0;
	public Player player1;
	public Player player2;
	public Player player3;
	public int playerNum = 0;
	public int playerToken = -1;
	public int allGameMoney= 0;
	// ArrayList<Player> clientList = new ArrayList<Player>();
	ArrayList<Integer> shuffleCard = new ArrayList<Integer>();

	public GameServer() {
		ArrayList<Player> clientList = new ArrayList<Player>();
		Collections.synchronizedList(clientList);
		priority = new int[21][21];
		setPriority();
		
	}

	public void start() {
		try {
			Socket socket;

			// 리스너 소켓 생성
			serverSocket = new ServerSocket(7776);
			System.out.println("게임 서버가 시작되었습니다.");

			// 클라이언트와 연결되면
			while (player1 == null || player0 == null || player2 == null || player3 == null) {
				socket = serverSocket.accept();
				System.out.println("소켓생성");
				System.out.println("Accept");
				// 쓰레드 생성
				GameServerReceiver receiver = new GameServerReceiver(socket, playerNum);
				playerNum++;
				receiver.start();
				
				if(playerNum >= 4){
					break;
				}
			}
			// gameStart();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dealCard();
	}
	public Player getMyPlayer(String player){
		if(player.equals("0")){
			return player0;
		}else if(player.equals("1")){
			return player1;
		}else if(player.equals("2")){
			return player2;
		}else{
			return player3;
		}
	}
	
 	public Player getFirstPlayer(String player) {
 		if (player.equals("0")) {
 			return player1;
 		} else if(player.equals("1")){
 			return player0;
 		} else if(player.equals("2")){
 			return player0;
 		} else{
 			return player0;
 		}
 	}
 	
 	public Player getSecondPlayer(String player){
 		if(player.equals("0")){
 			return player2;
 		} else if(player.equals("1")){
 			return player2;
 		} else if(player.equals("2")){
 			return player1;
 		} else{
 			return player1;
 		}
 	}
 	
 	public Player getThirdPlayer(String player){
 		if(player.equals("0")){
 			return player3;
 		} else if(player.equals("1")){
 			return player3;
 		} else if(player.equals("2")){
 			return player3;
 		} else{
 			return player2;
 		}
 	}
	
 	  // player card를 생성하고 족보에따라 winner를 결정(using judge method)
	public void dealCard() {
		int[] card = new int[21];
		Random random = new Random(System.currentTimeMillis());
		shuffleCard.removeAll(shuffleCard);
		while (shuffleCard.size() < 8) {
			int rVal = random.nextInt(19) + 1;
			if (card[rVal] == 0) {
				shuffleCard.add(rVal);
				card[rVal] = 1;
			}
		}
		judge(shuffleCard.get(0),shuffleCard.get(1),shuffleCard.get(2),shuffleCard.get(3),shuffleCard.get(4),shuffleCard.get(5),shuffleCard.get(6),shuffleCard.get(7));
	}

	public void setPriority() {
		priority[13][18] = Integer.MAX_VALUE; //38광땡
		priority[11][13] = 10000; //13광땡
		priority[11][18] = 10000; //18광땡

		for (int i = 1; i <= 10; i++) {
			for (int j = 11; j <= 20; j++) {
				int num = (i+j)%10;
				priority[i][j] = num; // 끗
				if((i+10)==j){
					priority[i][j] = priority[i][j]+1000; // 땡
				}
			}
		}
		priority[10][20] = priority[10][20]+1100;
		//알리1,2
		priority[1][12] = priority[1][12] + 900;
		priority[1][2] = priority[1][2] + 900;
		priority[2][11] = priority[2][11] + 900;
		priority[11][12] = priority[11][12] + 900;
		//구삥 1,9
		priority[1][19] = priority[1][19] + 800;
		priority[1][9] = priority[1][9] + 800;
		priority[9][11] = priority[9][11] + 800;
		priority[11][19] = priority[11][19] + 800;
		//장삥 1,10
		priority[1][20] = priority[1][20] + 700;
		priority[1][10] = priority[1][10] + 700;
		priority[10][11] = priority[10][11] + 700;
		priority[11][20] = priority[11][20] + 700;
		//세륙
		priority[6][14] = priority[6][14] + 600;
		priority[4][6] = priority[4][6] + 600;
		priority[4][16] = priority[4][16] + 600;
		priority[14][16] = priority[14][16] + 600;
		//갑오
		priority[1][9] = priority[1][9]+500;
		priority[1][19] = priority[1][19]+500;
		priority[10][9] = priority[10][9]+500;
		priority[10][19] = priority[10][19]+500;
		priority[2][7] = priority[2][7]+500;
		priority[2][17] = priority[2][17]+500;
		priority[12][7] = priority[12][7]+500;
		priority[12][17] = priority[12][17]+500;
		priority[3][6] = priority[3][6]+500;
		priority[3][16] = priority[3][16]+500;
		priority[13][6] = priority[13][6]+500;
		priority[13][16] = priority[13][16]+500;
		priority[3][6] = priority[3][6]+500;
		priority[4][15] = priority[4][15]+500;
		priority[13][6] = priority[13][6]+500;
		priority[13][16] = priority[13][16]+500;
		//3,7땡잡이
		priority[3][17] = priority[3][17] + 400;
		priority[3][7] = priority[3][7] + 400;
		priority[7][13] = priority[7][13] + 400;
		priority[13][17] = priority[13][17] + 400;
		//암행어사
		priority[4][17] = priority[4][17] + 300;
		priority[4][7] = priority[4][7] + 300;
		priority[7][14] = priority[7][14] + 300;
		priority[14][17] = priority[14][17] + 300;
		//구사
		priority[4][19] = priority[4][19] + 200;
		priority[4][9] = priority[4][9] + 200;
		priority[9][14] = priority[9][14] + 200;
		priority[14][19] = priority[14][19] + 200;
		
//		for(int i =0; i<10;i++){
//			for(int j =11; j<=20;j++){
//				System.out.println(i+","+j+"="+priority[i][j]);
//			}
//		}
		
	}
	public void result(){
		
	}
	public void judge(int num1, int num2, int num3, int num4, int num5, int num6, int num7, int num8) {
		int[] player = new int[4];
		int max = 0;
		if(num1<num2){
			player[0] = priority[num1][num2]; 
		}else{
			player[0] = priority[num2][num1];
		}
		if(num3<num4){
			player[1] = priority[num3][num4]; 
		}else{
			player[1] = priority[num4][num3];
		}
		if(num5<num6){
			player[2] = priority[num5][num6]; 
		}else{
			player[2] = priority[num6][num5];
		}
		if(num7<num8){
			player[3] = priority[num7][num8]; 
		}else{
			player[3] = priority[num8][num7];
		}
		for(int i = 0; i<player.length; i++){
			
			if(player[i]>=400 && player[i] <500){
				for(int j = 0; j<player.length; j++){
					if(player[j] >= 1000 && player[j] <3000){
						player[i] = player[i] + 3000;
					}
				}
				if(player[i]>=400 && player[i] <500){
					player[i] = player[i] - 400;
				}
			}
			
			if(player[i]>=300 && player[i] <400){
				for(int j = 0; j<player.length; j++){
					if(player[j] == 10000){
						player[i] = player[i] + 10100;
					}
				}
				if(player[i]>=300 && player[i] <400){
					player[i] = player[i] - 300;
				}
			}
			
			// 구사
			if(player[i]>=200 && player[i] <300){
				
			}
			if(max < player[i]){
				max = player[i];
			}
		}
		if(max == player[0]){
			System.out.println("WINNER IS PLAYER "+winner);
			winner = 0;
		}else if(max == player[1]){
			winner = 1;
			System.out.println("WINNER IS PLAYER "+winner);
		}else if(max == player[2]){
			winner = 2;
			System.out.println("WINNER IS PLAYER "+winner);
		}else{
			winner = 3;
			System.out.println("WINNER IS PLAYER "+winner);
		}
		
	}

	class GameServerReceiver extends Thread {
		Socket socket;
		DataInputStream input;
		DataOutputStream output;
		int player;

		public GameServerReceiver(Socket socket, int player) {
			this.socket = socket;
			this.player = player;
			try {
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				if (player == 0) {
					player0 = new Player(this, output, player);
				} else if (player == 1) {
					player1 = new Player(this, output, player);
				} else if (player == 2) {
					player2 = new Player(this, output, player);
				} else if (player == 3) {
					player3 = new Player(this, output, player);
				}
			} catch (IOException e) {
			}
		}
		
		// 대부분 getOtherPlayer를 사용해서 비교를 하더라고요
		// 저희는 4명이서 해야하기때문에 getOtherPlayer말고 다른 메소드를 써야할것 같아요!
		@Override
		public void run() {
			System.out.println("player " + player + " 접속");
			try {
				// 클라이언트가 서버에 접속하면 대화방에 알린다.
				output.writeInt(player);
				// 메세지 전송
				while (true) {
					System.out.println(player + "--------------------");
					String temp = input.readUTF();
					String[] str = temp.split("_");
					
					System.out.println(temp);
					if (str[0].equals(CODE_KEY.CODE_GAMESTART)) {
						Player p = getMyPlayer(player+"");
						p.money = Integer.parseInt(str[2]);
						if (getFirstPlayer(str[1]).state.equals(CODE_KEY.CODE_GAMESTART) && getSecondPlayer(str[1]).state.equals(CODE_KEY.CODE_GAMESTART)
								&& getThirdPlayer(str[1]).state.equals(CODE_KEY.CODE_GAMESTART)) {
							System.out.println("player"+player+"My money:"+p.money);
							allGameMoney = 400;
							p.state = "";
							dealCard();
							p.card1 = shuffleCard.get(0);
							p.card2 = shuffleCard.get(1);
							getFirstPlayer(str[1]).card1 = shuffleCard.get(2);
							getFirstPlayer(str[1]).card2 = shuffleCard.get(3);
							getSecondPlayer(str[1]).card1 = shuffleCard.get(4);
							getSecondPlayer(str[1]).card2 = shuffleCard.get(5);
							getThirdPlayer(str[1]).card1 = shuffleCard.get(6);
							getThirdPlayer(str[1]).card2 = shuffleCard.get(7);
							output.writeUTF(
									CODE_KEY.CODE_GAMESTART + "_" + shuffleCard.get(0) + "_" + shuffleCard.get(1)+ "_" +CODE_KEY.CODE_TOKEN_OFF);
							getFirstPlayer(str[1]).dos.writeUTF(
									CODE_KEY.CODE_GAMESTART + "_" + shuffleCard.get(2) + "_" + shuffleCard.get(3)+ "_" +CODE_KEY.CODE_TOKEN_ON);
							getSecondPlayer(str[1]).dos.writeUTF(
									CODE_KEY.CODE_GAMESTART + "_" + shuffleCard.get(4) + "_" + shuffleCard.get(5)+ "_" +CODE_KEY.CODE_TOKEN_ON);
							getThirdPlayer(str[1]).dos.writeUTF(
									CODE_KEY.CODE_GAMESTART + "_" + shuffleCard.get(6) + "_" + shuffleCard.get(7)+ "_" +CODE_KEY.CODE_TOKEN_ON);
							playerToken = 0;
						} else {
							System.out.println("else");
							System.out.println("player"+player+"My money:"+p.money);
							if (str[1].equals("0")) {
								player0.state = CODE_KEY.CODE_GAMESTART;
							} else if (str[1].equals("1")) {
								player1.state = CODE_KEY.CODE_GAMESTART;
							} else if (str[1].equals("2")) {
								player2.state = CODE_KEY.CODE_GAMESTART;
							} else if (str[1].equals("3")) {
								player3.state = CODE_KEY.CODE_GAMESTART;
							} 
						}
					} else if (str[0].equals(CODE_KEY.CODE_GAME)) {
						playerToken = getMyPlayer(str[5]).playerNumber;
						Player p = getMyPlayer(str[5]);
						p.state = str[1];
						allGameMoney = Integer.parseInt(str[3]);
						p.money = Integer.parseInt(str[4]);
						if(str[1].equals(CODE_KEY.CODE_CALL)){
							if(getFirstPlayer(str[5]).state.equals(CODE_KEY.CODE_CALL) && getSecondPlayer(str[5]).state.equals(CODE_KEY.CODE_CALL)
									&& getThirdPlayer(str[5]).state.equals(CODE_KEY.CODE_CALL)){
								//넷다 콜
								//저지
								//게임종료
								StringBuilder winStr = new StringBuilder();
								winStr.append(CODE_KEY.CODE_GAMEEND);
								winStr.append("_");
								winStr.append(CODE_KEY.CODE_WIN);
								winStr.append("_");
								winStr.append(allGameMoney);
								winStr.append("_");
								
								StringBuilder loseStr = new StringBuilder();
								loseStr.append(CODE_KEY.CODE_GAMEEND);
								loseStr.append("_");
								loseStr.append(CODE_KEY.CODE_LOSE);
								loseStr.append("_");
								loseStr.append(allGameMoney);
								loseStr.append("_");
								
								
								if(winner==p.playerNumber){
									p.money = p.money+ allGameMoney;
									winStr.append(p.money);
									winStr.append("_");
									winStr.append(getFirstPlayer(str[5]).money + getSecondPlayer(str[5]).money + getThirdPlayer(str[5]).money);
									p.dos.writeUTF(winStr.toString());
									
									loseStr.append(getFirstPlayer(str[5]).money + getSecondPlayer(str[5]).money + getThirdPlayer(str[5]).money);
									loseStr.append("_");
									loseStr.append(p.money);
									getFirstPlayer(str[5]).dos.writeUTF(loseStr.toString());
									getSecondPlayer(str[5]).dos.writeUTF(loseStr.toString());
									getThirdPlayer(str[5]).dos.writeUTF(loseStr.toString());
								}else{
									
									if(winner == 0){
										p.money = p.money+allGameMoney;
										loseStr.append(p.money);
										loseStr.append("_");
										loseStr.append(getFirstPlayer("0").money + getSecondPlayer("0").money + getThirdPlayer("0").money);
										p.dos.writeUTF(loseStr.toString());
										
										winStr.append(getMyPlayer("0").money);
										winStr.append("_");
										winStr.append(p.money);
										getMyPlayer("0").dos.writeUTF(winStr.toString());
									} else if (winner == 1){
										p.money = p.money+allGameMoney;
										loseStr.append(p.money);
										loseStr.append("_");
										loseStr.append(getFirstPlayer("1").money + getSecondPlayer("1").money + getThirdPlayer("1").money);
										p.dos.writeUTF(loseStr.toString());
										
										getMyPlayer("1").money = getMyPlayer("1").money+allGameMoney;
										winStr.append(getMyPlayer("1").money);
										winStr.append("_");
										winStr.append(getFirstPlayer("1").money + getSecondPlayer("1").money + getThirdPlayer("1").money);
										getMyPlayer("1").dos.writeUTF(winStr.toString());
									} else if (winner == 2){
										p.money = p.money+allGameMoney;
										loseStr.append(p.money);
										loseStr.append("_");
										loseStr.append(getFirstPlayer("2").money + getSecondPlayer("2").money + getThirdPlayer("2").money);
										p.dos.writeUTF(loseStr.toString());
										
										getMyPlayer("2").money = getMyPlayer("2").money+allGameMoney;
										winStr.append(getMyPlayer("2").money);
										winStr.append("_");
										winStr.append(getFirstPlayer("2").money + getSecondPlayer("2").money + getThirdPlayer("2").money);
										getMyPlayer("2").dos.writeUTF(winStr.toString());
									} else{
										p.money = p.money+allGameMoney;
										loseStr.append(p.money);
										loseStr.append("_");
										loseStr.append(getFirstPlayer("3").money + getSecondPlayer("3").money + getThirdPlayer("3").money);
										p.dos.writeUTF(loseStr.toString());
										
										getMyPlayer("3").money = getMyPlayer("3").money+allGameMoney;
										winStr.append(getMyPlayer("3").money);
										winStr.append("_");
										winStr.append(getFirstPlayer("3").money + getSecondPlayer("3").money + getThirdPlayer("3").money);
										getMyPlayer("3").dos.writeUTF(winStr.toString());
									}
									
								}
							}else{
								StringBuilder strbuilder = new StringBuilder();
								strbuilder.append(CODE_KEY.CODE_GAME);
								strbuilder.append("_");
								strbuilder.append(str[1]);
								strbuilder.append("_");
								strbuilder.append(str[2]);
								strbuilder.append("_");
								strbuilder.append(str[3]);
								strbuilder.append("_");
								strbuilder.append(str[4]);
								getFirstPlayer(str[5]).dos.writeUTF(strbuilder.toString());
								getSecondPlayer(str[5]).dos.writeUTF(strbuilder.toString());
								getThirdPlayer(str[5]).dos.writeUTF(strbuilder.toString());
							}
						}else if(str[1].equals(CODE_KEY.CODE_DIE)){
							//게임종료
							if(winner == 0){
								getMyPlayer("0").money = getMyPlayer("0").money+allGameMoney;
								StringBuilder winStr = new StringBuilder();
								winStr.append(CODE_KEY.CODE_GAMEEND);
								winStr.append("_");
								winStr.append(CODE_KEY.CODE_WIN);
								winStr.append("_");
								winStr.append(allGameMoney);
								winStr.append("_");
								winStr.append(getMyPlayer("0").money);
								System.out.println("WINNER:"+winStr.toString());
								getMyPlayer("0").dos.writeUTF(winStr.toString());
								
								StringBuilder loseStr = new StringBuilder();
								loseStr.append(CODE_KEY.CODE_GAMEEND);
								loseStr.append("_");
								loseStr.append(CODE_KEY.CODE_LOSE);
								loseStr.append("_");
								loseStr.append(allGameMoney);
								loseStr.append("_");
								loseStr.append(p.money);
								loseStr.append("_");
								loseStr.append(getMyPlayer("0").money);
								System.out.println("LOSER:"+loseStr.toString());
								p.dos.writeUTF(loseStr.toString());
								
							} else if(winner == 1){
								
								getMyPlayer("1").money = getMyPlayer("1").money+allGameMoney;
								StringBuilder winStr = new StringBuilder();
								winStr.append(CODE_KEY.CODE_GAMEEND);
								winStr.append("_");
								winStr.append(CODE_KEY.CODE_WIN);
								winStr.append("_");
								winStr.append(allGameMoney);
								winStr.append("_");
								winStr.append(getMyPlayer("1").money);
								System.out.println("WINNER:"+winStr.toString());
								getMyPlayer("1").dos.writeUTF(winStr.toString());
							} else if(winner == 2){
								
								getMyPlayer("2").money = getMyPlayer("2").money+allGameMoney;
								StringBuilder winStr = new StringBuilder();
								winStr.append(CODE_KEY.CODE_GAMEEND);
								winStr.append("_");
								winStr.append(CODE_KEY.CODE_WIN);
								winStr.append("_");
								winStr.append(allGameMoney);
								winStr.append("_");
								winStr.append(getMyPlayer("2").money);
								System.out.println("WINNER:"+winStr.toString());
								getMyPlayer("2").dos.writeUTF(winStr.toString());
							} else{
								
								getMyPlayer("3").money = getMyPlayer("3").money+allGameMoney;
								StringBuilder winStr = new StringBuilder();
								winStr.append(CODE_KEY.CODE_GAMEEND);
								winStr.append("_");
								winStr.append(CODE_KEY.CODE_WIN);
								winStr.append("_");
								winStr.append(allGameMoney);
								winStr.append("_");
								winStr.append(getMyPlayer("3").money);
								System.out.println("WINNER:"+winStr.toString());
								getMyPlayer("3").dos.writeUTF(winStr.toString());
							}
							
							
						}else{//half, double
							StringBuilder strbuilder = new StringBuilder();
							strbuilder.append(CODE_KEY.CODE_GAME);
							strbuilder.append("_");
							strbuilder.append(str[1]);
							strbuilder.append("_");
							strbuilder.append(str[2]);
							strbuilder.append("_");
							strbuilder.append(str[3]);
							strbuilder.append("_");
							strbuilder.append(str[4]);
							getFirstPlayer(str[5]).dos.writeUTF(strbuilder.toString());
							getSecondPlayer(str[5]).dos.writeUTF(strbuilder.toString());
							getThirdPlayer(str[5]).dos.writeUTF(strbuilder.toString());
						}
							
					}
				
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void main(String[] args) {
		GameServer go = new GameServer();
		go.start();
	}
}
