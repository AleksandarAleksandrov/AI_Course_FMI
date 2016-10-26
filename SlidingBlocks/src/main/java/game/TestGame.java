package game;

public class TestGame {
	
	public static void main(String[] args) {
		
//		SlidingBlocks game = new SlidingBlocks(3, new int[][]{
//			{3,5,1},
//			{4,2,6},
//			{0,7,8}			
//		});
		
//		SlidingBlocks game = new SlidingBlocks(3, new int[][]{
//			{1,2,3},
//			{4,5,6},
//			{0,7,8}			
//		});
		
		SlidingBlocks game = new SlidingBlocks(3, new int[][]{
			{2,4,7},
			{8,6,5},
			{0,1,3}
		});
		
	// > 40	
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{6,11,1,3},
//			{7,12,15,9},
//			{0,4,8,5},
//			{2,13,10,14}
//		});
		
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{14,4,2,3},
//			{1,7,6,11},
//			{13,10,0,9},
//			{15,12,8,5}
//		});
		

//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{14,1,8,2},
//			{5,10,4,11},
//			{0,12,9,15},
//			{13,3,6,7}
//		});
		
		//>50
		
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{8,14,10,4},
//			{7,2,1,5},
//			{9,15,11,3},
//			{12,0,13,6}
//		});
		
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{ // slow
//			{0,13,10,14},
//			{6,4,2,8},
//			{12,5,9,1},
//			{3,15,11,7}
//		});
		
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{13,2,0,6},
//			{10,8,14,12},
//			{15,9,11,4},
//			{1,3,5,7}
//		});
		//> 60
//		SlidingBlocks game = new SlidingBlocks(4, new int[][]{
//			{0,11,10,9},
//			{15,3,14,13},
//			{12,5,4,1},
//			{7,8,2,6}
//		});
		
		game.useIDAStarSolver();		
		
	}
}
