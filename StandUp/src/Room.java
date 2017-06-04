
import java.util.ArrayList;
import java.util.Random;

import com.sun.media.jfxmedia.events.PlayerTimeListener;

public class Room {
	int roomNumber;
	String roomName;
	ArrayList<User> userArray;

	User roomMaster;

	//배팅액
	int bet=100;
	//현재 걸린 금액
	int amountMoney=0;
	
	//플레이어턴. 판돈걸때 사용됩니다. 0번째 부터 3번째까지 하나씩 올라감.
	int playerturn=0;
	
	//족보 저장
	int[][] priority =new int[21][21];;
	
	String gameState="idle";
	
	boolean isGameStart=false;
	
	ArrayList<Integer> shuffleCard=new ArrayList<Integer>();
	//셔플 카드에서 몇장빼왔는지 기억
	int cardPoint=0;
	
	public Room(int rNum,String rName, User roomMaster) {
		// TODO Auto-generated constructor stub
		this.roomNumber=rNum;
		this.roomName=rName;
		this.roomMaster=roomMaster;
		userArray=new ArrayList<User>();
		userArray.add(roomMaster);
		setPriority();
	}
	//방장을 넘긴다.
	int passMaster(){
		if(userArray.size()<=1){
			//방에 오직 방장 한명뿐이라면 0을 리턴
			return 0;
		}else{
			//방에 2명이상이면 다음 유저에게 방장을 넘긴다.
			int i=0;
			for( i=0;i<userArray.size();i++){
				if(userArray.get(i).equals(roomMaster)){
					if(i==3){
						i=0;
					}else{
						i=i+1;
					}
					break;
				}
			}
			roomMaster=userArray.get(i);
			return 1;
		}
		
		
	}
	//기본 공탁금제공
	void setFirstMoney(){
		for(int i=0; i< userArray.size() ;i++){
			userArray.get(i).money-=bet;
			amountMoney+=bet;
		}	
	}
	
