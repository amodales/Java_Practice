import java.util.Vector;
import java.util.Random;

import java.io.*;
import java.awt.*;
import javax.swing.*;

import java.net.URL;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class Main {
	private static final long serialVersionUID = 1L;

	Vector<Card> deck = null;
	Vector<Vector<Card>> player_cards = null;
	
	AI[] cpu = new AI[3];

	//Painting Component
	ImageIcon lost = null, won = null;
	ImageIcon left = null, right = null, cross = null, cancel = null;
	ImageIcon start_menu = null;
	ImageIcon[] show_players = new ImageIcon[4];
	ImageIcon[] rotation = new ImageIcon[2];
	Canvas Game_Canvas = null;
	SCanvas Sum_Canvas = null;

	//Swing Component
	JFrame Game_Frame = null;
	JLayeredPane Game_Pane = null;
	JPanel Bottom = null, Top = null, Screen = null, Summary = null;
	JLabel board = null, menu = null;
	JLabel[] buttons = new JLabel[4];
	JLabel[] status = new JLabel[2];

	int score, outside, max, turn, dir;
	
	boolean is_started = false, is_end;
	boolean[] dead = new boolean[4];

	public static void main(String argv[]){
		Main Game = new Main();
		Game.CreateAndShow();
		//Game.start();
	}

	public Main(){
		Card temp;
		int i, j;
		/*
			Initialization Cards.
		*/
		deck = new Vector<Card>(52);
		player_cards = new Vector<Vector<Card>>(4);
		for(i=1; i<=4; i++){
			for(j=1; j<=13; j++){
				temp = new Card(i, j);
				deck.add(temp);
			}
		}
		//Pre-Loading
		try{
			lost = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/lost.gif", this)));
			won = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/won.gif", this)));
			left = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/left.png",this)));
			right = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/right.png", this)));
			cross = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/cross.png", this)));
			cancel = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/cancel.png", this)));
			rotation[0] = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/ck.png", this)));
			rotation[1] = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/cck.png", this)));
			start_menu = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/menu.png", this)));
			for(i=0; i<4; i++){
				show_players[i] = new ImageIcon(new URL(ResourceLoader.getImagePath("pic/p" + (i+1) + ".png", this)));
			}
		}catch(Exception e){

		}
		buttons[0] = new JLabel(left);
		buttons[1] = new JLabel(cross);
		buttons[2] = new JLabel(right);
		buttons[3] = new JLabel(cancel);
		board = new JLabel();
		menu = new JLabel(start_menu);
		status[0] = new JLabel();
		status[1] = new JLabel();
	}

	void CreateAndShow(){
		//Frame
		Game_Frame = new JFrame("Game");
		Game_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Top
		Top = new JPanel();
		Top.setLayout(new GridLayout(1, 4, 0, 0));
		Top.setBackground(new Color(0, 51, 0));
		for(int i=0; i<4; i++){
			Top.add(buttons[i]);
		}
		//Screen
		Screen = new JPanel();
		Screen.setLayout(new BorderLayout(0, 0));
		Screen.add(menu);
		//Bottom
		Bottom = new JPanel();
		Game_Canvas = new Canvas(this);
		Game_Canvas.setPreferredSize(new Dimension(800, 600));
		Bottom.setLayout(new BorderLayout(0, 0));
		Bottom.add(Game_Canvas);
		//Summary
		Summary = new JPanel();
		Sum_Canvas = new SCanvas(this);
		Sum_Canvas.setPreferredSize(new Dimension(600, 500));
		Summary.setLayout(new BorderLayout(0, 0));
		Summary.add(Sum_Canvas);
		//Layered
		Game_Pane = new JLayeredPane();
		Game_Pane.add(Bottom, JLayeredPane.DEFAULT_LAYER);
		Game_Pane.add(Top, JLayeredPane.PALETTE_LAYER);
		Game_Pane.add(Screen, JLayeredPane.MODAL_LAYER);
		Game_Pane.add(Summary, JLayeredPane.POPUP_LAYER);
		Bottom.setBounds(0, 0, 800, 600);
		Top.setBounds(250, 370, 300, 100);
		Screen.setBounds(100, 50, 600, 500);
		Summary.setBounds(100, 50, 600, 500);
		Summary.setVisible(false);
		Top.setVisible(false);
		Game_Pane.setPreferredSize(new Dimension(800, 600));
		Game_Frame.getContentPane().add(Game_Pane);
		Game_Frame.pack();
		Game_Frame.setVisible(true);
		Game_Canvas.UpdateGUI();
	}

	void Start_A_New_Game(){
		/*
			Initialization Cards & Distribute Cards
		*/
		deck = new Vector<Card>(52);
		player_cards = new Vector<Vector<Card>>(4);
		for(int i=1; i<=4; i++){
			for(int j=1; j<=13; j++){
				Card temp = new Card(i, j);
				deck.add(temp);
			}
		}
		Shuffle();
		Distribute_Cards();
		/*
			Initialization Variables
		*/
		score = 0;
		outside = 0;
		turn = 0;
		dir = 0;
		status[0].setIcon(show_players[turn]);
		status[1].setIcon(rotation[dir]);
		Top.revalidate();
		is_started = true;
		is_end = false;
		Game_Canvas.UpdateGUI();
		for(int i=0; i<3; i++){
			cpu[i] = new AI(this, i+1, 0);
		}
	}

	void start(){
		score = 0;
		outside = 0;
		turn = 0;
		dir = 0;
		status[0].setIcon(show_players[turn]);
		status[1].setIcon(rotation[dir]);
		Top.revalidate();
		Shuffle();
		Distribute_Cards();
		for(int i=0; i<3; i++){
			cpu[i] = new AI(this, i+1, 0);
		}
		is_started = true;
		Game_Canvas.UpdateGUI();
		is_end = false;
		while(!is_end){
			if(turn!=0){
				try{
					Thread.sleep(1000);
				}catch(Exception e){

				}
				if(!dead[turn]){
					cpu[turn-1].nextMove();
					status[0].setIcon(show_players[turn]);
					status[1].setIcon(rotation[dir]);
					Game_Pane.revalidate();
					Game_Canvas.UpdateGUI();
				}else{
					is_end = true;
				}
			}else{
				if(dead[turn])	is_end = true;
				Game_Pane.revalidate();
			}
		}
		is_started = false;
		status[0].setIcon(show_players[turn]);
		status[1].setIcon(rotation[dir]);
		Game_Pane.revalidate();
		Game_Canvas.UpdateGUI();
		System.out.print("Loser: Player ");
		System.out.println(turn+1);
	}

	void Shuffle(){
		int i, j;
		Card temp;
		Random rand = new Random();
		for(i=0; i<deck.size(); i++){
			j = rand.nextInt(deck.size());
			while(j==i){
				j = rand.nextInt(deck.size());
			}
			temp = deck.get(i);
			deck.set(i, deck.get(j));
			deck.set(j, temp);
		}
	}

	void Distribute_Cards(){
		int i, j, k;
		Vector<Card> temp;
		Card c;
		Random rand = new Random();
		for(i=0; i<4; i++){
			temp = new Vector<Card>();
			player_cards.add(temp);
			for(j=0; j<5; j++){
				k = rand.nextInt(deck.size());
				c = deck.get(k);
				c.isOkay = Preset_Card(c);
				player_cards.get(i).add(c);
				deck.removeElementAt(k);
			}
			dead[i] = false;
		}
		max = deck.size();
	}

	void GiveAndTake(int player_id, int card_id, int op){
		if(outside==max){
			outside = 0;
			Shuffle();
		}
		int delta, i, next_turn;
		Random rand = new Random();
		Card temp = player_cards.get(player_id).get(card_id);
		next_turn = (dir==0)? (turn + 1) % 4: (turn + 3) % 4;
		switch(temp.get_Num()){
			case 'A':
				if(temp.get_Face()=='S')	score = 0;
				else	score ++;
				break;
			case '2': case '3':
			case '6': case '7':
			case '8': case '9':
				delta = (int) temp.get_Num() - (int) '0';
				score += delta;
				break;
			case '4':
				dir = (dir + 1) % 2;
				next_turn = (dir==0)? (turn + 1) % 4: (turn + 3) % 4;
				break;
			case '5':
				if(op==0){
					next_turn = (turn + 3) % 4;
				}else if(op==1){
					next_turn = (turn + 2) % 4;
				}else if(op==2){
					next_turn = (turn + 1) % 4;
				}
				break;
			case 'T':
				score -= 10;
				if(score<0)	score = 0;
				break;
			case 'J': case 'Q':
				score += 10;
				break;
			case 'K':
				score = 99;
				break;
		}
		i = rand.nextInt(deck.size());
		deck.add(temp);
		outside++;
		temp = deck.get(i);
		deck.removeElementAt(i);
		player_cards.get(player_id).setElementAt(temp, card_id);
		turn = next_turn;
		Reset_All_Flags();
	}

	boolean Preset_Card(Card c){
		int delta;
		switch(c.get_Num()){
			case 'A':
				if(c.get_Face()=='S'){
					return true;
				}else{
					if(score+1<=99)	return true;
					else	return false;
				}
			case '2': case '3':
			case '6': case '7':
			case '8': case '9':
				delta = (int) c.get_Num() - (int) '0';
				if(delta+score<=99)	return true;
				else return false;
			case '4': case '5':
			case 'T': case 'K':
				return true;
			case 'J': case 'Q':
				if(score+10<=99)	return true;
				else	return false;
		}
		return false;
	}

	void Reset_All_Flags(){
		Card temp;
		boolean check;
		for(int i=0; i<4; i++){
			check = false;
			for(int j=0; j<5; j++){
				temp = player_cards.get(i).get(j);
				temp.isOkay = Preset_Card(temp);
				check = check || temp.isOkay;
			}
			dead[i] = !check;
		}
	}
}
