import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Edge {

	Node node1;
	Node node2;
	boolean isVisited;
	Line2D line;
	Color colour = Color.BLACK;
	int distance;
	private int midx;
	private int midy;
	
	Edge(Node node1,Node node2){
		this.node1 = node1;
		this.node2 = node2;
		line = new Line2D.Float(node1.x,node1.y,node2.x,node2.y);
		int x1 = (int) node1.x;
		int x2 = (int) node2.x;
		int y1 = (int) node1.y;
		int y2 = (int) node2.y;
		distance = (int) Math.hypot(x1-x2,y1-y2);
		distance /= 10;
		midx = (x1 + x2)/2;
		midy = (y1 + y2)/2;

	}
	
	boolean contains(Node node1,Node node2) {
		if((node1 == this.node1 || node1 == this.node2) && (node2 == this.node1 || node2 == this.node2)) return true;
		else return false;
	}
	
	void draw(Graphics2D g){
		Color c = g.getColor();
		g.drawString(Integer.toString(distance),midx+5,midy+5);
		g.setColor(new Color(colour.getRed(),colour.getGreen(),colour.getBlue(),125));
		g.setStroke(new BasicStroke(4));
		g.draw(line);
		g.setColor(c);
	}
	
	Node getOtherOne(Node node) {
		if(node == node1) return node2;
		else  return node1;
	}
	
}
