package com.example.mahoutaikomaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;

public class GameLevelPanel extends SurfaceView implements SurfaceHolder.Callback {

	private LevelThread thread;
	private String avgFps;
	private Rect noteRect;
	private Rect noteNameRect;
	private Rect background;
	private String song;
	private Paint paint;
	
	public GameLevelPanel(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new LevelThread(getHolder(), this);
		setFocusable(true);
	}
	public GameLevelPanel(Context context, String song) {
		this(context);
		this.song = song;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while(retry) {
			try{
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (event.getY() > getHeight() - 50) {
				thread.setRunning(false);
				((Activity)getContext()).finish();
		    } 
		}
				
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		renderBackground(canvas);
		displayFps(canvas, avgFps);
	}

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	private void displayFps(Canvas canvas, String fps) {
		if(canvas != null && fps != null) {
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, this.getWidth() -50, 20, paint);
			canvas.drawText(song, this.getWidth()/2+20, this.getHeight()/2+20, paint);
		}
		
	}
	
	private void renderBackground(Canvas canvas) {
		if(canvas != null) {
			paint.setColor(Color.BLACK);
			canvas.drawRect(background, paint);
			paint.setARGB(255, 38, 38, 38);
			canvas.drawRect(noteRect, paint);
			paint.setColor(Color.WHITE);
			canvas.drawRect(noteNameRect, paint);
		}
	}
	public void initBackground() {
		this.paint = new Paint();
		this.noteRect = new Rect(this.getWidth()/5, this.getHeight()/4-33, this.getWidth(), (this.getHeight()/4)*2-33);
		this.noteNameRect = new Rect(this.getWidth()/5, ((this.getHeight()/4)*2)-29, this.getWidth(), ((this.getHeight()/4)*2)+1+(this.getHeight()/12)-30);
		this.background = new Rect(0,0,this.getWidth(),this.getHeight());
	}

}
