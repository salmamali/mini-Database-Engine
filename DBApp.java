import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DBApp {
	Hashtable<String, String> tableHash = new Hashtable<String, String>();// table

	public void init() throws IOException {
		FileReader file = new FileReader("metadata.csv");
		BufferedReader br = new BufferedReader(file);
		String currentLine = "";
		String currentTableName = "";
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		ArrayList<String> ind = new ArrayList<String>();
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
				t.indices = ind;
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(new File(t.strTableName + ".ser")));
				oos.writeObject(t);
				oos.close();
			}

			htblColNameType.put(x[1], x[2]);
			if (!x[5].equals("null")) {
				htblColNameRefs.put(x[1], x[5]);
			}
			if (x[3].equalsIgnoreCase("true")) {
				strKeyColName = x[1];
				ind.add(x[1]);
			}
			if (x[4].equalsIgnoreCase("true")) {
				ind.add(x[1]);

			}

		}
		br.close();
	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws Exception {// ,DBAppException

		Table t = new Table(strKeyColName, htblColNameType, htblColNameRefs,
				strKeyColName);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(strTableName + ".ser")));
		oos.writeObject(t);
		oos.close();
		createIndex(strTableName, strKeyColName);
		try {
			FileWriter writer = new FileWriter("metadata.csv");

			Set<Entry<String, String>> set1 = htblColNameType.entrySet();
			Iterator<Entry<String, String>> iterator1 = set1.iterator();
			while (iterator1.hasNext()) {
				Entry<String, String> en = iterator1.next();
				writer.append(strTableName);
				writer.append(en.getKey());
				writer.append(en.getValue());
				if (strKeyColName.equals(en.getKey())) {
					writer.append("True");
				} else {
					writer.append("False");
				}
				writer.append("False");
				if (htblColNameRefs.contains(en.getKey())) {
					writer.append(htblColNameRefs.get(en.getKey()));
				} else {
					writer.append("null");
				}
				writer.append("\n");
			}

			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createIndex(String strTableName, String strColName)
			throws Exception {
		// throws DBAppException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(strTableName + ".ser")));
		Table t = (Table) ois.readObject();
		ois.close();
		ExtensibleHash hash = t.getUpdatedExtensibleHash(strColName);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(t.strTableName + "," + strColName + ".ser")));
		oos.writeObject(hash);
		oos.close();

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

			binaryIndex += Integer.toBinaryString(b);
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
	// public static void main(String[] args) {
	// Hashtable<String, String> h = new Hashtable<String,String>();
	// h.put("1", "salma");
	// h.put("2", "ereeny");
	// h.put("3", "ruba");
	// Set<Entry<String,String>> s = h.entrySet();
	// Iterator<Entry<String,String>> e = s.iterator();
	// while(e.hasNext()){
	// Entry<String, String> en = e.next();
	// System.out.println(en.getKey());
	// System.out.println(en.getValue());
	// }
	// }

}
