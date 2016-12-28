package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.CustomUtils;

public class TreeNode {

	public double entropy;
	/**
	 * The count of RowModel data in this node.
	 */
	public int dataCount;
	/**
	 * The count of RowModel data in this node's father.
	 */
	public int fatherCount;
	public int level;
	public boolean isLeaf = false;
	public String attribute;
	/**
	 * The actual value of the attribute column.
	 */
	public String pathFromParent;
	public Set<String> clazz = new HashSet<>();
	public TreeNode parent;
	public List<TreeNode> children = new ArrayList<>();
	public List<RowModel> nodeData = new ArrayList<>();
	
	public static Set<String> classify(TreeNode node, RowModel rm) {
		
		if(node.isLeaf) return node.clazz;
		
		Integer attrIndx = Attributes.COLUMN_INDEXES.get(node.attribute);
		String attrValue = rm.attributes.get(attrIndx);
				
		for(TreeNode tn : node.children) {
			if(tn.pathFromParent.equals(attrValue)) {
				return classify(tn, rm);
			}
		}
		
		// If the data is really good should not reach here !
		// But it is not so ...
		return null;
	}
	
	public String prettyString() {
		StringBuilder builder = new StringBuilder();
		builder.append(CustomUtils.spaces(10 * level));
		builder.append("{level:").append(level).append("}--");
		builder.append("(PFP:").append(pathFromParent).append(")--");
		builder.append("[attribute:").append(attribute).append("] - |");
		
		if(!clazz.isEmpty()) {
			builder.append(clazz);
		}
		
		if(level == 0 || isLeaf) {
			builder.append(" entropy: " + entropy);
		}
		builder.append('\n');
		
		for(TreeNode tn : children) {
			builder.append(tn.prettyString());
		}

		return builder.toString();
	}
}
