


import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

	int size;
	int domain[][];
	int pairs[];
	int conflicts;

	// each queen uses the same domain i.e 8x8 blocks
	public Chromosome(int size) {
		this.size = size;
		this.domain = new int[size][size];
		this.pairs = new int[size];
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				domain[i][j] = -1;
			}
		}
		// places queens on random pos initially
		for (int i = 0; i < size; i++) {
			int row = rand.nextInt(size);
			pairs[i] = row;
			placeQueen(row, i);
		}
	}

	// as name implies
	void chkConflict() {
		conflicts = 0;
		this.domain = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				domain[i][j] = -1;
			}
		}
		for (int i = 0; i < size; i++) {
			placeQueen(pairs[i], i);
		}
	}

	void setDomain(int i, int j, int k) {
		if (domain[i][j] == -1)
			domain[i][j] = k;
	}

	void placeQueen(int row, int col) {
		if (domain[row][col] != -1) {
			conflicts++;
		}
		for (int i = 0; i < size; i++) {
			setDomain(row, i, col);
			setDomain(i, col, col);
			int j = row + i;
			int k = row - i;
			int l = col + i;
			int m = col - i;
			if (j < size && l < size)
				setDomain(j, l, col);
			if (k > -1 && l < size)
				setDomain(k, l, col);
			if (j < size && m > -1)
				setDomain(j, m, col);
			if (k > -1 && m > -1)
				setDomain(k, m, col);
		}
		domain[row][col] = size;
	}

	void print() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (domain[i][j] == size)
					System.out.printf("%3s\t", "*");
				else
					System.out.printf("%3d\t", domain[i][j]);
			}
			System.out.println();
		}
		System.out.println("----------------------");
	}

	// use for sorting chromosomes after fitness
	public int compareTo(Chromosome o) {
		if (conflicts < o.conflicts)
			return -1;
		if (conflicts > o.conflicts)
			return 1;
		else
			return 0;
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < pairs.length; i++) {
			res += pairs[i] + "\t" + i + "\n";
		}
		return res;
	}

}
