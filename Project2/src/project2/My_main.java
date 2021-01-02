package project2;

import java.awt.List;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.lang.model.element.Element;
	
//KALOGERAKIS STEFANOS 
//AM 2015030064
public class My_main {
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		final String DIRECTORY = new java.io.File( "." ).getCanonicalPath();
		final String BFILE = "bst.tmp";
		final String PFILE = "posting_list.tmp";
		final int SIZE = 128;
		
		ArrayList<Posting_List> posting_list = new ArrayList<Posting_List>();
		TreeMap<String, Integer> tmap = new TreeMap<String, Integer>();		//Treemaps where used so that we could save as objects btree and posting list
		TreeMap<Integer, BTree> tmapbtree = new TreeMap<Integer, BTree>();
		TreeMap<String, Posting_List> tmaplist = new TreeMap<String, Posting_List>();

		File f = new File(BFILE);
		File f1 = new File(PFILE);
		int bytesStart=0; 
		Posting_List plist = null;
		int plist_count = -1;
		
		int n = (SIZE-8)/20;	//n of btree changes depending on page SIZE 
		BTree btree = new BTree(n);
		
		System.out.println("Welcome to B-Tree/Posting List project!!!!");
		System.out.println("Wait until the btree and the posting list are creating......");
	 

		File folder = new File(DIRECTORY);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {	//search in the file of the project all the files with .txt extension
		  File file = listOfFiles[i];
		  
		  if (file.isFile() && file.getName().endsWith(".txt")) {
	
			  bytesStart = 0;
			  Scanner s = null;
			    try {
			    	s = new Scanner(new BufferedReader(new FileReader(DIRECTORY+"\\"+file.getName())));
			    	s.useDelimiter("\\W+");		//read every word seperately from file
			    	while (s.hasNext()) {
			    		int element = -1;
			    		String CurrentWord = s.next();
			    		if (CurrentWord.length() != 0)
			    			CurrentWord = CurrentWord.toLowerCase();	//read every word as lowercase so that there is no problem while parsing the btree
			    			CurrentWord = CurrentWord.substring(0, Math.min(CurrentWord.length(), 12));	//read up to 12 strings per word
			    			
			    			int flag = btree.add(CurrentWord,CurrentWord);		//add word to btree
			    			if(flag==1){
			    				plist_count++;
			    				plist = new Posting_List(CurrentWord);
			    				element++;
			    				plist.addPosting_list(file.getName(), bytesStart, plist_count,element);	//add word to posting list
			    				posting_list.add(plist_count, plist);
			    				plist.current_page = element;
			    				tmap.put(CurrentWord, plist_count);
			    				tmapbtree.put(plist_count, btree);
			    				//btree.saveBST2(BFILE, tmapbtree);		save btree in every access
								
			    			}else{
			    				int cur_counter=0;
			    				while (cur_counter<posting_list.size()){
			    					if(posting_list.get(cur_counter).word.compareTo(CurrentWord)==0 && posting_list.get(cur_counter).another_pointer==0){
		    							Posting_List p = posting_list.get(cur_counter);
			    						try{
			    							int found_element = p.element+1;
			    							int page_found = p.current_page;
			    							p.addPosting_list(file.getName(), bytesStart, page_found,found_element);
			    						}catch(ArrayIndexOutOfBoundsException e){
			    							plist_count++;
			    							p.another_pointer = plist_count;
						    				plist = new Posting_List(CurrentWord);
						    				element++;
						    				plist.addPosting_list(file.getName(), bytesStart, plist_count,element);
						    				posting_list.add(plist_count, plist);
						    				
			    						}
			    						break;
			    					}
		    						
			    					cur_counter++;
			    				}
			    			}
			    			tmaplist.put(CurrentWord, plist);
			    			//plist.savePostingList(PFILE, tmaplist);	save posting list every access
			    			bytesStart = bytesStart + CurrentWord.length()+1;
			    	}
			    } finally {
			    	if (s != null) {
			    		s.close();
			    	}
			    }
			    System.out.println(file.getName()+" successfully embedded into btree and posting list");
			    System.out.println();
		  } 
		  
		}
		System.out.println("Total words in the files given are "+btree.totalsame);
		System.out.println("Total different words in the files given are "+btree.totalwords);
		System.out.println("Total accesses while creating btree "+btree.total);
		System.out.println("Average accesses while creating btree "+(float)btree.total/btree.totalsame);
		System.out.println();
		
		btree.saveBST(BFILE, tmap);		//save and load btree and posting list from their files
		tmap = btree.loadBST(BFILE);	//In the comments above exists the choice to insert both of them EVERY TIME but the performance is low and the program gets slower
		plist.savePostingList(PFILE, tmaplist);
		tmaplist = plist.loadPostingList(PFILE);
		
		/*Collections.sort( posting_list, new Comparator<Posting_List>(){	//that code was sorting posting list and was used for debbuging puprose
			@Override
			public int compare(Posting_List arg0, Posting_List arg1) {
				// TODO Auto-generated method stub
				 String x1 = ((Posting_List) arg0).word;
		         String x2 = ((Posting_List) arg1).word;
		         int sComp = x1.compareTo(x2);
		         if (sComp != 0) {
		               return sComp;
		            } else {
		               Integer x3 = ((Posting_List) arg0).another_pointer;
		               Integer x4 = ((Posting_List) arg1).another_pointer;
		               return x3.compareTo(x4);
		            }
				//return arg0.word.compareTo(arg1.word);
			}
	    });*/
		
		Scanner reader = new Scanner(System.in);
		printMenu();		//print menu
		
