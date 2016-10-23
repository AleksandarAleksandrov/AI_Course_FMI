package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.FrogState;

/**
 * Represents a single instance of the game from start to finish.
 */
public class FrogGame {	
	
	/**
	 * The count of the frogs in one direction for the current instance of the game.
	 */
	private int frogCount;
	/**
	 * The count of the lilypads for the the current instance of the game.
	 */
	private int lilypadCount;
	
	/**
	 * The initial state for the game.
	 */
	private FrogState initialState;
	/**
	 * The final state for the game.
	 */
	private FrogState finalState;
	
	/**
	 * A stack holding the elements in the DFS search.
	 */
	private Deque<FrogState> stack = new LinkedList<>();
	/**
	 * A set containing the visited states. Used to not go the same root twice.
	 */
	private Set<String> visited = new HashSet<>();
	
	/**
	 * Creates the initial state of the game.
	 */
	private void createInitState() {
		String leftFrogs = new String(new char[frogCount]).replace('\0', FrogState.RIGHT_FROG_CHAR);
		String rightFrogs = new String(new char[frogCount]).replace('\0', FrogState.LEFT_FROG_CHAR);
		
		String frogState = leftFrogs + FrogState.EMPTY_SPOT_CHAR + rightFrogs;
		
		initialState = new FrogState(frogState, frogCount);
	}
	
	/**
	 * Creates the final state of the game.
	 */
	private void createFinalState() {
		String leftFrogs = new String(new char[frogCount]).replace('\0', FrogState.RIGHT_FROG_CHAR);
		String rightFrogs = new String(new char[frogCount]).replace('\0', FrogState.LEFT_FROG_CHAR);
		
		String frogState = rightFrogs + FrogState.EMPTY_SPOT_CHAR + leftFrogs;
		
		finalState =  new FrogState(frogState, frogCount);
	}
	

	private void makeInitPush() {
		stack.push(initialState);
		visited.add(initialState.getState());
	}
	
	private void iterativeDFS() {
		
		makeInitPush();
		
		while(!stack.isEmpty()) {
			FrogState currState = stack.pop();
			
			if(currState.equals(finalState)) {
				printPath(currState);
				return;
			} else {
				List<FrogState> nextStates = getNextValidStates(currState);
				stack.addAll(nextStates);
			}			
		}
		
		System.out.println("Hmm it seems something isn't working ! Sorry :( ");
	}
	
	private void printPath(FrogState state) {
		FrogState iterState = state;
		List<String> lines = new ArrayList<>();
		do {
			lines.add(iterState.getState());
			iterState = iterState.getParent();
		} while (iterState != null);
		
		Collections.reverse(lines);
		
		lines.forEach(currState -> System.out.println(currState));
	}
	
	/**
	 * Returns a list of the next valid states to be considered for the search.
	 * @param parent
	 * @return
	 */
	private List<FrogState> getNextValidStates(FrogState parent) {
		List<FrogState> validStates = new LinkedList<>();
		
		FrogState leftTwo = caseRight(parent, 2);
		if(leftTwo != null) validStates.add(leftTwo);
		
		FrogState leftOne = caseRight(parent, 1);
		if(leftOne != null) validStates.add(leftOne);
		
		FrogState rightOne = caseLeft(parent, 1);
		if(rightOne != null) validStates.add(rightOne);
		
		FrogState rightTwo = caseLeft(parent, 2);
		if(rightTwo != null) validStates.add(rightTwo);		

				
		return validStates;
	}
	
	private FrogState caseRight(FrogState parent, int oneOrTwo) {
		
		int parentEmptyPos = parent.getEmptyPos();
		int childEmptyPos = parentEmptyPos - oneOrTwo;
		
		if(childEmptyPos >= 0 && parent.getState().charAt(childEmptyPos) == FrogState.RIGHT_FROG_CHAR) {
			char[] stateArr = parent.getState().toCharArray();
			stateArr[parentEmptyPos] = FrogState.RIGHT_FROG_CHAR;
			stateArr[childEmptyPos] = FrogState.EMPTY_SPOT_CHAR;
			
			String childState = new String(stateArr);
			if(!visited.contains(childState)) {
				visited.add(childState);
				FrogState child = new FrogState(childState, childEmptyPos, parent);
				return child;
			}
		}
		
		return null;
	}
	
	private FrogState caseLeft(FrogState parent, int oneOrTwo) {
		int parentEmptyPos = parent.getEmptyPos();
		int childEmptyPos = parentEmptyPos + oneOrTwo;
		
		if(childEmptyPos < lilypadCount && parent.getState().charAt(childEmptyPos) == FrogState.LEFT_FROG_CHAR) {
			char[] stateArr = parent.getState().toCharArray();
			stateArr[parentEmptyPos] = FrogState.LEFT_FROG_CHAR;
			stateArr[childEmptyPos] = FrogState.EMPTY_SPOT_CHAR;
			
			String childState = new String(stateArr);
			if(!visited.contains(childState)) {
				visited.add(childState);
				FrogState child = new FrogState(childState, childEmptyPos, parent);
				return child;
			}
		}
		
		return null;
	}
	
	private void initGameParams(int frogCount) {
		this.frogCount = frogCount;
		this.lilypadCount = frogCount * 2 + 1;
	}
	
	public void startGame(int frogCount) {
		
		initGameParams(frogCount);
				
		createInitState();
		createFinalState();
		
		iterativeDFS();
	}
	

}
