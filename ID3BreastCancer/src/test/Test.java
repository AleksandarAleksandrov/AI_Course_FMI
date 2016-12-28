package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import algorithm.ID3;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ID3 dr = new ID3();
		dr.doID3fromFile();
	}

}
