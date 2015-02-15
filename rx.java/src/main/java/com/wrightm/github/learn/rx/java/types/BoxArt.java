package com.wrightm.github.learn.rx.java.types;

public class BoxArt {

	private final int width;
	private final int height;
	private final String url;
	
	public BoxArt(final int width, final int height, final String url) {
		this.width = width;
		this.height = height;
		this.url = url;
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
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + width;
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		BoxArt other = (BoxArt) obj;
		if(height != other.height){
			return false;
		}
		if(url == null){
			if(url != other.url){
				return false;
			}
		} else if(!other.equals(url)){
			return false;
		}
		if(width != other.width){
			return false;
		}
		return true;
	}
}
