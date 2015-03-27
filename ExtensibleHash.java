import java.util.ArrayList;
import java.util.Hashtable;

public class ExtensibleHash {

	ArrayList<String> prefix = new ArrayList<String>();
	ArrayList<Bucket> buckets = new ArrayList<Bucket>();
	Hashtable<String, Bucket> maps = new Hashtable<String, Bucket>();
	final int BUCKET_SIZE = 3;

	public ExtensibleHash() {
		prefix.add("0");
		prefix.add("1");
		buckets.add(new Bucket());
		buckets.add(new Bucket());
	}
	public void hash(){
		maps.clear();
		for(int i = 0; i< prefix.size(); i++){
			String key = prefix.get(i);
			maps.put(key, buckets.get(i));
		}
	}

	public void insertIndex(String index) {
		boolean inserted = false;
		for (int i = 0; i < prefix.size() && !inserted; i++) {
			if (prefix.get(i)
					.equals(index.substring(0, prefix.get(i).length()))) {
				if (buckets.get(i).bucket.size() < BUCKET_SIZE) {
					buckets.get(i).bucket.add(index);
					inserted = true;
				} else {
					elongate(i);
					refresh(index);
					break;
				}
			}
		}
		hash();
	}

	public void elongate(int prefixToElongate) {
		ArrayList<String> newPrefix = new ArrayList<String>();
		ArrayList<Bucket> newBuckets = new ArrayList<Bucket>();
		for (int i = 0; i < prefix.size(); i++) {
			if (i == prefixToElongate) {
				String pre = prefix.get(i);
				newPrefix.add(pre + "0");
				newPrefix.add(pre + "1");
				newBuckets.add(buckets.get(i));
				newBuckets.add(new Bucket());
			} else {
				newPrefix.add(prefix.get(i));
				newBuckets.add(buckets.get(i));
			}
		}
		prefix = newPrefix;
		buckets = newBuckets;

	}
	public void delete(String index){
		boolean found = false;
		String key = index+" ";	
		while(!found&&key.length()>0){
			key = key.substring(0,key.length()-1);
			System.out.println(key);
			if(maps.containsKey(key)){
				Bucket b = maps.get(key);
				b.bucket.remove(index);
				found = true;
			}
		
		}
	}

	public void refresh(String index) {
		ArrayList<String> indices = new ArrayList<String>();
		for (int i = 0; i < prefix.size(); i++) {
			int j = 0;
			while (j < buckets.get(i).bucket.size()) {
				
				if (!prefix.get(i).equals(
						buckets.get(i).bucket.get(j).substring(0,
								prefix.get(i).length()))) {
					indices.add(buckets.get(i).bucket.get(j));
					buckets.get(i).bucket.remove(j);
					j = 0;
				}else j++;
			}
		}
		while (!indices.isEmpty()) {
			insertIndex(indices.remove(0));
		}
		insertIndex(index);
	}
//main method to test insertion
	public static void main(String[] args) {
		ExtensibleHash t = new ExtensibleHash();
		t.insertIndex("0000");
		t.insertIndex("0001");
		t.insertIndex("0010");
		t.insertIndex("0011");
		t.insertIndex("0100");
		t.insertIndex("0101");
		t.insertIndex("0110");
		t.insertIndex("0111");
		t.insertIndex("1000");
		t.insertIndex("1001");
		t.insertIndex("1010");
		t.insertIndex("1011");
		t.insertIndex("1100");
		t.insertIndex("1101");
		t.insertIndex("1110");
		t.insertIndex("1111");

//		for (int i = 0; i < t.prefix.size(); i++) {
//			System.out.println("      ---");
//			if (t.buckets.get(i).bucket.size() > 0) {
//				System.out.println("     " + t.buckets.get(i).bucket.get(0));
//			} else
//				System.out.println("     null");
//			System.out.println("     ---");
//			if (t.buckets.get(i).bucket.size() > 1) {
//				System.out.println(t.prefix.get(i)+"->" + t.buckets.get(i).bucket.get(1));
//			} else
//				System.out.println("     null");
//			System.out.println("      ---");
//			if (t.buckets.get(i).bucket.size() > 2) {
//				System.out.println("     " + t.buckets.get(i).bucket.get(2));
//			} else
//				System.out.println("     null");
//			System.out.println("      ---");
//
//			System.out.println();
//
//		}
		t.delete("0110");
		t.delete("1110");
		System.out.println(t.maps.toString());
	}
}
