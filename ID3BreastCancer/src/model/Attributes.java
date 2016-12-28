package model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.CustomUtils;

/**
 * Class that holds the attributes and classes data.
 */
public class Attributes {
	
	/**
	 * Holds the attributes and classes as keys and their allowed values.
	 */
	public static final Map<String, List<String>> COLUMNS = new LinkedHashMap<>();
	
	/**
	 * Holds the indexes of the attributes in the columns.
	 */
	public static final Map<String,Integer> COLUMN_INDEXES = new LinkedHashMap<>();
	
	public static String prettyString() {
		
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<String, List<String>> entry : COLUMNS.entrySet()) {
			int spaces = entry.getKey().length();
			builder.append(entry.getKey()).append(" - | ").append('\n');
			for(String v : entry.getValue()) {
				builder.append(CustomUtils.spaces(spaces)).append(" - | ").append(v).append("\n");
			}
		}
		
		return builder.toString();
	}

}
