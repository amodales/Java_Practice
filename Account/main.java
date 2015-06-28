import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class main extends Frame implements WindowListener{
	public int base=5,level=5;
	String filename = null;
	int round = 1,makers = 0,lap = 1;
	FileReader fr = null;
	FileWriter fw = null;
	BufferedReader fin = null;
	PrintStream cout = null;
	PrintStream fout = null;
	JPanel all_status;
	JPanel[] p_status = new JPanel[4];
	ControlName[] p_editname = new ControlName[4];
	ControlGame[] p_game = new ControlGame[4] ;
	ControlAll setting;
	JLabel[] p_name = new JLabel[4];
	JLabel[] p_money = new JLabel[4];
	JLabel[] p_delta = new JLabel[4];
	JLabel[] p_makers = new JLabel[4];
	JLabel[] p_directions = new JLabel[4];
	int[] rank = new int[4];
	Vector<Record> records = new Vector<Record>();
	
	public static void main(String argv[]){
		main me = new main("Accounting Test");
		me.setSize(850,236);
		me.setVisible(true);
		me.prepareIO();
		me.prepareUI();
		me.prepareOthers();
	}
	
	public main(String name){
		super(name);
	}
	
	void prepareOthers(){
		for(int i=0;i<4;i++){
			rank[i] = 0;
		}
		makers = askMakers();
		records.add(new Record(this));
		changeMakers();
		//changeDirections();
		validate();
		repaint();
	}
	
	void prepareIO(){
		cout = System.out;
		Date now = new Date();
		filename = parseFilename(now);
		filename += ".dat";
	}
	void prepareUI(){
		addWindowListener(this);
		
		all_status = new JPanel();
		all_status.setLayout(new GridLayout(4,3));
		

		for(int i = 0 ; i<=3 ;i++){
			p_name[i] = new JLabel("玩家"+Integer.toString(i+1)+" : ");
			p_money[i] = new JLabel("0");
			p_delta[i] = new JLabel("(0)");
			p_makers[i] = new JLabel(" ");
			p_directions[i] = new JLabel(" ");
			p_makers[i].setForeground(Color.RED);
			p_status[i] = new JPanel();
			p_status[i].add(p_directions[i]);
			p_status[i].add(p_makers[i]);
			p_status[i].add(p_name[i]);
			p_status[i].add(p_money[i]);
			p_status[i].add(p_delta[i]);
			p_editname[i] = new ControlName(p_name[i],this);
			p_game[i] = new ControlGame(this,i);
		}
		for(int i = 0;i<=3;i++){
			all_status.add(p_status[i]);
			all_status.add(p_game[i]);
			all_status.add(p_editname[i]);
		}
		setting = new ControlAll(this);
		add(all_status,"Center");
		add(setting,"South");
		validate();
		repaint();
	}
	
	public void windowDeiconified(WindowEvent e){;}
	public void windowIconified(WindowEvent e){;}
	public void windowClosing(WindowEvent e){saveRecord(false);System.exit(0);}
	public void windowOpening(WindowEvent e){;}
	public void windowClosed(WindowEvent e){;}
	public void windowOpened(WindowEvent e){;}
	public void windowActivated(WindowEvent e){;}
	public void windowDeactivated(WindowEvent e){;}
	
	public String parseFilename(Date now){
		String temp = null;
		String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		StringTokenizer stk = new StringTokenizer(now.toString()," ");
		stk.nextToken();
		temp=stk.nextToken();
		for(int i=0;i<12;i++){
			if(temp.equals(months[i])) temp = Integer.toString(i+1);
		}
		temp+="_";
		temp+=stk.nextToken();
		temp+="_";
		stk.nextToken();
		stk.nextToken();
		temp+=stk.nextToken();
		return temp;
	}
	
	public void saveRecord(boolean kind){
		records.get(round-1).updateInf();
		int index = 1;
		String stemp = null;
		Enumeration<Record> e = records.elements();
		if(kind){
			if(isFileExist(filename)){
				stemp = filename.substring(0, filename.length()-4) + "(" + Integer.toString(index)+ ").dat";
				index ++;
				while(isFileExist(stemp)){
					stemp = filename.substring(0, filename.length()-4) + "(" + Integer.toString(index)+ ").dat";
					index++;
				}
			}
		}
		if(stemp != null) filename = stemp;
		try{
			fout = new PrintStream(new FileOutputStream(filename,false));
		}catch(Exception ex){;}
		while(e.hasMoreElements()){
			Record temp = e.nextElement();
			if(temp.isModified)	fout.println(temp.toString());
		}
		fout.close();
	}
	
	public void loadRecord(String filename){
		
	}
	
	public int askMaker(){
		if(isCon()>=1){ return records.get(round-2).makers;}
		String[] choices;
		int temp = 0;
		if(round>=2){	
			choices = new String[3];
			int index = 0 ;
			for(int i=0;i<4;i++){
				if(records.get(round-2).makers != i){
					choices[index] = p_name[i].getText().substring(0,p_name[i].getText().length()-3);
					index++;
				}
			}
		}else{
			choices = new String[4];
			for(int i=0;i<4;i++){
				choices[i] = p_name[i].getText().substring(0,p_name[i].getText().length()-3);
			}
		}
		String s = null;
		while(s==null){
			s = (String)JOptionPane.showInputDialog(new Frame(),"請選擇這把誰莊家：",
		               " ",JOptionPane.PLAIN_MESSAGE,null,choices,choices[0]);
		}
		for(int i=0;i<4;i++){
			if(s.equals(p_name[i].getText().substring(0,p_name[i].getText().length()-3))) temp = i;
		}
		return temp;
	}
	
	public int askMakers(){
		if(rank[0] != 0){
			if(isCon()>=1){ return records.get(round-2).makers;}
			int index = -1;
			for(int i=0;i<4;i++){
				if(rank[i] - rank[records.get(round-2).makers] == 1){
					index = i;
				}
			}
			if(index == -1){
				for(int i=0;i<4;i++){
					if(rank[i] - rank[records.get(round-2).makers] == -3){
						lap ++;
						if(lap%4 == 1){
							changeDirections();
						}
						return i;
					}
				}
			}else{
				return index;
			}
		}else{
			changeDirections();
			for(int i=0;i<4;i++){
				if(rank[i] == 1){
					return i;
				}
			}
		}
		return 0;
	}
	
	public void changeDirections(){
		String[] choices = {"東","南","西","北"};
		String[] temp = new String[4];
		for(int i=0;i<4;i++){
			temp[i] = null;
			while(temp[i]== null){
				temp[i] = (String)JOptionPane.showInputDialog(new Frame(),"請選擇"+p_name[i].getText().substring(0,p_name[i].getText().length()-3)+"的位置","",JOptionPane.PLAIN_MESSAGE,
						null,choices,choices[0]);
				for(int j=0;j<4;j++){
					if(i!=j){
						if(temp[i]==temp[j]){
							temp[i] = null;
						}
					}
				}
			}
			p_directions[i].setText(temp[i]);
			for(int j=0;j<4;j++){
				if(choices[j] == temp[i]){
					rank[i] = j+1;
				}
			}
			validate();
			repaint();
		}
	}
	
	public void changeMakers(){
		for(int i=0;i<4;i++){
			if(i == records.get(round-1).makers){
				p_makers[i].setText("(莊)");
			}else{
				p_makers[i].setText("");
			}
		}
	}
	
	public int isCon(){
		int temp = 0,index=round-1;
		while(index>=1){
			if(records.get(index-1).makers == records.get(index-1).winner) temp++;
			else break;
			index--;
		}
		return temp;
	}
	
	public boolean isFileExist(String filename){
		FileReader fr = null;
		BufferedReader fin = null;
		try{
			fr = new FileReader(filename);
			fin = new BufferedReader(fr);
		}catch(Exception e){;}
		if(fr == null || fin == null){
			return false;
		}else{
			try{
				fr.close();
				fin.close();
			}catch(Exception e){;}
			return true;
		}
	}
}