		int user_choice=0;
		try{
			user_choice = reader.nextInt();		//user choice.Terminates if invalid
		}catch(InputMismatchException e){
			System.out.println("You must enter an integer value at menu. Programm terminating.....");
			System.exit(0);
		}
		
		if(user_choice==1){
			Scanner search_reader = new Scanner(System.in);
			System.out.println("Please insert the string you wish to search");
			String string_chosen= search_reader.nextLine();
			plist.search_PostingList(posting_list, string_chosen.toLowerCase());
			System.out.println("Total number of accesses "+plist.getDiskAccess());
		}else if(user_choice==2){
			File folderRead = null;
			folderRead = new File(DIRECTORY+"\\Files_read");
			File[] listOfRandomFiles = folderRead.listFiles();
			
			for (int i = 0; i < listOfRandomFiles.length; i++) {		//read all the files from file_read file. So you can enter more if you wish
				File file = listOfRandomFiles[i];
				if (file.isFile() && file.getName().endsWith(".txt")) {
					  System.out.println("File "+file.getName());
					  Scanner word = new Scanner(new BufferedReader(new FileReader(DIRECTORY+"\\Files_read"+"\\"+file.getName())));
					  word.useDelimiter("\\W+");
					  int word_counter=0;
					  while(word.hasNext()){
						  String curr_word = word.next();
						  plist.search_PostingList(posting_list, curr_word.toLowerCase());
						  word_counter++;
					  }
					  System.out.println("Total words in files " +word_counter);
					  System.out.println("Average Accesses demanded: "+(float)plist.getDiskAccess()/word_counter);
				}	  
			}
			
		}else if(user_choice==3){
			File folderRead = null;
			folderRead = new File(DIRECTORY+"\\Files_read");//read all the files from file_read file. So you can enter more if you wish
			File[] listOfRandomFiles = folderRead.listFiles();
			
			for (int i = 0; i < listOfRandomFiles.length; i++) {
				File file = listOfRandomFiles[i];
				if (file.isFile() && file.getName().endsWith(".txt")) {
					  System.out.println("File "+file.getName());
					  Scanner word = new Scanner(new BufferedReader(new FileReader(DIRECTORY+"\\Files_read"+"\\"+file.getName())));
					  word.useDelimiter("\\W+");
					  int word_counter=0;
					  while(word.hasNext()){
						  String curr_word = word.next();
						  btree.search(curr_word.toLowerCase());
						  word_counter++;
					  }
					  System.out.println("Total words in files " +word_counter);
					  System.out.println("Average Accesses demanded: "+(float)btree.getdiskAccessees()/word_counter);
				}	  
			}
		}else if(user_choice==4){
			File folderRead = null;
			folderRead = new File(DIRECTORY+"\\Files_read_random");//read all the files from file_read_random file. So you can enter more if you wish
			File[] listOfRandomFiles = folderRead.listFiles();
			
			for (int i = 0; i < listOfRandomFiles.length; i++) {
				File file = listOfRandomFiles[i];
				if (file.isFile() && file.getName().endsWith(".txt")) {
					  System.out.println("File "+file.getName());
					  Scanner word = new Scanner(new BufferedReader(new FileReader(DIRECTORY+"\\Files_read_random"+"\\"+file.getName())));
					  word.useDelimiter("\\W+");
					  int word_counter=0;
					  while(word.hasNext()){
						  String curr_word = word.next();
						  btree.search(curr_word.toLowerCase());
						  word_counter++;
					  }
					  System.out.println("Total words in files " +word_counter);
					  System.out.println("Average Accesses demanded: "+(float)btree.getdiskAccessees()/word_counter);
				}	  
			}
		}else if(user_choice==5){
			File folderRead = null;
			folderRead = new File(DIRECTORY+"\\Files_read_random");//read all the files from file_read_random file. So you can enter more if you wish
			File[] listOfRandomFiles = folderRead.listFiles();
			
			for (int i = 0; i < listOfRandomFiles.length; i++) {
				File file = listOfRandomFiles[i];
				if (file.isFile() && file.getName().endsWith(".txt")) {
					  System.out.println("File "+file.getName());
					  Scanner word = new Scanner(new BufferedReader(new FileReader(DIRECTORY+"\\Files_read_random"+"\\"+file.getName())));
					  word.useDelimiter("\\W+");
					  int word_counter=0;
					  while(word.hasNext()){
						  String curr_word = word.next();
						  plist.search_PostingList(posting_list, curr_word.toLowerCase());
						  word_counter++;
					  }
					  System.out.println("Total words in files " +word_counter);
					  System.out.println("Average Accesses demanded: "+(float)plist.getDiskAccess()/word_counter);
				}	  
			}
		}else if(user_choice==6){//print the dictionary option
			Set set = tmap.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()){
				 Map.Entry mentry = (Map.Entry)iterator.next();
				 System.out.println("Word "+mentry.getKey()+" at page "+mentry.getValue());
				 }
		}
		else{
			System.out.println("You must enter a valid integer value at menu. Programm terminating......");
			System.exit(0);
		}
		System.out.println("Program terminated. Thanks for using!!!!");
	}
	
	static void printMenu(){
		System.out.println("Please type the integer you wish of the following options");
		System.out.println("1.Insert manually a word to search");
		System.out.println("2.Search words that exist in file from posting list");
		System.out.println("3.Search words that exist in file from btree");
		System.out.println("4.Search random words from btree");
		System.out.println("5.Search random words from posting list");
		System.out.println("6.Print Dictionary");
	}
	
}