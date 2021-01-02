//class that solves the serial binary search section
package package1;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;


public class SerialBinarySearch {
	
	private Nfile file;
	private byte[] buffer_s;
	private int key;
	private DataInputStream inStream;
	private ByteArrayInputStream BinStream;
	private Random r;
	private int AccessCounter;
	
	public SerialBinarySearch() throws IOException{
		
	}
	
	public void SBsearch() throws IOException{
		if(AccessCounter==0){
			file = new Nfile();			
		file.fillFile();
		}
		buffer_s = new byte[this.file.buffer.length];
		r = new Random();				//random number as requested
		key  =r.nextInt(this.file.N_INT-1)+1;
		System.out.println("Key is "+this.key);
		SerialMainSearch(key);			//main serial search function
	}
	
	
	public void SerialMainSearch(int key) throws IOException{
		int Counter = 1;
		int PageFound = 1;
		AccessCounter=1;
		boolean bs_result;
		while(Counter!=this.file.N_INT ){
			for(int i=0;i<this.file.P_SIZE;i++){
				this.file.randFile.seek((PageFound-1)*this.file.P_SIZE*4);	//defines from where the search begins
				this.file.randFile.read(buffer_s);							//load data in a buffer
		
				if(Counter==this.file.getN_INT()){							//exits when we reach last number
					break;
				}
				Counter++;
			}
			
			bs_result = binarySearch(buffer_s,key);							//binary search function

			if (bs_result != false){
				System.out.println("Found on page:"+PageFound);
				this.setAccessCounter(AccessCounter);
				return;
			}
			AccessCounter++;
			PageFound++;
		}
		System.out.println("The key is not found.");
	}
			 
	   public boolean binarySearch(byte[] buffer_s2, int key) throws IOException {
		   	
	        int start = 0;
	        int result;
	        int end =  this.file.P_SIZE- 1;
	        while (start <= end) {
	        	BinStream = new ByteArrayInputStream(buffer_s);		//in every loop we load again buffer so that we won't lose data from skip byte
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
	   
	  //setters and getters 	
	public Nfile getFile() {
		return file;
	}

	public void setFile(Nfile file) {
		this.file = file;
	}

	public byte[] getBuffer_s() {
		return buffer_s;
	}

	public void setBuffer_s(byte[] buffer_s) {
		this.buffer_s = buffer_s;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
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

	public Random getR() {
		return r;
	}

	public void setR(Random r) {
		this.r = r;
	}

	public int getAccessCounter() {
		return AccessCounter;
	}

	public void setAccessCounter(int accessCounter) {
		AccessCounter = accessCounter;
	}
	  
}