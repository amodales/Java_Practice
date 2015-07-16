import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.Vector;

public class Canvas extends Component{
	public static final long serialVersionUID = 2L;

	Graphics BufferedGraphics = null;
	Image img = null;

	Main Game_Info = null;
	Dimension Canvas_Dim = null;
	
	BufferedImage[] image_score = new BufferedImage[10];
	BufferedImage[] poker_cards = new BufferedImage[52];
	BufferedImage[] poker_cards_p90 = new BufferedImage[52];
	BufferedImage[] poker_cards_m90 = new BufferedImage[52];
	BufferedImage[] select = new BufferedImage[4];

	BufferedImage card_back_vertical = null;
	BufferedImage card_back_horizontal = null;
	BufferedImage background = null;
	BufferedImage new_cck = null, new_ck = null;
	
	Buttons_Listener[] B_Listeners = new Buttons_Listener[4];

	boolean is_showMenu = false;

	Canvas(Main m){
		/*
			Variable Initialization
			and 
			Setting Listeners.
		*/
		Game_Info = m;
		addMouseListener(new Cards_Listener(this));
		for(int i=0; i<4; i++){
			B_Listeners[i] = new Buttons_Listener(this, i);
			Game_Info.buttons[i].addMouseListener(B_Listeners[i]);
		}
		Game_Info.Screen.addMouseListener(new Menu_Listener(this));
		/*
			Pre-loading images.
			Digits
		*/
		for(int i=0; i<10; i++){
			try{
				image_score[i] = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/D" + i + ".jpg", this)));
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		/*
			Pre-loading images.
			Card_Back, ck&cck icons and Background
		*/
		try{
			card_back_vertical = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/card_back.jpg", this)));
			background = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/background.png", this)));
			new_ck = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/new_ck.png", this)));
			new_cck = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/new_cck.png", this)));
		}catch(IOException e){
			e.printStackTrace();
		}
		/*
			Pre-loading images.
			4 Select icons
		*/
		for(int i=0; i<4; i++){
			try{
				select[i] = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/select_p"+(i+1)+".png", this)));
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		/*
			Rotate 90 degree Card_Back
		*/
		if(card_back_vertical!=null){
			int w = card_back_vertical.getWidth();
			int h = card_back_vertical.getHeight();
			card_back_horizontal = new BufferedImage(h, w, card_back_vertical.getType());
			for(int i=0; i<w; i++){
				for(int j=0; j<h; j++){
					card_back_horizontal.setRGB(j, i, card_back_vertical.getRGB(i, j));
				}
			}
		}
		/*
			Pre-loading images.
			Cards
		*/
		for(int i=0; i<52; i++){
			try{
				poker_cards[i] = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/" + (i+1) + ".jpg", this)));
			}catch(IOException e){
				e.printStackTrace();
				System.out.println(ResourceLoader.getImagePath("/pic/"+(i+1)+".jpg", this));
			}
		}
		/*
			Rotate 90 degree Card_Face
		*/
		for(int i=0; i<52; i++){
			int w = poker_cards[i].getWidth();
			int h = poker_cards[i].getHeight();
			poker_cards_m90[i] = new BufferedImage(h, w, poker_cards[i].getType());
			poker_cards_p90[i] = new BufferedImage(h, w, poker_cards[i].getType());
			for(int j=0; j<w; j++){
				for(int k=0; k<h; k++){
					poker_cards_m90[i].setRGB(k, j, poker_cards[i].getRGB(j, k));
					poker_cards_p90[i].setRGB(h-k-1, j, poker_cards[i].getRGB(j, k));
				}
			}
		}
	}

	public void Prepare_Painter(){
		if(Canvas_Dim==null)	Canvas_Dim = getSize();
		if(Canvas_Dim.width==0||Canvas_Dim.height==0)	return;
		img = createImage(Canvas_Dim.width, Canvas_Dim.height);
		BufferedGraphics = img.getGraphics();
	}

	public void update(Graphics g){
		paint(g);
	}

	public void UpdateGUI(){
		while(Canvas_Dim==null||img==null||BufferedGraphics==null){
			Prepare_Painter();
		}
		Paint_BackGround(BufferedGraphics);
		Paint_DecknScore(BufferedGraphics);
		if(Game_Info.is_started){
			Paint_Players_Cards(BufferedGraphics);
		}else{
			if(Game_Info.is_end){
				Paint_All_Players_Cards(BufferedGraphics);
			}
		}
		repaint();
	}

	public void paint(Graphics g){
		while(Canvas_Dim==null||img==null||BufferedGraphics==null){
			Prepare_Painter();
		}
		g.drawImage(img, 0, 0, null);
	}

	void Paint_BackGround(Graphics g){
		g.drawImage(background, 0, 0, null);
	}

	void Paint_DecknScore(Graphics g){
		int center_x, center_y, x, y;
		Card temp;
		center_x = Canvas_Dim.width/2;
		center_y = Canvas_Dim.height/2;
		if(Game_Info.outside!=0){
			temp = Game_Info.deck.get(Game_Info.max-1);
			g.drawImage(poker_cards[get_Card_Index(temp)], center_x-135, center_y-50, null);
		}
		if(Game_Info.outside!=Game_Info.max){
			g.drawImage(card_back_vertical, center_x+61, center_y-50, null);
		}
		if(Game_Info.dir==0){
			g.drawImage(new_cck, center_x-47, center_y-25-29, null);
		}else{
			g.drawImage(new_ck, center_x-47, center_y-25-29, null);
		}
		g.drawImage(image_score[Game_Info.score/10], center_x-47, center_y-25+29, null);
		g.drawImage(image_score[Game_Info.score%10], center_x+3, center_y-25+29, null);
		switch(Game_Info.turn){
			case 0:
				x = center_x-30+2;
				y = center_y-25+29+50+30;
				break;
			case 1:
				x = center_x+61+80+30;
				y = center_y-30;
				break;
			case 2:
				x = center_x-30+2;
				y = center_y-25-29-60-30;
				break;
			case 3:
				x = center_x-135-60-30;
				y = center_y-30;
				break;
			default:
				//NO WAY
				x = center_x;
				y = center_y;
		}
		g.drawImage(select[Game_Info.turn], x, y, null);
	}

	void Paint_Players_Cards(Graphics g){
		/*
			Top and Bottom Player
		*/
		for(int i=0; i<5; i++){
			g.drawImage(card_back_vertical, (Canvas_Dim.width-400)/2+80*i, 2, null);
			Card temp = Game_Info.player_cards.get(0).get(i);
			int index = get_Card_Index(temp);
			if(temp.isOkay){
				g.drawImage(poker_cards[index], (Canvas_Dim.width-400)/2+80*i, Canvas_Dim.height-102, null);
			}else{
				g.drawImage(get_Gray_ICard(poker_cards[index]), (Canvas_Dim.width-400)/2+80*i, Canvas_Dim.height-102, null);
			}
		}
		/*
			Left and Right Player
		*/
		for(int i=0; i<5; i++){
			g.drawImage(card_back_horizontal, Canvas_Dim.width-102, (Canvas_Dim.height-400)/2+80*i, null);
			g.drawImage(card_back_horizontal, 2, (Canvas_Dim.height-400)/2+80*i, null);
		}
	}

	void Paint_All_Players_Cards(Graphics g){
		/*
			Top and  Bottom Player
		*/
		for(int i=0; i<5; i++){
			Card temp = Game_Info.player_cards.get(2).get(i);
			int index = get_Card_Index(temp);
			if(temp.isOkay){
				g.drawImage(poker_cards[index], (Canvas_Dim.width-400)/2+80*i, 2, null);
			}else{
				g.drawImage(get_Gray_ICard(poker_cards[index]), (Canvas_Dim.width-400)/2+80*i, 2, null);
			}	
			temp = Game_Info.player_cards.get(0).get(i);
			index = get_Card_Index(temp);
			if(temp.isOkay){
				g.drawImage(poker_cards[index], (Canvas_Dim.width-400)/2+80*i, Canvas_Dim.height-102, null);
			}else{
				g.drawImage(get_Gray_ICard(poker_cards[index]), (Canvas_Dim.width-400)/2+80*i, Canvas_Dim.height-102, null);
			}
		}
		/*
			Left and Right player
		*/
		for(int i=0; i<5; i++){
			Card temp = Game_Info.player_cards.get(3).get(i);
			int index = get_Card_Index(temp);
			if(temp.isOkay){
				g.drawImage(poker_cards_m90[index], 2, (Canvas_Dim.height-400)/2+80*i, null);
			}else{
				g.drawImage(get_Gray_ICard(poker_cards_m90[index]), 2, (Canvas_Dim.height-400)/2+80*i, null);
			}
			temp = Game_Info.player_cards.get(1).get(i);
			index = get_Card_Index(temp);
			if(temp.isOkay){
				g.drawImage(poker_cards_p90[index], Canvas_Dim.width-102, (Canvas_Dim.height-400)/2+80*i, null);
			}else{
				g.drawImage(get_Gray_ICard(poker_cards_p90[index]), Canvas_Dim.width-102, (Canvas_Dim.height-400)/2+80*i, null);
			}
		}
	}

	int get_Card_Index(Card c){
		int ret = -1;
		switch(c.get_Face()){
			case 'S':
				ret += 39;
				break;
			case 'H':
				ret += 26;
				break;
			case 'D':
				ret += 13;
				break;
			case 'C':
				break;
		}
		switch(c.get_Num()){
			case 'A':
				ret += 1;
				break;
			case '2': case '3':
			case '4': case '5':
			case '6': case '7':
			case '8': case '9':
				ret += ((int) c.get_Num() - (int) '0');
				break;
			case 'T':
				ret += 10;
				break;
			case 'J':
				ret += 11;
				break;
			case 'Q':
				ret += 12;
				break;
			case 'K':
				ret += 13;
				break;
		}
		return ret;
	}

	public BufferedImage get_Gray_ICard(BufferedImage c){
		BufferedImage ret = new BufferedImage(c.getWidth(), c.getHeight(), c.getType());
		int lw = c.getWidth();
		int lh = c.getHeight();
		for(int w=0; w<lw; w++){
			for(int h=0; h<lh; h++){
				int rgb = c.getRGB(w, h);
				int cr = (rgb >> 16) & 0xFF;
				int cg = (rgb >> 8) & 0xFF;
				int cb = rgb & 0xFF;
				int graylevel = (cr + cg + cb) / 3;
				int gray;
				if(cr<=0x33&&cg<=0x33&&cb<=0x33)	gray = 0x666666;
				else gray = (graylevel << 16) + (graylevel << 8) + graylevel;
				ret.setRGB(w, h, gray);
			}
		}
		return ret;
	}

	private static class Cards_Listener implements MouseListener{
		
		Canvas owner; 
	
		public Cards_Listener(Canvas c){
			owner = c;
		}

		public void mouseClicked(MouseEvent e){
			if(owner.Game_Info.is_started&&owner.Game_Info.turn==0){
				int x = e.getX();
				int y = e.getY();
				int p = -1, i;
				char num;
				Card temp = null;
				if(x>=(owner.Canvas_Dim.width-400)/2&&x<(owner.Canvas_Dim.width-400)/2+80&&y<=(owner.Canvas_Dim.height-2)&&y>=(owner.Canvas_Dim.height-102)){
					p = 0;
				}
				if(x>=(owner.Canvas_Dim.width-400)/2+80&&x<(owner.Canvas_Dim.width-400)/2+160&&y<=(owner.Canvas_Dim.height-2)&&y>=(owner.Canvas_Dim.height-102)){
					p = 1;
				}
				if(x>=(owner.Canvas_Dim.width-400)/2+160&&x<(owner.Canvas_Dim.width-400)/2+240&&y<=(owner.Canvas_Dim.height-2)&&y>=(owner.Canvas_Dim.height-102)){
					p = 2;
				}
				if(x>=(owner.Canvas_Dim.width-400)/2+240&&x<(owner.Canvas_Dim.width-400)/2+320&&y<=(owner.Canvas_Dim.height-2)&&y>=(owner.Canvas_Dim.height-102)){
					p = 3;
				}
				if(x>=(owner.Canvas_Dim.width-400)/2+320&&x<(owner.Canvas_Dim.width-400)/2+400&&y<=(owner.Canvas_Dim.height-2)&&y>=(owner.Canvas_Dim.height-102)){
					p = 4;
				}
				if(p!=-1)	temp = owner.Game_Info.player_cards.get(0).get(p);
				if(temp!=null&&temp.isOkay){
					num = temp.get_Num();
					if(num!='5'){
						owner.is_showMenu = false;
						owner.Game_Info.GiveAndTake(0, p, 0);
						owner.Game_Info.Top.setVisible(false);
						owner.UpdateGUI();
					}
					if(num=='5'&&!owner.is_showMenu){
						owner.is_showMenu = true;
						for(i=0; i<4; i++){
							owner.B_Listeners[i].set_Info(0, p);
						}
						owner.Game_Info.Top.setVisible(true);
						owner.UpdateGUI();
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e){
			//Do Nothing
		}

		public void mouseReleased(MouseEvent e){
			//Do Nothing
		}

		public void mousePressed(MouseEvent e){
			//Do Nothing
		}

		public void mouseExited(MouseEvent e){
			//Do Nothing
		}
	}

	private static class Buttons_Listener implements MouseListener{

		int player_id, card_id, opt;

		Canvas owner;

		public Buttons_Listener(Canvas c, int op){
			owner = c;
			opt = op;
		}

		void set_Info(int p_id, int c_id){
			player_id = p_id;
			card_id = c_id;
		}

		public void mouseClicked(MouseEvent e){
			if(owner.Game_Info.is_started&&owner.Game_Info.turn==0){
				if(opt!=3){
					owner.Game_Info.GiveAndTake(player_id, card_id, opt);
				}
				owner.is_showMenu = false;
				owner.Game_Info.Top.setVisible(false);
				owner.UpdateGUI();
			}
		}

		public void mouseEntered(MouseEvent e){
			//Do Nothing
		}

		public void mouseReleased(MouseEvent e){
			//Do Nothing
		}

		public void mousePressed(MouseEvent e){
			//Do Nothing
		}

		public void mouseExited(MouseEvent e){
			//Do Nothing
		}
	}

	private static class Menu_Listener implements MouseListener{
		
		Canvas owner;

		public Menu_Listener(Canvas c){
			owner = c;
		}
		
		public void mouseClicked(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			if(is_inButton1(x, y)){
				owner.Game_Info.Screen.setVisible(false);
				Game_Process gp = new Game_Process(owner.Game_Info);
				gp.start();
			}else if(is_inButton2(x, y)){
				//Temporarily do nothing
			}else if(is_inButton3(x, y)){
				System.exit(0);
			}
		}

		public void mouseEntered(MouseEvent e){
			//Do Nothing
		}

		public void mouseReleased(MouseEvent e){
			//Do Nothing
		}

		public void mousePressed(MouseEvent e){
			//Do Nothing
		}

		public void mouseExited(MouseEvent e){
			//Do Nothing
		}

		boolean is_inButton1(int x, int y){
			if(x>=436&&x<=575&&y>=276&&y<=276+39){
				return true;
			}else if(x>=424&&x<=587&&y>=286&&y<=286+19){
				return true;
			}else{
				return false;
			}
		}

		boolean is_inButton2(int x, int y){
			if(x>=436&&x<=575&&y>=329&&y<=329+39){
				return true;
			}else if(x>=424&&x<=587&&y>=339&&y<=339+19){
				return true;
			}else{
				return false;
			}
		}

		boolean is_inButton3(int x, int y){
			if(x>=436&&x<=575&&y>=435&&y<=435+39){
				return true;
			}else if(x>=424&&x<=587&&y>=445&&y<=445+19){
				return true;
			}else{
				return false;
			}
		}
	}
}
