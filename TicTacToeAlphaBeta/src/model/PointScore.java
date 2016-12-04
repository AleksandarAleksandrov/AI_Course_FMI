package model;

public class PointScore {
	
	int score;
    Point point;

    public PointScore(int score, Point point) {
        this.score = score;
        this.point = point;
    }
    
    public Point getPoint() {
		return point;
	}
    
    public void setPoint(Point point) {
		this.point = point;
	}
    
    public int getScore() {
		return score;
	}
    
    public void setScore(int score) {
		this.score = score;
	}
}
