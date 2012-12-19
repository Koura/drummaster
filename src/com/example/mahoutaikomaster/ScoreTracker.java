package com.example.mahoutaikomaster;

public class ScoreTracker {

	private long score;
	private int hitStreak;
	private double bonus;
	
	public ScoreTracker() {
		this.score = 0;
		this.hitStreak = 0;
		this.bonus = 1.0;
	}
	
	public void addScore(Note note) {
		if(note != null) {
			score += bonus * note.getPoints();
			incrementBonus();
		} else {
			nullifyBonus();
		}
	}
	
	public int getStreak() {
		return hitStreak;
	}
	
	private void incrementBonus(){
		this.hitStreak++;
		this.bonus += 0.25;
	}
	
	private void nullifyBonus() {
		this.hitStreak = 0;
		this.bonus = 1.0;
	}
	public String streakToString() {
		String hits = "" + this.hitStreak;
		return hits;
	}
	public String scoreToString() {
		String points = "" + this.score;
		return points;
	}
}
