import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Trig extends JPanel implements Runnable{

	JFrame frame;
	Graphics2D g;
	shape rect;
	
	Trig(){
		rect = new shape();
		frame = new JFrame();
		frame.setContentPane(this);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(3);
		exec.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);
	}
	
	public static void main(String[] args) {
		Trig obj = new Trig();
	}
	
	@Override
	public void run() {
		g = (Graphics2D) getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,800,600);
		g.setColor(Color.BLACK);
		g.draw(rect);
	    Path2D.Double path = new Path2D.Double();
	    path.append(rect, true);
	    AffineTransform t = new AffineTransform();
	    t.rotate(Math.toRadians(-45),rect.getCenterX(),rect.getCenterY());
	    t.translate(100, 0);
	    path.transform(t);
	    g.draw(path);
		
		
	}

}

class shape extends Rectangle{
	
	shape(){
		super(400,300,50,50);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
