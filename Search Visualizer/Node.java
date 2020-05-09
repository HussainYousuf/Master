import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Set;

public class Node{
	
	boolean isVisited;
	boolean isStartNode;
	boolean isFinalNode;
	boolean isSelected;

	static int count = 0;
	int label = 0;
	final static int size = 30;
	final static float radius = size / 2;
	float x;
	float y;
	private Ellipse2D ellipse2d;
	
	
	Node(float x,float y){
		label = count++;
		this.x = x;
		this.y = y;
		ellipse2d = new Ellipse2D.Float(x-radius,y-radius,size,size);
	}
	
	
	boolean isIntersect(float x,float y) {
		if(ellipse2d.contains(x, y)) return true;
		else return false;	
	}
	
	void draw(Graphics2D g){
		Color color = g.getColor();
		g.setColor(Color.black);
		if(isStartNode) g.setColor(Color.green);
		if(isFinalNode) g.setColor(Color.red);
		if(isSelected) g.setColor(Color.blue);
		g.draw(ellipse2d);
		g.drawString(Integer.toString(label),x-10,y+4);
		g.setColor(color);
	}

}
