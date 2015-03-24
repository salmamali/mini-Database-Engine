import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Table implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> pagesName = new ArrayList<String>();
	ArrayList<Page> pages = new ArrayList<Page>();
	String currentPage = "0";
	String strTableName;
	Hashtable<String, String> htblColNameType;
	Hashtable<String, String> htblColNameRefs;
	String strKeyColName;

	public Table(String tblName) {
		strTableName = tblName;
		htblColNameType = new Hashtable<String, String>();
		htblColNameRefs = new Hashtable<String, String>();
	}

	public void insert(Hashtable<String, String> htblColNameValue)
			throws Exception {
		updatePages();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(currentPage + ".ser")));
		Page p = (Page) ois.readObject();
		ois.close();
		p.insert(htblColNameValue);
	}

	public void updatePages() throws Exception {
		Page p;
		if (pages.isEmpty()) {
			p = new Page(currentPage, htblColNameType.size());
			pagesName.add(currentPage);
			pages.add(p);

		} else {
			p = pages.get(Integer.parseInt(currentPage));
			if (p.exceedLimit()) {
				p = new Page(currentPage, htblColNameType.size());
				pagesName.add(currentPage);
				pages.add(p);
				currentPage = (Integer.parseInt(currentPage) + 1) + "";

			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(currentPage + ".ser")));
		oos.writeObject(p);
		oos.close();
		currentPage = (Integer.parseInt(currentPage) + 1) + "";
	}
}
