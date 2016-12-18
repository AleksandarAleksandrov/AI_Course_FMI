package algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import model.DataModel;
import model.PointModel;
import model.TableModel;

public class NBC {

	TableModel probabilityTable;
	
	private List<DataModel> allData = new ArrayList<>();
	private List<DataModel> testingSet = new ArrayList<>();
	private int columnSize;

	public void trainSet() {

		double accumulatedAcuracy = 0.0;

		for (int i = 0; i < 10; i++) {
			try {
				readFromFile();
				double accuracy = train();
				System.out.println("Acuraccy on try " + i + " is: " + accuracy * 100);
				accumulatedAcuracy += accuracy;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Total accuracy is: " + accumulatedAcuracy * 10);
	}

	private void readFromFile() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("house-votes-84.data"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(",");
				columnSize = split.length - 1;
				String result = split[0];
				String[] dataPoints = new String[split.length - 1];

				for (int i = 1; i < split.length; i++) {
					dataPoints[i - 1] = split[i];
				}

				DataModel dm = new DataModel(dataPoints, result, result);
				allData.add(dm);
			}
		}		
	}

	private void randomiseData() {
		int allDataSize = allData.size();

		int oneTenth = allDataSize / 10;

		Set<Integer> chosen = new HashSet<>();
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		int counter = 0;
		testingSet =  new ArrayList<>();
		while (counter < oneTenth) {
			int nextIndex = tlr.nextInt(allData.size());
			if (!chosen.contains(nextIndex)) {
				testingSet.add(allData.get(nextIndex));
				chosen.add(nextIndex);
				counter++;
			}
		}

		List<DataModel> trainingSet = new ArrayList<>();
		for (int i = 0; i < allData.size(); i++) {
			if (!chosen.contains(i)) {
				trainingSet.add(allData.get(i));
			}
		}

		probabilityTable = new TableModel(columnSize);

		probabilityTable.addData(trainingSet);
	}

	private double train() {
		randomiseData();
		double correctCount = 0.0;
		for (DataModel dm : testingSet) {
			findBestProbability(dm);
			if (dm.getResult().equals(dm.getGuess())) {
				correctCount++;
			}
		}

		return correctCount / testingSet.size();
	}

	private void findBestProbability(DataModel dm) {
		Map<String, List<PointModel>> table = probabilityTable.getTable();
		String bestName = "Other";
		double bestResult = Double.MIN_VALUE;
		for (Map.Entry<String, List<PointModel>> entry : table.entrySet()) {
			double bayes = calculateBayes(dm, entry.getValue());
			if (bestResult < bayes) {
				bestResult = bayes;
				bestName = entry.getKey();
			}
		}

		dm.setGuess(bestName);
	}

	private double calculateBayes(DataModel dm, List<PointModel> pm) {
		String[] dataPoints = dm.getDataPoints();
		double probability = 1.0;
		for (int i = 0; i < dataPoints.length; i++) {
			String dp = dataPoints[i];
			PointModel point = pm.get(i);

			switch (dp) {
			case "y":
				probability *= (1.0 * point.yesCount) / point.getTotal();
				break;
			case "n":
				probability *= (1.0 * point.noCount) / point.getTotal();
				break;
			case "?":
				probability *= (1.0 * point.questionCount) / point.getTotal();
				break;
			default:
				break;
			}
		}

		return probability;
	}

}
