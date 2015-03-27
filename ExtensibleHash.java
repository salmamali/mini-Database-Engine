import java.util.ArrayList;
import java.util.Hashtable;

public class ExtensibleHash {

	ArrayList<String> prefix = new ArrayList<String>();
	ArrayList<Bucket> buckets = new ArrayList<Bucket>();
	Hashtable<String, Bucket> maps = new Hashtable<String, Bucket>();
	final int BUCKET_SIZE = 2;

	public ExtensibleHash() {
		prefix.add("0");
		prefix.add("1");
		buckets.add(new Bucket());
		buckets.add(new Bucket());
	}
	
	private static String stringToBinary(String s) throws Exception{
		byte[] infoBin = null;
        infoBin = s.getBytes("UTF-8");
        String binary = "";
        for (byte b : infoBin) {
             binary +=Integer.toBinaryString(b);
             if ( binary.length() < 8 )
            	  binary = "0" + binary;
        }
        return binary;
    
	}
	public void hash(){
		maps.clear();
		for(int i = 0; i< prefix.size(); i++){
			String key = prefix.get(i);
			maps.put(key, buckets.get(i));
		}
	}

	public void insertIndex(String index) throws Exception{
		boolean inserted = false;
		String[] splitIndex = index.split(",");
		String bin = stringToBinary(splitIndex[0]);
		for (int i = 0; i < prefix.size() && !inserted; i++) {
			if (prefix.get(i)
					.equals(bin.substring(0, prefix.get(i).length()))) {
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
	public void delete(String index) throws Exception{
		boolean found = false;
		String key = stringToBinary(index)+" ";	
		while(!found&&key.length()>0){
			key = key.substring(0,key.length()-1);
			if(maps.containsKey(key)){
				Bucket b = maps.get(key);
				for(int j = 0; j<b.bucket.size();j++){
					String column = b.bucket.get(j).split(",")[0];
					if(column.equals(index)){
						b.bucket.remove(j);
					}
				}
			 	found = true;
			}
		
		}
	}
	public String search(String s) throws Exception{//get index of s
		//String bin = stringToBinary(s);
		boolean found = false;
		String key = stringToBinary(s)+" ";	
		while(!found&&key.length()>0){
			key = key.substring(0,key.length()-1);
			if(maps.containsKey(key)){
				Bucket b = maps.get(key);
				for(int j = 0; j<b.bucket.size();j++){
					String column = b.bucket.get(j).split(",")[0];
					if(column.equals(s)){
						return b.bucket.get(j);
					}
				}
			 	found = true;
			}
		
		}
		return "";
	}

	public void refresh(String index) throws Exception{
		ArrayList<String> indices = new ArrayList<String>();
		for (int i = 0; i < prefix.size(); i++) {
			int j = 0;
			while (j < buckets.get(i).bucket.size()) {
				String bin = stringToBinary(buckets.get(i).bucket.get(j));
				if (!prefix.get(i).equals(
						bin.substring(0,
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
	//main method to test string to binary converter
//	public static void main(String[] args) throws Exception {
//		String s = "2";
//		System.out.println(stringToBinary(s));
//	}
//main method to test insertion
//	public static void main(String[] args) throws Exception{
//		ExtensibleHash t = new ExtensibleHash();
//		t.insertIndex("0000");
//		t.insertIndex("0001");
//		t.insertIndex("0010,1");
//		t.insertIndex("0011");
////		t.insertIndex("0100");
////		t.insertIndex("0101");
////		t.insertIndex("0110");
////		t.insertIndex("0111");
////		t.insertIndex("1000");
////		t.insertIndex("1001");
////		t.insertIndex("1010");
////		t.insertIndex("1011");
////		t.insertIndex("1100");
////		t.insertIndex("1101");
////		t.insertIndex("1110");
////		t.insertIndex("1111");
//
////		for (int i = 0; i < t.prefix.size(); i++) {
////			System.out.println("      ---");
////			if (t.buckets.get(i).bucket.size() > 0) {
////				System.out.println("     " + t.buckets.get(i).bucket.get(0));
////			} else
////				System.out.println("     null");
////			System.out.println("     ---");
////			if (t.buckets.get(i).bucket.size() > 1) {
////				System.out.println(t.prefix.get(i)+"->" + t.buckets.get(i).bucket.get(1));
////			} else
////				System.out.println("     null");
////			System.out.println("      ---");
////			if (t.buckets.get(i).bucket.size() > 2) {
////				System.out.println("     " + t.buckets.get(i).bucket.get(2));
////			} else
////				System.out.println("     null");
////			System.out.println("      ---");
////
////			System.out.println();
////
////		}
//	//	t.delete("0110");
//		//t.delete("0010");
//		System.out.println(t.search("0010"));
//		System.out.println(t.maps.toString());
//	}
}
