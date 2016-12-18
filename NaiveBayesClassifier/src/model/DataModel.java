package model;

public class DataModel {
	
	private String[] dataPoints;

	private String result;
	private String guess;

	public DataModel(String[] dataPoints, String result, String guess) {
		this.dataPoints = dataPoints;
		this.result = result;
		this.guess = guess;
	}

	public String[] getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(String[] dataPoints) {
		this.dataPoints = dataPoints;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}
}
