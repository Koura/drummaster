package com.example.mahoutaikomaster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera.Size;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;

public class GameLevelPanel extends SurfaceView implements
		SurfaceHolder.Callback {

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
	private Bitmap noteTarget;
	private TaikoAnim taiko;
	private DrumNoteDetecter noteDetecter;
	private CollisionDetecter collision;
	private ScoreTracker scores;

	public GameLevelPanel(Context context) {
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
		this.taikoDrum = BitmapFactory.decodeResource(getResources(),
				R.drawable.taiko);
		this.noteTarget = BitmapFactory.decodeResource(getResources(),
				R.drawable.target);
		this.scores = new ScoreTracker();
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
		while (retry) {
			try {
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
			if (noteDetecter.isDon(x, y)) {
				if (x > this.getWidth() / 2) {
					this.taiko.setFrameY(1);
				}
				this.taiko.animate();
				this.dj.playDrum(0);
				synchronized (noteList) {
					scores.addScore(this.collision.donDestroyed(noteList));
				}
			}
			if (noteDetecter.isKatsu(x, y)) {
				if (x > this.getWidth() / 2) {
					this.taiko.setFrameY(3);
				} else {
					this.taiko.setFrameY(2);
				}
				this.taiko.animate();
				this.dj.playDrum(1);
				synchronized (noteList) {
					scores.addScore(this.collision.katsuDestroyed(noteList));
				}
			}
			if (x < 50 && y < this.getHeight() - 50) {
				thread.setRunning(false);
				this.dj.stopSong();
				((Activity) getContext()).finish();
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
		displayText(canvas, avgFps);
	}

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	private void drawNotes(Canvas canvas) {
		paint.setARGB(255, 227, 227, 227);
		synchronized (noteList) {
			for (Note note : noteList) {
				note.render(canvas);
			}
		}
	}

	//displays fps, hit streak, song name and points 
	private void displayText(Canvas canvas, String fps) {
		if (canvas != null && fps != null) {
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, 0, 20, paint);
			canvas.drawText(song, this.getWidth() / 2 - 20, 20, paint);
			canvas.drawText(scores.scoreToString(),
					this.getWidth() - this.getWidth() / 10, 20, paint);
			paint.setColor(Color.BLACK);
			paint.setTextSize(paint.getTextSize() + 6);
			if (scores.getStreak() > 0) {
				canvas.drawText(scores.streakToString(), this.getWidth() / 13,
						this.getHeight() / 4 - 33 + (this.getHeight() / 8),
						paint);
			}
			paint.setTextSize(paint.getTextSize() - 6);
		}

	}

	private void renderBackground(Canvas canvas) {
		if (canvas != null) {
			paint.setColor(Color.BLACK);
			canvas.drawRect(background, paint);
			canvas.drawBitmap(taikoDrum, 0, this.getHeight() - this.getHeight()
					/ 2, null);
			paint.setARGB(255, 38, 38, 38);
			canvas.drawRect(noteRect, paint);
			paint.setColor(Color.WHITE);
			canvas.drawRect(noteNameRect, paint);
			canvas.drawBitmap(noteTarget, this.getWidth() / 5 + this.getWidth()
					/ 30, this.getHeight() / 4 - 33 + this.getHeight() / 8
					- this.getHeight() / 13, null);
		}
	}

	//initializes images and classes etc. 
	public void init() {
		this.paint = new Paint();

		this.noteRect = new Rect(this.getWidth() / 5,
				this.getHeight() / 4 - 33, this.getWidth(),
				(this.getHeight() / 4) * 2 - 33);

		this.noteNameRect = new Rect(this.getWidth() / 5,
				((this.getHeight() / 4) * 2) - 29, this.getWidth(),
				((this.getHeight() / 4) * 2) + 1 + (this.getHeight() / 12) - 30);

		this.background = new Rect(0, 0, this.getWidth(), this.getHeight());

		this.dj.playSong(song);

		this.taikoDrum = Bitmap.createScaledBitmap(this.taikoDrum,
				this.getWidth(), this.taikoDrum.getHeight() - 10, true);
		this.noteTarget = Bitmap.createScaledBitmap(this.noteTarget,
				this.getHeight() / 13 * 2, this.getHeight() / 13 * 2, true);

		this.taiko = new TaikoAnim(BitmapFactory.decodeResource(getResources(),
				R.drawable.taiko2), this.getHeight() / 8 - 1,
				this.getWidth() / 5, this.getHeight() / 4, 30);

		this.noteDetecter = new DrumNoteDetecter(this.getWidth() / 2,
				this.getHeight(), this.getHeight() / 3, this.getHeight() / 3
						+ this.getHeight() / 6);

		this.collision = new CollisionDetecter(this.getWidth() / 5
				+ this.getWidth() / 30 + this.getHeight() / 13,
				this.getHeight() / 4 - 33 + (this.getHeight() / 8),
				this.getHeight() / 13);
	}

	// updates note position and may add new notes to noteList
	public void update(long currentTime) {
		taiko.update(currentTime);
		if ((currentTime - lastLine) >= 2000) {
			lastLine = currentTime;
			noteList.add(new TempoLine(this.getWidth(),
					this.getHeight() / 4 - 33, (this.getHeight() / 4) * 2 - 33));
			if (Math.random()<0.5) {

				noteList.add(new DonNote(this.getWidth(), this.getHeight() / 4
						- 33 + (this.getHeight() / 8), this.getHeight() / 13));
			} else {
				noteList.add(new KatsuNote(this.getWidth(), this.getHeight() /4 - 33 + (this.getHeight()/8), this.getHeight()/13));
			}
		}
		synchronized (noteList) {
			for (Iterator<Note> iterator = noteList.iterator(); iterator
					.hasNext();) {
				Note note = iterator.next();
				note.move(tempoSpeed);
				if (note.getX() <= maxX) {
					iterator.remove();
				}
			}
		}
	}

	// max x-coordinate until notes are automatically removed
	public void setMaxX() {
		this.maxX = this.getWidth() / 5;
	}

}
