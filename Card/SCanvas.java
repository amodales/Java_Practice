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

public class SCanvas extends Component{
	public static final long serialVersionUID = 3L;

	Graphics BufferedGraphics = null;
	Image img = null;

	Main Game_Info = null;
	Dimension Canvas_Dim = null;

	BufferedImage background = null;
	BufferedImage coin = null;

	SCanvas(Main m){
		Game_Info = m;
		try{
			background = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/summary.png", this)));
			coin = ImageIO.read(new URL(ResourceLoader.getImagePath("pic/coin.png", this)));
		}catch(IOException e){
			e.printStackTrace();
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

	public void UpdateSummary(){
		while(Canvas_Dim==null||img==null||BufferedGraphics==null){
			Prepare_Painter();
		}
		Paint_BackGround(BufferedGraphics);
		Paint_Coins(BufferedGraphics);
		repaint();
	}

	public void paint(Graphics g){
		while(Canvas_Dim==null||img==null||BufferedGraphics==null){
			Prepare_Painter();
		}
		g.drawImage(img, 0, 0, null);
	}

	public void Paint_BackGround(Graphics g){
		g.drawImage(background, 0, 0, null);
	}

	public void Paint_Coins(Graphics g){

	}

	private static class Summary_Listener implements MouseListener{
		
		SCanvas owner;

		Summary_Listener(SCanvas sc){
			owner = sc;
		}
		
		public void mouseClicked(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			if(is_inButton(x, y)){
				owner.Game_Info.Summary.setVisible(false);
				Game_Process gp = new Game_Process(owner.Game_Info);
				gp.start();
			}
		}

		public void mouseEntered(MouseEvent e){
			//Do Nothing
		}

		public void mousePressed(MouseEvent e){
			//Do Nothing
		}

		public void mouseReleased(MouseEvent e){
			//Do Nothing
		}
		
		public void mouseExited(MouseEvent e){
			//Do Nothing
		}

		boolean is_inButton(int x, int y){
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
