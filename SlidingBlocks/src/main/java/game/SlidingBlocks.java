package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import model.Board;
import model.TilePosition;

/**
 * Class implementing the game logic.
 */
public class SlidingBlocks {	
	
	/**
	 * A priority queue used for the smart search to find the optimal solution.
	 */
	private PriorityQueue<Board> prioritisedQ = new PriorityQueue<>();
	
	/**
	 * An upper bound on the number of elements the priority queue can hold.
	 */
	private final static int MAX_QUEUE_SIZE = 100_000;
	
	/**
	 * The initial board for the start of the game.
	 */
	private Board initialBoard;
	
	/**
	 * The state of the board we are trying to reach.
	 */
	private BidiMap<Integer, TilePosition> finalBoardSate;
	
	/**
	 * The size of the puzzle.
	 */
	private int puzzleSize;
	
	public SlidingBlocks(int puzzleSize, int[][] initBoard) {
		this.puzzleSize = puzzleSize;
		constructInitBoard(initBoard);
		constructFinalBoardState(puzzleSize);
	}
	
	/**
	 * Starts an instance of the game with the parameters passed 
	 * to the constructor of the class. Prints the path from the root 
	 * to the final solution.
	 */
	public void startGame() {
		// use ida* by default
		//aStar();
		iterativeDeepeningAStar();
	}
	
	public void useAStarSolver() {
		aStar();
	}
	
	public void useIDAStarSolver() {
		iterativeDeepeningAStar();
	}

	
	/**
	 * Method used to solve the problem. 
	 * Uses the A* algorithm. Does not give the optimal solution.
	 */
	private void aStar() {
		
		// make initial push
		prioritisedQ.add(initialBoard);
			
		while(!prioritisedQ.isEmpty()) {
			
			Board currBoard = prioritisedQ.poll();
			
//			if(currBoard.getCombinedDistance() > 40) {
//				System.out.println(currBoard.toStringDetailed(puzzleSize));
//			}
			
			// if the final state is found print it and end the program
			if(currBoard.getBoardState().equals(finalBoardSate)) {
				printPath(currBoard);
				return;
			}			
			
			// get the next valid moves and add them to the queue
			List<Board> nextMoves = getNextMoves(currBoard);

			prioritisedQ.addAll(nextMoves);
			
			// when the queue becomes too big remove the last 20_000 elements of it
			if(prioritisedQ.size() > MAX_QUEUE_SIZE + 20_000) {
				for(int i = 0 ; i < prioritisedQ.size() - MAX_QUEUE_SIZE ; i++) {
					prioritisedQ.remove(prioritisedQ.size() - 1);
				}
			}
			
		}			

	}
	
	/**
	 * The iterative deepening version of the A* algorithm.  
	 * This one uses much less memory and CPU.
	 */
	private void iterativeDeepeningAStar() {
		
		int bound = initialBoard.getHeuristicDistance();
		
		// start the search by limited by the bound
		while(true) {
			// if the search did not find a solution at the old bound
			// it returns the new bound to be used
			int newBound = searchForSolution(initialBoard, 0, bound);
			// if the new bound is Integer.MAX_VALUE something went wrong
			if(newBound == Integer.MAX_VALUE) {
				System.out.println("Something went wrong! Terminating application ...");
				System.exit(-1);
			}
			bound = newBound;
		}
		
		
	}
	
	/**
	 * Does the search for the solution node. Returns the new bound to be used for the search 
	 * if a solution was not found. Prints the solution path and terminates the program if a 
	 * solution is found.
	 * @param node
	 * @param distance
	 * @param bound
	 * @return
	 */
	private int searchForSolution(Board node, int distance, int bound) {
		int nodeDist = distance + node.getHeuristicDistance();
		// if the node bound if bigger return it
		// and use it as the new bound
		if(nodeDist > bound) {
			return nodeDist;
		}

		// check if we have found the final state
		isFinalStateNode(node);
		// set the initial minimum bound to MAX_VALUE
		int minBound = Integer.MAX_VALUE;
		// get the next moves
		List<Board> nextMoves = getNextMoves(node);
		// for each new move do an informed DFS
		for(Board move : nextMoves) {
			int newBound = searchForSolution(move, distance + 1, bound);
			if(newBound < minBound) minBound = newBound;
		}
		
		return minBound;
	}
	
	/**
	 * Checks if the passed node is the current state node and if it is 
	 * prints the path from the root and terminates the program.
	 * @param node
	 */
	private void isFinalStateNode(Board node) {
		if(finalBoardSate.equals(node.getBoardState())) {
			printPath(node);
			System.exit(0);
		}
	}
	
