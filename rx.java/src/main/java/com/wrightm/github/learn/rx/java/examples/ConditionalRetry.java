package com.wrightm.github.learn.rx.java.examples;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class ConditionalRetry {

	
	public static void main(String[] args){
		//runJava8();
		runJava7();
	}
	
	public static void runJava8 (){
		final AtomicInteger counter = new AtomicInteger();
		// create an onSubscribe observable
		// is invoked once there is a subscriber
		Observable<String> messagesWithRunTimeException = Observable.create((Subscriber<? super String> subscriber) -> {
			System.out.println("Execution: " + counter.get());
			if(counter.incrementAndGet() < 3){
				// Raise error until count is greater than 2 
				subscriber.onError(new RuntimeException("retryable"));
			} else {
				// return "hello" 
				subscriber.onNext("hello");
				// complete stream
				subscriber.onCompleted();
			}
		});
		
		final AtomicInteger counter2 = new AtomicInteger();
		Observable<String> messageWithIllegalStateException = Observable.create((Subscriber<? super String> message) -> {
			System.out.println("Execution ex2: " + counter2.get());
			if(counter2.incrementAndGet() < 3){
				message.onError(new RuntimeException("retryable"));
			} else {
				message.onError(new IllegalStateException());
			}
		});
		
		subscribeJava8(messagesWithRunTimeException);
		subscribeJava8(messageWithIllegalStateException);
	}
	
	public static void subscribeJava8(Observable<String> messages){
		// materialize converts the onNext, OnError and OnCompleted streams into one emitted stream.
		// flatMap - turns the observable into observable and flatens them into a single observable.
		messages = messages.materialize().flatMap(notification -> {
			// On error
			if(notification.isOnError()) {
				// then check if notification is an IllegalStateException
				// this is then converted to an observable which then will raise an IllegalStateException
				if(notification.getThrowable() instanceof IllegalStateException){
					// convert notification into an observable
					return Observable.just(notification);
				} else {
					// create an observable that emits nothing and signal an error
					return Observable.error(notification.getThrowable());
				}
			} else {
				// create an observable from 
				return Observable.just(notification);
			}
		// retry - if error is thrown resubscribe to the stream
		// dematerialize - undoes materialize to separate the onNext, onError, onCompleted emits.
		}).retry().dematerialize();
		
		// subscribe to stream, 
		// print line OnNext and print stack trace onError 
		messages.subscribe(System.out::println, t -> t.printStackTrace());
	}
	
	public static void runJava7(){
		
		AtomicInteger counter = new AtomicInteger();
		Observable<String> messagesWithRunTimeException = Observable.create(new Observable.OnSubscribe<String>(){

			@Override
			public void call(Subscriber<? super String> subscriber) {
				System.out.println("Exception: "+ counter.get());
				if(counter.incrementAndGet() < 3){
					subscriber.onError(new RuntimeException("retryable"));
				} else {
					subscriber.onNext("hello");
					subscriber.onCompleted();
				}
			}
			
		});
		
		AtomicInteger counter2 = new AtomicInteger();
		Observable<String> messagesWithIllegalStateException = Observable.create(new Observable.OnSubscribe<String>(){

			@Override
			public void call(Subscriber<? super String> subscriber) {
				System.out.println("Exception: "+ counter2.get());
				if(counter2.incrementAndGet() < 3){
					subscriber.onError(new RuntimeException("retryable"));
				} else {
					subscriber.onError(new IllegalStateException("illegal state"));
				}
			}
		});
		
		subscriberJava7(messagesWithRunTimeException);
		subscriberJava7(messagesWithIllegalStateException);
		
	}
	
	public static void subscriberJava7(Observable<String> messages){
		
		messages = messages.materialize().flatMap(new Func1<Notification<String>, Observable<Notification<String>>>() {

			@Override
			public Observable<Notification<String>> call(Notification<String> notification) {
				if(notification.isOnError()){
					if(notification.getThrowable() instanceof IllegalStateException){
						return Observable.just(notification);
					} else {
						return Observable.error(notification.getThrowable());
					}
				} else {
					return Observable.just(notification);
				}
			}
		}).retry().dematerialize();
		
		messages.subscribe(new Action1<String>() {

			@Override
			public void call(String message) {
				System.out.println("Message: " + message);
			}
		}, new Action1<Throwable>() {

			@Override
			public void call(Throwable exception) {
				System.out.println("Exception: "+ exception.getMessage());
			}
		});
	}
}
