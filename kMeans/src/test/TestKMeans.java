package test;

import algo.KMeans;

public class TestKMeans {

	public static void main(String[] args) {
		KMeans algo = new KMeans("normal.txt", 4);
		algo.kMeans();
	}

}
