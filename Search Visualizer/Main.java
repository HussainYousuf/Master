import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JPanel implements Runnable{

	Main This;
	

	
	Node startNode, finalNode; 
	Node tempNode;
	
	boolean nodeTrigger;
	boolean startTrigger;
	boolean finalTrigger;
	boolean edgeTrigger;
	
	Set<JComponent> allComponents = new HashSet<JComponent>();
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	private int width = 1024;
	private int height = 768;
	
	private RenderingHints rh;

	public static Graphics2D g2d;

	private void reset() {
		for (Node node : nodes) {
			node.isVisited = false;
			node.isStartNode = false;
			node.isFinalNode = false;
		}
		for (Edge edge : edges) {
			edge.colour = Color.BLACK;
			edge.isVisited =false;
		}
		startNode = null;
		finalNode = null;
		repaint();
	}

	private void clear() {
		Node.count = 0;
		nodes.clear();
		edges.clear();
		startNode = null;
		finalNode = null;
		repaint();
	}

	public void run(){}

	Main() {
		This = this;
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		JFrame frame = new JFrame("Demo");
		JPanel panel = new JPanel();

		JButton search = new JButton("search");
		JButton reset = new JButton("reset");
		JButton markStart = new JButton("mark start");
		JButton markFinal = new JButton("mark final");
		JButton addNodes = new JButton("add nodes");
		JButton addEdges = new JButton("add edges");
		JButton clear = new JButton("clear");
		JButton saveMap = new JButton("save map");
		JButton loadMap = new JButton("load map");

		allComponents.add(search);
		allComponents.add(reset);
		allComponents.add(markStart);
		allComponents.add(markFinal);
		allComponents.add(addNodes);
		allComponents.add(addEdges);
		allComponents.add(clear);
		allComponents.add(saveMap);
		allComponents.add(loadMap);

		String res[] = { "DFS", "BFS", "Dijkstra"};
		JComboBox<String> box = new JComboBox<String>(res);

		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		menuPanel.add(box);
		menuPanel.add(search);
		menuPanel.add(addNodes);
		menuPanel.add(addEdges);
		menuPanel.add(markStart);
		menuPanel.add(markFinal);
		menuPanel.add(reset);
		menuPanel.add(clear);
		menuPanel.add(saveMap);
		menuPanel.add(loadMap);

		
		panel.setLayout(new BorderLayout());
		panel.add(this, BorderLayout.CENTER);
		panel.add(menuPanel, BorderLayout.NORTH);

		// listeners

		addNodes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nodeTrigger = !nodeTrigger;
				JButton thisBtn = (JButton) e.getSource();
				if (nodeTrigger) {
					addNodes.setText("done");
					disableAllBtns(thisBtn);
				} else {
					addNodes.setText("add nodes");
					enableAllBtns(thisBtn);
				}
			}
		});
		
		
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}

		});

		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (box.getSelectedItem().toString().equals(res[0])) {
					new DFS(This).execute();
				}else if(box.getSelectedItem().toString().equals(res[1])) {
					new BFS(This).execute();
				}else if(box.getSelectedItem().toString().equals(res[2])) {
					Dijkstra graph = new Dijkstra(nodes.size(),startNode.label,finalNode.label,This);
					for(Node node : nodes) graph.addVertex(node.label);
					for(Edge edge : edges) {
						graph.addEdge(edge.node1.label, edge.node2.label,edge.distance);
					}
					graph.execute();
				}

			}
		});

		markFinal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				finalTrigger = !finalTrigger;
				if (finalTrigger) {
					markFinal.setText("done");
					disableAllBtns((JButton) e.getSource());
				} else {
					markFinal.setText("mark final");
					enableAllBtns((JButton) e.getSource());
				}
			}
		});

		markStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startTrigger = !startTrigger;
				if (startTrigger) {
					markStart.setText("done");
					disableAllBtns((JButton) e.getSource());
				} else {
					markStart.setText("mark start");
					enableAllBtns((JButton) e.getSource());
				}
			}
		});

		addEdges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				edgeTrigger = !edgeTrigger;
				if (edgeTrigger) {
					addEdges.setText("done");
					disableAllBtns((JButton) e.getSource());
				} else {
					if (tempNode != null) {
						tempNode.isSelected = false;
						tempNode = null;
					}
					addEdges.setText("add egdes");
					enableAllBtns((JButton) e.getSource());
					repaint();
				}
			}
		});

		

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (nodeTrigger) {
					
					Node node = new Node(e.getX(), e.getY());
					if (!nodes.contains(node)) {
						nodes.add(node);
					}
						
					repaint();
					
				} else if (edgeTrigger) {
					for (Node node : nodes) {
						if (node.isIntersect(e.getX(), e.getY())) {
							if (tempNode == null) {
								tempNode = node;
								tempNode.isSelected = true;
								repaint();
							} else if (tempNode != node) {
								edges.add(new Edge(tempNode,node));
								tempNode.isSelected = false;
								tempNode = null;
								repaint();
							}
						}
					}
				} else if (startTrigger) {
					for (Node node : nodes) {
						if (node.isIntersect(e.getX(), e.getY())) {
							node.isStartNode = true;
							startNode = node;
							for (Node node1 : nodes) {
								if (node1 != node && node1.isStartNode)
									node1.isStartNode = false;
							}
							repaint();
						}
					}
				} else if (finalTrigger) {
					for (Node node : nodes) {
						if (node.isIntersect(e.getX(), e.getY())) {
							node.isFinalNode = true;
							finalNode = node;
							for (Node node1 : nodes) {
								if (node1 != node && node1.isFinalNode)
									node1.isFinalNode = false;
							}
							repaint();
						}
					}
				}

			}
		});

		saveMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String fileName = JOptionPane.showInputDialog(null, "Enter file name");
				if (fileName == null)
					return;
				else if (fileName.length() < 1)
					return;
				try {
					reset();
					PrintWriter writer = new PrintWriter(new File(fileName));
					for (Edge edge : edges) {
						String input = edge.node1.x + "," + edge.node1.y + "," + edge.node2.x + "," + edge.node2.y;
						writer.println(input);
					}
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		loadMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						clear();
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String line = "";
						while ((line = reader.readLine()) != null) {
							String inputs[] = line.split(",");
							float x1 = Float.parseFloat(inputs[0]);
							float y1 = Float.parseFloat(inputs[1]);
							float x2 = Float.parseFloat(inputs[2]);
							float y2 = Float.parseFloat(inputs[3]);
							boolean n1 = true;
							boolean n2 = true;
							Node node1 = null;
							Node node2 = null;
							for(Node node : nodes) {
								if(node.x == x1 && node.y == y1){
									node1 = node;
									n1 = false;
								} 
								if(node.x == x2 && node.y == y2){
									node2 = node;
									n2 = false;
								} 
							}
							if(n1){
								node1 = new Node(x1,y1);
								nodes.add(node1);
							} 
							if(n2){
								node2 = new Node(x2,y2);
								nodes.add(node2);
							} 
							
							
							edges.add(new Edge(node1,node2));
							
						}

						reader.close();
						repaint();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}
		});

		// listeners
		frame.setContentPane(panel);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
	

	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Main());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g;
		g2d.setRenderingHints(rh);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Serif",Font.BOLD,16));
		g2d.setStroke(new BasicStroke(2));
		for (Node node : nodes) {
			node.draw(g2d);
		}
		for (Edge edge : edges) {
			edge.draw(g2d);
		}
		
	}

	public void disableAllBtns(JComponent e) {
		for (JComponent btn : allComponents) {
			if (btn != e)
				btn.setEnabled(false);
		}
	}

	public void enableAllBtns(JComponent e) {
		for (JComponent btn : allComponents) {
			btn.setEnabled(true);
		}
	}

}
