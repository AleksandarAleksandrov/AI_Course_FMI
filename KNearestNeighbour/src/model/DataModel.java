package model;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
	
	private List<Double> points = new ArrayList<>();
	
	private String result;
	
	private String guess;

	public DataModel(List<Double> points, String result, String guess) {
		this.points = points;
		this.result = result;
		this.guess = guess;
	}

	public DataModel(List<Double> points, String result) {
		this.points = points;
		this.result = result;
	}

	public List<Double> getPoints() {
		return points;
	}

	public void setPoints(List<Double> points) {
		this.points = points;
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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String rowStr = String.format("%50s", "").replace(' ', '-');
		builder.append(rowStr);
		builder.append('\n');
		builder.append('|');
		for(Double d : points) {
			builder.append(d).append('|');
		}
		
		builder.append(result);
		builder.append('|');
		builder.append(guess);
		builder.append('|');
		
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((guess == null) ? 0 : guess.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModel other = (DataModel) obj;
		if (guess == null) {
			if (other.guess != null)
				return false;
		} else if (!guess.equals(other.guess))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}
	
	

}
