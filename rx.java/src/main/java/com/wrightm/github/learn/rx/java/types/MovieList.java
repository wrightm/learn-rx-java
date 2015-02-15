package com.wrightm.github.learn.rx.java.types;

public class MovieList {

	private final String name;
	private final ComposableList<Video> videos;
	
	public MovieList(final String name, final ComposableList<Video> videos){
		this.name = name;
		this.videos = videos;
	}
	
	public String getName(){
		return name;
	}
	
	public ComposableList<Video> getVideos(){
		return videos;
	}
	
	@Override
	public String toString(){
		return new StringBuilder().append("MovieList{")
				.append("name=")
				.append(name)
				.append(", videos=")
				.append(videos)
				.append('}')
				.toString();
				
	}
}
