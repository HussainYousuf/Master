import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Demo1 implements Runnable{
	
	JFrame frame;
	
	BufferedImage image;
	JPanel panel;
	int width = 750;
	int height = 750;
	int radius = 1;
	Graphics2D g;
	BasicStroke bold = new BasicStroke(2);
	BasicStroke normal = new BasicStroke(1);
	BasicStroke functionStroke = new BasicStroke(1.1f);


	double realTime_x= -5;
	int parts = 10;
	int x_count = 10;
	double y_count;
	int mid = x_count/2;

	double max_y;
	double min_y;

	boolean completed = true;
	
	
	Demo1(){
		JButton btn1 = new JButton("-f(x)");
		image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		frame = new JFrame();
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);
		//panel.add(btn1,700,50);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		calculateMax();
		drawGrid();
		drawFunction();
		
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ADD){
					if(x_count == 10) return;
					x_count-=10;
					calculateMax();
					drawGrid();
					drawFunction();
				}
				if(e.getKeyCode() == e.VK_SUBTRACT){
					x_count+=10;
					calculateMax();
					drawGrid();
					drawFunction();
				}
			}
		});
		frame.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				//System.out.println(e.getX() + " ; " + e.getY());
				
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		frame.setLocationRelativeTo(null);

	}
	

	public static void main(String[] args) {
		Demo1 demo = new Demo1();
//		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
//		exec.scheduleAtFixedRate(demo,0,2,TimeUnit.SECONDS);
		//new Thread(demo).start();
	}

	double function(double x){
		x = Math.pow(x, 5);
		//x = Math.sin(Math.toRadians(x*90));
		return x;
	}
	
	
	void calculateMax(){
		completed = false;
		mid = x_count/2;
		max_y = Integer.MIN_VALUE;
		min_y = Integer.MAX_VALUE;
		
		for(double x=-mid;x<=mid;x+=.0001){
			double y = function(x);
			if(y < min_y) min_y = y;
			if(y > max_y) max_y = y;
			//drawPoint(x, function(x));
		}
		
		if(Math.abs(min_y) > Math.abs(max_y)) max_y = -min_y;
		else min_y = -max_y;
		
		y_count = (max_y - min_y);
		y_count = Math.abs(y_count);
		
		
	}
	
	public void drawFunction(){
		for(double x=-mid;x<=mid;x+=.0001){
			double y = function(x);
			drawPoint(x, function(x));
		}
		completed = true;
		panel.getGraphics().drawImage(image, 0, 0, null);
	}
	
	
	public void run() {
//		while(true){
//			System.out.println("running");
//			while(!completed){
//				System.out.println("blocked");
//
//			}
//			panel.getGraphics().drawImage(image, 0, 0, null);
//		}

	}
	
	double X(double x){
		x += mid;  
		x *= width;
		x /= x_count;
		return x;
	}
	
	double Y(double y){
		y += Math.abs(min_y);
		y *= height;
		y /= y_count;
		return height - y;
	}
	
	void drawPoint(double x, double y){
		Line2D line = new Line2D.Double();
		g.setStroke(functionStroke);
		line.setLine(X(x),Y(y),X(x),Y(y));
		g.draw(line);
		g.setStroke(normal);
	}
	
	void drawGrid(){
		Line2D line = new Line2D.Double();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Sans",Font.BOLD,15));
		g.setColor(Color.black);
		
		for(int i=0;i<=parts;i++){
			if(i == parts/2) g.setStroke(bold);
			else g.setStroke(normal);
			line.setLine(i*width/parts, 0, i*width/parts, height);
			g.draw(line);
			int x = i*x_count/parts - mid;
			g.drawString(" "+Integer.toString(x), i*width/parts, height/2+20);
			String y = String.format("%.1f", (max_y-i*y_count/parts));
			g.drawString(y, width/2-30, i*height/parts);
			line.setLine(0, i*height/parts, width, i*height/parts);
			g.draw(line);
			g.setStroke(new BasicStroke(8));
			line.setLine(X(x),Y(function(x)),X(x),Y(function(x)));
			g.draw(line);
			g.setStroke(normal);
		}
	}

}
