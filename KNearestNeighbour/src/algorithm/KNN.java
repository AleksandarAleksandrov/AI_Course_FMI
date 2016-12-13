package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import model.DataModel;
import util.BoundedSortedMap;

public class KNN {
	
	private int kSize;
	
	private List<DataModel> learningSet =  new ArrayList<>();
	
	private List<DataModel> testingSet = new ArrayList<>();
	
	public void showClasificatorResults(int kSize) {
		try {
			this.kSize = kSize;
			readDataFromFile();
			calculateResults();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readDataFromFile() throws IOException {
		
		List<DataModel> allData = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("iris.data"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	
		       String[] split = line.split(",");
		       List<Double> points = new ArrayList<>();
		       
		       for(int i = 0 ; i < split.length - 1; i++) {
		    	   points.add(Double.parseDouble(split[i]));
		       }	       
		       String result = split[split.length - 1];
		       
		       DataModel dm = new DataModel(points, result, result);
		       allData.add(dm);
		    }
		}
		
		Set<Integer> chosen = new HashSet<>();
		ThreadLocalRandom tlr = ThreadLocalRandom.current();		
		int counter = 0;
		while(counter < 50) {
			int nextIndex = tlr.nextInt(allData.size());
			if(!chosen.contains(nextIndex)) {
				testingSet.add(allData.get(nextIndex));
				chosen.add(nextIndex);
				counter++;
			}
		}
		
		for(int i = 0 ; i < allData.size(); i++) {
			if(!chosen.contains(i)) {
				learningSet.add(allData.get(i));
			}
		}
		
	}
	
	private void calculateResults() {
		double rightGuesses = 0.0;
		for(DataModel testModel : testingSet) {
			TreeMap<Double, DataModel> closest = new BoundedSortedMap<>(kSize, false);
			
			for(DataModel learningModel : learningSet) {
				double cosine = calculateCosine(testModel, learningModel);
				closest.put(cosine, learningModel);
			}
			
			DataModel bestMatch = closest.firstEntry().getValue();
			makeGuess(testModel, bestMatch);
			System.out.println(testModel);
			if(testModel.getGuess().equals(testModel.getResult())) {
				rightGuesses++;
			}
		}
		
		System.out.println();
		System.out.println("The aquiracy is: " + rightGuesses / testingSet.size() * 100 + "%");
	}
	
	private void makeGuess(DataModel testModel, DataModel bestMatch) {
		testModel.setGuess(bestMatch.getResult());
	}	
	
	private double calculateCosine(DataModel testModel, DataModel learningModel) {
		
		List<Double> tPoints = testModel.getPoints();
		List<Double> lPoints = learningModel.getPoints();
		
		double nominator = 0;
		for(int i = 0 ; i < tPoints.size() ; i++) {
			nominator += tPoints.get(i) * lPoints.get(i);
		}
		
		double firstDenominator = 0;
		for(int i = 0 ; i < tPoints.size() ; i++) {
			firstDenominator += tPoints.get(i) * tPoints.get(i);
		}
		firstDenominator = Math.sqrt(firstDenominator);
		
		double secondDenominator = 0;
		for(int i = 0 ; i < lPoints.size() ; i++) {
			secondDenominator += lPoints.get(i) * lPoints.get(i);
		}		
		secondDenominator = Math.sqrt(secondDenominator);		
		
		return nominator / (firstDenominator * secondDenominator);
	}
}
