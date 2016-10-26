package model;

import java.util.Map;

import org.apache.commons.collections4.BidiMap;

/**
 * This class represents a single node in the search algorithm.
 */
public class Board implements Comparable<Board>{
	
	// static fields for directions
	public static final String MOVE_UP = "up";
	public static final String MOVE_DOWN = "down";
	public static final String MOVE_LEFT = "left";
	public static final String MOVE_RIGHT = "right";
	// label for the root
	public static final String ROOT_LABEL = "root";
	
	/**
	 * A bi-directional map used for storing the board state of this node.
	 * A map is used for minimal memory footprint, meaning that many maps 
	 * share the same keys and values and two maps in a parent-child 
	 * relationship share the same object except for the ones marking 
	 * the zero state and the one used to swap it with the zero state. 
	 */
	private BidiMap<Integer, TilePosition> boardState;
	/**
	 * The distance of this node from the root node. 
	 * Used for the weight function.
	 */
	private int distanceFromRoot;
	/**
	 * The heuristic distance from this node to the goal node.
	 * Used for the weight function.
	 */
	private int heuristicDistance;
	/**
	 * The direction made from the parent node to reach this one.
	 */
	private String direction;
	/**
	 * The parent node of this node.
	 */
	private Board parent;
	
	public Board(BidiMap<Integer, TilePosition> boardState, int distanceFromRoot, int hieridicalDistance,
			String direction, Board parent) {
		this.boardState = boardState;
		this.distanceFromRoot = distanceFromRoot;
		this.heuristicDistance = hieridicalDistance;
		this.direction = direction;
		this.parent = parent;
	}

	public Board(BidiMap<Integer, TilePosition> boardState, int totalCost, String direction, Board parent) {
		this.boardState = boardState;
		this.distanceFromRoot = totalCost;
		this.direction = direction;
		this.parent = parent;
	}

	public Board(int totalCost, String direction, Board parent) {
		this.distanceFromRoot = totalCost;
		this.direction = direction;
		this.parent = parent;
	}

	public BidiMap<Integer, TilePosition> getBoardState() {
		return boardState;
	}

	public void setBoardState(BidiMap<Integer, TilePosition> boardState) {
		this.boardState = boardState;
	}

	public int getDistanceFromRoot() {
		return distanceFromRoot;
	}

	public void setDistanceFromRoot(int totalCost) {
		this.distanceFromRoot = totalCost;
	}
	
	public int getHeuristicDistance() {
		return heuristicDistance;
	}
	
	public void setHeuristicDistance(int hieridicalDistance) {
		this.heuristicDistance = hieridicalDistance;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Board getParent() {
		return parent;
	}

	public void setParent(Board parent) {
		this.parent = parent;
	}
	
	/**
	 * @return
	 * 		The sum of the distance from the root and the heuristic distance. 
	 * 		Used as the weight function.
	 */
	public int getCombinedDistance() {
		return distanceFromRoot + heuristicDistance;
	}
	
	@Override
	public int compareTo(Board o) {
		// compares the instances by their combined(weight) distance
		if(o == null) {
			return -1;
		}		
		return this.heuristicDistance - o.heuristicDistance;
	}
	
	/**
	 * A detailed String representation of a node.
	 * Used for debugging and tracking.
	 * @param puzzleSize
	 * @return
	 */
	public String toStringDetailed(int puzzleSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("direction: " + direction + "\n");
		builder.append("distance from root: " + distanceFromRoot + "\n");
		builder.append("hieriditical distance: " + heuristicDistance + "\n");
		
		int[][] table = new int[puzzleSize][puzzleSize];
		
		for(Map.Entry<Integer, TilePosition> tile : boardState.entrySet()) {
			table[tile.getValue().getRow()][tile.getValue().getColumn()] = tile.getKey();
		}
		
		builder.append("---------TABLE---------\n");
		for(int i = 0 ; i < puzzleSize ; i++) {
			for(int j = 0; j < puzzleSize; j++) {
				builder.append(table[i][j] + " |");
			}
			builder.append("\n");
		}
		builder.append("---------TABLE---------\n");
		
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardState == null) ? 0 : boardState.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + distanceFromRoot;
		result = prime * result + heuristicDistance;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Board other = (Board) obj;
		if (boardState == null) {
			if (other.boardState != null) {
				return false;
			}
		} else if (!boardState.equals(other.boardState)) {
			return false;
		}
		if (direction == null) {
			if (other.direction != null) {
				return false;
			}
		} else if (!direction.equals(other.direction)) {
			return false;
		}
		if (distanceFromRoot != other.distanceFromRoot) {
			return false;
		}
		if (heuristicDistance != other.heuristicDistance) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}
	

}
