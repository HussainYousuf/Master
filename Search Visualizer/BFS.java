import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javax.swing.SwingWorker;

public class BFS extends SwingWorker<Void,Integer>{
	
	Main main;
	Node startNode,finalNode;
	
	
	BFS(Main main){
		this.main = main;	
		this.startNode = main.startNode;
		this.finalNode = main.startNode;
	}

	@Override
	protected void process(List<Integer> arg0) {
		main.repaint();
	}

	
	@Override
	protected Void doInBackground() throws Exception {

		Queue<Node> queue = new LinkedList<Node>();
		boolean exit = false;
		queue.add(startNode);
		while(!queue.isEmpty()) {
			Node prevNode = queue.poll();
			Edge edge;
			while((edge = getEdge(prevNode)) != null) {
				Node adjacent = edge.getOtherOne(prevNode);
				edge.isVisited = true;
				queue.add(adjacent);
				edge.colour = Color.RED;
				publish(1);
				if(adjacent.isFinalNode) {
					for(Edge edge2 : main.edges) edge2.isVisited =false;
					exit = true;
					break;
				}
				Thread.sleep(1000);			
			}
			if(exit) break;
		}
		
		return null;
	}

	private Edge getEdge(Node prevNode) {
		for(Edge edge : main.edges) {
			if(edge.isVisited) continue;
			for(Node node : main.nodes) {
				if(edge.contains(prevNode, node)) return edge;
			}
		}
		return null;
	}

	




	
	
}


