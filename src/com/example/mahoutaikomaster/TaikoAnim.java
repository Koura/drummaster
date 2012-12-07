package com.example.mahoutaikomaster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class TaikoAnim {

	private Bitmap bitmap;
	private Rect sourceRect;
	private int frameNrX;
	private int frameNrY;
	private int currentFrame;
	private long frameTicker;
	private int framePeriod;
	
	private int spriteWidth;
	private int spriteHeight;
	private int y;
	
	public TaikoAnim(Bitmap bitmap, int y, int width, int height, int fps) {

		this.currentFrame = 0;
		this.frameNrX = 3;
		this.frameNrY = 4;
		this.y = y;
		this.bitmap = Bitmap.createScaledBitmap(bitmap, width*frameNrX, height*frameNrY, true);
		spriteWidth = this.bitmap.getWidth() / frameNrX;
		spriteHeight = this.bitmap.getHeight() / frameNrY;
		sourceRect = new Rect(0,0, spriteWidth, spriteHeight);
		framePeriod = 1000/fps;
		frameTicker = 0l;
	}
	
	public void render(Canvas canvas) {
		Rect destRect = new Rect(0, this.y, spriteWidth, this.y+spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
	
}