class ControlName extends JPanel implements ActionListener{
	JLabel jl;
	JButton edit_name;
	JTextField name;
	main me;
	public ControlName(JLabel jl,main me){
		this.jl = jl;
		this.me = me;
		edit_name = new JButton("編輯玩家名稱");
		name = new JTextField(10);
		edit_name.addActionListener(this);
		this.add(name);
		this.add(edit_name);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == edit_name){
			jl.setText(name.getText()+" : ");
			me.records.get(me.round-1).updateInf();
			me.validate();
		}
	}
}

class ControlAll extends JPanel implements ActionListener{ // ³]©wªºµøµ¡€j€p¡A7­Ó­èŠn
	main me;
	JLabel message;
	JButton[] buttons = new JButton[9];
	String[] text = {"修改名稱","鎖定名稱","上一把","下一把","湊莊","另存紀錄","儲存紀錄","重設這把","罰款"};
	public ControlAll(main me){
		this.me = me;
		message = new JLabel("第" + Integer.toString(me.round) + "把");
		add(message);
		for(int i = 0 ;i< 9;i++){
			buttons[i] = new JButton(text[i]);
			add(buttons[i]);
			buttons[i].addActionListener(this);
		}
		buttons[0].setEnabled(false);
		buttons[5].setEnabled(false);
		if(me.round == 1) buttons[2].setEnabled(false);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == buttons[0]){
			String temp = null;
			temp = JOptionPane.showInputDialog("請輸入驗證碼：");
			if(temp != null){
				if(temp.equals("999111333222")){
					me.p_editname[0].setVisible(true);
					me.p_editname[1].setVisible(true);
					me.p_editname[2].setVisible(true);
					me.p_editname[3].setVisible(true);
					buttons[0].setEnabled(false);
					buttons[1].setEnabled(true);
				}
			}
		}
		if(e.getSource() == buttons[1]){
			me.p_editname[0].setVisible(false);
			me.p_editname[1].setVisible(false);
			me.p_editname[2].setVisible(false);
			me.p_editname[3].setVisible(false);
			buttons[0].setEnabled(true);
			buttons[1].setEnabled(false);
		}
		if(e.getSource() == buttons[2]){
			if(me.round > 1 ){
				me.records.get(me.round-1).updateInf();
				me.round--;
				if(me.round == 1) buttons[2].setEnabled(false);
				message.setText("第" + Integer.toString(me.round) + "把");
				for(int i=0;i<4;i++){
					me.p_name[i].setText(me.records.get(me.round-1).p_name[i]+" : ");
					me.p_money[i].setText(Long.toString(me.records.get(me.round-1).p_money[i]));
					me.p_delta[i].setText("("+Long.toString(me.records.get(me.round-1).p_delta[i])+")");
					me.changeMakers();
					if(me.records.get(me.round-1).isModified){
						for(int k=0;k<=1;k++){
							me.p_game[i].buttons[k].setEnabled(false);
						}
					}
				}
			}
		}
		if(e.getSource() == buttons[3]){
			me.records.get(me.round-1).updateInf();
			me.round++;
			buttons[2].setEnabled(true);
			message.setText("²Ä" + Integer.toString(me.round) + "§â");
			if(me.records.size() >= me.round){
				for(int i=0;i<4;i++){
					me.p_name[i].setText(me.records.get(me.round-1).p_name[i]+" : ");
					me.p_money[i].setText(Long.toString(me.records.get(me.round-1).p_money[i]));
					me.p_delta[i].setText("("+Long.toString(me.records.get(me.round-1).p_delta[i])+")");
					me.changeMakers();
					if(me.records.get(me.round-1).isModified){
						for(int k=0;k<=1;k++){
							me.p_game[i].buttons[k].setEnabled(false);
						}
					}
				}
			}else{ //initialize a new record
				if(me.records.get(me.round-2).isModified){
					for(int i=0;i<4;i++){
						long temp_d=0,temp_m=0;
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
							temp_m = Long.parseLong(me.p_money[i].getText());
						}catch(Exception ex){;}
						me.p_money[i].setText(Long.toString(temp_d+temp_m));
						me.p_delta[i].setText("(0)");
						for(int k=0;k<=1;k++){
							me.p_game[i].buttons[k].setEnabled(true);
						}
					}
					me.makers = me.askMakers();
					me.records.add(new Record(me));
					me.changeMakers();
				}
				else{
					me.round--;
					message.setText("第" + Integer.toString(me.round) + "把");
					if(me.round == 1) buttons[2].setEnabled(false);
				}
			}
			me.records.get(me.round-1).updateInf();
			me.validate();
			me.repaint();
		}
		if(e.getSource() == buttons[4]){ //Žê²ø
			me.records.get(me.round-1).howwin = 3;
			me.records.get(me.round-1).winner = me.makers;
			me.records.get(me.round-1).updateInf();
			me.records.get(me.round-1).isModified = true;
			me.round++;
			message.setText("第" + Integer.toString(me.round) + "把");
			for(int i=0;i<4;i++){
				long temp_d=0,temp_m=0;
				try{
					temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
					temp_m = Long.parseLong(me.p_money[i].getText());
				}catch(Exception ex){;}
				me.p_money[i].setText(Long.toString(temp_d+temp_m));
				me.p_delta[i].setText("(0)");
				for(int k=0;k<=1;k++){
					me.p_game[i].buttons[k].setEnabled(true);
				}
			}
			me.makers = me.askMakers();
			me.records.add(new Record(me));
			me.changeMakers();
			me.records.get(me.round-1).updateInf();
			me.validate();
			me.repaint();
		}
		if(e.getSource() == buttons[5]){
			me.saveRecord(true);
		}
		if(e.getSource() == buttons[6]){
			me.saveRecord(false);
		}
		if(e.getSource() == buttons[7]){
			String temp = null;
			temp = JOptionPane.showInputDialog("請輸入驗證碼");
			if(temp != null){
				if(temp.equals("999111333222")){
					for(int i=0;i<4;i++){
						for(int k=0;k<=1;k++){
							me.p_game[i].buttons[k].setEnabled(true);
						}
						me.p_delta[i].setText("(0)");
						me.p_makers[i].setText("");
					}
					me.makers = me.askMakers();
					me.records.get(me.round-1).updateInf();
				}
			}
			me.validate();
		}
		if(e.getSource() == buttons[8]){
			String temp_1 = null;
			int choice1,choice2,choice3;
			String[] choices_1 = new String[4];
			for(int i =0;i<4;i++){
				choices_1[i] = me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3);
			}
			while(temp_1 == null){
				temp_1 = (String)JOptionPane.showInputDialog(new Frame(),"請選擇犯人：",
				           " ",JOptionPane.PLAIN_MESSAGE,null,choices_1,choices_1[0]);
			}
			String temp_2 = null;
			String[] choices_2 = {"詐胡","詐碰"};
			while(temp_2 == null){
				temp_2 = (String)JOptionPane.showInputDialog(new Frame(),"請選擇罪名"," ",
						JOptionPane.PLAIN_MESSAGE,null,choices_2,choices_2[0]);
			}
			if(temp_2 == choices_2[0]){ //¶B­J
				String temp_3 = null;
				long temp_d = 0,pay,sum = 0;
				int nums = 0,index = 0;
				for(int i=0;i<4;i++){
					if(temp_1 != me.p_name[i].getText()){
						temp_3 = null;
						nums = 0;
						temp_3 = JOptionPane.showInputDialog("罰給"+me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3)+"多少台？輸入台數（不輸入或亂輸入即為零）：");
						if(temp_3 != null){
							if(temp_3.length()<=10){
								try{
									nums = Integer.parseInt(temp_3);
								}catch(Exception ex){;}
								if(nums < 0 ) nums = 0;
							}
						}
						temp_d = 0;
						pay = nums*me.level + me.base;
						sum += pay;
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
						}catch(Exception ex){;}
						me.p_delta[i].setText("("+Long.toString(temp_d+pay)+")");
					}else{
						index = i;
					}
				}
				try{
					temp_d = Long.parseLong(me.p_delta[index].getText().substring(1,me.p_delta[index].getText().length()-1));
				}catch(Exception ex){;}
				me.p_delta[index].setText("("+Long.toString(temp_d-sum)+")");
				for(int i=0;i<4;i++){
					for(int k=0;k<=1;k++){
						me.p_game[i].buttons[k].setEnabled(false);
					}
				}
				me.records.get(me.round-1).isModified = true;
				me.records.get(me.round-1).howwin = 2;
				me.records.get(me.round-1).winner = index;
			}else{ //¶BžI
				String temp_3 = null;
				String[] choices_3 = new String[3];
				int index = 0;
				for(int i = 0;i<4;i++){
					if(choices_1[i] != temp_1){
						choices_3[index] = choices_1[i];
						index++;
					}
				}
				while(temp_3 == null){
					temp_3 = (String)JOptionPane.showInputDialog(new Frame(),"請選擇受害人",""
							,JOptionPane.PLAIN_MESSAGE,null,choices_3,choices_3[0]);
				}
				for(int i = 0;i<4;i++){
					long temp_d = 0;
					if(me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3) == temp_1){ //¥Ç€H
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
						}catch(Exception ex){;}
						me.p_delta[i].setText("("+Long.toString(temp_d-me.level)+")");
						}
					if(me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3) == temp_3){ //šü®`ªÌ
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
						}catch(Exception ex){;}
						me.p_delta[i].setText("("+Long.toString(temp_d+me.level)+")");
					}
				}
			}
		}
	}//actionPerformed
} //class

