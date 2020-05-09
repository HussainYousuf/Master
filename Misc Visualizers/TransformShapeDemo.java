import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class TransformShapeDemo extends JFrame {
    private JSlider modificationSlider;
    private ShapeTransformPanel panel = new ShapeTransformPanel();
	
    public TransformShapeDemo() {	
        // Setup our JFrame details
        setTitle("Transform Polygon Example");
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
		
        // Create a JSlider to rotate our shape with
        modificationSlider = new JSlider(0, 360, 0);
        modificationSlider.addChangeListener(new ChangeListener() {
			
                // For each tick of the thumb on the slider, get the value
                // rotate the image by that many degrees and ask it to repaint itself.
                public void stateChanged(ChangeEvent e) {
                        int modifyValue = ((JSlider)e.getSource()).getValue() % 360;
                        panel.modify(modifyValue);
                        panel.repaint();
                }
        });
		
        // Add our panel to the center and the slider to the bottom.
        add(panel, BorderLayout.CENTER);
        add(modificationSlider, BorderLayout.SOUTH);
    }

    // Create our demo and show it.
    public static void main(String[] args) {
        TransformShapeDemo demo = new TransformShapeDemo();
        demo.setVisible(true);
    }
}

//Specialized panel which will draw our shape
class ShapeTransformPanel extends JPanel {
 private int[] p1x = {150, 210, 280, 210, 120, 110, 100};
 private int[] p1y = {20, 60, 120, 200, 240, 200, 100};
 private double modifyValue = 0;

 public ShapeTransformPanel() { }
	
	
 // Sets our modification value
 public void modify(int value) {
     modifyValue = value;
 }
	
 @Override
 protected void paintComponent(Graphics g) {
     super.paintComponent(g);
		
     drawTransform(g, modifyValue);
 }
	
 // Create our polygon shape with the points, transform it and then draw it to the panel.
 private void drawTransform(Graphics g, double modifier) {
     Polygon examplePolygon = new Polygon(p1x, p1y, p1x.length);
		
     Rectangle rect = examplePolygon.getBounds();
		
     // Create our transform and modify it based on the line we use.
     // Uncomment the lines below to see their effect
     AffineTransform at = new AffineTransform();
		
     at.rotate(Math.toRadians(modifier), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
     //at.scale(modifier / 100.0, modifier/ 100.0);
     //at.translate(modifier, modifier);
		
     // Transform the shape and draw it to screen
     Graphics2D g2d = (Graphics2D) g;
     g2d.draw(at.createTransformedShape(examplePolygon));
 }
}
