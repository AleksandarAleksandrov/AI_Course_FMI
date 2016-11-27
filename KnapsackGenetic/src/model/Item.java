package model;

public class Item {
	
	private long value;
	private long weight;
	
	public Item(long value, long weight) {
		this.value = value;
		this.weight = weight;
	}
	
	public long getValue() {
		return value;
	}
	
	public long getWeight() {
		return weight;
	}	

}
