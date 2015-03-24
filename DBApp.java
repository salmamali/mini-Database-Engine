import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DBApp {

	// public void init();
	// Hashtahtable<String, kdTree>

	public void createTable() throws IOException, Exception {// ,DBAppException

		FileReader file = new FileReader("metadata.csv");
		BufferedReader br = new BufferedReader(file);
		String currentLine = "";
		String currentTableName = "";
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		String strKeyColName = "";
		while ((currentLine = br.readLine()) != null) {
			String[] x = currentLine.split(",");
			if (currentTableName.length() == 0) {
				currentTableName = x[0];
			} else if (!currentTableName.equals(x[0])) {
				currentTableName = x[0];
				Table t = new Table(currentTableName);
				t.htblColNameRefs = htblColNameRefs;
				t.htblColNameType = htblColNameType;
				t.strKeyColName = strKeyColName;
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(new File(t.strTableName + ".ser")));
				oos.writeObject(t);
				oos.close();
			}

			htblColNameType.put(x[1], x[2]);
			if (x[5] != null) {
				htblColNameRefs.put(x[1], x[5]);
			}
			if (x[3].equalsIgnoreCase("true")) {
				strKeyColName = x[1];
			}

		}
	}

	public void createIndex(String strTableName, String strColName)
			throws IOException, ClassNotFoundException {
		// throws DBAppException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(strTableName + ".ser")));
		Table t = (Table) ois.readObject();
		ois.close();
		ExtensibleHash hash = new ExtensibleHash();

	}

	public void createMultiDimIndex(String strTableName,
			Hashtable<String, String> htblColNames) {
		// throws DBAppException{
	}

	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws Exception

	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(strTableName + ".ser")));
		Table t = (Table) ois.readObject();
		ois.close();
		t.insert(htblColNameValue);
		String index = htblColNameValue.get(t.strKeyColName);
		String binaryIndex = ""; // index to be used in the extensible hashtable
									// has to be in binary for our
									// implementation to work
		byte[] infoBin = index.getBytes("UTF-8");
		for (byte b : infoBin) {

			binaryIndex+=Integer.toBinaryString(b);
		}
	}

	public void deleteFromTable(String strTableName,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws Exception

	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(strTableName + ".ser")));
		Table t = (Table) ois.readObject();
		ois.close();
	}

	// public Iterator selectFromTable(String strTable,
	// Hashtable<String,String> htblColNameValue,
	// String strOperator)
	// //throws DBEngineException
	// {
	//
	// }
	public void saveAll() // throws DBEngineException
	{

	}

}
