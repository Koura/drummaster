package com.example.mahoutaikomaster;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class LevelThread extends Thread{
	private static final String TAG = LevelThread.class.getSimpleName();
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private GameLevelPanel gamePanel;
	
	private final static int MAX_FPS = 50;
	private final static int MAX_FRAME_SKIPS = 5;
	private final static int FRAME_PERIOD = 1000/ MAX_FPS;
	
	private final static int STAT_INTERVAL = 10000;
	private final static int FPS_HISTORY_NR = 10;
	
	private long lastStatusStore = 0;
	private long statusIntervalTimer = 0l;

	private DecimalFormat df = new DecimalFormat("0.##");
	private long totalFramesSkipped = 0l;
	private long framesSkippedPerStatCycle = 0l;
	
	private int frameCountPerStatCycle = 0;
	private long totalFrameCount = 0l;
	
	private double fpsStore[];
	
	private long statsCount = 0;
	private double averageFps = 0.0;
	
	public LevelThread(SurfaceHolder surfaceHolder, GameLevelPanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "starting game loop");
		initTimingElements();
		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;
		this.gamePanel.init();
		this.gamePanel.setMaxX();
		sleepTime = 0;
		
		while(running) {
			canvas = null;
			try{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					this.gamePanel.update(System.currentTimeMillis());
					this.gamePanel.onDraw(canvas);
					timeDiff = System.currentTimeMillis() - beginTime;
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					if(sleepTime > 0 ) {
						try {
							Thread.sleep(sleepTime);
						} catch(InterruptedException e) {}
					}
					while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						this.gamePanel.update(System.currentTimeMillis());
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
					if(framesSkipped > 0) {
						Log.d(TAG, "Skipped: " + framesSkipped);
					}
					framesSkippedPerStatCycle += framesSkipped;
					storeStats();
				}								
			} finally {
				if(canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			
		}
	}
	private void storeStats() {
		frameCountPerStatCycle++;
	    totalFrameCount++;
			 	        
	    statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);
			 
		if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
			          
			double actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));
			 
			         
			fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;
		 
			            
			statsCount++;
		 
			double totalFps = 0.0;
		            
			for (int i = 0; i < FPS_HISTORY_NR; i++) {
		    totalFps += fpsStore[i];
		    }
			 
			           
			if (statsCount < FPS_HISTORY_NR) {
			              
				averageFps = totalFps / statsCount;
			} else {
				averageFps = totalFps / FPS_HISTORY_NR;
			}
				totalFramesSkipped += framesSkippedPerStatCycle;
	    
			    framesSkippedPerStatCycle = 0;
			    statusIntervalTimer = 0;
		        frameCountPerStatCycle = 0;
			 
			    statusIntervalTimer = System.currentTimeMillis();
			    lastStatusStore = statusIntervalTimer;
		
			     gamePanel.setAvgFps("FPS: " + df.format(averageFps));
		}
	}
	private void initTimingElements() {
		fpsStore = new double[FPS_HISTORY_NR];
		for (int i = 0; i < FPS_HISTORY_NR; i++) {
			fpsStore[i] = 0.0;
		}
		Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialised.");
	}
}
