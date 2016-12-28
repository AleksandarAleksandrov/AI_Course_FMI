package algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import model.Attributes;
import model.RowModel;
import model.TreeNode;

public class ID3 {
	
	/**
	 * Reads the data from the file. 
	 * Separates the data into learning and testing set. 
	 * Build an ID3 and uses the testing set on it.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void doID3fromFile() throws FileNotFoundException, IOException {
		// all the data in the file
		List<RowModel> allData = new ArrayList<>();
		// have we reached the reading point for data
		boolean startDataReading = false;
		int attrCounter = 0;
		try (BufferedReader br = new BufferedReader(new FileReader("breast-cancer.arff"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// start reading the row data
				if(startDataReading) {
					allData.add(createRowModel(line));
					continue;
				}				
				// read the attributes data
				if(line.startsWith("@attribute")) {
					addToAttributes(line,attrCounter);
					attrCounter++;
				} else if(line.equals("@data")) {
					startDataReading = true;
				}
			}
		}	
		
		
		// create the testing and learning sets
		int oneTenth = allData.size() / 10;

		Set<Integer> chosen = new HashSet<>();
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		int counter = 0;
		List<RowModel>testingData =  new ArrayList<>();
		while (counter < oneTenth) {
			int nextIndex = tlr.nextInt(allData.size());
			if (!chosen.contains(nextIndex)) {
				testingData.add(allData.get(nextIndex));
				chosen.add(nextIndex);
				counter++;
			}
		}
		List<RowModel> learningData = new ArrayList<>();
		for (int i = 0; i < allData.size(); i++) {
			if (!chosen.contains(i)) {
				learningData.add(allData.get(i));
			}
		}
		
		TreeNode root = prepareRoot(learningData.size());
		constructID3(root, learningData, Attributes.COLUMNS.keySet());
		//System.out.println(Attributes.prettyString());
		System.out.println(root.prettyString());
		
		// at this point the root contains all the necessary data
		int guessed = 0;
		for(RowModel rm : testingData) {
			Set<String> guess = TreeNode.classify(root, rm);
			if(guess != null && guess.contains(rm.clazz)) {
				guessed++;
			}
			System.out.println(rm);
			System.out.println(guess);
		}
		
		System.out.printf("Total accuracy is %f %% \n", 100.0 * guessed / testingData.size());
	}
	
	/**
	 * Create the root node. This just sets the size of the data set.
	 * @param dataSize
	 * @return
	 */
	private TreeNode prepareRoot(int dataSize) {
		TreeNode root = new TreeNode();
		root.dataCount = dataSize;
		return root;
	}
	
	private void constructID3(TreeNode node, List<RowModel> allData, Set<String> attributes) {
		
		// stop the creation if the node is a leaf
		if(node.isLeaf) return;
		
		// this new node will be used 
		// to determine the best node attributes
		// that the current node should have
		// including it's children
		TreeNode currBest = new TreeNode();
		currBest.entropy = Double.POSITIVE_INFINITY;
		for(String attr: attributes) {
			// skip the class attribute we
			if(attr.equals("Class")) continue;
			// set the data count (used for the entropy sum)
			TreeNode tn = new TreeNode();
			tn.dataCount = node.dataCount;
			tn.fatherCount = node.fatherCount;
			tn.level = node.level;
			// calculate the node data for the current attribute
			calculateNode(attr, allData, tn, attributes);
			// is this the best ?
			if(tn.entropy < currBest.entropy) {
				currBest = tn;
			}
		}
		
		copyDataBetweenNodes(node, currBest);
		
		// make a new attribute set that doesn't contain this 
		// the currently selected attribute
		Set<String> removedAttr = new HashSet<>();
		for(String s : attributes) {
			if(!s.equals(node.attribute)) {
				removedAttr.add(s);
			}
		}
		
		// find the best node for the kids
		for(TreeNode tn : node.children) {
			constructID3(tn, tn.nodeData, removedAttr);
		}
	}

