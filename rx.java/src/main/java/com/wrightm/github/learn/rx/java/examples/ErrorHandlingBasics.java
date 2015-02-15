package com.wrightm.github.learn.rx.java.examples;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Action;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class ErrorHandlingBasics {
	
	public static void main(String[] args){
		
		Observable.create(new Observable.OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onError(new RuntimeException("failed"));
			}
			
		}).subscribe(new Action1<String>() {

			@Override
			public void call(String message) {
				System.out.println("Shouldn't get here");
			}
		}, new Action1<Throwable>() {

			@Override
			public void call(Throwable exception) {
				System.out.println("Exception: "+ exception.getMessage());
			}
		});
		
		
		Observable<String> message = Observable.just("hello").map(new Func1<String, String>(){
			
			@Override
			public String call(String message) {
				System.out.println(message);
				if(message == "hello"){
					throw new RuntimeException("Pish");
				}
				return message;
			}
			
		});
		
		message.subscribe(new Action1<String>() {

			@Override
			public void call(String message) {
				System.out.println("Shouldnt get here");
			}
		}, new Action1<Throwable>() {

			@Override
			public void call(Throwable exception) {
				System.out.println(exception.getMessage());
			}
			
		});
		
		
		/*
		 * Conditionals that may return an error can be done in a flatMap
		 */
		Observable<String> message2 = Observable.just(true).flatMap(new Func1<Boolean, Observable<String>>(){

			@Override
			public Observable<String> call(Boolean condition) {
				if(condition){
					return Observable.error(new RuntimeException("failed"));
				} else {
					return Observable.just("data","here");
				}
			}
			
		});
		
		message2.subscribe(new Action1<String>(){

			@Override
			public void call(String message) {
				System.out.println("subscribe: "+message);
			}
			
		}, new Action1<Throwable>(){

			@Override
			public void call(Throwable exception) {
				System.out.println("Exception: "+exception.getMessage());
			}
			
		});
		
		/*
		 * Errors can be handled by Observables
		 */
		
		// error - emits nothing and signals an error
		// onErrorResumeNext - On an error an observable emits a string
		Observable<String> error = Observable.error(new RuntimeException("failed"));
		error.onErrorResumeNext(Observable.just("5) data"))
		.subscribe(System.out::println, t -> System.out.println("5) Error: " + t));
		
		/*
		 * A throwable is obtained by conditional logic
		 */
		Observable<Object> message3 = Observable.error(new IllegalStateException("6) failed"))
			.onErrorResumeNext( new Func1<Throwable,Observable<String> >(){

				@Override
				public Observable<String> call(Throwable exception) {
					if(exception instanceof IOException){
						return Observable.error(exception);
					} else {
						return Observable.just("6) data");
					}
					/*
					if(exception instanceof IllegalStateException){
						return Observable.error(exception);
					} else {
						return Observable.just("6) data");
					}
					*/
				}
				
			});
			
		message3.subscribe(new Action1<Object>() {

				@Override
				public void call(Object message) {
					System.out.println("Message: "+message);
				}
			}, new Action1<Throwable>(){

				@Override
				public void call(Throwable exception) {
					System.out.println("Exception: "+exception.getMessage());
				}
				
			});
		
	}

}
