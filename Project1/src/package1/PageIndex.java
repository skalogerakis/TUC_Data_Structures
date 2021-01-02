//class that generates keys for binary search after grouping question

package package1;

import java.io.IOException;
import java.util.Random;

public class PageIndex {
	private Random r;
	private int key;
	final int N_INT = 10000000;					//number of integers
	public PageIndex(){
		
	}
	
	public int PageIndexRandKeyGenerator() throws IOException{
		r = new Random();				//random number as requested
		key  =r.nextInt(N_INT-1)+1;
		System.out.println("Key is "+this.key);
		return key;
	}
}
