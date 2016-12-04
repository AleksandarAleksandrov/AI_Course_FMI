package test;

import java.util.Random;

import game.Board;
import model.Point;

public class TestGame {

	public static void main(String[] args) {
		Board b = new Board();
        Random rand = new Random();

        b.displayBoard();

        System.out.println("Choose who plays first: 1 for AI and 2 for user: ");
        int choice = b.getScan().nextInt();
        if (choice == 1) {
            Point p = new Point(rand.nextInt(3), rand.nextInt(3));
            b.makeMove(p, 1);
            b.displayBoard();
        }

        while (!b.isGameOver()) {
            System.out.println("Make next move: ");
            Point userMove = new Point(b.getScan().nextInt(), b.getScan().nextInt());

            b.makeMove(userMove, 2);
            b.displayBoard();
            if (b.isGameOver()) break;
            
            b.minimaxAlphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
//            for (PointScore pas : b.getRootsChildrenScore()) 
//                System.out.println("Point: " + pas.getPoint() + " Score: " + pas.getScore());
            
            b.makeMove(b.getBestMove(), 1);
            b.displayBoard();
        }
        if (b.isXWinner()) {
            System.out.println("You lost, hahahahahaha !");
        } else if (b.isOWinner()) {
            System.out.println("You won, big deal :|");
        } else {
            System.out.println("It's a draw, you stupid");
        }

	}

}