	//만약 재시작될때는 좀더 수정해서 사용해야합니다.
	String GetRoomINFO(){
		String str="";
		if(isGameStart){
			str+="/true";
		}else{
			str+="/false";
		}
		str+="/"+gameState+"/"+bet+"/"+amountMoney+"/"+playerturn;
		
		return str;
	}
	//12개의 카드를 뽑아놓는다.
	public void dealCard() {
		int[] card = new int[21];
		Random random = new Random(System.currentTimeMillis());
		shuffleCard.removeAll(shuffleCard);
		while (shuffleCard.size() < 12) {
			int rVal = random.nextInt(19) + 1;
			if (card[rVal] == 0) {
				shuffleCard.add(rVal);
				card[rVal] = 1;
			}
		}
	}
	public void drawTwoCards(){
		
		for(int i=0; i< userArray.size() ;i++){
			if(!userArray.get(i).state.equals("die")){
				userArray.get(i).card1=shuffleCard.get(cardPoint);
				cardPoint++;
				userArray.get(i).card2=shuffleCard.get(cardPoint);
				cardPoint++;
			}
		}	
	}
	public void drawOneCard(){
		for(int i=0; i< userArray.size() ;i++){
			if(!userArray.get(i).state.equals("die")){
				userArray.get(i).card3=shuffleCard.get(cardPoint);
				cardPoint++;
			}
		}	
	}
	public void setPlayerOrder(){
		int masterNumber=0;
		for(int i=0;i< userArray.size();i++){
			if(userArray.get(i).equals(roomMaster)){
				masterNumber=i;
			}
		}
		for(int i=0;i< userArray.size();i++){
			if(masterNumber==0){
			userArray.get(i).playerNumber=i;
			}else if(masterNumber==1){
				userArray.get(i).playerNumber=(i+3)%4;
			}else if(masterNumber==2){
				userArray.get(i).playerNumber=(i+2)%4;
			}else if(masterNumber==3){
				userArray.get(i).playerNumber=(i+1)%4;
			}
		}
		
	}
	//카드를 뒷모습으로 세팅 2장만
	public void setPlayersCardBack1(){
		for(int i=0;i< userArray.size();i++){
			userArray.get(i).card1=0;
			userArray.get(i).card2=0;
			userArray.get(i).openCard=0;
			userArray.get(i).selectedCard1=0;
		}
	}
	public void setPlayersCardBack2(){
		for(int i=0;i< userArray.size();i++){
			if(!userArray.get(i).state.equals("die")){
			userArray.get(i).card3=0;
			userArray.get(i).selectedCard2=0;
			}
		}
	}
	void roomStateSet(String state){
		gameState=state;
	}
	//모든플레이어가 행동을 끝냈는지 확인
	boolean allPlayerReady(){
		boolean t=true;
		for(int i=0;i< userArray.size();i++){
			if(!userArray.get(i).state.equals("die")){
				if(t && userArray.get(i).isReady){
					t=true;
				}else{
					t=false;
				}
			}
		}
		return t;
	}
	//모든 플레이어의 상태를 세팅한다.
	void SetAllPlayerState(String state){
		
	}
	void SetNotReady(){
		for(int i=0;i< userArray.size();i++){
			userArray.get(i).isReady=false;
		}
	}
	int isEndBetState(){
		int dieNum=0;
		int callNum=0;
		for(int i=0;i< userArray.size();i++){
			if(userArray.get(i).state.equals("call")){
				callNum++;
			}else if(userArray.get(i).state.equals("die")){
				dieNum++;
			}
		}
		if(dieNum== (userArray.size()-1)){
			return 1;
		}
		if(callNum== (userArray.size()-dieNum)){
			return 2;
		}
		
		return 0;
	}
	//판돈을 올렸을때 콜을 리셋한다.
	void callReset(){
		for(int i=0;i< userArray.size();i++){
			if(userArray.get(i).state.equals("call")){
				userArray.get(i).state="idle";
			}
		}
	}
	synchronized void setNextPlayerTurn(){
		playerturn++;
		if(playerturn>=userArray.size()){
			playerturn=0;
		}
		for(int i=0;i< userArray.size();i++){
			if(userArray.get(i).playerNumber==playerturn){
				if(userArray.get(i).state.equals("die")){
					setNextPlayerTurn();
				}else{
					break;
				}
			}
		}
	}
	void roomInitilize(){
		bet=100;
		amountMoney=0;
		playerturn=0;
		gameState="idle";
		isGameStart=false;
		shuffleCard.clear();
		cardPoint=0;
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
		priority[14][17] = priority[14][17] + 300;
		//구사
		priority[4][19] = priority[4][19] + 200;
		priority[4][9] = priority[4][9] + 200;
		priority[9][14] = priority[9][14] + 200;
		priority[14][19] = priority[14][19] + 200;
		
	}
	public int judge() {
		int winner=0;
		ArrayList<User> players=new ArrayList<User>();
		for(int i=0;i<userArray.size();i++){
			if(userArray.get(i).state!="die"){
				players.add(userArray.get(i));
			}
		}
		
		for(int i=0;i<players.size();i++){
			if(players.get(i).selectedCard1<players.get(i).selectedCard2){
			players.get(i).cardValue=
					priority[players.get(i).selectedCard1][players.get(i).selectedCard2];
			}else{
				players.get(i).cardValue=
						priority[players.get(i).selectedCard2][players.get(i).selectedCard1];
			}
		}
		
		int max = 0;
		for(int i = 0; i<players.size(); i++){
			//플레이어 i가 땡잡이일때
			if(players.get(i).cardValue>=400 && players.get(i).cardValue <500){
				for(int j = 0; j< players.size(); j++){
					//플레이어 j 중 땡이 있다면 3000점 플러스해서 땡을 잡는다.
					if(players.get(j).cardValue >= 1000 && players.get(j).cardValue <3000){
						players.get(i).cardValue = players.get(i).cardValue + 3000;
					}
				}
				//플레이어가 땡잡이인데 땡이없으면 망통으로 계산한다.
				if(players.get(i).cardValue>=400 && players.get(i).cardValue <500){
					players.get(i).cardValue = players.get(i).cardValue - 400;
				}
			}
			//플레이어 i가 암행어사일때
			if(players.get(i).cardValue>=300 && players.get(i).cardValue <400){
				for(int j = 0; j<players.size(); j++){
					//플레이어 j중에 광땡이 있다면 광땡을 이긴다.
					if(players.get(j).cardValue == 10000){
						players.get(i).cardValue = players.get(i).cardValue + 10100;
					}
				}
				//광땡이 없다면 한끝으로 계산된다.
				if(players.get(i).cardValue>=300 && players.get(i).cardValue <400){
					players.get(i).cardValue = players.get(i).cardValue - 300;
				}
			}
			
			// 플레이어 i가 구사일경우
			int goosa=0;
			if(players.get(i).cardValue>=200 &&players.get(i).cardValue <300){
				for(int j = 0; j<players.size(); j++){
					//플레이어가 모두 알리 이하이면 재경기를 한다.
					if(players.get(j).cardValue <= 1000){
						goosa+=1;
					}
				}
				if(goosa == players.size()){
					//구사 재경기!
					winner=1000;
					return winner;
				}
			}
			//최대값 갱신
			if(max < players.get(i).cardValue){
				max = players.get(i).cardValue;
			}
		}
		User winUSER;
		int maxvaluePlayers=0;
		for(int i = 0; i<players.size(); i++){
			if( players.get(i).cardValue==max){
				maxvaluePlayers++;
				winUSER=players.get(i);
				for(int j = 0; i<userArray.size(); j++){
					if( userArray.get(i).equals(winUSER)){
						winner=j;
						break;
					}
				}
			}
		}
		if(maxvaluePlayers>=2){
			winner=2000;
		}
		return winner;
		
		/*
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
		*/
	}
}

