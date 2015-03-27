import java.util.ArrayList;
import java.util.Arrays;

class Node {
	String key;
	Node left;
	Node right;
	Node parent;
	Bucket leftBucket;
	Bucket rightBucket;
	int level;

	public Node() {

	}

	public Node(String key) {
		this.key = key;
	}

}


public class KDTree {
    String name;
	Node root;
	String key1Name;
	String key2Name;
	int max = 2;
	int columnNumberOfKey1 = 0; // assign it to the column number of key one(the
								// Table has a hashtable of columns, this
								// variable
								// is the index of the column that we build the
								// index on )
	int columnNumberOfKey2 = 0; // like the previous variable
	Table table; // assign it to the table you build the kd on

	public KDTree(int maxPerBucket, String name) {
		this.name = name;
		key1Name = "";
		key2Name = "";
		this.root = new Node();
		root.level = 1;
		root.parent = null;
		root.leftBucket = new Bucket();
		root.rightBucket = new Bucket();
		this.max = maxPerBucket;
	}

	public KDTree(Node root) {
		this.root = root;
	}

	public void put(String k1, String k2, String value) {
		if (root.key == null) {
			root.key = k1;
			root.leftBucket.put(value);

		} else {
			int level = 1;
			Node current = root;
			int cmp = 0;
			Node parent = null;
			while (current != null) {
				if (level % 2 == 1) {
					cmp = k1.compareTo(current.key);
				} else {
					cmp = k2.compareTo(current.key);
				}
				parent = current;
				if (cmp >= 0) {
					current = current.left;
				} else {
					current = current.right;
				}
				level = 1 - level;
			}

			if (cmp >= 0) {
				if (parent.leftBucket == null) {
					parent.leftBucket = new Bucket();
					parent.leftBucket.put(k1+','+k2+','+value);
				} else {
					Bucket tempbucket = parent.leftBucket;
					if (tempbucket.bucket.size() < this.max) {
						parent.leftBucket.put(k1+','+k2+','+value);
					} else {
						Node tempNode = new Node();
						tempNode.parent = parent;
						parent.left = tempNode;
						int parentLevel = parent.level;
						tempNode.level = 1 - parentLevel;
						ArrayList<String> a = loadAllbucketInBucket(tempbucket);
						parent.leftBucket = null;
						tempNode.leftBucket = new Bucket();
						tempNode.rightBucket = new Bucket();
						for (String s : a) {
							// redistribute the original bucket on left and
							// right buckets of the new node
							String[] listOfbucketInRecord = s.split(",");
							String compareVal = "";
							int compareIndex = 0;
							if (tempNode.level % 2 == 1) { // compare k1
								compareVal = getAvg(a, 1);
								compareIndex = columnNumberOfKey1;
							} else { // compare k2
								compareVal = getAvg(a, 2);
								compareIndex = columnNumberOfKey2;
							}
							tempNode.key = compareVal;
							if (listOfbucketInRecord[compareIndex]
									.compareTo(tempNode.key) >= 0) {
								tempNode.leftBucket
										.put(listOfbucketInRecord[compareIndex]);
							} else {
								tempNode.rightBucket
										.put(listOfbucketInRecord[compareIndex]);
							}

						}
						// int cmp2 = 0;
						// if (tempNode.level % 2 == 1) {
						// cmp2 = k1.compareTo(tempNode.key);
						// }
						// else {
						// cmp2 = k2.compareTo(tempNode.key);
						// }
						// if (cmp2 >= 0) {
						// tempNode.leftBucket.put(value);
						// }
						// else {
						// tempNode.leftBucket.put(value);
						// }
						//
					}
				}
			} else {
				if (parent.rightBucket == null) {
					parent.rightBucket = new Bucket();
					parent.rightBucket.put(k1+','+k2+','+value);
				} else {

					Bucket tempbucket = parent.rightBucket;
					if (tempbucket.bucket.size() < this.max) {
						parent.rightBucket.put(k1+','+k2+','+value);
					} else {
						Node tempNode = new Node();
						tempNode.parent = parent;
						parent.right = tempNode;
						int parentLevel = parent.level;
						tempNode.level = 1 - parentLevel;
						ArrayList<String> a = loadAllbucketInBucket(tempbucket);
						parent.rightBucket = null;
						tempNode.leftBucket = new Bucket();
						tempNode.rightBucket = new Bucket();
						for (String s : a) {
							// redistribute the original bucket on left and
							// right buckets of the new node
							String[] listOfbucketInRecord = s.split(",");
							String compareVal = "";
							int compareIndex = 0;
							if (tempNode.level % 2 == 1) { // compare k1
								compareVal = getAvg(a, 1);
								compareIndex = columnNumberOfKey1;
							} else { // compare k2
								compareVal = getAvg(a, 2);
								compareIndex = columnNumberOfKey2;
							}
							tempNode.key = compareVal;
							if (listOfbucketInRecord[compareIndex]
									.compareTo(tempNode.key) >= 0) {
								tempNode.leftBucket
										.put(listOfbucketInRecord[compareIndex]);
							} else {
								tempNode.rightBucket
										.put(listOfbucketInRecord[compareIndex]);
							}

						}
						int cmp2 = 0;
						if (tempNode.level % 2 == 1) {
							cmp2 = k1.compareTo(tempNode.key);
						} else {
							cmp2 = k2.compareTo(tempNode.key);
						}
						if (cmp2 >= 0) {
							tempNode.leftBucket.put(k1+','+k2+','+value);
						} else {
							tempNode.leftBucket.put(k1+','+k2+','+value);
						}
					}

				}

			}
		}
	}

