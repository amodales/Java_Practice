import java.lang.Thread;
import javax.swing.SwingUtilities;

public class Game_Process extends Thread{
	
	Main Game_Info;

	private static boolean started = false;

	Game_Process(Main g){
		Game_Info = g;
	}

	public void run(){
		if(!started){
			started = true;
			int winner = 4;
			Game_Info.Start_A_New_Game();
			while(!Game_Info.is_end){
				if(Game_Info.turn!=0){
					try{
						sleep(1000);
					}catch(Exception e){

					}
					if(Game_Info.is_able[Game_Info.turn]){
						Game_Info.cpu[Game_Info.turn-1].nextMove();
						Game_Info.status[0].setIcon(Game_Info.show_players[Game_Info.turn]);
						Game_Info.status[1].setIcon(Game_Info.rotation[Game_Info.dir]);
						Update_GUI();
					}else{
						Game_Info.dead[Game_Info.turn] = true;
						Update_GUI();
						try{
							sleep(2000);
						}catch(Exception e){

						}
						/*
							Put Cards Back to the deck.
						*/
						Card top, temp;
						top = Game_Info.deck.get(Game_Info.deck.size()-1);
						Game_Info.deck.removeElementAt(Game_Info.deck.size()-1);
						for(int i=0; i<5; i++){
							temp = Game_Info.player_cards.get(Game_Info.turn).get(i);
							Game_Info.deck.add(temp);
						}
						Game_Info.player_cards.get(Game_Info.turn).clear();
						Game_Info.deck.add(top);
						while(Game_Info.dead[Game_Info.turn]){
							Game_Info.turn = (Game_Info.dir==0)? (Game_Info.turn + 1) % 4: (Game_Info.turn + 3) % 4;
						}
						Update_GUI();
					}
				}else{
					//Player's turn
					if(Game_Info.is_able[0]){
						while(Game_Info.turn==0){
							yield();
						}
					}else{
						Game_Info.dead[0] = true;
						Update_GUI();
						try{
							sleep(2000);
						}catch(Exception e){

						}
						/*
							Put Cards Back to the deck.
						*/
						Card top, temp;
						top = Game_Info.deck.get(Game_Info.deck.size()-1);
						Game_Info.deck.removeElementAt(Game_Info.deck.size()-1);
						for(int i=0; i<5; i++){
							temp = Game_Info.player_cards.get(Game_Info.turn).get(i);
							Game_Info.deck.add(temp);
						}
						Game_Info.player_cards.get(Game_Info.turn).clear();
						Game_Info.deck.add(top);
						while(Game_Info.dead[Game_Info.turn]){
							Game_Info.turn = (Game_Info.dir==0)? (Game_Info.turn + 1) %4: (Game_Info.turn + 3) % 4;
						}
						Update_GUI();
					}
				}
				/*
					Check if game end.
				*/
				int count = 0;
				for(int i=0; i<4; i++){
					if(!Game_Info.dead[i])	winner = i;
					else	count++;
				}
				if(count==3)	Game_Info.is_end = true;
			}
			Game_Info.is_started = false;
			Update_GUI();
			try{
				sleep(2000);
			}catch(Exception e){

			}
			Game_Info.is_end = false;
			if(winner!=4){
				Game_Info.player_coins[winner]++;
			}else{
				System.err.println("Error in Game Loop.");
			}
			try{
				sleep(1000);
			}catch(Exception e){
			
			}
			Update_GUI();
			Game_Info.Sum_Canvas.UpdateSummary();
			Game_Info.Summary.setVisible(true);
			started = false;
		}
	}

	public void Update_GUI(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Game_Info.Game_Canvas.UpdateGUI();
			}
		});
	}
}
