import java.util.ArrayList;
import java.util.LinkedList;


public class SeparateChaining<K, V> implements HashTable<K, V>{
	
	//instance variables
	int M, size, Collisions , MaxSize;
	LinkedList< Pair<K, V> >[] hashtable;// = (LinkedList< Pair<K, V> >[])new LinkedList<?>[M];
	ArrayList<K> keys = new ArrayList<>();
	float load_factor = 3;
	
	
	//Constructor
	public SeparateChaining(){
		M = 10;
		size = MaxSize = 0;
		Collisions = 0;
		hashtable = new LinkedList[M];
		for(int i=0;i<M;i++){
			hashtable[i] = new LinkedList<>();
		}
		keys = new ArrayList<>();
		load_factor = 3;
	}
	
	/***
	 * This method calculates the index of the key object using
	 * the Object method hashCode() and inserts it in the 
	 * appropriate place in the hashTable
	 * 
	 * if the key already exists in the table the value associated
	 * with that key is set to value
	 * 
	 * 
	 * @param key the key of the entry
	 * @param value the value of the entry
	 **/
	@Override
	public void put(K key, V value) {
		if((((float)load_factor)<(float)((float)size/(float)M))){
			rehash();
		}
		int index = key.hashCode()%M;
		if(index<0)index *= -1;
		if(contains(key)){
			for(int i=0;i<hashtable[index].size();i++){
				if(hashtable[index]!=null && hashtable[index].get(i).getFirst().equals(key)){
					hashtable[index].get(i).setSecond(value);
					return;
				}
			}
		}else{
			size ++;
			if(MaxSize<size)MaxSize = size;
			Pair<K, V> p = new Pair<K, V>(key,value);
			if(hashtable[index]==null){ //checking if this is the first entry in this index
				hashtable[index].addFirst(p); ;
			}else {
				hashtable[index].addLast(p);
			}
			keys.add(key);
		}
	}

	/***
	 * This method gets the value whose key is key
	 * 
	 * @param key the key of required value
	 * @return value the value associated with the given key
	 **/
	@Override
	public V get(K key) {
		int index = key.hashCode()%M;
		if(index<0)index *= -1;
		for(int i=0;i<hashtable[index].size();i++){
			if(hashtable[index]!=null && hashtable[index].get(i).getFirst().equals(key)){
				return hashtable[index].get(i).getSecond();
			}
		}
		return null;
	}

	
	/***
	 *This method deletes the entry whose
	 *key equals key
	 * O(n)
	 * 
	 * @param key is the key to which we want to delete the entry
	 **/
	@Override
	public void delete(K key) {
		int index = key.hashCode()%M;
		if(index<0)index *= -1;
		for(int i=0;i<hashtable[index].size();i++){
			if(hashtable[index]!=null && hashtable[index].get(i).getFirst().equals(key)){
				hashtable[index].remove(i);
				keys.remove(key);
				size--;
				return;
			}
		}
	}

	
	/***
	 * This method checks whether the given key exists in
	 * the hash table or not
	 * 
	 * @param key the key searched for
	 * @return true if the entry whose key is equal to key exists and false otherwise
	 **/
	@Override
	public boolean contains(K key) {
		int index = key.hashCode()%M;
		if(index<0)index *= -1;
		for(int i=0;i<hashtable[index].size();i++){
			if(hashtable[index]!=null && hashtable[index].get(i).getFirst().equals(key)){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method check whether there are
	 * entries within the hashtable
	 * 
	 * @return true if the hashtable is empty ,false otherwise
	 * */
	@Override
	public boolean isEmpty() {
		if(size==0)return true;
		return false;
	}

	/***
	 * This method gets the number of 
	 * entries within the hashtable 
	 * 
	 * @return number of entries within the hashtable
	 **/
	@Override
	public int size() {
		return size;
	}

	/***
	 *This method gets an iterable of keys 
	 * 
	 * @return an arraylist of all the keys in the hashtable
	 **/
	@Override
	public Iterable<K> keys() {
		return keys;
	}

	
	/***
	 * This method increases the size of the hashtable when
	 * the load-factor increases above a certain thres-
	 * hold
	 **/
	@Override
	public void rehash() {
		M *= 2;
		LinkedList< Pair<K, V> >[] temp = hashtable;
		hashtable = new LinkedList[M];//(LinkedList< Pair<K, V> >[])new LinkedList<?>[M];
		for(int i=0;i<M;i++){
			hashtable[i] = new LinkedList<>();
		}
		size = 0;
		int loop = M/2;
		for(int i=0;i<loop;i++){
			for(int j=0;j<(temp[i].size());j++){
				put(temp[i].get(j).getFirst(), temp[i].get(j).getSecond());
			}
		}
	}
	
	@Override
	public int getCollisions() {
		return Collisions;
	}

	@Override
	public int getMaxSize() {
		return M;
	}
}
