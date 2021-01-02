
package project2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Node implements Serializable {

	public  int T ;					//the values are transient to serialize objects to write in the file
	public int mNumKeys;
	public transient String[] mKeys;
	public Node[] mChildNodes ;
	public transient Object[] mObjects;
	public int[] info;
	public boolean mIsLeafNode;


	public int count;
	
	public Node(int t, int Counter){
		T=t;
		mNumKeys = 0 ;
		mKeys = new String[2 * t - 1];
		info = new int[2 * t - 1];
		this.setCount(Counter);
		mObjects = new Object[2 * T - 1];
		mChildNodes = new Node[2 * t];
	}
	
	int subtreeRootNodeIndex(String key) {
		for (int i = 0; i < mNumKeys; i++) {
			if (key.compareTo(mKeys[i])< 0) {
				return i;
			}
		}
		return mNumKeys;
	}
	

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	public int[] getInfo() {
		return info;
	}

	public void setInfo(int[] info) {
		this.info = info;
	}
	
}