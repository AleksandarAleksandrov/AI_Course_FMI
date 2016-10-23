package model;

/**
 * Represents a single state of the frogs on the lilypads in the game.
 */
public class FrogState {
	
	/**
	 * Char used to distinguish a frog going in a left direction.
	 */
	public static final char LEFT_FROG_CHAR = '<';
	/**
	 * Char used to distinguish a frog going in a right direction.
	 */
	public static final char RIGHT_FROG_CHAR = '>';
	/**
	 * Char used to distinguish an empty lilypad.
	 */
	public static final char EMPTY_SPOT_CHAR = '_';
	
	/**
	 * The current state of the game.
	 */
	private String state;
	/**
	 * The position of the empty lilypad.
	 */
	private int emptyPos;
	/**
	 * The previous state of the game.
	 */
	private FrogState parent = null;
	
	public FrogState(String state, int emptyPos) {
		this.state = state;
		this.emptyPos = emptyPos;
	}
	
	public FrogState(String state, int emptyPos, FrogState parent) {
		this.state = state;
		this.emptyPos =  emptyPos;
		this.parent = parent;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public int getEmptyPos() {
		return emptyPos;
	}
	
	public void setEmptyPos(int emptyPos) {
		this.emptyPos = emptyPos;
	}
	
	public FrogState getParent() {
		return parent;
	}
	
	public void setParent(FrogState parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		FrogState other = (FrogState) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
}
