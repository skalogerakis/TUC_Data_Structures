package project2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class Posting_List implements Serializable{
	public transient String[] name_of_file;	//the values are transient to serialize objects to write in the file
	public String word;
	public transient int[] bytes_from_start;
	public int another_pointer;
	public int current_page;
	public int element;
	private int diskAccess = 0;
	
	
	public Posting_List(String nword) {
		name_of_file= new String[10];
		bytes_from_start = new int[10];
		another_pointer = 0;
		this.current_page = 0;
		this.word = nword;
	}
	
	public void addPosting_list(String filename, int bytesfromstart, int Counter,int elem){
		name_of_file[elem] = filename;
		bytes_from_start[elem] = bytesfromstart;
		this.current_page = Counter;
		element = elem;
		
		
	}
	
	public void search_PostingList(ArrayList<Posting_List> arr, String key){
		int i=0;
		boolean flag=false;
		while(i<arr.size()){
			diskAccess++;
			while(arr.get(i).word.compareTo(key)==0 ){
				diskAccess++;
				for(int j=0; j<10; j++){
					if(arr.get(i).name_of_file[j]==null){
						break;
					}
					System.out.println("Text "+arr.get(i).name_of_file[j]+" contains word "+arr.get(i).word+" "+arr.get(i).bytes_from_start[j]+" bytes from start");
				}
				if(arr.get(i).another_pointer!=0){
					i=arr.get(i).another_pointer;
					
				}else{
					flag=true;
					break;
				}
			}
			i++;
			if(flag==true){
				break;
			}
		}
		if(flag==false){
			System.out.println("The word you asked "+key+" could not be found");
		}
	}
	
	public void savePostingList(String myfile,TreeMap<String,Posting_List> plist) throws IOException {
	      if (plist != null) {
	      	FileOutputStream fos = new FileOutputStream(myfile);
	      	ObjectOutputStream oos = new ObjectOutputStream(fos);
	      	try {
	      		oos.writeObject(plist); // write root of BST to file
	      	} finally {
	      		oos.close();
	      		fos.close();
	      	}
	      }
	  }
	
	public TreeMap<String,Posting_List> loadPostingList(String myfile) throws IOException, ClassNotFoundException {
	  	FileInputStream fis = new FileInputStream(myfile);
	  	ObjectInputStream ois = new ObjectInputStream(fis);
	  	TreeMap<String,Posting_List> tmap;
	  	try {
	  		tmap = (TreeMap<String,Posting_List>) ois.readObject();
	  	} finally {
	  		ois.close();
	  		fis.close();
	  	}
	  	return tmap;
	  }

	public int getDiskAccess() {
		return diskAccess-1;
	}

	

}
