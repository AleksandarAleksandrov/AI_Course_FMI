package game;

public class TestGame {
	
	public static void main(String[] args) {
		
		SlidingBlocks game = new SlidingBlocks(3, new int[][]{
			{3,5,1},
			{4,2,6},
			{0,7,8}			
		});
		
		
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{1,2,3,4},
//			{5,6,7,8},
//			{9,10,11,12},
//			{0,13,14,15}
//		});
		
		game.startGame();
		
	}
}
