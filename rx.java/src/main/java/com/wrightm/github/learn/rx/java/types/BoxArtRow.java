package com.wrightm.github.learn.rx.java.types;

public class BoxArtRow {
	
	private final int videoId;
	private final int width;
	private final int height;
	private final String url;
	
	public BoxArtRow(final int videoId, final int width, final int height, final String url){
		this.videoId = videoId;
		this.width = width;
		this.height = height;
		this.url = url;
	}
	
	public final int getVideoID(){
		return videoId;
	}
	
	public final int getWidth(){
		return width;
	}

	public final int getHeight(){
		return height;
	}
	
	public final String getURL(){
		return url;
	}
}
