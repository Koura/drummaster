package com.example.mahoutaikomaster;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DonNote implements Note{

	private int x;
	private int y;
	private int radius;
	private Paint paint;
	
	public DonNote(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.radius = r;
		this.paint = new Paint();
		this.paint.setColor(Color.RED);
	}
	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawCircle(this.x, this.y, radius, this.paint);
	}

	@Override
	public void move(int tempoSpeed) {
		this.x -= tempoSpeed;
	}

}
