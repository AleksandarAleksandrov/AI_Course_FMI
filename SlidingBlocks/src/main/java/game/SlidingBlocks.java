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
	 * The initial board fot the start of the game.
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
		aStar();
	}
	
	/**
	 * Method used to solve the problem. 
	 * Uses the A* algorithm.
	 */
	private void aStar() {
		
		// make initial push
		prioritisedQ.add(initialBoard);
			
		while(!prioritisedQ.isEmpty()) {

			Board currBoard = prioritisedQ.poll();			
			
			// if the final state is found print it and end the program
			if(currBoard.getBoardState().equals(finalBoardSate)) {
				printPath(currBoard);
				return;
			}			
			
			// get the next valid moves and add them to the queue
			List<Board> nextMoves = getNextMoves(currBoard);

			prioritisedQ.addAll(nextMoves);
			
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
		
		return nextMoves;
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
	public int getTrueCol(int value) {
	    return (value - 1) % puzzleSize;
	}
	
	/**
	 * Returns the final row position for the passed value.  
	 * @param value
	 * @return
	 */
	public int getTrueRow(int value) {
	    return (value - 1) / puzzleSize;
	}
	
}
