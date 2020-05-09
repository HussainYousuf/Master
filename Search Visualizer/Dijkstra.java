import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

class DistPar2 {
	public int distance;
	public int parentVert;

	public DistPar2(int pv, int d) {
		distance = d;
		parentVert = pv;
	}
}

class Vertex {
	public int label;
	public boolean isInTree;
	public boolean wasVisited;

	public Vertex(int lab) {
		label = lab;
		isInTree = false;
	}

}

public class Dijkstra extends SwingWorker<Void,Integer>{
	private final int INFINITY = 1000000;
	Main main;
	Vertex vertexList[];
	int adjMat[][];
	private int nVerts;
	private int nTree;
	private DistPar2 sPath[];
	private int currentVert;
	private int startToCurrent;

	private int startVertex;
	private int finalVertex;

	public Dijkstra(int MAX_VERTS, int startVertex, int finalVertex,Main main) {
		this.main = main;
		this.startVertex = startVertex;
		this.finalVertex = finalVertex;
		vertexList = new Vertex[MAX_VERTS];
		adjMat = new int[MAX_VERTS][MAX_VERTS];
		nVerts = 0;
		nTree = 0;
		for (int j = 0; j < MAX_VERTS; j++)
			for (int k = 0; k < MAX_VERTS; k++)
				adjMat[j][k] = INFINITY;
		sPath = new DistPar2[MAX_VERTS];
	}

	public void addVertex(int lab) {
		vertexList[nVerts++] = new Vertex(lab);
	}

	public void addEdge(int start, int end, int weight) {
		adjMat[start][end] = weight;
		adjMat[end][start] = weight;
	}

	public void path() {
		int startTree = startVertex;
		vertexList[startTree].isInTree = true;
		nTree = 1;

		for (int j = 0; j < nVerts; j++) {
			int tempDist = adjMat[startTree][j];
			sPath[j] = new DistPar2(startTree, tempDist);
		}

		while (nTree < nVerts) {
			int indexMin = getMin();
			int minDist = sPath[indexMin].distance;

			if (minDist == INFINITY) {
				System.out.println("There are unreachable vertices");
				break;
			} else {
				currentVert = indexMin;
				startToCurrent = sPath[indexMin].distance;

			}

			vertexList[currentVert].isInTree = true;
			nTree++;
			adjust_sPath();
		}

	}

	public int getMin() {
		int minDist = INFINITY;
		int indexMin = 0;
		for (int j = 0; j < nVerts; j++) {
			if (!vertexList[j].isInTree && sPath[j].distance < minDist) {
				minDist = sPath[j].distance;
				indexMin = j;
			}
		}
		return indexMin;
	}

	public void adjust_sPath() {

		int column = 0;
		while (column < nVerts) {

			if (vertexList[column].isInTree) {
				column++;
				continue;
			}

			int currentToFringe = adjMat[currentVert][column];

			int startToFringe = startToCurrent + currentToFringe;

			int sPathDist = sPath[column].distance;

			if (startToFringe < sPathDist) {
				sPath[column].parentVert = currentVert;
				sPath[column].distance = startToFringe;
			}
			column++;
		}
	}


	public Node findNode(int label) {
		for(Node node : main.nodes) {
			if(label == node.label) return node;
		}
		return null;
	}
	

	@Override
	protected Void doInBackground() throws Exception {
		
		path();
		
		
		for (int j = 0; j < nVerts; j++) {
			
			
			if (vertexList[j].label == finalVertex) {
				
				int parent = vertexList[sPath[j].parentVert].label;
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(finalVertex);
				list.add(parent);

				while (parent != startVertex) {

					list.add(parent);
					parent = vertexList[sPath[parent].parentVert].label;
					list.add(parent);
	
				}
				
				
				
				
				for (int i = list.size() -1 ; i >= 0 ; i-=2) {
					for(Edge edge : main.edges) {
						Node node1 = findNode(list.get(i));
						Node node2 = findNode(list.get(i-1));
						if(edge.contains(node1,node2)) {
							edge.colour = Color.red;
							publish(1);
							Thread.sleep(1000);
						}
					}
				}
				
				
				
				
			}
			
			

			System.out.print(vertexList[j].label + "=");
			if (sPath[j].distance == INFINITY)
				System.out.print("inf");
			else
				System.out.print(sPath[j].distance);
			int parent = vertexList[sPath[j].parentVert].label;
			System.out.print("(" + parent + ") ");
		}
		System.out.println("");
		
		
		return null;
	}

	@Override
	protected void process(List<Integer> chunks) {
		
		main.repaint();
		
	}

}
