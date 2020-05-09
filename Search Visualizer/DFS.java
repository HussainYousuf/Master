import java.awt.Color;
import java.util.List;
import java.util.Stack;

import javax.swing.SwingWorker;

public class DFS extends SwingWorker<Void,Integer>{
	
	Main main;
	Node startNode,finalNode;
	
	
	DFS(Main main){
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

		Stack<Node> stack = new Stack<Node>();
		startNode.isVisited = true;
		stack.push(startNode);
		while(!stack.isEmpty()) {
			Node prevNode = stack.peek();
			Edge edge = getEdge(prevNode);
			Node adjacent = null;
			if(edge != null) adjacent = edge.getOtherOne(prevNode);
			if(adjacent == null) stack.pop(); 
			else {
				adjacent.isVisited = true;
				stack.push(adjacent);
				edge.colour = Color.RED;
				publish(1);
				if(adjacent.isFinalNode) {
					break;
				}
				Thread.sleep(1000);
				
			}	
		}
		
		return null;
	}

	private Edge getEdge(Node prevNode) {
		for(Edge edge : main.edges) {
			for(Node node : main.nodes) {
				if(node.isVisited) continue;
				if(edge.contains(prevNode, node)) return edge;
			}
		}
		return null;
	}

	

	
	
}