class ControlGame extends Panel implements ActionListener{
	main me;
	private final int no;
	String[] text = {"自摸","胡牌"};
	JButton[] buttons = new JButton[2];
	public ControlGame(main me,int no){
		this.me = me;
		this.no = no;
		for(int i=0;i<2;i++){
			buttons[i] = new JButton(text[i]);
			add(buttons[i]);
			buttons[i].addActionListener(this);
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == buttons[0]){
			String temp = null;
			int nums = 0;
			temp = JOptionPane.showInputDialog("輸入台數（不輸入或亂輸入即為零）:");
			if(temp!=null){
				if(temp.length()<=10){
					try{
						nums = Integer.parseInt(temp);
					}catch(Exception ex){;}
				}
				if(nums<0) nums = 0;
			}
			long temp_d = 0,pay;
			if(this.no == me.makers){
				pay = me.base + me.level*nums;
				for(int i=0;i<4;i++){
					if(i==no){
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
						}catch(Exception ex){;}
						me.p_delta[i].setText("("+Long.toString(temp_d+3*pay)+")");
					}else{
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
						}catch(Exception ex){;}
						me.p_delta[i].setText("("+Long.toString(temp_d-pay)+")");
					}
				}
			}else{
				long sum = 0;
				for(int i=0;i<4;i++){
					temp_d = 0;
					if(i!=no){
						try{
							temp_d = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
						}catch(Exception ex){;}
						if(i!=me.makers){
							pay = me.base + me.level*nums;
							sum += pay;
							me.p_delta[i].setText("("+Long.toString(temp_d-pay)+")");
						}else{
							pay = me.base + me.level*(nums+(me.isCon()*2+1));
							sum += pay;
							me.p_delta[i].setText("("+Long.toString(temp_d-pay)+")");
						}
					}
				}
				temp_d = 0;
				try{
					temp_d = Long.parseLong(me.p_delta[no].getText().substring(1,me.p_delta[no].getText().length()-1));
				}catch(Exception ex){;}
				me.p_delta[no].setText("("+Long.toString(temp_d+sum)+")");
			}
			for(int i=0;i<4;i++){
				for(int k=0;k<=1;k++){
					me.p_game[i].buttons[k].setEnabled(false);
				}
			}
			me.records.get(me.round-1).isModified = true;
			me.records.get(me.round-1).updateInf();
			me.records.get(me.round-1).winner = no;
			me.records.get(me.round-1).howwin = 0;
		}
		if(e.getSource() == buttons[1]){
			String temp = null;
			int nums = 0;
			temp = JOptionPane.showInputDialog("輸入台數（不輸入或亂輸入即為零）:");
			if(temp!=null){
				if(temp.length()<=10){
					try{
						nums = Integer.parseInt(temp);
					}catch(Exception ex){;}
					if(nums<0) nums=0;
				}
			}
			String[] choices = new String[3];
			int j = 0;
			for(int i=0;i<4;i++){
				if(i != no){
					choices[j] = me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3);
					j++;
				}
			}
			String s = (String)JOptionPane.showInputDialog(new Frame(),"請選擇誰放砲：",
					           " ",JOptionPane.PLAIN_MESSAGE,null,choices,choices[0]);
			while(s== null){
				s = (String)JOptionPane.showInputDialog(new Frame(),"請選擇誰放砲：",
				           " ",JOptionPane.PLAIN_MESSAGE,null,choices,choices[0]);
			}
			for(int i=0;i<4;i++){
				if(s.equals(me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3))) j=i;
			}
			long t_l=0,t_w=0,pay=0;
			try{
				t_l = Long.parseLong(me.p_delta[j].getText().substring(1,me.p_delta[j].getText().length()-1));
				t_w = Long.parseLong(me.p_delta[no].getText().substring(1,me.p_delta[no].getText().length()-1));
			}catch(Exception ex){;}
			pay = me.base + me.level*nums;
			me.p_delta[j].setText("("+Long.toString(t_l - pay)+")");
			me.p_delta[no].setText("("+Long.toString(t_w + pay)+")");
			for(int i=0;i<4;i++){
				for(int k=0;k<=1;k++){
					me.p_game[i].buttons[k].setEnabled(false);
				}
			}
			me.records.get(me.round-1).isModified = true;
			me.records.get(me.round-1).updateInf();
			me.records.get(me.round-1).winner = no;
			me.records.get(me.round-1).howwin = 1;
		}
		me.validate();
		me.repaint();
	}
}

