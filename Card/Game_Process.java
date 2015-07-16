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
			Game_Info.Start_A_New_Game();
			while(!Game_Info.is_end){
				if(Game_Info.turn!=0){
					try{
						sleep(1000);
					}catch(Exception e){

					}
					if(!Game_Info.dead[Game_Info.turn]){
						Game_Info.cpu[Game_Info.turn-1].nextMove();
						Game_Info.status[0].setIcon(Game_Info.show_players[Game_Info.turn]);
						Game_Info.status[1].setIcon(Game_Info.rotation[Game_Info.dir]);
						Update_GUI();
					}else{
						Game_Info.is_end = true;
					}
				}else{
					if(Game_Info.dead[0]){
						Game_Info.is_end = true;
					}else{
						while(Game_Info.turn==0){
							yield();
						}
					}
				}
			}
			Game_Info.is_started = false;
			Update_GUI();
			try{
				sleep(4500);
			}catch(Exception e){

			}
			Update_GUI();
			try{
				Game_Info.is_end = false;
				sleep(1500);
			}catch(Exception e){
				
			}
			Game_Info.Sum_Canvas.UpdateSummary();
			Game_Info.Summary.setVisible(true);
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
