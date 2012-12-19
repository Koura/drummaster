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
	private int currentFrameX;
	private int currentFrameY;
	private long frameTicker;
	private int framePeriod;
	private boolean animate;

	private int spriteWidth;
	private int spriteHeight;
	private int y;

	public TaikoAnim(Bitmap bitmap, int y, int width, int height, int fps) {

		this.currentFrameX = 0;
		this.currentFrameY = 0;
		this.frameNrX = 3;
		this.frameNrY = 4;
		this.y = y;
		this.bitmap = Bitmap.createScaledBitmap(bitmap, width * frameNrX,
				height * frameNrY, true);
		spriteWidth = this.bitmap.getWidth() / frameNrX;
		spriteHeight = this.bitmap.getHeight() / frameNrY;
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
		this.animate = false;
	}

	public void render(Canvas canvas) {
		Rect destRect = new Rect(0, this.y, spriteWidth, this.y + spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}

	public void animate() {
		this.animate = true;
	}
	
	public void setFrameY(int y) {
		this.currentFrameY = y;
	}
	public void update(long gameTime) {
		if (animate) {
			if (gameTime > frameTicker + framePeriod) {
				frameTicker = gameTime;
				this.currentFrameX++;
				if (this.currentFrameX >= this.frameNrX) {
					this.currentFrameX = 0;
					this.currentFrameY = 0;
					this.animate = false;
				}
			}
			this.sourceRect.left = this.currentFrameX * spriteWidth;
			this.sourceRect.right = this.sourceRect.left + spriteWidth;
			this.sourceRect.top = this.currentFrameY * spriteHeight;
			this.sourceRect.bottom = this.sourceRect.top + spriteHeight;
		}
	}
}
