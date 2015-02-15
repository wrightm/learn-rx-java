package com.wrightm.github.learn.rx.java.types;

public class InterestingMoment {
	
	private final String type;
	private final int time;
	
	public InterestingMoment(final String type, final int time){
		this.type = type;
		this.time = time;
	}

	public final String getType(){
		return type;
	}
	
	public final int getTime(){
		return time;
	}
	
	@Override
	public String toString(){
		return new StringBuilder().append("InterestingMoment{type=")
					.append(type)
					.append(", time=")
					.append(time)
					.append('}')
					.toString();
	}
	
}
