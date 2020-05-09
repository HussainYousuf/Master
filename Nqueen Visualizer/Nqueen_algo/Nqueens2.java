/*
 * 
 * Hussain
 * k16-3805
 * sec: D
 * use attached GUI to verify results
 * 
 */


public class Nqueens2 {

	int size;
	int visited[][];

	public Nqueens2(int size) {
		this.size = size;
		this.visited = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				visited[i][j] = -1;
			}
		}
	}

	public static void main(String[] args) {
		new Nqueens2(8).search(); // <------- change this (8 to 16 or 32 or whatever) to take it up a notch, but be patient
	}

	void setVisited(int i, int j, int k) {
		if (visited[i][j] == -1)
			visited[i][j] = k;
	}

	void unSetVisited(int i, int j, int k) {
		if (visited[i][j] == k)
			visited[i][j] = -1;
	}
	
	void print() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.printf("%3d\t", visited[i][j]);
			}
			System.out.println();
		}
		System.out.println("----------------------");
	}

	/*
	 * 
	 * The method iterates in order i.e from 0-size, row-wise and col-wise
	 * where it does not find a conflict it places the number of 'col' in position.
	 * it also places the same col number in the corresponding row and col and diagonals
	 * now when another col overlaps i.e matches with the previously occupied block,
	 * a conflict is generated.
	 * since each col places its col number in all effectees its easier to remove all effectees 
	 * if that col is to be removed
	 * loops until all conflicts are removed
	 * backtrack if all pos are occupied or tried in a col.
	 * 
	 */
	
	
	void search() {
		int q = 0;
		int queens[] = new int[size];
		for (int j = 0; j < size;) {
			for (int i = 0; i < size; i++) {
				if (visited[i][j] == -1) {
					for (int k = 0; k < size; k++) {
						setVisited(k, j, j);
						setVisited(i, k, j);
						int l = i + k;
						int m = i - k;
						if (l < size && j + k < size)
							setVisited(l, j + k, j);
						if (m > -1 && j + k < size)
							setVisited(m, j + k, j);
					}
					queens[j] = i;
					// print();
					j++;
					break;
				} else {
					if (i == size - 1) {
						for (int k = 0; k < size; k++) {
							if (visited[k][j] == size)
								visited[k][j] = -1;
						}
						j--;
						i = queens[j];
						for (int k = 0; k < size; k++) {
							unSetVisited(k, j, j);
							unSetVisited(i, k, j);
							int l = i + k;
							int m = i - k;
							if (l < size && j + k < size)
								unSetVisited(l, j + k, j);
							if (m > -1 && j + k < size)
								unSetVisited(m, j + k, j);
						}
						visited[i][j] = size;
						// print();
						q++;
					}
				}
			}
		}
		System.out.println("iterations : " +q);
		System.out.println("row\tcol");
		for (int i = 0; i < queens.length; i++) {
			System.out.println(queens[i] + "\t" + i);
		}
		System.out.println("-------------");
	}
	
}
