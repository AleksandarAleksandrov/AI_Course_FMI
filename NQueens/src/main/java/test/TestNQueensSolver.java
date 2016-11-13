package test;


import model.Board;
import solver.MinConflictsSolver;

public class TestNQueensSolver {
	
	public static void main(String[] args) {
		
		new MinConflictsSolver().solveBoard(new Board(10000), "solution.txt");
		
	}
}
