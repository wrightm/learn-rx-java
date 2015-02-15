package com.wrightm.github.learn.rx.java.types;

import java.util.List;
import rx.Observable;

public class Movie {

	private final int id;
	private final String title;
	private final double rating;
	private Observable<Bookmark> bookmarks;
	private Observable<BoxArt> boxArts;
	private Observable<InterestingMoment> interestingMoments;
	
	public Movie(final int id, final String title, final double rating){
		this.id = id;
		this.title = title;
		this.rating = rating;
	}
	
	public Movie(final int id, final String title, final double rating, List<Bookmark> bookmarks, List<BoxArt> boxArts){
		this(id,title,rating);
		this.bookmarks = Observable.from(bookmarks);
		this.boxArts = Observable.from(boxArts); 
	}
	
	public Movie(final int id, final String title, final double rating, final List<Bookmark> bookmarks, List<BoxArt> boxArts, final List<InterestingMoment> interestingMoments){
		this(id, title, rating, bookmarks, boxArts);
		this.interestingMoments = Observable.from(interestingMoments);
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
	
	public final Observable<Bookmark> getBookmark(){
		return bookmarks;
	}
	
	public final Observable<BoxArt> getBoxArts(){
		return boxArts;
	}
	
	public final Observable<InterestingMoment> getInterstingMoments(){
		return interestingMoments;
	}
}
