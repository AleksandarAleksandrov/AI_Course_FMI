package game;

import java.util.Scanner;

import model.Point;
import model.PointScore;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	/**
	 * Holds the available points after each move.
	 */
	List<Point> availablePoints;
	/**
	 * Scanner for reading the user input from the console.
	 */
	Scanner scan = new Scanner(System.in);
	/**
	 * The game board. Updated after each move.
	 */
	int[][] board = new int[3][3];
	
	/**
	 * The scores of the root points.
	 */
	private List<PointScore> rootChildrenScore = new ArrayList<>();
	
	/**
	 * Variable used to determine the maximum number of moves to consider.
	 */
	int movesDepth = 5;
	
	/**
	 * Evaluates the score of the current board.
	 * @return
	 */
	public int evaluateBoard() {
		int score = 0;

		score = evaluateAllRows(score);
		score = evaluateAllColumns(score);
		score = checkFirstDiagonal(score);
		score = checkSecondDiagonal(score);

		return score;
	}

	public int minimaxAlphaBeta(int alpha, int beta, int depth, int turn) {
		
		// do pruning
		if (beta <= alpha) {
			if (turn == 1)
				return Integer.MAX_VALUE;
			else
				return Integer.MIN_VALUE;
		}
		
		// evaluate the board if we have reached the max depth
		// or the game is over
		if (depth == movesDepth || isGameOver()) {
			return evaluateBoard();
		}

		List<Point> pointsAvailable = getAvailableStates();

		if (pointsAvailable.isEmpty())
			return 0;

		if (depth == 0) {
			rootChildrenScore.clear();
		}

		int maxValue = Integer.MIN_VALUE;
		int minValue = Integer.MAX_VALUE;

		for (int i = 0; i < pointsAvailable.size(); ++i) {
			Point point = pointsAvailable.get(i);

			int currentScore = 0;

			if (turn == 1) {
				makeMove(point, 1);
				currentScore = minimaxAlphaBeta(alpha, beta, depth + 1, 2);
				maxValue = Math.max(maxValue, currentScore);

				// Set the alpha value
				alpha = Math.max(currentScore, alpha);

				if (depth == 0) {
					rootChildrenScore.add(new PointScore(currentScore, point));
				}
				
			} else if (turn == 2) {
				makeMove(point, 2);
				currentScore = minimaxAlphaBeta(alpha, beta, depth + 1, 1);
				minValue = Math.min(minValue, currentScore);

				// Set the beta value
				beta = Math.min(currentScore, beta);
			}
			
			// reset the board
			board[point.getX()][point.getY()] = 0;

			// If a pruning has been done, don't evaluate the rest of the
			// sibling states, we don't care about them
			if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) {
				break;
			}
		}
		
		return turn == 1 ? maxValue : minValue;
	}

	public boolean isGameOver() {
		return (isXWinner() || isOWinner() || getAvailableStates().isEmpty());
	}

	public boolean isXWinner() {
		// check diagonals
		if (
				(board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1)
				|| 
				(board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)
			) {
			return true;
		}
		// check columns and rows
		for (int i = 0; i < 3; ++i) {
			if (
					(board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
					|| 
					(board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1)
				) {

				return true;
			}
		}
		return false;
	}

	public boolean isOWinner() {
		// check diagonals
		if (
				(board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2)
				|| 
				(board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)
			) {
			return true;
		}
		
		// check columns and rows
		for (int i = 0; i < 3; ++i) {
			if (
					(board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
					|| 
					(board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)
			   ) {
				return true;
			}
		}

		return false;
	}

	public List<Point> getAvailableStates() {
		availablePoints = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					availablePoints.add(new Point(i, j));
				}
			}
		}
		return availablePoints;
	}

	public void makeMove(Point point, int player) {
		board[point.getX()][point.getY()] = player; // player = 1 for X, 2 for O
	}

	public Point getBestMove() {
		int MAX = -100000;
		int best = -1;

		for (int i = 0; i < rootChildrenScore.size(); ++i) {
			if (MAX < rootChildrenScore.get(i).getScore()) {
				MAX = rootChildrenScore.get(i).getScore();
				best = i;
			}
		}

		return rootChildrenScore.get(best).getPoint();
	}

	void getInput() {
		System.out.println("Your move: ");
		int x = scan.nextInt();
		int y = scan.nextInt();
		Point point = new Point(x, y);
		makeMove(point, 2);
	}

	public void displayBoard() {
		System.out.println();

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int value = board[i][j];
				if(value == 0) {
					System.out.print('+');
				} else if (value == 1) {
					System.out.print('X');
				} else if(value == 2)  {
					System.out.print('O');
				}
			}
			System.out.println();

		}
	}

	public void resetBoard() {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				board[i][j] = 0;
			}
		}
	}

	private int checkSecondDiagonal(int score) {
		int X = 0;
		int O = 0;
		for (int i = 2, j = 0; i > -1; --i, ++j) {
			if (board[i][j] == 1) {
				X++;
			} else if (board[i][j] == 2) {
				O++;
			}
		}
		score += recalculateScore(X, O);
		return score;
	}

	private int checkFirstDiagonal(int score) {
		int X = 0;
		int O = 0;
		for (int i = 0, j = 0; i < 3; ++i, ++j) {
			if (board[i][j] == 1) {
				X++;
			} else if (board[i][j] == 2) {
				O++;
			}
		}
		score += recalculateScore(X, O);
		return score;
	}

	private int evaluateAllColumns(int score) {
		for (int j = 0; j < 3; ++j) {
			int X = 0;
			int O = 0;
			for (int i = 0; i < 3; ++i) {
				if (board[i][j] == 0) {
					continue;
				} else if (board[i][j] == 1) {
					X++;
				} else {
					O++;
				}
			}
			score += recalculateScore(X, O);
		}
		return score;
	}

	private int evaluateAllRows(int score) {
		for (int i = 0; i < 3; ++i) {
			int X = 0;
			int O = 0;
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					continue;
				} else if (board[i][j] == 1) {
					X++;
				} else {
					O++;
				}

			}
			score += recalculateScore(X, O);
		}
		return score;
	}

	private int recalculateScore(int X, int O) {
		int change;
		if (X == 3) {
			change = 100;
		} else if (X == 2 && O == 0) {
			change = 10;
		} else if (X == 1 && O == 0) {
			change = 1;
		} else if (O == 3) {
			change = -100;
		} else if (O == 2 && X == 0) {
			change = -10;
		} else if (O == 1 && X == 0) {
			change = -1;
		} else {
			change = 0;
		}
		return change;
	}
	
	public Scanner getScan() {
		return scan;
	}

	public List<PointScore> getRootsChildrenScore() {
		return rootChildrenScore;
	}

	public void setRootsChildrenScore(List<PointScore> rootsChildrenScore) {
		this.rootChildrenScore = rootsChildrenScore;
	}

}
