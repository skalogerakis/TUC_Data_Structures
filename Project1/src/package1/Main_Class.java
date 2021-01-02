//main class of the project

package package1;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Stack;
import java.util.Scanner;

//import myPackage.PageBinarySearch;

public class Main_Class {
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		
		Scanner scanner = new Scanner(System.in);				//Scanner class to read from keyboard
		BinaryPageSearch b_Search = new BinaryPageSearch();		//create instance of other classes
		SerialBinarySearch s_Search = new SerialBinarySearch();
		PageIndex p_index = new PageIndex();
		CacheSearch c_search = new CacheSearch();
		//Nfile file;
		
		final int TIMES = 10000;									//number of repetitions demanded
		int choice=0;
		int totalAccessCounter=0;
		
		int[] PageIndex = new int[TIMES];
		
		Main_Class main = new Main_Class();
		main.MainMenu();
		try{
			choice = scanner.nextInt();
		}catch(InputMismatchException e){
			System.out.println("You should have entered an integer.");
			System.out.println("Terminating.......");
			System.exit(0);
		}
		//if the choice is not valid the programm terminates
		switch(choice){
			case 1:{
				System.out.println("You chose option 1");
				System.out.println("Serial search on disk pages");
				for(int i=0; i<TIMES; i++){
					System.out.println();
					s_Search.SBsearch();
					System.out.println("Accesses demanded:"+s_Search.getAccessCounter());
					totalAccessCounter = totalAccessCounter+s_Search.getAccessCounter();
				}
				float accessAverage = (float)totalAccessCounter/(float)TIMES;
				System.out.println();
				System.out.println("Average accesses demanded: "+accessAverage);
				break;
			}
			case 2:{
				System.out.println("You chose option 2");
				System.out.println("Binary Search on disk pages");
				for(int i=0; i<TIMES; i++){
					System.out.println();
					b_Search.BPsearch();
					System.out.println("Accesses demanded:"+b_Search.getAccessCounter());
					totalAccessCounter = b_Search.getAccessCounter();
				}
				float accessAverage = (float)totalAccessCounter/(float)TIMES;
				System.out.println();
				System.out.println("Average accesses demanded: "+accessAverage);
				break;
			}
			case 3:{
				System.out.println("You chose option 3");
				System.out.println("Search on disk pages after grouping keys");
				for(int i=0; i<TIMES; i++){
					PageIndex[i] = p_index.PageIndexRandKeyGenerator();
				}
				Arrays.sort(PageIndex);
				System.out.println("Keys after sorting");
				for(int i=0; i<TIMES; i++){
					System.out.println(PageIndex[i]);
				}
				b_Search.BPsearch(PageIndex);
				float accessAverage = (float)b_Search.getTotalAccessCounter()/(float)TIMES;
				System.out.println();
				System.out.println("Average accesses demanded: "+accessAverage);
				break;
			}
			case 4:{
				System.out.println("You chose option 4");
				c_search.MainCache();
				float accessAverage = (float)c_search.getTotalAccessCounter()/(float)TIMES;
				System.out.println();
				System.out.println("Average accesses demanded: "+accessAverage);
				break;
			}
			default:
				System.out.println("You didn't insert one of the following options.");
				System.out.println("Terminating.......");
				System.exit(0);
			}
			System.out.println("Programm terminated.Thanks for using!!!");	
}
	//my main menu
	public void MainMenu(){
		System.out.println("Welcome!!!!");
		System.out.println("Choose one of the following numbers to start:");
		System.out.println("1.Serial search on file for random key");
		System.out.println("2.Binary search on file for random key");
		System.out.println("3.Binary search on file after grouping keys");
		System.out.println("4.Binary search on file using cache");
		System.out.println("**In each case a file is created automatically**");
	}

}
