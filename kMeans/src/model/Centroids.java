package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Centroids {

	public Map<DataPoint, List<DataPoint>> centroids = new HashMap<>();

	public void constructCentroids(int kCentroids, double xMin, double xMax, double yMin, double yMax) {
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		centroids = new HashMap<>();
		for (int i = 0; i < kCentroids; i++) {
			double xPoint = tlr.nextDouble(xMax - xMin) + xMin;
			double yPoint = tlr.nextDouble(yMax - yMin) + yMin;
			DataPoint dp = new DataPoint(xPoint, yPoint);
			centroids.put(dp, new ArrayList<>());
		}

	}
	
	public DataPoint addDataToNearestCentroid(DataPoint dp) {
		DataPoint nearestCentroid = findNearestCentroid(dp);
		centroids.get(nearestCentroid).add(dp);
		dp.centroid = nearestCentroid;
		return nearestCentroid;
	}
	
	public void repositionCentroids() {
		DataPoint[] keys = new DataPoint[centroids.keySet().size()];
		int counter = 0;
		for(DataPoint centroid : centroids.keySet()) {
			keys[counter] = centroid;
			counter++;
		}
		
		for(int i = 0 ; i < keys.length; i++) {
			repositionCentroid(keys[i]);
		}
	}
	
	public DataPoint repositionCentroid(DataPoint centroid) {
		List<DataPoint> points = centroids.get(centroid);
		double xSum = 0;
		double ySum = 0;
		
		for(DataPoint dp : points) {
			xSum += dp.x;
			ySum += dp.y;
		}
		
		centroids.remove(centroid);
		DataPoint repositionedCentroid = new DataPoint(xSum / points.size() , ySum / points.size());
		centroids.put(repositionedCentroid, new ArrayList<>());
		return repositionedCentroid;
	}

	public DataPoint findNearestCentroid(DataPoint dp) {
		double distance = Double.POSITIVE_INFINITY;
		DataPoint closest = new DataPoint(0, 0);
		for (DataPoint centroid : centroids.keySet()) {
			double temp = Math.sqrt(
							Math.pow((dp.x - centroid.x), 2) + 
							Math.pow((dp.y - centroid.y), 2)
						);
			
			if(temp < distance) {
				distance = temp;
				closest = centroid;
			}
		}
		
		return closest;
	}

	public void clearCentroids() {
		Set<DataPoint> keys = centroids.keySet();
		centroids = new HashMap<>();
		for (DataPoint dp : keys) {
			centroids.put(dp, new ArrayList<>());
		}
	}

}
