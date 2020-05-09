/*
 * 
 * Hussain
 * k16-3805
 * sec: D
 * use attached GUI to verify results
 * 
 */

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;


public class Nqueen_Genetic {

	static int NO_OF_CHROMO = 4; // shud be even
	static int NO_OF_QUEENS = 8;	// <------- change this to take it up a notch, but be patient
	static Random rand = new Random();


	
	public static void main(String[] args) {
		// initial pop
		Chromosome chromosomes[] = new Chromosome[NO_OF_CHROMO];
		for (int i = 0; i < chromosomes.length; i++) {
			chromosomes[i] = new Chromosome(NO_OF_QUEENS);
		}	
		
		int count = 0;
		
		while(true){
			
			count++;
			// doing selection
			Arrays.sort(chromosomes); // sorts by fitness func
			if(chromosomes[0].conflicts == 0){	
				System.out.println("row\tcol");
				System.out.println(chromosomes[0]);
				System.out.println("iterations : " +count);
				System.exit(0);	// break loop
			}
			Arrays.fill(chromosomes[NO_OF_CHROMO-1].pairs, 0); // removing 4th chromosome from pop
			int temp[][] = new int[NO_OF_CHROMO-1][NO_OF_QUEENS]; // making copy for crossover
			
			for (int j = 0; j < temp.length; j++) {
				temp[j] = Arrays.copyOfRange(chromosomes[j].pairs, 0, NO_OF_QUEENS);
			}
	
			int k = 0;
			int c = rand.nextInt(NO_OF_QUEENS); // random crossover pos
			for (int i = 0; i < NO_OF_CHROMO; i+=2) {
				// doing crossover
				System.arraycopy(temp[k], 0, chromosomes[i].pairs, 0, c);
				System.arraycopy(temp[k+1], c, chromosomes[i].pairs, c, NO_OF_QUEENS-c);
				System.arraycopy(temp[k+1], 0, chromosomes[i+1].pairs, 0, c);
				System.arraycopy(temp[k], c, chromosomes[i+1].pairs, c, NO_OF_QUEENS-c);
				k++;
				
			}

			//mutation
			for (int i = 0; i < chromosomes.length; i++) {
				randomMutate(chromosomes[i].pairs); //adding random nums
				removeDupInIntArray(chromosomes[i].pairs); //removing duplicates
				chromosomes[i].chkConflict(); // now checking conflicts
			}
			
			for (int j = 0; j < chromosomes.length; j++) {
				System.out.println(Arrays.toString(chromosomes[j].pairs) + " conflicts : " + chromosomes[j].conflicts);
			}
			System.out.println("-----");
			
		}
		
		
	}
	
	private static void randomMutate(int[] pairs) {

		for (int i = 0; i < 2; i++) {
			int x1 = rand.nextInt(NO_OF_QUEENS);
			int x2 = rand.nextInt(NO_OF_QUEENS);
			pairs[x1] = x2;
		}
		
	}

	public static void removeDupInIntArray(int[] ints){
		
	    Set<Integer> setString = new LinkedHashSet<Integer>();
	    Set<Integer> setString2 = new LinkedHashSet<Integer>();

	    for(int i=0;i<ints.length;i++){
	    	setString.add(ints[i]);
	    	setString2.add(i);
	    }
	    
	    setString2.removeAll(setString);
	    setString.addAll(setString2);
	    int i = 0;
	    for(Integer j : setString) ints[i++] = j;
	    
	}
	

}