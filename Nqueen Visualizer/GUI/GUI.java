/*
 * 
 * Hussain
 * k16-3805
 * sec: D
 * for extra weightage
 * 
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class GUI extends JPanel implements Runnable,MouseListener,MouseMotionListener{

	int size = 768;
	int offset = 5;
	Block blocks[][];
	Block prev;
	Block block;
	static boolean isValid;
	static Color my_color = new Color(215,215,215,215);
	
	GUI(int n){
		
		JFrame frame = new JFrame();
		frame.setTitle("NQueens");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(this);
		setPreferredSize(new Dimension(size+offset,size+offset));
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		Block.size = size / n;
		Block.offset = offset;
		blocks = new Block[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				blocks[i][j] = new Block(i, j);
			}
		}
		
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI(8);
		SwingUtilities.invokeLater(gui);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.black);
		g.fillRect(0, 0, size+5, size+5);
		g.setStroke(new BasicStroke(2));
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				Block block = blocks[i][j];
				g.setColor(block.color);
				g.fill(block);
			}
		}
		g.setColor(Color.black);

	}

	@Override
	public void run() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(isValid && block != null) block.setColor(Color.GREEN);
		else if(!isValid && block != null) block.setColor(Color.red);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				blocks[i][j].setColor(Color.white);
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(prev != null && prev.contains(e.getPoint())) return;
		int row,col;
		row = col = 0;
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				blocks[i][j].setColor(Color.white);
				if(blocks[i][j].contains(e.getPoint())){
					block = blocks[i][j];
					prev = block;
					row = i;
					col = j;
				}
			}
		}
		if(block == null) return;
		isValid = true;
		for (int p = 0; p < blocks.length; p++) {
			blocks[row][p].setColor(my_color);
			blocks[p][col].setColor(my_color);
			int n = row + p;
			int k = row - p;
			int l = col + p;
			int m = col - p;
			if(n < blocks.length && l < blocks.length) blocks[n][l].setColor(my_color);
			if(k > -1 && l < blocks.length) blocks[k][l].setColor(my_color);
			if(n < blocks.length && m > -1) blocks[n][m].setColor(my_color);
			if(k > -1 && m > -1) blocks[k][m].setColor(my_color);
		}
		repaint();
	}



}
