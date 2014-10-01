import java.util.ArrayList;


public class QuadraticProbing<K, V> implements HashTable<K, V> {

	//instance variables
	int M, size, Collisions , MaxSize;
	Pair<K, V>[] hashtable = new Pair[M];
	ArrayList<K> keys = new ArrayList<>();
	float load_factor = (float)0.75;
	
	//Constructor
	public QuadraticProbing(){
		M = 16;
		size = MaxSize = 0;
		Collisions = 0;
		hashtable = new Pair[M];
		keys = new ArrayList<>();
		load_factor = (float)0.75;
		
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
		if(((load_factor)<(float)((float)size/(float)M))){
			rehash();
		}
		int home = key.hashCode()%M;
		if(home<0)home *= -1;
		Pair<K, V> p = new Pair<K, V>(key, value);
		if(contains(key)){
			for(int i=0;i<M;i++){
				int current = probe(home, i);
				if(hashtable[current]!=null && hashtable[current].getFirst()!=null && hashtable[current].getFirst().equals(key)){
					hashtable[current].setSecond(value);
					return;
				}
			}
		}else{
			for(int i=0;i<M;i++){
				int current = probe(home, i);
				Collisions++;
				if(hashtable[current]==null || hashtable[current].getFirst()==null){
					Collisions--;
					hashtable[current] = p;
					size++;
					if(MaxSize<size)MaxSize = size;
					keys.add(key);
					return;
				}
			}
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
		int home = key.hashCode()%M;
		if(home<0)home *= -1;
		for(int i=0;i<M;i++){
			int current = probe(home, i);
			if(hashtable[current]==null)return null;
			if(hashtable[current].getFirst()!=null && hashtable[current].getFirst().equals(key)){
				return hashtable[current].getSecond();
			}
		}
		return null;
	}
	
	/***
	*This method deletes the entry whose
	*key equals key
	* 
	* @param key is the key to which we want to delete the entry
	**/
	@Override
	public void delete(K key) {
		int home = key.hashCode()%M;
		if(home<0)home *= -1;
		for(int i=0;i<M;i++){
			int current = probe(home, i);
			if(hashtable[current]==null)return;
			if(hashtable[current].getFirst()!=null && hashtable[current].getFirst().equals(key)){
				hashtable[current].setFirst(null);
				hashtable[current].setSecond(null);
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
		int home = key.hashCode()%M;
		if(home<0)home *= -1;
		for(int i=0;i<M;i++){
			int current = probe(home, i);
			if(hashtable[current]==null)return false;
			if(hashtable[current].getFirst()!=null && hashtable[current].getFirst().equals(key)){
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
		if (size==0) {
			return true;
		}
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
		M*=2;
		size = 0;
		Pair<K, V>[] temp = hashtable;
		hashtable = new Pair[M];
		for(int i=0;i<temp.length;i++){
			if(temp[i]!=null && temp[i].getFirst()!=null){
				put(temp[i].getFirst(), temp[i].getSecond());
			}
		}
	}


	/**
	 *This method gets the next candidate position for this key
	 * 
	 * @param home the initial place that the key ought to be placed
	 * @param idx is the counter of the trial to put the key in the hashtable
	 * 
	 * @return the index that should be checked next for the given key
	 **/
	public int probe(int home,int idx){
		return ((home+(((idx*idx)+idx))/2)%M);
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
