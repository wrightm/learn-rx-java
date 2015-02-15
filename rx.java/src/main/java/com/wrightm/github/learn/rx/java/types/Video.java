package com.wrightm.github.learn.rx.java.types;

public class Video {

	private final int id;
	private final String title;
	private final double rating;
	private ComposableList<Bookmark> bookmarks;
	private ComposableList<BoxArt> boxArts;
    private ComposableList<InterestingMoment> interestingMoments;
    
    public Video(final int id, final String title, final double rating){
    	this.id = id;
    	this.title = title;
    	this.rating = rating;
    }
    
    public Video(final int id, final String title, final double rating, ComposableList<Bookmark> bookmarks, ComposableList<BoxArt> boxArts){
    	this(id,title,rating);
    	this.bookmarks = bookmarks;
    	this.boxArts = boxArts;
  
    }
    
    public Video(final int id, final String title, final double rating, ComposableList<Bookmark> bookmarks, ComposableList<BoxArt> boxArts, ComposableList<InterestingMoment> interestingMoments){
    	this(id,title,rating);
    	this.bookmarks = bookmarks;
    	this.boxArts = boxArts;
    	this.interestingMoments = interestingMoments;
    }
    
    public final int getID(){
    	return id;
    }
    
    public final String getTitle(){
    	return title;
    }
    
    public final double getRating(){
    	return rating;
    }
    
    public final ComposableList<Bookmark> getBookmarks(){
    	return bookmarks;
    }
    
    public final ComposableList<BoxArt> getBoxArt(){
    	return boxArts;
    }
    
    public final ComposableList<InterestingMoment> getInterestingMoments(){
    	return interestingMoments;
    }
}
