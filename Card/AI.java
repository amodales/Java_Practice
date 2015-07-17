import java.util.Vector;
import java.util.Random;

public class AI{
	
	Main Game_Info;

	int player_id;
	int mode;

	AI(Main me, int p_id, int mod){
		Game_Info = me;
		player_id = p_id;
		mode = mod;
	}

	public void setMode(int mod){
		mode = mod;
	}

	public void nextMove(){
		Random rand = new Random();
		int c_id = rand.nextInt(5), op;
		while(!Game_Info.player_cards.get(player_id).get(c_id).isOkay){
			c_id = rand.nextInt(5);
		}
		if(Game_Info.player_cards.get(player_id).get(c_id).get_Num()=='5'){
			int next_turn;
			do{
				op = rand.nextInt(3);
				next_turn = (player_id + 3 - op) % 4;
			}while(Game_Info.dead[next_turn]);
		}else{
			op = 0;
		}
		Game_Info.GiveAndTake(player_id, c_id, op);
	}
}
