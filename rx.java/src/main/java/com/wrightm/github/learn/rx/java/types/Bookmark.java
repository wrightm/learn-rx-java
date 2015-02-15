package com.wrightm.github.learn.rx.java.types;

public class Bookmark {
	
	private final int id;
	private final int offset;
	
	public Bookmark(final int id, final int offset) {
		this.id = id;
		this.offset = offset;
	}
	
	public final int getID(){
		return this.id;
	}
	
	public final int getOffset(){
		return this.offset;
	}
	
	@Override
	public String toString(){
		return new StringBuilder().append("BookMark{id=")
					.append(id)
					.append(", offset=")
					.append(offset)
					.append("}").toString();
	}
}
