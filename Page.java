import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String pageNo; // no of the page
	String[][] tuples; // 2d array representing the tuples in the same order as
						// the columns in the hashtable given when inserting
	int currentLine = 0; //specifies which line in the page is empty now 

	public Page(String pageNo, int columnNo) {
		this.pageNo = pageNo;
		tuples = new String[columnNo][200];//assume 200 max no of tuples for now
	}

	public void insert(Hashtable<String, String> htblColNameValue){
		Enumeration<String> en = htblColNameValue.elements();
		int i = tuples.length -1;
		while(en.hasMoreElements()){
			tuples[i][currentLine] = en.nextElement();
			i--;
		}
	}
	public boolean exceedLimit(){
		if(tuples[0].length==199)
			return true;
		return false;
	}
	
//	public static void main(String[] args) {
//		Page p = new Page("0", 4);
//		Hashtable<String,String> h = new Hashtable<String, String>();
//		h.put("id", "1");
//		h.put("department", "csen");
//		h.put("gpa", "0.95");
//		Enumeration<String> en = h.elements();
//        while(en.hasMoreElements()){
//		System.out.println(en.nextElement());
//        }
//	}
}
