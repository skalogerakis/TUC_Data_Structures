/*Creates-Edits the demanded file using RandomAccessFile and fills it with 10^7 integers. Each number is presented in 4 bytes
binary so its size is 4*10^7. Each page-buffer can take up to 128 integers(512 bytes)*/

package package1;

import java.io.*;

public class Nfile {
	final int P_SIZE = 128;					//integers in one page
	final int N_INT = 10000000;					//number of integers
	final String FILE_NAME = "new_file.bin";//file name with .bin extension for binary files
	
	private DataOutputStream outputfile;
	private ByteArrayOutputStream bstream;
	byte[] buffer;
	protected RandomAccessFile randFile;
	private int total_pages = this.N_INT/this.P_SIZE;
	
	public Nfile() {
		bstream = new ByteArrayOutputStream();
		outputfile = new DataOutputStream(bstream);	
		try {
			randFile = new RandomAccessFile(this.FILE_NAME,"rw");	//create file with random access file
		} catch (FileNotFoundException e) {
			System.out.println("Error while creating file.");
		}
		
		
	}
	
	protected void fillFile() throws IOException{
		int page = 0;
		int first_element_pointer = 0;
		int Counter = 1;
		while(Counter!=this.N_INT){
			for(int i=0;i<P_SIZE;i++){
				outputfile.writeInt(Counter);
				if(Counter==N_INT){
					break;						//Exit loop when we reach the last element
				}
				Counter++;
				outputfile.close();
			}
			
			//Get the bytes of the serialized object
			buffer = bstream.toByteArray();
			
			first_element_pointer = buffer.length*page;			//find where seek must begin(seek works with bytes)
			randFile.seek(first_element_pointer);
			randFile.write(buffer);				//fills files directly from buffer
			
			bstream.reset();					//discards all existing data so that we can insert new ones in the allocated memory
			outputfile.close();					//closes stream
			if(total_pages== (page+1)){
				break;
			}
			page++;
			
		}
	}

	//setters getters
	public DataOutputStream getOutputfile() {
		return outputfile;
	}

	public void setOutputfile(DataOutputStream outputfile) {
		this.outputfile = outputfile;
	}

	public ByteArrayOutputStream getBstream() {
		return bstream;
	}

	public void setBstream(ByteArrayOutputStream bstream) {
		this.bstream = bstream;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public RandomAccessFile getRandFile() {
		return randFile;
	}

	public void setRandFile(RandomAccessFile randFile) {
		this.randFile = randFile;
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}

	public int getP_SIZE() {
		return P_SIZE;
	}

	public int getN_INT() {
		return N_INT;
	}

	public String getFILE_NAME() {
		return FILE_NAME;
	}
}
	
	

