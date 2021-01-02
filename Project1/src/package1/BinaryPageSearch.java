//class that solves the binary search and binary search with grouping keys questions

package package1;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;
//import java.util.Stack;

public class BinaryPageSearch {
	
	final int NEG= -1;
	private Nfile file;
	private DataInputStream inStream;
	private ByteArrayInputStream BinStream;
	protected byte[] buffer_s; 
	private int pages;
	private Random r;
	private int rkey;
	private int AccessCounter=0;
	private int totalAccessCounter = 0;
	private boolean if_found;
	
	
	final int TIMES = 10000;
	
	public BinaryPageSearch(){
		
	}
	//method that solves the first binary search problem
	public void BPsearch() throws IOException{
		if(AccessCounter==0){
			file = new Nfile();				//create a new file first
			file.fillFile();
		}
		buffer_s = new byte[this.file.buffer.length];
		pages = this.file.N_INT/this.file.P_SIZE;	//find how many pages exist
		r = new Random();							//auto-generate key
		rkey = r.nextInt(this.file.N_INT-1)+1; 
		System.out.println("Key: "+rkey);
		MainBinSearch(0,this.pages,rkey);
	}
	//method that solves the binary search after grouping keys
	public void BPsearch(int[] index) throws IOException{
		System.out.println("Binary Search on disk pages");
		System.out.println();
		if(AccessCounter==0){
			file = new Nfile();				//create a new file first
			file.fillFile();
		}
		pages = this.file.N_INT/this.file.P_SIZE;	//find how many pages exist
		buffer_s = new byte[this.file.buffer.length];
		int n=0;
		for(int i=0; i<TIMES; i++){					//check if key is in the current page so that an access is not demanded
			AccessCounter =0;
			if(index[i]<=n*128){					
				System.out.println("Key "+index[i]+" also in the page "+n);
				continue;
			}
			System.out.println("Key "+index[i]);
			n= MainBinSearch2(n,this.pages,index[i]);
			System.out.println("Page "+n);
			
			
		}
		
	}
	
	public void MainBinSearch(int start, int finish,int nkey) throws IOException{
		AccessCounter++;
		this.setAccessCounter(AccessCounter);
		int middlePageFile = start+(finish-start)/2;		//the middle page of the file
		int startmiddlePage = middlePageFile*this.file.P_SIZE;//first integer of middle page
		
		this.file.randFile.seek((startmiddlePage)*4+508);	//defines from where the search begins
		this.file.randFile.read(buffer_s);
		BinStream = new ByteArrayInputStream(buffer_s);		//in every loop we load again buffer so that we won't lose data from skip byte
    	inStream = new DataInputStream(BinStream);
    	int finishPage_result = inStream.readInt();
	
		BinStream.close();
		inStream.close();
		
		this.file.randFile.seek((startmiddlePage)*4);	//defines from where the search begins
		this.file.randFile.read(buffer_s);
		BinStream = new ByteArrayInputStream(buffer_s);		//in every loop we load again buffer so that we won't lose data from skip byte
    	inStream = new DataInputStream(BinStream);
    
    	int startPage_result = inStream.readInt();
    	this.setBuffer_s(buffer_s);
		
		if(nkey < startPage_result){
			finish = middlePageFile - 1;
			MainBinSearch(start,finish,nkey);
		}else if(nkey > finishPage_result){
			start = middlePageFile + 1;
			MainBinSearch(start,finish,nkey);
		}else{
			int result = onPage(buffer_s,nkey);
			if(result!=NEG){
				System.out.println("Key found on page: "+(middlePageFile+1));
				if_found=true;
			}else{
				System.out.println("Key is not found");
				if_found=false;
			}
		}
	
		}
	
	public int MainBinSearch2(int start, int finish,int nkey) throws IOException{
		int flag =-1;
		int ret_val = 0;
		while(flag!=0){
			AccessCounter++;									//counts number if accesses
			this.setAccessCounter(AccessCounter);
			int middlePageFile = start+(finish-start)/2;		//the middle page of the file
			int startmiddlePage = middlePageFile*this.file.P_SIZE;//first integer of middle page
			
			
			this.file.randFile.seek((startmiddlePage)*4+508);	//defines the last element of the page we want
			this.file.randFile.read(buffer_s);
			BinStream = new ByteArrayInputStream(buffer_s);		//in every loop we load again buffer so that we won't lose data from skip byte
	    	inStream = new DataInputStream(BinStream);
	    	int finishPage_result = inStream.readInt();
			
			BinStream.close();								//close all streams
			inStream.close();
			
			this.file.randFile.seek((startmiddlePage)*4);	//defines from where the search begins
			this.file.randFile.read(buffer_s);
			BinStream = new ByteArrayInputStream(buffer_s);		//in every loop we load again buffer so that we won't lose data from skip byte
	    	inStream = new DataInputStream(BinStream);
	    	int startPage_result = inStream.readInt();
			
			if(nkey < startPage_result){
				finish = middlePageFile - 1;
			}else if(nkey > finishPage_result){
				start = middlePageFile + 1;
			}else{
				int result = onPage(buffer_s,nkey);
				if(result!=NEG){
					System.out.println("Key found on page: "+(middlePageFile+1));
					System.out.println("Accesses demanded "+this.getAccessCounter());
					totalAccessCounter = totalAccessCounter+this.getAccessCounter();
					this.setTotalAccessCounter(totalAccessCounter);
					flag=0;
					ret_val = middlePageFile+1;			//returns the page found
					continue;
				}else{
					System.out.println("Key is not found");
					ret_val = 0;
					continue;
				}
			}
	
			}
		return ret_val;	
		}
		//method to handle the case we find the page of the key. Returns 0 if the number is found and zero otherwise
		public int onPage(byte[] buffer_s3,int nkey) throws IOException{
			boolean flag;
			flag = mybinarySearch(buffer_s3, nkey);
			if(flag!=false){
				return 0;
			}
			return NEG;
		}
		
		//basic binary search
		public boolean mybinarySearch(byte[] buffer_s2, int key) throws IOException {
		   	
	        int start = 0;
	        int result;
	        int end =  this.file.P_SIZE-1;
	        while (start <= end) {
	        	BinStream = new ByteArrayInputStream(buffer_s2);		//in every loop we load again buffer so that we won't lose data from skip byte
	        	inStream = new DataInputStream(BinStream);
	            int mid = (start + end) / 2;
	            inStream.skip(mid*4);			//skips needed bytes to reach middle number of the page
	            
	            result = inStream.readInt();
	            BinStream.close();				//close byte array
	            inStream.close();				//close data input
	            if (key == result) {
	                return true;
	            }
	            if (key < result) {
	                end = mid - 1;
	            } else {
	                start = mid + 1;
	            }
	        }
	        return false;
	    }
		
		//setters getters
		public int getAccessCounter() {
			return AccessCounter;
		}
		public void setAccessCounter(int accessCounter) {
			AccessCounter = accessCounter;
		}
		public boolean isIf_found() {
			return if_found;
		}
		public void setIf_found(boolean if_found) {
			this.if_found = if_found;
		}
		public byte[] getBuffer_s() {
			return buffer_s;
		}
		public void setBuffer_s(byte[] buffer_s) {
			this.buffer_s = buffer_s;
		}
		public int getTotalAccessCounter() {
			return totalAccessCounter;
		}
		public void setTotalAccessCounter(int totalAccessCounter) {
			this.totalAccessCounter = totalAccessCounter;
		}
		
}
