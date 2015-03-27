import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.omg.CORBA.StructMemberHelper;

public class Table implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> pagesName = new ArrayList<String>();
	transient ArrayList<Page> pages = new ArrayList<Page>();
	String currentPage = "0";
	String strTableName;
	ArrayList<String> indices = new ArrayList<String>();
	Hashtable<String, String> htblColNameType;
	Hashtable<String, String> htblColNameRefs;
	String strKeyColName;

	public Table(String strTableName,
			Hashtable<String,String> htblColNameType,
			Hashtable<String,String>htblColNameRefs,
			String strKeyColName) {
		this.strKeyColName = strKeyColName;
		this.strTableName = strTableName;
		this.htblColNameRefs = htblColNameRefs;
		this.htblColNameType = htblColNameType;
	}
	public Table(String tblName){
		strTableName = tblName;
	}
	public Page loadPage(String filename) throws Exception{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(filename)));
		Page p = (Page) ois.readObject();
		ois.close();
		return p;
		
	}

	public void insert(Hashtable<String, String> htblColNameValue)
			throws Exception {
		updatePages();
		Page p = loadPage(currentPage+".ser");
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
				currentPage = (Integer.parseInt(currentPage) + 1) + "";
				p = new Page(currentPage, htblColNameType.size());
				pagesName.add(currentPage);
				pages.add(p);

			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(currentPage + ".ser")));
		oos.writeObject(p);
		oos.close();
	}
	public ExtensibleHash getUpdatedExtensibleHash(String columnName)throws Exception{
		ExtensibleHash h = new ExtensibleHash();
		indices.add(columnName);
		Enumeration<String> en = htblColNameType.keys();
		int count = 0;
		int columnNo = 0;
		while(!en.hasMoreElements()){
			if(en.nextElement().equals(columnName)){
				columnNo = htblColNameType.size()-1-count;
			}
			count++;
		}
		for(int i = 0; i< pages.size();i++){
			Page p=pages.get(i);
			for(int j = 0; j<200;j++){
			String index = p.tuples[columnNo][j]+","+i+j;
			h.insertIndex(index);
			}
			
		}
		return h;
	}
}
