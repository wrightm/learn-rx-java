package com.wrightm.github.learn.rx.java.examples;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FlowControlWindowExample {
	
	public static void main(String[] args){
		
		hotStream()
		// every 500 ms subdivide items from an Observable into Observable windows and emit these windows rather than emitting the items one at a time
		.window(500, TimeUnit.MILLISECONDS)
		.take(10)
		.flatMap(new Func1<Observable<Integer>, Observable<Integer>>() {

			@Override
			public Observable<Integer> call(Observable<Integer> t1) {
				return t1.startWith(999999999);
			}
			
		})
		.toBlocking().forEach(System.out::println);
		
	}

	/**
     * This is an artificial source to demonstrate an infinite stream that bursts intermittently
     */
    public static Observable<Integer> hotStream() {
        return Observable.create((Subscriber<? super Integer> s) -> {
            while (!s.isUnsubscribed()) {
                // burst some number of items
                for (int i = 0; i < Math.random() * 20; i++) {
                    s.onNext(i);
                }
                try {
                    // sleep for a random amount of time
                    // NOTE: Only using Thread.sleep here as an artificial demo.
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (Exception e) {
                    // do nothing
                }
            }
        }).subscribeOn(Schedulers.newThread()); // use newThread since we are using sleep to block
    }
}
