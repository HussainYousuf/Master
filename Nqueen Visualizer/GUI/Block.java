import java.awt.Color;
import java.awt.Rectangle;


public class Block extends Rectangle{

	static int size;
	Color color;
	static int offset;

	
	Block(int row,int col){
		color = Color.white;
		setBounds(col*size+offset, row*size+offset, size-offset, size-offset);
	}


	public void setColor(Color color) {
		if(this.color == Color.green && color == GUI.my_color) GUI.isValid = false;
		else if(this.color == Color.green && color == Color.white) ;
		else this.color = color;
	}
	
}
