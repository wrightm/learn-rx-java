package com.wrightm.github.learn.rx.java.examples;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class ZipInterval {

	public static void main(String... args) {
        Observable<String> data = Observable.just("one", "two", "three", "four", "five");
        Observable
        // combine two observables/streams together.
        .zip(data, Observable.interval(1, TimeUnit.SECONDS), (d, t) -> {
            return d + " " + t+1;
        }).toBlocking().forEach(System.out::println);
        
    }
	
}
