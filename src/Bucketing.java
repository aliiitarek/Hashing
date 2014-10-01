import java.util.ArrayList;

public class Bucketing<K, V> implements HashTable<K, V> {

	//instance variables
	int M, Bucket, size, Collisions,MaxSize;
	Pair<K, V>[] hashtable;
	ArrayList< Pair<K, V> > overflow;
	ArrayList<K> keys;
	float load_factor;
	
	//Constructor
	public Bucketing(){
		M = 10;
		Bucket = 5;
		size = MaxSize = 0;
		Collisions = 0;
		hashtable = new Pair[M];
		overflow = new ArrayList<>();
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
		if((((float)load_factor)<(float)((float)size/(float)M))){
			rehash();
		}
		int index = key.hashCode()%M;
		if(index<0)index *= -1;
		Pair<K, V> p = new Pair<K, V>(key, value);
		if(contains(key)){
			for(int i=0;i<Bucket;i++){
				if((index+1)%Bucket==0)index = index-Bucket+1;
				if(hashtable[index]!=null && hashtable[index].getFirst()!=null && hashtable[index].getFirst().equals(key)){
					hashtable[index].setSecond(value);
					return;
				}
				index++;
			}
			for(int i=0;i<overflow.size();i++){
				if (overflow.get(i).getFirst().equals(key)) {
					overflow.get(i).setSecond(value);
					return;
				}
			}
		}else{
			size++;
			if(MaxSize<size)MaxSize = size;
			keys.add(key);
			for(int i=0;i<Bucket;i++){
				Collisions++;
				if((index+1)%Bucket==0)index = index-Bucket+1;
				if(hashtable[index]==null || hashtable[index].getFirst()==null){
					Collisions--;
					hashtable[index] = p;
					return;
				}
				index++;
			}
			overflow.add(overflow.size(), p);
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

		for(int i=0;i<Bucket;i++){
			if((index+1)%Bucket==0)index = index-Bucket+1;
			if(hashtable[index]==null)return null;
			if(hashtable[index].getFirst()!=null && hashtable[index].getFirst().equals(key))
				return hashtable[index].getSecond();
			index++;
		}
		for(int i=0;i<overflow.size();i++){
			if (overflow.get(i).getFirst().equals(key)) {
				return overflow.get(i).getSecond();
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
		int index = key.hashCode()%M;
		if(index<0)index *= -1;
		for(int i=0;i<Bucket;i++){
			if((index+1)%Bucket==0)index = index-Bucket+1;
			if(hashtable[index]==null)return;
			if(hashtable[index].getFirst()!=null && hashtable[index].getFirst().equals(key)){
				hashtable[index].setFirst(null);
				hashtable[index].setSecond(null);
				keys.remove(key);
				size--;
				return;
			}
			index++;
		}
		for(int i=0;i<overflow.size();i++){
			if (overflow.get(i).getFirst().equals(key)) {
				overflow.remove(i);
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
		for(int i=0;i<Bucket;i++){
			if((index+1)%Bucket==0)index = index-Bucket+1;
			if(hashtable[index]==null)return false;
			if(hashtable[index].getFirst()!=null && hashtable[index].getFirst().equals(key)){
				return true;
			}
			index++;
		}
		for(int i=0;i<overflow.size();i++){
			if (overflow.get(i).getFirst().equals(key)) {
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
		Pair<K, V>[] tem = hashtable;
		hashtable = new Pair[M];
		ArrayList< Pair<K, V> > tempover = overflow;
		overflow = new ArrayList<>();
		int loop = M/2;
		size = 0;
		for(int i=0;i<loop;i++){
			if(tem[i]!=null && !tem[i].getFirst().equals(null))put(tem[i].getFirst(), tem[i].getSecond());
		}
		for(int i=0;i<tempover.size();i++){
			put(tempover.get(i).getFirst(), tempover.get(i).getSecond());
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
