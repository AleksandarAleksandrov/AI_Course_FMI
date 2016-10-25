package model;

/**
 * This class represents the position of a value in the square.
 */
public class TilePosition {
	
	/**
	 * The row where the value is present. Row starts from 0.
	 */
	private int row;
	
	/**
	 * The column where the value is present. Column starts from 0. 
	 */
	private int column;
	
	public TilePosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public TilePosition cloneTile() {
		return new TilePosition(row, column);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
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
		TilePosition other = (TilePosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[row:" + row + ", col:" + column + "]";
	}

}