	private void copyDataBetweenNodes(TreeNode node, TreeNode currBest) {
		node.attribute = currBest.attribute;
		node.children = currBest.children;
		node.nodeData = currBest.nodeData;
		node.dataCount = currBest.dataCount;
		node.entropy = currBest.entropy;
		node.fatherCount = currBest.fatherCount;
		node.isLeaf = currBest.isLeaf;
		node.level = currBest.level;
		node.clazz = currBest.clazz;
		node.parent = currBest.parent;
	}
	
	/**
	 * Calculates the entropy of a single node, given the attribute.
	 * @param attr
	 * @param allData
	 * @param tn
	 */
	private void calculateNode(String attr, List<RowModel> allData, TreeNode tn, Set<String> attrs) {
		
		tn.attribute = attr;
		tn.nodeData = allData;
		
		// get the index for the attribute and create the sub data map
		Integer attrIndx = Attributes.COLUMN_INDEXES.get(attr);
		Map<String, List<RowModel>> subData = new HashMap<>();
		
		// populate the map
		for(RowModel rm : allData) {
			String attrValue = rm.attributes.get(attrIndx);
			subData.putIfAbsent(attrValue, new ArrayList<>());
			subData.get(attrValue).add(rm);
		}
		
		// for every possible path for the attribute
		// calculate the entropy
		for(Map.Entry<String, List<RowModel>> entry : subData.entrySet()) {
			TreeNode temp = new TreeNode();
			temp.fatherCount = tn.dataCount;
			temp.pathFromParent = entry.getKey();
			temp.dataCount = entry.getValue().size();
			temp.nodeData = entry.getValue();
			temp.entropy = calculateEntropy(entry.getValue());
			temp.level = tn.level+1;
			// set the data for the leaf
			// this means all the data at the leaf is homogeneous
			// the check for 2 is because we may have the same values for the attributes,
			// but the classes can be different. This is basically bad data.
			if(Double.doubleToRawLongBits(temp.entropy) == 0L || attrs.size() == 2) {
				temp.isLeaf = true;
				for(RowModel rm : temp.nodeData) {
					temp.clazz.add(rm.clazz);
				}
				temp.clazz.add(temp.nodeData.get(0).clazz);
			}
			temp.parent = tn;
			tn.children.add(temp);
		}
		
		// calculate the total entropy of the node using it's children
		tn.entropy = 0;

		for(TreeNode t : tn.children) {
			tn.entropy += t.entropy * t.dataCount / t.fatherCount;
		}	
	}
	
	/**
	 * Calculates the entropy based on the homogeneousness of the data. 
	 * Thermodynamics is AWESOME.
	 * @param data
	 * @return
	 */
	private double calculateEntropy(List<RowModel> data) {
		if(data.isEmpty()) {
			return 0;
		}
		double sum = 0;
		
		Map<String, Integer> differenceCount = new HashMap<>();
		
		for(RowModel rm : data) {
			differenceCount.putIfAbsent(rm.clazz, 0);
			differenceCount.put(rm.clazz, differenceCount.get(rm.clazz) + 1);
		}
		
		if(differenceCount.size() == 1) return 0;
		
		// the magic formula :)
		for(Map.Entry<String, Integer> entry : differenceCount.entrySet()) {
			double clazz = 1.0 * entry.getValue() / data.size();			
			sum -= clazz * Math.log(clazz)/Math.log(2);
		}
		
		return sum;
	}
	
	
	/**
	 * Does the data parsing for the attributes data.
	 * @param line
	 * @param attrCount
	 */
	private void addToAttributes(String line, int attrCount) {
		String[] split = line.split("\\s+");
		String attrName = split[1].replace("'", "");
		String[] attrClasses = split[2].replace("{", "").replace("}", "").replace("'", "").split(",");
		Attributes.COLUMNS.put(attrName, Arrays.asList(attrClasses));
		Attributes.COLUMN_INDEXES.put(attrName, attrCount);
	}
	
	/**
	 * Parse the data for a row and construct the object.
	 * @param line
	 * @return
	 */
	private RowModel createRowModel(String line) {
		String[] data = line.replace("'", "").split(",");
		RowModel rm = new RowModel(data);
		return rm;
	}

}
