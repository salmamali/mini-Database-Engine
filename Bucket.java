import java.util.ArrayList;


public class Bucket {

	ArrayList<String> bucket;
	public Bucket(){
		bucket  = new ArrayList<String>();
	}
	public String toString(){
		return "["+((bucket.size())>0?bucket.get(0):"")+","+((bucket.size())>1?bucket.get(1):"")+"]";
				
	}

		public void put(String val) {
		if (this.bucket == null) {
			this.bucket = new ArrayList<String>();
		}
		this.bucket.add(val);
	}
	
	public void remove(String val) {
		for (String string : bucket) {
			if (string.contains(val))
				bucket.remove(bucket.indexOf(string));
		}
	}
}
