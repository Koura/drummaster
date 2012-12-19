package com.example.mahoutaikomaster;

import java.util.ArrayList;

public class CollisionDetecter {

	private int r;
	private int cx;
	private int cy;

	public CollisionDetecter(int x, int y, int radius) {
		this.cx = x;
		this.cy = y;
		this.r = radius;
	}

	public Note donDestroyed(ArrayList<Note> noteList) {
		int i = 0;
		double distance = 0;
		for (Note note : noteList) {
			if (i > 3) {
				return null;
			}
			if (note.isDestroyable()) {
				distance = Math.sqrt((cx - note.getX()) * (cx - note.getX())
						+ (cy - cy) * (cy - cy));
				if (distance < (r + note.getRadius())) {
					if (note.getClass().equals(DonNote.class)) {
						Note score = note;
						noteList.remove(i);
						return score;
					}
					return null;
				}
			}
			i++;
		}
		return null;
	}
	
	public Note katsuDestroyed(ArrayList<Note> noteList) {
		int i = 0;
		double distance = 0;
		for (Note note : noteList) {
			if (i > 3) {
				return null;
			}
			if (note.isDestroyable()) {
				distance = Math.sqrt((cx - note.getX()) * (cx - note.getX())
						+ (cy - cy) * (cy - cy));
				if (distance < (r + note.getRadius())) {
					if (note.getClass().equals(KatsuNote.class)) {
						Note score = note;
						noteList.remove(i);
						return score;
					}
					return null;
				}
			}
			i++;
		}
		return null;
	}
}
