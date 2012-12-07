package com.example.mahoutaikomaster;

public class DrumNoteDetecter {

	private float cx;
	private float cy;
	private float donR;
	private float katsuR;
	
	public DrumNoteDetecter(int donX, int donY, int donR, int katsuR) {
		this.cx = donX;
		this.cy = donY;
		this.donR = donR;
		this.katsuR = katsuR;
	}
	
	public boolean isDon(float x, float y) {
		if(((x-this.cx)*(x-this.cx)) + ((y-this.cy)*(y-this.cy)) < (this.donR * this.donR)) {
			return true;
		}
		return false;
	}
	
	public boolean isKatsu(float x, float y) {
		if(!isDon(x, y)) {
			if(((x-this.cx)*(x-this.cx)) + ((y-this.cy)*(y-this.cy)) < (this.katsuR * this.katsuR)) {
				return true;
			}
		}
		return false;
	}
}
