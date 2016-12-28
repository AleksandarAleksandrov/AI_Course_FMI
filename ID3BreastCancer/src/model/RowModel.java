package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Models one row of data with it's attributes and it's classification.
 */
public class RowModel {
	
	public List<String> attributes = new ArrayList<>();
	public String clazz;
	
	public RowModel(String... cells) {
		for(int i = 0 ; i < cells.length ; i++) {
			if(i == cells.length - 1) {
				clazz = cells[i];
			} else {
				attributes.add(cells[i]);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("attributes: ");
		for(String s : attributes) {
			builder.append(s).append(" | ");
		}
		builder.append('\n');
		builder.append("class: ").append(clazz);
		return builder.toString();
	}
}
