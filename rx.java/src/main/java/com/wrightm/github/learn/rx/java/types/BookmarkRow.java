package com.wrightm.github.learn.rx.java.types;

public class BookmarkRow {

	private final int videoId;
	private final int bookmarkId;
	
	public BookmarkRow(final int videoId, final int bookmarkId){
		this.videoId = videoId;
		this.bookmarkId = bookmarkId;
	}
	
	public final int getVideoID(){
		return this.videoId;
	}
	
	public final int getBookmarkID(){
		return this.bookmarkId;
	}
}
