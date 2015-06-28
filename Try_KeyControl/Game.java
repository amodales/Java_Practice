import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Game extends JApplet{
	private static final long serialVersionUID = 1L;
	public static boolean isFrame = false;
	static Dimension f_dim;
	Ani up = null;
	Control bottom = null; 
	public static void main(String argv[]){
		JFrame f = new JFrame("Animation Test");
		Game me = new Game();
		isFrame = true;
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600,600);
		f_dim = f.getSize();
		f.setVisible(true);
		me.init(); me.start();
		f.add(me);
		f.validate();
	}
	public void init(){
		up = new Ani();
		bottom = new Control(up);
		if(isFrame) up.dim = f_dim;
		setLayout(new BorderLayout());
		add(up,"Center");
		add(bottom,"South");
		up.init();
		up.start();
		validate();
	}
	public void start(){
		super.start();
	}
}

class Ani extends JApplet implements KeyListener{
	public long delayTime = 100;
	public boolean isstop = false;
	public boolean ispause = false;
	Graphics bufferedGraphic = null;
	Image img = null;
	Dimension dim = null;
	Process_Ani_Move p = null;
	Vector<Square> objs = new Vector<Square>();
	String string_now = "0";
	int x_now, y_now,index = 0;
	boolean isUp = false,isLeft = false,isRight = false,isDown = false;
	
	public void init(){
		p = new Process_Ani_Move(this);
		addKeyListener(this);
		p.start();
	}
	
	public void prepare(){
		if(dim == null){
			dim = getSize();
			while(dim.width <= 0 || dim.height <= 0){
				dim = getSize();
			}
		}
		if(img == null || bufferedGraphic == null){
			while(img == null){
				img = createImage(dim.width,dim.height);
			}
			bufferedGraphic = img.getGraphics();
		}
		x_now = (dim.width/2)-25;
		y_now = (dim.height/2)-25;
		objs.addElement(new Square(this,dim));
	}
	
	public void redraw(){
		Enumeration<Square> e = objs.elements();
		if(dim == null || img == null || bufferedGraphic == null) prepare();
		bufferedGraphic.setColor(Color.BLACK);
		bufferedGraphic.fillRect(0,0,dim.width,dim.height);
		while(e.hasMoreElements()){
			e.nextElement().draw();
		}
		repaint();
	}
	
	public void moveup(Square s){
		if(s.y - 5 >= 0) s.y -= 5;
		redraw();
	}

	public void moveright(Square s){
		if(s.x + 5 <= dim.width-70) s.x += 5;
		redraw();
	}
	
	public void moveleft(Square s){
		if(s.x - 5 >= 0) s.x -=5;
		redraw();
	}
	
	public void movedown(Square s){
		if(s.y + 5 <= dim.height-130 ) s.y +=5;
		redraw();
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == 38) isUp = true;
		if(e.getKeyCode() == 37) isLeft = true;
		if(e.getKeyCode() == 39) isRight = true;
		if(e.getKeyCode() == 40) isDown = true;
		if(isUp){ moveup(objs.get(index));}
		if(isLeft) moveleft(objs.get(index));
		if(isDown) movedown(objs.get(index));
		if(isRight) moveright(objs.get(index));
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == 38) isUp = false;
		else if(e.getKeyCode() == 37) isLeft = false;
		else if(e.getKeyCode() == 39) isRight = false;
		else if(e.getKeyCode() == 40) isDown = false;
		else if(e.getKeyCode() == 10) {index++; objs.add(new Square(this,dim));redraw();}
		else if(e.getKeyChar() != e.CHAR_UNDEFINED){
			char[] k = new char[2];
			k[0] = e.getKeyChar();
			k[1] = '\0';
			string_now = new String(k);
			string_now = string_now.toUpperCase();
			objs.get(index).text = string_now;
			redraw();
		}
	}
	
	public void keyTyped(KeyEvent e){
		;
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}
}

class Process_Ani_Move extends Thread{
	Ani me;
	public Process_Ani_Move(Ani me){
		this.me = me;
	}
	public void run(){
		me.prepare();
		me.redraw();
		while(!me.isstop){
			me.requestFocus();
		}
	}
}

class Control extends JPanel implements ActionListener{
	Ani me;
	JButton green,red,blue,up,down;
	public Control(Ani me){
		this.me = me;
		setBackground(Color.BLACK);
		add_buttons_color();
		add_buttons_layer();
	}
	
	public void add_buttons_color(){
		green = new JButton("Green");
		red = new JButton("Red");
		blue = new JButton("Blue");
		green.setBackground(Color.WHITE);
		green.setForeground(Color.BLACK);
		red.setBackground(Color.WHITE);
		red.setForeground(Color.BLACK);
		blue.setBackground(Color.WHITE);
		blue.setForeground(Color.BLACK);
		green.addActionListener(this);
		red.addActionListener(this);
		blue.addActionListener(this);
		add(green);
		add(red);
		add(blue);
	}
	
	public void add_buttons_layer(){
		up = new JButton("＋");
		down = new JButton("–");
		up.setBackground(Color.WHITE);
		up.setForeground(Color.BLACK);
		down.setBackground(Color.WHITE);
		down.setForeground(Color.BLACK);
		up.addActionListener(this);
		down.addActionListener(this);
		add(up);
		add(down);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == green){
			me.objs.get(me.index).c = Color.green;
			me.redraw();
		}
		if(e.getSource() == red){
			me.objs.get(me.index).c = Color.red;
			me.redraw();
		}
		if(e.getSource() == blue){
			me.objs.get(me.index).c = Color.blue;
			me.redraw();
		}
		if(e.getSource() == up){
			if(me.index+1 < me.objs.size()) me.index ++;
		}
		if(e.getSource() == down){
			if(me.index-1 >= 0) me.index --;
		}
	}
}

class Square {
	Ani me;
	public int x,y;
	public String text = "A";
	Color c = Color.red;
	public Square(Ani me,Dimension dim){
		this.me = me;
		x = dim.width/2-25;
		y = dim.height/2-25;
	}
	public void draw(){
		me.bufferedGraphic.setColor(Color.WHITE);
		me.bufferedGraphic.fillRect(x, y, 50, 50);
		me.bufferedGraphic.setColor(c);
		me.bufferedGraphic.setFont(new Font("Courier",Font.PLAIN , 20));
		me.bufferedGraphic.drawString(text,x+20,y+33);
	}
}
