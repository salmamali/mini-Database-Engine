import java.util.ArrayList;


public class Bucket {

	ArrayList<String> bucket;
	public Bucket(){
		bucket  = new ArrayList<String>();
	}
	public String toString(){
		return "["+((bucket.size())>0?bucket.get(0):"")+","+((bucket.size())>1?bucket.get(1):"")+"]";
				
	}
}
