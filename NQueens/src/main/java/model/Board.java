package model;

import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
	
	public static final char MARKER_EMPTY = '_';
	public static final char MARKER_QUEEN = '*';

	/**
	 * The size of the chess board.
	 */
	private int boardSize;

	/**
	 * An array representing the position of each queen of every column. 
	 */
	private int[] rows;

	public Board(int boardSize) {
		this.boardSize = boardSize;
		rearrangeBoard();
	}

	public Board(int boardSize, int[] rows) {
		this.boardSize = boardSize;
		this.rows = rows;
	}
	
	
	public void rearrangeBoard() {
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		
		// only construct a new array once 
		// for each new rearrangement just put new data
		if(rows == null || rows.length != boardSize) {
			rows = new int[boardSize];
		}
		
		for(int i = 0 ; i < boardSize ; i++) {
			rows[i] = tlr.nextInt(boardSize);
		}
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public int[] getRows() {
		return rows;
	}
	
	@Override
	public String toString() {
		
		if(rows == null) {
			return "[]";
		}
		
		StringJoiner sj = new StringJoiner(",", "[", "]");
		
		for(int i = 0 ; i < rows.length ; i++) {
			sj.add(String.valueOf(rows[i]));
		}
		
		return sj.toString();
	}

}
