package com.wrightm.github.learn.rx.java.types;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ComposableList<T> extends Iterable<T> {

	// Transform from one object T to another object R
	public <R> ComposableList<R> map(Function<T, R> projectionFunction);
	
	// Filter abject from the list if it fails predicate test.
	public ComposableList<T> filter(Predicate<T> predicateFunction);
	
	// Transform one object to another List of objects R
	public <R> ComposableList<R> concatMap(Function<T, ComposableList<R>> projectionFunctionThatReturnsList);
	
	public ComposableList<T> reduce(BiFunction<T, T, T> combiner);
	
	public <R> ComposableList<T> reduce(R initialValue, BiFunction<R, T, R> combiner);
	
	public int size();
	
	public void forEach(Consumer<? super T> action);
	
	public T get(int index);
}
