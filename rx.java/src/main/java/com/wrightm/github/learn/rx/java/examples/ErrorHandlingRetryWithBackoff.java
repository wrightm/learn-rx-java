package com.wrightm.github.learn.rx.java.examples;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.Subscriber;

public class ErrorHandlingRetryWithBackoff {
	
	public static void main(String[] args){
		/*
         * retry(n) can be used to immediately retry n times
         */
		//java7();
		java8();
	}
	
	public static void java7(){
		
		/*
         * retry(n) can be used to immediately retry n times
         */
		Observable.create( new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> message) {
				message.onError(new RuntimeException("1) always fails"));
			}
			
		}).retry(3).subscribe(new Action1<String>() {

			@Override
			public void call(String message) {
				System.out.println("OnNext: "+message);
			}
		}, new Action1<Throwable>() {

			@Override
			public void call(Throwable exception) {
				System.out.println("Exception: "+exception.getMessage());
			}
		});
		
		/*
         * retryWhen allows custom behavior on when and if a retry should be done
        
		Observable.create(new OnSubscribe<Object>() {

			@Override
			public void call(Subscriber<? super Object> subscriber) {
				System.out.println("2) subscribing");
				subscriber.onError(new RuntimeException("2) always fails"));
			}
		})
		// if a source Observable emits an error, 
		// pass that error to another Observable to determine whether to resubscribe to the source
		.retryWhen(new Func1<Observable<Throwable>,Observable<Long>>() {

			@Override
			public Observable<Long> call(Observable<Throwable> exception) {
				return exception
						//
						// Combines the one or more Streams and allows you to combine them and return some observable
						//
						.zipWith(Observable.range(1,3), new Func2<Throwable, Integer, Integer>() {

							@Override
							public Integer call(Throwable arg0, Integer arg1) {
								return arg1;
							}
				})
				.flatMap(new Func1<Integer, Observable<Long>>() {

					@Override
					public Observable<Long> call(Integer arg0) {
						System.out.println("2) delay retry by " + arg0 + " second(s)");
						return Observable.timer(arg0, TimeUnit.SECONDS);
					}
				})
				//
				// emit the emissions from two or more Observables without interleaving them. concat 1 stream to the end of another
				//
				.concatWith(Observable.error(new RuntimeException("Failed after 3 retries")));
			}
			
		})
		//
		//
		//
		.toBlocking()
		//
		//
		.forEach(System.out::println);
		**/
	}
	
	public static void java8(){
		
		/*
         * retry(n) can be used to immediately retry n times
         */
		Observable.create(s -> {
            System.out.println("1) subscribing");
            s.onError(new RuntimeException("1) always fails"));
        }).retry(3).subscribe(System.out::println, t -> System.out.println("1) Error: " + t));

        System.out.println("");
        
        /*
         * retryWhen allows custom behavior on when and if a retry should be done
         */
        Observable.create(s -> {
            System.out.println("2) subscribing");
            s.onError(new RuntimeException("2) always fails"));
        }).retryWhen(attempts -> {
            return attempts.zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(i -> {
                System.out.println("2) delay retry by " + i + " second(s)");
                return Observable.timer(i, TimeUnit.SECONDS);
            }).concatWith(Observable.error(new RuntimeException("Failed after 3 retries")));
        }).toBlocking().forEach(System.out::println);
	}

}
