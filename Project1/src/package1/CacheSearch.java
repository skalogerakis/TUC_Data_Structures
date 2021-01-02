//class that solves the final part with the cache queue

package package1;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class CacheSearch {
	final int EMPTY = 0;
	final int FULL = 1;
	final int TIMES = 10000;
	final int NEG= -1;
	private int pages;
	private DataInputStream inStream;
	private ByteArrayInputStream BinStream;
	Deque<int[]> cache = new LinkedList<int[]>();;
	Random r;
	public int[] aBuffer;
	public int[] bBuffer;
	protected byte[] buffer_c;
	private Nfile file;
	private int AccessCounter=0;
	private int totalAccessCounter = 0;
	
	public CacheSearch(){
		
	}
	
	public void MainCache() throws IOException{
		System.out.println("Binary Search on disk pages");
		System.out.println();
		if(AccessCounter==0){
			file = new Nfile();				//create a new file first
			file.fillFile();
		}
		buffer_c = new byte[this.file.buffer.length];
		aBuffer = new int[this.file.P_SIZE];
		cache = new LinkedList<int[]>();

		pages = this.file.N_INT/this.file.P_SIZE;	//find how many pages exist
		
		for(int i=0; i<TIMES; i++){
			//AccessCounter =0;
			r = new Random();							//auto-generate key
			int rkey = r.nextInt(this.file.N_INT-1)+1; 
			System.out.println("Key "+rkey);
			MainBinSearch2(0,this.pages,rkey);
			//System.out.println("Access "+this.getAccessCounter());
		}
	}
	
	public void MainBinSearch2(int start, int finish,int nkey) throws IOException{
		boolean result=false;
		boolean flag = false;

		
		for(int i=0; i<cache.size(); i++){	//check if the element already exists in cache
			bBuffer =cache.pollFirst();
			
			result = binarySearch(bBuffer, nkey);
			if (result != false){
				//We found the key in cache so we have got a hit
				System.out.println("We have a hit! The key exists in cache.");
				cache.addLast(bBuffer);
				break;

			}
			cache.addLast(bBuffer);
			
		}
		if(result==true){
			return;			//if we find it we don't want to count an access so the function returns
		}else{
			System.out.println("The key doesn't exist in cache");
			
		}
	
		while(flag!=true){	//if the key is not in the cache we proceed
			totalAccessCounter = 0;
			AccessCounter++;
			totalAccessCounter = totalAccessCounter+AccessCounter;
			this.setTotalAccessCounter(totalAccessCounter);
			//this.setAccessCounter(AccessCounter);
			int middlePageFile = start+(finish-start)/2;		//the middle page of the file
			int startmiddlePage = middlePageFile*this.file.P_SIZE;//first integer of middle page
			
	    	for(int i=0;i<this.file.P_SIZE;i++){
	    		this.file.randFile.seek((startmiddlePage+i)*4);	//defines from where the search begins
	    		this.file.randFile.read(buffer_c);
	    		BinStream = new ByteArrayInputStream(buffer_c);		//in every loop we load again buffer so that we won't lose data from skip byte
	        	inStream = new DataInputStream(BinStream);
	    		aBuffer[i] = inStream.readInt();					//buffer of integers 
	    	}
	    	this.file.randFile.seek((startmiddlePage)*4);	//defines from where the search begins
			this.file.randFile.read(buffer_c);
			BinStream = new ByteArrayInputStream(buffer_c);		//in every loop we load again buffer so that we won't lose data from skip byte
	    	inStream = new DataInputStream(BinStream);
	    	
	    	int startPage_result = aBuffer[0];					//first number of the page
	    	int finishPage_result = aBuffer[127];				//last number of the page
			
			
			if(nkey < startPage_result){
				finish = middlePageFile - 1;
			}else if(nkey > finishPage_result){
				start = middlePageFile + 1;
			}else{
				int flag_n = onPage(buffer_c,nkey);
				if(flag_n!=NEG){
					System.out.println("Key found on page: "+(middlePageFile+1));
					updateCache(aBuffer);		//if we find it we update the cache
					flag = true;
				}else{
					System.out.println("Key is not found");
				}
			}
		}
		
	
	}
	
	public int onPage(byte[] buffer_s3,int nkey) throws IOException{
		boolean flag;
		flag = mybinarySearch(buffer_s3, nkey);
		if(flag!=false){
			return 0;
		}
		return NEG;
	}
	
	//function that updates cache.If it is not full we simply add the page in queue.If it is we remove the oldest.
	public void updateCache(int[] newBuffer){
		System.out.println();
		System.out.println("Searching using cache");
		System.out.println("*************************************************");
		if (cache.size() < FULL){
			System.out.println("Caching a new page...");
			cache.addLast(newBuffer);
		}
		else{
			System.out.println("Removing the oldest buffer...");
			cache.removeFirst();
			System.out.println("Adding the new buffer to cache...");
			cache.addLast(newBuffer);
		}
	}
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
	
	public boolean binarySearch(int[] Buffer, int key) {
        
        int start = 0;
        int end = Buffer.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (key == Buffer[mid]) {
                return true;
            }
            if (key < Buffer[mid]) {
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

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public DataInputStream getInStream() {
		return inStream;
	}

	public void setInStream(DataInputStream inStream) {
		this.inStream = inStream;
	}

	public ByteArrayInputStream getBinStream() {
		return BinStream;
	}

	public void setBinStream(ByteArrayInputStream binStream) {
		BinStream = binStream;
	}

	public Deque<int[]> getCache() {
		return cache;
	}

	public void setCache(Deque<int[]> cache) {
		this.cache = cache;
	}

	public Random getR() {
		return r;
	}

	public void setR(Random r) {
		this.r = r;
	}

	public int[] getaBuffer() {
		return aBuffer;
	}

	public void setaBuffer(int[] aBuffer) {
		this.aBuffer = aBuffer;
	}

	public int[] getbBuffer() {
		return bBuffer;
	}

	public void setbBuffer(int[] bBuffer) {
		this.bBuffer = bBuffer;
	}

	public byte[] getBuffer_c() {
		return buffer_c;
	}

	public void setBuffer_c(byte[] buffer_c) {
		this.buffer_c = buffer_c;
	}

	public Nfile getFile() {
		return file;
	}

	public void setFile(Nfile file) {
		this.file = file;
	}

	public int getEMPTY() {
		return EMPTY;
	}

	public int getFULL() {
		return FULL;
	}

	public int getTotalAccessCounter() {
		return totalAccessCounter;
	}

	public void setTotalAccessCounter(int totalAccessCounter) {
		this.totalAccessCounter = totalAccessCounter;
	}

	public int getTIMES() {
		return TIMES;
	}

	public int getNEG() {
		return NEG;
	}
	
	
			
	
	
}