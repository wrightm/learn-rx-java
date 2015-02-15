package com.wrightm.github.learn.rx.java.types;

public class VideoRow {
	
	private final int listId;
	private final int videoId;
	private final String name;
	
	public VideoRow(final int listId, final int videoId, final String name){
		this.listId = listId;
		this.videoId = videoId;
		this.name = name;
	}

}
