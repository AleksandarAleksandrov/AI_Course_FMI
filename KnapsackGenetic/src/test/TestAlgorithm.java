package test;

import java.util.ArrayList;
import java.util.List;

import algorithm.GeneticAlgorithm;
import model.Item;

public class TestAlgorithm {

	public static void main(String[] args) {
		
		List<Item> items = new ArrayList<>();
		
		items.add(new Item(5,3));
		items.add(new Item(2,3));
		items.add(new Item(5,1));
		items.add(new Item(3,2));
		
		
		GeneticAlgorithm.findSolution(items, 2, 0.30, 0.95, 0, 7, 50);
	}
}