	// Remove methode
	public void remove(String key1, String key2) {
		if (root.key == key1){
			if (root.leftBucket != null)
				root.leftBucket.remove(key1+','+key2);
		}
				if(root.key == key2) {
			if (root.leftBucket != null) {
				root.leftBucket.remove(key1+','+key2);
			}
		} else {
 // how will i define which Dimension is it ?
			int cmp = 0;
			Node current = root;
			int level=1;
			while (current != null) {
				if (current.leftBucket != null) {
					current.leftBucket.remove(key1+','+key2);
					break;
				}
				if (current.rightBucket != null) {
					current.rightBucket.remove(key1+','+key2);
					break;
				}
				if(level%2 == 1)
				cmp = key1.compareTo(current.key);
				else 
				cmp = key2.compareTo(current.key);	
				if (cmp >= 0) {
					current = current.left;
					if (current.leftBucket != null) {
							current.leftBucket.remove(key1+','+key2);
							break;
						}
				} else {
					current = current.right;
					if (current.rightBucket != null) {
							current.rightBucket.remove(key1+','+key2);
							break;
						}
				}
				level++;
			}
		}
	}

	// get the average value in the bucket to be the new key
	public String getAvg(ArrayList<String> a, int keyNumber) {
		String avg = "";
		String[] toBeSorted = new String[a.size()];
		int index = 0;
		if (keyNumber == 1) {
			index = columnNumberOfKey1;
		} else {
			index = columnNumberOfKey2;
		}
		int i = 0;
		for (String string : a) {
			String[] columnbucket = string.split(",");
			toBeSorted[i] = columnbucket[index];
			i++;
		}
		Arrays.sort(toBeSorted);
		avg = toBeSorted[a.size() / 2];
		return avg;
	}

	// The bucket stores references to the records, so we need to get the record
	// to compare them to redistribute the records in new buckets after
	// resizing occurs
	public ArrayList<String> loadAllbucketInBucket(Bucket b) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> valInBucket = b.bucket;
		for (String string : valInBucket) {
			String[] pos = string.split(",");
			String tableName = pos[0];
			String pageNumber = pos[1];
			String recordNumber = pos[2];
			// load The page to get the record and add it to the result
			Page p = this.table.pages.get(Integer.parseInt(pageNumber));
			String[] r = p.tuples[Integer.parseInt(recordNumber)];
			String r1 = "";
			for (int i = 0; i < r.length; i++) {
				r1 += r[i] + ",";
			}
			r1 = r1.substring(0, r1.length() - 1);
			result.add(r1);
		}
		return result;
	}

	public ArrayList<String> get(String k1, String k2) {
		ArrayList<String> result = new ArrayList<String>();
		if (root.key == null) {
			return result;
		} else {
			int level = 1;
			Node current = root;
			int cmp = 0;
			Node parent = null;
			while (current != null) {
				if (level % 2 == 1) {
					cmp = k1.compareTo(current.key);
				} else {
					cmp = k2.compareTo(current.key);
				}
				parent = current;
				if (cmp >= 0) {
					current = current.left;
				} else {
					current = current.right;
				}
				level = 1 - level;
			}
			// check on number of bucket per bucket

			if (cmp >= 0) {
				if (parent.leftBucket == null) {
					parent.leftBucket.bucket = new ArrayList<String>();
				}
				return parent.leftBucket.bucket;
			} else {
				if (parent.rightBucket == null) {
					parent.rightBucket.bucket = new ArrayList<String>();
				}
				return parent.rightBucket.bucket;
			}
		}
	}

	public static void main(String[] args) {
		KDTree k = new KDTree(3,"name");
		k.put("mariam", "25", "Employee,2,4");
		k.put("sara", "21", "Employee,4,6");
	    k.put("ssara", "22", "Employee,4,5");
		//k.put("sssara", "22", "Employee,4,5");
		k.put("alaa", "24", "Employee,2,5");
		k.columnNumberOfKey1 = 3;
		k.columnNumberOfKey2 = 4;
		// ArrayList<String> tempRecords = new ArrayList<String>();
		// tempRecords.add("1,0,1,aariam,25");
		// tempRecords.add("1,0,2,sara,41");
		// tempRecords.add("1,0,3,aager,35");
		// tempRecords.add("1,0,4,yara,21");
		// System.out.println(k.getAvg(tempRecords, 2));
		System.out.println(k.root.rightBucket.bucket.get(0));
		//System.out.println(k.root.left.leftBucket.bucket.get(0));
		//System.out.println(k.root.left.leftBucket.bucket.get(0));
	}

}
