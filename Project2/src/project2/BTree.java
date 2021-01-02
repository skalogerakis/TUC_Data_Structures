
package project2;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

public class BTree implements Serializable {
	public int T;
	public int Counter=1;
	private Node mRootNode;
	public int totalwords=0;
	public int diskAccessees ;
	public int total;
	int totalsame;
	
	public BTree(int n) {
		//T=(n+1)/2;
		T=n;
		mRootNode = new Node(T,Counter);
		mRootNode.mIsLeafNode = true;
		diskAccessees=0;
	}

	public int add(String key,Object object) {
		totalsame++;
		Node rootNode = mRootNode;
		
		if (!update(mRootNode,key,object)) {
			totalwords++;
			Counter ++;
			if (rootNode.mNumKeys == (2 * T - 1)) {
				total++;
				Node newRootNode = new Node(T,Counter);
				mRootNode = newRootNode;
				newRootNode.mIsLeafNode = false;
				mRootNode.mChildNodes[0] = rootNode;
				splitChildNode(newRootNode, 0, rootNode); // Split rootNode and move its median (middle) key up into newRootNode.
				insertIntoNonFullNode(newRootNode, key,object); // Insert the key into the B-Tree with root newRootNode.												
			} 
			
			else {
				
				insertIntoNonFullNode(rootNode, key,object); // Insert the key into the B-Tree with root rootNode.
			}
			
		}else
			return 0;
		return 1;
		
	}

	// Split the node, node, of a B-Tree into two nodes that both contain T-1
	// elements and move node's median key up to the parentNode.
	// This method will only be called if node is full; node is the i-th child
	// of parentNode.
	
	void splitChildNode(Node parentNode, int i, Node node) {
		Counter++;
		total++;
		Node newNode = new Node(T,Counter);
		newNode.mIsLeafNode = node.mIsLeafNode;
		newNode.mNumKeys = T - 1;
		
		for (int j = 0; j < T - 1; j++) { // Copy the last T-1 elements of node into newNode.
			newNode.mKeys[j] = node.mKeys[j + T];
			newNode.mObjects[j] = node.mObjects[j + T];
		}
		
		if (!newNode.mIsLeafNode) {
			
			for (int j = 0; j < T; j++) { // Copy the last T pointers of node into newNode.
				newNode.mChildNodes[j] = node.mChildNodes[j + T];
			}
			
			for (int j = T; j <= node.mNumKeys; j++) {
				node.mChildNodes[j] = null;
			}
		}
		for (int j = T; j < node.mNumKeys; j++) {
			node.mObjects[j] = null;
			node.mKeys[j] = null;
		}
		node.mNumKeys = T - 1;

		// Insert a (child) pointer to node newNode into the parentNode, moving
		// other keys and pointers as necessary.
		for (int j = parentNode.mNumKeys; j >= i + 1; j--) {
			parentNode.mChildNodes[j + 1] = parentNode.mChildNodes[j];
		}
		
		parentNode.mChildNodes[i + 1] = newNode;
		
		for (int j = parentNode.mNumKeys - 1; j >= i; j--) {
			parentNode.mKeys[j + 1] = parentNode.mKeys[j];
			parentNode.mObjects[j + 1] = parentNode.mObjects[j];
			
		}
		
		parentNode.mKeys[i] = node.mKeys[T - 1];
		parentNode.mObjects[i] = node.mObjects[T - 1];
		
		node.mKeys[T - 1] = null;
		node.mObjects[T - 1] = null;
		parentNode.mNumKeys++;
		
	}

	// Insert an element into a B-Tree. (The element will ultimately be inserted
	// into a leaf node).
	void insertIntoNonFullNode(Node node, String key,Object object) {
		int i = node.mNumKeys - 1;
		if (node.mIsLeafNode) {  // Since node is not a full node insert the new element into its proper place within node.
			
			while (i >= 0 && key.compareTo(node.mKeys[i])< 0) {
				node.mKeys[i + 1] = node.mKeys[i];
				node.mObjects[i + 1] = node.mObjects[i];
				i--;
			}
			
			i++;
			node.mKeys[i] = key;
			node.mObjects[i] = object;
			node.mNumKeys++;
		}
		else {
			
			// Move back from the last key of node until we find the child pointer to the node that is the root node of the subtree where the new element should be placed.
			while (i >= 0 && key.compareTo(node.mKeys[i])< 0) {
				i--;
			}
			i++;
			if (node.mChildNodes[i].mNumKeys == (2 * T - 1)) {
				splitChildNode(node, i, node.mChildNodes[i]);
				if (key.compareTo(node.mKeys[i])> 0) {
					i++;
				}
			}
			insertIntoNonFullNode(node.mChildNodes[i], key,object);
		}
	}
	//search methods for btree
	public Object search(String key) {
		return search(mRootNode, key);
	}
	
	public Object search(Node node, String key) {
		while (node != null) {
			int i = 0;
			diskAccessees++;
			while (i < node.mNumKeys && key.compareTo(node.mKeys[i])> 0) {
				i++;
			}
			if (i < node.mNumKeys && key.compareTo(node.mKeys[i])== 0) {				
				return node.mObjects[i];
			}
			if (node.mIsLeafNode) {
				return null;
			} else {
				node = node.mChildNodes[i];
			}

		}
		return null;
	}
	
	private boolean update(Node node, String key,Object object) {
		while (node != null) {
			total++;
			int i = 0;
			while (i < node.mNumKeys && key.compareTo(node.mKeys[i])> 0 ) {
				i++;
			}
			if (i < node.mNumKeys && key.compareTo(node.mKeys[i])== 0) {
				node.mObjects[i] = object;
				return true;
			}
			if (node.mIsLeafNode) {
				
				return false;
			} else {
				
				node = node.mChildNodes[i];
			}
		}
		return false;
	} 
	
	//save to file
	public void saveBST(String myfile,TreeMap plist) throws IOException {
	      if (plist != null) {
	      	FileOutputStream fos = new FileOutputStream(myfile);
	      	ObjectOutputStream oos = new ObjectOutputStream(fos);
	      	try {
	      		oos.writeObject((TreeMap<String,Posting_List>)plist); // write root of BST to file
	      		//oos.writeObject(i); // write totalwords count to file
	      	} finally {
	      		oos.close();
	      		fos.close();
	      	}
	      }
	  }
	//load from file
	public TreeMap loadBST(String myfile) throws IOException, ClassNotFoundException {
	  	FileInputStream fis = new FileInputStream(myfile);
	  	ObjectInputStream ois = new ObjectInputStream(fis);
	  	try {
	  		return (TreeMap<String,BTree>) ois.readObject(); 
	  	} finally {
	  		ois.close();
	  		fis.close();
	  	}
	  }
	
	public void saveBST2(String myfile,TreeMap<Integer, BTree> tmapbtree) throws IOException {
	      if (tmapbtree != null) {
	      	FileOutputStream fos = new FileOutputStream(myfile);
	      	ObjectOutputStream oos = new ObjectOutputStream(fos);
	      	try {
	      		oos.writeObject(tmapbtree); // write root of BST to file
	      		//oos.writeObject(i); // write totalwords count to file
	      	} finally {
	      		oos.close();
	      		fos.close();
	      	}
	      }
	  }
	
	public int getdiskAccessees() {
		return diskAccessees;
	}
	public Object objret(){
		return mRootNode.mObjects;
		
	}
	
	public int getTotalwords() {
		return totalwords;
	}

	public void setTotalwords(int totalwords) {
		this.totalwords = totalwords;
	}
	
}
