package com.example.mahoutaikomaster;

import android.graphics.Canvas;
import android.graphics.Paint;

public class TempoLine implements Note {
	private int x;
	private int yTop;
	private int yBot;
	private Paint paint;
	
	public TempoLine(int x,int yTop, int yBot) {
		this.x = x;
		this.yTop = yTop;
		this.yBot = yBot;
		this.paint = new Paint();
		this.paint.setARGB(255, 227, 227, 227);
	}
	
	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getyTop() {
		return yTop;
	}

	public void setyTop(int yTop) {
		this.yTop = yTop;
	}

	public int getyBot() {
		return yBot;
	}

	public void setyBot(int yBot) {
		this.yBot = yBot;
	}

	public void move(int tempoSpeed) {
		this.x -= tempoSpeed;
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawLine(this.x, this.yTop, this.x, this.yBot, this.paint);
	}
	
	public int getPoints() {
		return 0;
	}
	
	public boolean isDestroyable() {
		return false;
	}

	public int getRadius() {
		return 0;
	}
}
