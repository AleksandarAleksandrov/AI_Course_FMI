package solver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import gnu.trove.list.array.TIntArrayList;
import model.Board;

public class MinConflictsSolver {
	
	private ThreadLocalRandom tlr = ThreadLocalRandom.current();
	
	/**
	 * Solves the board and prints the solution to the console if 
	 * <b>fileName</b> is null, or to the file pointed by <b>fileName</b>. 
	 * @param board
	 * @param fileName
	 */
	public void solveBoard(Board board, String fileName) {
		
		int movesMade = 0;
		
        TIntArrayList worstQueens = new TIntArrayList();
        
        int[] rows = board.getRows();        
        
        long startTime = System.currentTimeMillis();
        
        while (true) {
            
            int maxConflicts = 0;
            // find the world queens
            maxConflicts = maxConflictsAndWorstQueens(worstQueens, rows, maxConflicts);
            
            // there are no conflicts, so we have found a solution
            if (maxConflicts == 0) {
            	
            	long endTime = System.currentTimeMillis();
            	
            	System.out.println("Solution finding took: " + (endTime - startTime) + " miliseconds.");
            	
            	if(fileName == null) {
            		printToConsole(board);
            	} else {
            		//printToFile(board, fileName);
            	}
            	
                return;
            }

            // Pick a random queen from those that had the most conflicts
            int worstQueenColumn = worstQueens.get(tlr.nextInt(worstQueens.size()));

            // Move the bad queen to the best position
            moveQueenToLeastConPos(worstQueens, rows, worstQueenColumn);
            
            // pick one queens from the worst at random
            if (!worstQueens.isEmpty()) {
                rows[worstQueenColumn] = worstQueens.get(tlr.nextInt(worstQueens.size()));
            }

            movesMade++;
            // the solution is taking too many moves, 
            // we are probably in a plateau or a local maximum, 
            // so rearrange the board
            if (movesMade == rows.length * 2) {
                board.rearrangeBoard();
                rows = board.getRows();
                movesMade = 0;
            }
        }
	}

	/**
	 * Moves a bad queen to the best position in the column.
	 * @param worstQueens
	 * @param rows
	 * @param worstQueenColumn
	 */
	private void moveQueenToLeastConPos(TIntArrayList worstQueens, int[] rows, int worstQueenColumn) {
		int minConflicts = rows.length;
		worstQueens.clear();
		for (int r = 0; r < rows.length; r++) {
		    int conflictsCOunt = countConflicts(rows, r, worstQueenColumn);
		    if (conflictsCOunt == minConflicts) {
		        worstQueens.add(r);
		    } else if (conflictsCOunt < minConflicts) {
		        minConflicts = conflictsCOunt;
		        worstQueens.clear();
		        worstQueens.add(r);
		    }
		}
	}

	/**
	 * Returns the maximum number of conflicts and populates the list of worst queens.
	 * @param worstQueens
	 * @param rows
	 * @param maxConflicts
	 * @return
	 */
	private int maxConflictsAndWorstQueens(TIntArrayList worstQueens, int[] rows, int maxConflicts) {
		worstQueens.clear();
		for (int c = 0; c < rows.length; c++) {
		    int conflictsCount = countConflicts(rows,rows[c], c);
		    if (conflictsCount == maxConflicts) {
		        worstQueens.add(c);
		    } else if (conflictsCount > maxConflicts) {
		    	// if a higher bound is found, clear the list
		    	// and set the new bound
		        maxConflicts = conflictsCount;
		        worstQueens.clear();
		        worstQueens.add(c);
		    }
		}
		return maxConflicts;
	}
	
	/**
	 * Returns the number of conflicts for the queen at <b>row</b> and <b>column</b>.
	 * @param rows
	 * @param row
	 * @param column
	 * @return
	 */
	private int countConflicts(int [] rows, int row , int column) {
		int count = 0;
        for (int c = 0; c < rows.length; c++) {
        	
            if (c == column) {
            	continue;
            }
            
            int r = rows[c];
            if (Math.abs(r-row) == Math.abs(c-column) || r == row) count++;
        }
        return count;
	}
	
	/**
	 * Prints the solution to the console.
	 * @param board
	 */
	private void printToConsole(Board board) {
		int[] rows = board.getRows();
		
		for (int r = 0; r < rows.length; r++) {
			StringBuilder sb = new StringBuilder(board.getBoardSize());
			for (int c = 0; c < rows.length; c++) {
				sb.append(rows[c] == r ? Board.MARKER_QUEEN : Board.MARKER_EMPTY);
			}
			System.out.println(sb.toString());
		}
	}

	/**
	 * Prints the solution to a file
	 * @param board
	 * @param fileName
	 */
	private void printToFile(Board board, String fileName) {
		
		int[] rows = board.getRows();
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			
			for (int r = 0; r < rows.length; r++) {
				StringBuilder sb = new StringBuilder(board.getBoardSize());
				for (int c = 0; c < rows.length; c++) {
					sb.append(rows[c] == r ? Board.MARKER_QUEEN : Board.MARKER_EMPTY);
				}
				
				bw.write(sb.toString());
				bw.newLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