class Record {
	String[] p_name = new String[4];
	long[] p_delta = new long[4];
	long[] p_money = new long[4];
	boolean isModified = false;
	main me;
	int makers,round,winner,howwin;
	public Record(main me){
		this.me = me;
		updateInf();
	}
	public String toString(){
		String temp = "第"+Integer.toString(round)+"把";
		for(int i=0;i<4;i++){
			temp += " ";
			temp += p_name[i];
			temp += " - ";
			temp += Long.toString(p_money[i]);
			temp += " (";
			temp += Long.toString(p_delta[i]);
			temp += ") ";
		}
		temp += " 莊家是" + p_name[makers];
		temp += " " + p_name[winner];
		if(howwin == 0){
			temp += "自摸\n";
		}else if(howwin == 1){
			temp += "­胡牌\n";
		}else if(howwin == 2){
			temp += "詐胡\n";
		}else{
			temp += "湊莊\n";
		}
		return temp;
	}
	public void updateInf(){
		for(int i=0;i<4;i++){
			p_name[i] = me.p_name[i].getText().substring(0,me.p_name[i].getText().length()-3);
			try{
				p_delta[i] = Long.parseLong(me.p_delta[i].getText().substring(1,me.p_delta[i].getText().length()-1));
				p_money[i] = Long.parseLong(me.p_money[i].getText());
			}catch(Exception e){;}
		}
		this.round = me.round;
		this.makers = me.makers;
	}
}
