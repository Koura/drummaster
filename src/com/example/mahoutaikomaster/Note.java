package com.example.mahoutaikomaster;

import android.graphics.Canvas;

public interface Note {
	public int getRadius();
    public int getX();
	public void render(Canvas canvas);
	public void move(int tempoSpeed);
	public int getPoints();
	public boolean isDestroyable();
}
