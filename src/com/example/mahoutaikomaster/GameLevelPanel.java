package com.example.mahoutaikomaster;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private ArrayList<Note> noteList;
	private int maxX;
	private int tempoSpeed;
	private long lastLine;
	private Dj dj;
	private Bitmap taikoDrum;
	private TaikoAnim taiko;
	private DrumNoteDetecter noteDetecter;
	
	public GameLevelPanel(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new LevelThread(getHolder(), this);
		setFocusable(true);
	}
	public GameLevelPanel(Context context, String song) {
		this(context);
		this.noteList = new ArrayList<Note>();
		this.tempoSpeed = 3;
		this.lastLine = 0;
		this.song = song;
		this.dj = new Dj(context);
		this.taikoDrum = BitmapFactory.decodeResource(getResources(), R.drawable.taiko);
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
			float x = event.getX();
			float y = event.getY();
			if(noteDetecter.isDon(x, y)) {
				this.dj.playDrum(0);
		    } 
			if(noteDetecter.isKatsu(x, y)) {
				this.dj.playDrum(1);
			}
			if(x<50 && y<this.getHeight()-50) {
				thread.setRunning(false);
				this.dj.stopSong();
				((Activity)getContext()).finish();
			}
		}
				
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		renderBackground(canvas);
		taiko.render(canvas);
		drawNotes(canvas);
		displayFps(canvas, avgFps);
		canvas.drawBitmap(taikoDrum, 0, this.getHeight()-this.getHeight()/2, null);
		//paint.setColor(Color.BLUE);
		//canvas.drawCircle(this.getWidth()/2, this.getHeight(), this.getHeight()/3+this.getHeight()/6, paint);
		//paint.setColor(Color.RED);
		//canvas.drawCircle(this.getWidth()/2, this.getHeight(), this.getHeight()/3, paint);
	}

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}
	
	private void drawNotes(Canvas canvas) {
		paint.setARGB(255, 227, 227, 227);
		synchronized(noteList) {
			for(Note note : noteList) {
				note.render(canvas);
			}
		}
	}

	private void displayFps(Canvas canvas, String fps) {
		if(canvas != null && fps != null) {
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, 0, 20, paint);
			canvas.drawText(song, this.getWidth()/2-20, 20, paint);
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
		this.dj.playSong(song);
		this.taikoDrum = Bitmap.createScaledBitmap(this.taikoDrum, this.getWidth(), this.taikoDrum.getHeight()-10, true);
		taiko = new TaikoAnim(BitmapFactory.decodeResource(getResources(), R.drawable.taiko2), this.getHeight()/8-1 ,this.getWidth()/5, this.getHeight()/4, 5);
		noteDetecter = new DrumNoteDetecter(this.getWidth()/2, this.getHeight(), this.getHeight()/3, this.getHeight()/4+this.getHeight()/6);
	}
	public void update(long currentTime) {
		if((currentTime-lastLine) >= 4000) {
			lastLine = currentTime;
			noteList.add(new TempoLine(this.getWidth(), this.getHeight()/4-33, (this.getHeight()/4)*2-33));
			noteList.add(new DonNote(this.getWidth(), this.getHeight()/4-33+((this.getHeight()/4)/2), this.getHeight()/13));
		}
		synchronized(noteList) {
			for(Iterator<Note> iterator = noteList.iterator(); iterator.hasNext();) {
				Note note = iterator.next();
				note.move(tempoSpeed);
				if(note.getX() <= maxX) {
					iterator.remove();
				}
 			}
		}
	}
	
	public void setMaxX() {
		this.maxX = this.getWidth()/5;
	}

}
