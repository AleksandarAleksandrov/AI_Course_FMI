package model;

public class PointModel {

	public int yesCount;
	public int noCount;
	public int questionCount;

	public int getTotal() {
		return yesCount + noCount  + questionCount;
	}
}
