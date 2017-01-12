package algo;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Centroids;
import model.DataPoint;
import plot.utils.Plot;
import plot.utils.Plot.Data;

public class KMeans {
	
	private String fileName;
	private int kSize;	
	private double xMin = Double.POSITIVE_INFINITY;
	private double xMax = Double.NEGATIVE_INFINITY;
	private double yMin = Double.POSITIVE_INFINITY;
	private double yMax = Double.NEGATIVE_INFINITY;
	private Centroids centroids = new Centroids();
	
	public KMeans(String fileName, int kSize) {
		this.fileName = fileName;
		this.kSize = kSize;
	}
	
	private List<DataPoint> readFromFile(String fileName) throws IOException {
		List<DataPoint> allPoints =  new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] pointStr = line.split("\\s+");
				double xPoint = Double.parseDouble(pointStr[0]);
				double yPoint = Double.parseDouble(pointStr[1]);
				DataPoint dp = new DataPoint(xPoint, yPoint);
				allPoints.add(dp);
				
				if(xPoint > xMax) xMax = xPoint;
				if(xPoint < xMin) xMin = xPoint;
				if(yPoint > yMax) yMax = yPoint;
				if(yPoint < yMin) yMin = yPoint;				
		    }
		}
		// construct the centroids before returning the data
		centroids.constructCentroids(kSize, xMin, xMax, yMin, yMax);
		return allPoints;		
	}
	
	private void doKMeans(List<DataPoint> allPoints) {
		
		// initial fill
		allPoints.forEach(p -> centroids.addDataToNearestCentroid(p));
		
		int changes = 0;
		
		do {
			changes = 0;
			centroids.repositionCentroids();
			for(DataPoint dp : allPoints) {
				DataPoint oldCentroid = dp.centroid;
				DataPoint nearestCentroid = centroids.addDataToNearestCentroid(dp);
				if(!oldCentroid.equals(nearestCentroid)) {
					changes++;
				}
			}
		} while(changes != 0);
		
	}
	
	private void writeInfoToFile() throws IOException {
		try(FileWriter fw = new FileWriter("dataInfo.txt")) {
			int counter = 0;
			for(Map.Entry<DataPoint,List<DataPoint>> entry : centroids.centroids.entrySet()) {
				fw.write("centroid_" + counter + " :");
				fw.write(entry.getKey().toString());
				fw.write("\n");
				for(DataPoint dp : entry.getValue()) {
					fw.write("                ");
					fw.write(dp.toString());
					fw.write("\n");
				}
				counter++;
			}
		}
	}
	
	private void createScatterPlot() throws IOException {
		Plot plot = Plot.plot(Plot.plotOpts().title("From file: " + fileName + " || kMeans: " + kSize));
		plot.xAxis("x", Plot.axisOpts().range(xMin, xMax));
		plot.yAxis("y", Plot.axisOpts().range(yMin, yMax));
		
		List<Color> colors = new ArrayList<>();
		Color[] chosenColors = new Color[] {
			Color.BLUE,
			Color.RED,
			Color.GREEN,
			Color.YELLOW,
			Color.ORANGE,
			Color.PINK,
			Color.MAGENTA,
			Color.BLACK,
		};
		
		int countColor = 0 ;
		
		for(int i = 0; i < centroids.centroids.keySet().size(); i++) {
			if(countColor == chosenColors.length) countColor = 0;
			colors.add(chosenColors[countColor]);
			countColor++;
		}
		
		int countCentr = 0;
		for(Map.Entry<DataPoint, List<DataPoint>> entry : centroids.centroids.entrySet()) {
			DataPoint centroid = entry.getKey();
			List<DataPoint> points = entry.getValue();
			plot.series(
					"centroid_" + countCentr,
					Plot.data().xy(centroid.x, centroid.y),
					Plot.seriesOpts().marker(Plot.Marker.SQUARE).color(colors.get(countCentr)).line(Plot.Line.NONE)
					);
			
			Data seriesData = Plot.data();
			for(DataPoint dp : points) {
				seriesData.xy(dp.x, dp.y);
			}
			
			plot.series("centroid_" + countCentr + "_data", 
						seriesData,
						Plot.seriesOpts().marker(Plot.Marker.CIRCLE).color(colors.get(countCentr)).line(Plot.Line.NONE)
					);
			
			countCentr++;
		}
		
		plot.save("scatterPlot", "png");
		
	}

	public void kMeans() {
		try {
			List<DataPoint> allPoints = readFromFile(fileName);
			doKMeans(allPoints);
			writeInfoToFile();
			createScatterPlot();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