	/**
	 * Returns a list of all the valid moves from the current state.
	 * @param parentBoard
	 * @return
	 */
	private List<Board> getNextMoves(Board parentBoard) {
		
		List<Board> nextMoves = new ArrayList<>();
		
		Board rightMove = getNextMove(parentBoard, Board.MOVE_RIGHT);
		if(rightMove != null) nextMoves.add(rightMove);
		
		Board leftMove = getNextMove(parentBoard, Board.MOVE_LEFT);
		if(leftMove != null) nextMoves.add(leftMove);
		
		Board upMove = getNextMove(parentBoard, Board.MOVE_UP);
		if(upMove != null) nextMoves.add(upMove);
		
		Board downMove = getNextMove(parentBoard, Board.MOVE_DOWN);
		if(downMove != null) nextMoves.add(downMove);
		
		// sort so the algorithms always chose the best variant
		// a small performance boost
		//Collections.sort(nextMoves);
		
		return nextMoves;
	}
	
	/**
	 * method used to check against loops of random size.
	 * @param state
	 * @param parent
	 * @return
	 */
	private boolean isInPath(BidiMap<Integer,TilePosition> state, Board parent) {
		
		while(parent != null) {
			if (state.equals(parent.getBoardState())) {
				return true;
			}
			parent = parent.getParent();
		}
		
		return false;
	}
	
	/**
	 * Returns the next board state corresponding to the move type 
	 * or null if the move is invalid.
	 * @param parentBoard
	 * @param moveType
	 * @return
	 */
	private Board getNextMove(Board parentBoard, String moveType) {
		
		Board nextMove = null;
		BidiMap<Integer, TilePosition> parentState = parentBoard.getBoardState();
		int distanceFromRoot = parentBoard.getDistanceFromRoot();
		TilePosition movingTile = parentState.get(0);
		
		switch (moveType) {
		case Board.MOVE_UP:	
			// check the boundaries
			if(movingTile.getRow() - 1 < 0) {
				break;
			}
			// get the new position for the zero tile
			TilePosition newPosUp = new TilePosition(movingTile.getRow() - 1, movingTile.getColumn());
			// create the new map state 
			BidiMap<Integer,TilePosition> newStateMapUp = createNewStateMap(parentState, movingTile, newPosUp);
			// check against loops of random size
			if(isInPath(newStateMapUp, parentBoard)) {
				break;
			}
			
			// calculate the manhattan distance for the new state to be used in the weight function 
			int hieridicalDistanceUp = calculateManhattanDistance(newStateMapUp);			
			nextMove =  new Board(	newStateMapUp,
									distanceFromRoot + 1,
									hieridicalDistanceUp,
									Board.MOVE_DOWN,
									parentBoard);
			return nextMove;
			
		case Board.MOVE_DOWN:
			if(movingTile.getRow() + 1 > puzzleSize - 1) {
				break;
			}
			TilePosition newPosDown = new TilePosition(movingTile.getRow() + 1, movingTile.getColumn());
			BidiMap<Integer, TilePosition> newStateMapDown = createNewStateMap(parentState, movingTile, newPosDown);
			if(isInPath(newStateMapDown, parentBoard)) {
				break;
			}
			int hieridicalDistanceDown = calculateManhattanDistance(newStateMapDown);
			nextMove =  new Board(	newStateMapDown,
									distanceFromRoot + 1,
									hieridicalDistanceDown,
									Board.MOVE_UP,
									parentBoard);
			return nextMove;
			
		case Board.MOVE_LEFT:
			if(movingTile.getColumn() - 1 < 0) {
				break;
			}
			TilePosition newPosLeft = new TilePosition(movingTile.getRow(), movingTile.getColumn() - 1);
			BidiMap<Integer,TilePosition> newStateMapLeft = createNewStateMap(parentState, movingTile, newPosLeft);
			if(isInPath(newStateMapLeft, parentBoard)) {
				break;
			}
			int manhattanDistanceLeft = calculateManhattanDistance(newStateMapLeft);
			nextMove = new Board(	newStateMapLeft,
									distanceFromRoot + 1,
									manhattanDistanceLeft,
									Board.MOVE_RIGHT,
									parentBoard);
			return nextMove;
			
		case Board.MOVE_RIGHT:
			if(movingTile.getColumn() + 1 > puzzleSize - 1) {
				break;
			}
			TilePosition newPosRight = new TilePosition(movingTile.getRow(), movingTile.getColumn() + 1);
			BidiMap<Integer,TilePosition> newStateMapRight = createNewStateMap(parentState, movingTile, newPosRight);
			if(isInPath(newStateMapRight, parentBoard)) {
				break;
			}
			int manhattanDistanceRight = calculateManhattanDistance(newStateMapRight);
			nextMove = new Board(	newStateMapRight,
									distanceFromRoot + 1,
									manhattanDistanceRight,
									Board.MOVE_LEFT,
									parentBoard);
			return nextMove;
			
		default:
			break;
		}
		
		return nextMove;
	}

