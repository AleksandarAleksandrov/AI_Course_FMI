package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableModel {
	
	private int columnSize;
	private Map<String, List<PointModel>> table = new HashMap<>();
	
	public TableModel(int columnSize) {
		this.columnSize = columnSize;
	}
	
	public void addData(List<DataModel> data) {
		for(DataModel dm : data) {
			String key = dm.getResult();
			List<PointModel> points;
			if(!table.containsKey(key)) {
				table.put(key, new ArrayList<>(columnSize));
				points = table.get(key);
				for(int i = 0 ; i < columnSize; i++) {
					points.add(new PointModel());
				}
			} else {
				points = table.get(key);
			}
			
			String[] dataPoints = dm.getDataPoints();
			for(int i = 0 ; i < columnSize; i++) {
				String dp = dataPoints[i];
				PointModel point = points.get(i);
				
				switch (dp) {
				case "y":
					point.yesCount++;
					break;
				case "n":
					point.noCount++;
					break;
				case "?":
					point.questionCount++;
					break;
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("republican | ");
		
		for(int i = 0 ; i < columnSize; i++) {
			builder.append(i + "_Y |" + i + "_N |" + i + "_? |");
		}		
		builder.append("\n");
		
		for(Map.Entry<String, List<PointModel>> row : table.entrySet()) {
			builder.append(row.getKey()).append(" |");
			List<PointModel> lpm = row.getValue();
			for(PointModel pm : lpm) {
				builder.append(pm.yesCount + "  |" + pm.noCount + "  |" + pm.questionCount + "  |");
			}
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	public Map<String, List<PointModel>> getTable() {
		return table;
	}
		
}
