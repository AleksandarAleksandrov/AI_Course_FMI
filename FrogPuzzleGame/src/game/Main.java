package game;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Please enter number of frogs in one direction: ");
		
		Scanner scan = new Scanner(System.in);
		int frogCount = scan.nextInt();
		
		new FrogGame().startGame(frogCount);

	}

}