	/**
	 * Returns a new map representing the new board state with the zero tile 
	 * and another tile that was next to it, switched. 
	 * @param parentState
	 * @param movingTile
	 * @param newPos
	 * @return
	 */
	private BidiMap<Integer, TilePosition> createNewStateMap(BidiMap<Integer, TilePosition> parentState, TilePosition movingTile, TilePosition newPos) {
		
		Integer oldVal = parentState.inverseBidiMap().get(newPos);
		
		TilePosition emptyPos = movingTile.cloneTile();
		
		BidiMap<Integer, TilePosition> childState = new DualHashBidiMap<>(parentState);
		
		childState.put(0, newPos);
		childState.put(oldVal.intValue(), emptyPos);
		
		return childState;
	}
	
	/**
	 * Prints the solution path.
	 * @param solution
	 */
	private void printPath(Board solution) {
		
		List<String> path = new LinkedList<>();
		
		System.out.println(solution.getDistanceFromRoot());
		
		while (solution != null) {
			// do not print the initial state
			if(solution.getDirection().equals(Board.ROOT_LABEL)) {
				solution = solution.getParent();
				continue;
			}
			
			String direction = solution.getDirection();
			path.add(direction);
			solution = solution.getParent();
		}
		
		Collections.reverse(path);
		
		path.forEach((p) -> System.out.println(p));
	}
	
	/**
	 * Constructs the initial state of the board.
	 * @param initBoard
	 */
	private void constructInitBoard(int[][] initBoard) {
		
		BidiMap<Integer, TilePosition> initMap = new DualHashBidiMap<Integer, TilePosition>();
		for(int i = 0 ; i < puzzleSize ; i++) {
			for(int j = 0 ; j < puzzleSize ; j++) {
				initMap.put(initBoard[i][j], new TilePosition(i, j));
			}
		}
		
		int manhattanDistance = calculateManhattanDistance(initMap);
		
		initialBoard = new Board(initMap, 0, manhattanDistance, Board.ROOT_LABEL, null);
	}
	
	/**
	 * Constructs the map for the final state of the game.
	 * @param puzzleSize
	 */
	private void constructFinalBoardState(int puzzleSize) {
		
		finalBoardSate = new DualHashBidiMap<Integer, TilePosition>();
		
		for(int i = 0; i < puzzleSize ; i++) {
			for(int j = 0; j < puzzleSize; j++) {
				finalBoardSate.put(i*puzzleSize + j + 1, new TilePosition(i, j));
			}
		}
		
		// remove the unnecessary tile and add the zero tile
		finalBoardSate.remove(puzzleSize*puzzleSize);
		finalBoardSate.put(0, new TilePosition(puzzleSize - 1, puzzleSize - 1));
		
	}
	
	/**
	 * Calculates the manhattan distance between the current positions of the values 
	 * in the table and the positions in which they should be in the final state.
	 * @param boardState
	 * @return
	 */
	private int calculateManhattanDistance(BidiMap<Integer, TilePosition> boardState) {
		
		int distance = 0;
		
		for(Map.Entry<Integer, TilePosition> tile : boardState.entrySet()) {
			
			Integer tileValue = tile.getKey();
			// skip the zero tile
			if(tileValue.intValue() == 0) {
				continue;
			}
			
			TilePosition tilePosition = tile.getValue();
			
			distance += Math.abs(tilePosition.getRow() - getTrueRow(tileValue)) + 
						Math.abs(tilePosition.getColumn() - getTrueCol(tileValue));
		}
		
		return distance;
	}
	
	/**
	 * Returns the final column position for the passed value.  
	 * @param value
	 * @return
	 */
	private int getTrueCol(int value) {
	    return (value - 1) % puzzleSize;
	}
	
	/**
	 * Returns the final row position for the passed value.  
	 * @param value
	 * @return
	 */
	private int getTrueRow(int value) {
	    return (value - 1) / puzzleSize;
	}
	
}
