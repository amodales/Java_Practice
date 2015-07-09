import java.io.*;
import java.util.*;

class Card{
	
	private char c_face;
	private int c_num;

	public boolean isOkay;

	public Card(int face, int num){
		char temp;
		switch(face){
			case 1:
				// Clubs
				temp = 'C';
				break;
			case 2:
				// Diamonds
				temp = 'D';
				break;
			case 3:
				// Hearts
				temp = 'H';
				break;
			default:
				// case 4:
				// Spades
				temp = 'S';
				break;
		}
		c_face = temp;
		c_num = num;
	}

	public char get_Face(){
		return c_face;
	}

	public char get_Num(){
		switch(c_num){
			case 1:
				return 'A';
			case 2: case 3: case 4: case 5:
			case 6: case 7: case 8: case 9:
				return (char)(c_num + (int)'0');
			case 10:
				return 'T';
			case 11:
				return 'J';
			case 12:
				return 'Q';
			default:
				//case 13:
				return 'K';
		}
	}
}
