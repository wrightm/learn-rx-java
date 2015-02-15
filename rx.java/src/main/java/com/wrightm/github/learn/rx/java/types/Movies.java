package com.wrightm.github.learn.rx.java.types;

import java.util.List;

import rx.Observable;

public class Movies {

	private final String name;
	private final Observable<Movie> videos;
	
	public Movies(final String name, final List<Movie> videos) {
		this.name = name;
		this.videos = Observable.from(videos);
	}
	
	public final String getName(){
		return name;
	}
	
	public final Observable<Movie> getVideos(){
		return videos;
	}
}
