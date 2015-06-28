import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

import javax.swing.*;
public class MontyHall extends MyApplet implements ActionListener{
	private static final long serialVersionUID = 1L;
	JPanel p_top,p_bottom,p_center;			//top:doors,center:checkboxgroup,bottom:start,list
	JButton start,send;							//start a new game.
	CheckboxGroup choice;					//represent which door the user chooses.
	Choice num_doors;							//save the number of doors; (3-7)
	JLabel[] l_door = new JLabel[7];
	JLabel text,door_right,door_wrong;
	int num_door,ans,select,turns;
	public static void main(String argv[]){
		JFrame f = new JFrame("MontyHall Game by 9722031 羅瑋騰");						//image of door.
		MontyHall mh = new MontyHall();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500,250);
		f.setVisible(true);	
		f.add(mh);
		f.validate();
		mh.init();	mh.start();
	}
	public MontyHall(){
		turns = 0;num_door = 3;
		ans = (int)(Math.random()*num_door)+1;
		for(int i=0;i<=6;i++){
			l_door[i] = createLabelWithPicture("door.jpg","");
		}
		door_wrong = createLabelWithPicture("door_wrong.jpg","");
		door_right = createLabelWithPicture("door_right.jpg","");
	}
	public void init(){
		p_top = new JPanel(); p_center = new JPanel(); p_bottom = new JPanel();
		num_doors = new Choice();
		start = new JButton("Start a new game");
		send = new JButton("Send and check");
		text = new JLabel("Doors : ");
		for(int i=3;i<=7;i++){
			num_doors.add(Integer.toString(i));
		}
		for(int i=0;i<=2;i++){
			p_top.add(l_door[i]);
		}
		choice = new CheckboxGroup();
		for(int i=0;i<num_door;i++){
			p_center.add(new Checkbox(Integer.toString(i+1),choice,false));
		}
		p_bottom.add(text);p_bottom.add(num_doors);
		p_bottom.add(start);p_bottom.add(send);
		setLayout(new BorderLayout());
		add(p_top,"North");add(p_center,"Center");add(p_bottom,"South");
		start.addActionListener(this);
		send.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==start){
			num_door = Integer.parseInt(num_doors.getSelectedItem());
			p_top.removeAll();
			p_center.removeAll();
			choice = new CheckboxGroup();
			ans = (int)(Math.random()*num_door)+1;
			turns = 0;
			for(int i=0;i<num_door;i++){
				p_top.add(l_door[i]);
			}
			for(int i=0;i<num_door;i++){
				p_center.add(new Checkbox(Integer.toString(i+1),choice,false));
			}
			p_top.updateUI();
			p_center.updateUI();
			send.setEnabled(true);
			validate();
		}
		if(e.getSource()==send){
			if(choice.getSelectedCheckbox()!=null){
				if(turns == 0){
					p_top.removeAll();
					for(int i=0;i<=1;i++){
						p_top.add(l_door[i]);
					}
					select = Integer.parseInt(choice.getSelectedCheckbox().getLabel());
					p_center.removeAll();
					choice = new CheckboxGroup();
					//This part is to ensure the selected choice will be choosed after player checks first time..
					//and the rank of doors won't changed.
					if(select > ans){
						p_center.add(new Checkbox(Integer.toString(ans),choice,false));
						p_center.add(new Checkbox(Integer.toString(select),choice,true));
					}
					else if(select < ans){
						p_center.add(new Checkbox(Integer.toString(select),choice,true));
						p_center.add(new Checkbox(Integer.toString(ans),choice,false));
					}
					else{
						while(select == ans){
							select = (int)(Math.random()*num_door)+1;
						}
						if(select > ans){
							p_center.add(new Checkbox(Integer.toString(ans),choice,true));
							p_center.add(new Checkbox(Integer.toString(select),choice,false));
						}else{
							p_center.add(new Checkbox(Integer.toString(select),choice,false));
							p_center.add(new Checkbox(Integer.toString(ans),choice,true));
						}
					}
					p_top.updateUI();
					p_center.updateUI();
					validate();
					turns++;
				}else if(turns == 1){
					send.setEnabled(false);
					p_top.removeAll();
					select = Integer.parseInt(choice.getSelectedCheckbox().getLabel());
					p_center.removeAll();
					if(select == ans){
						p_top.add(door_right);
					}else{
						p_top.add(door_wrong);
					}
					p_top.updateUI();
					p_center.updateUI();
					validate();
				}else{;}
			}
		}
	}
	public void start(){
		super.start();
	}
}
