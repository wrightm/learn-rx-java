package com.wrightm.github.learn.rx.java.examples;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class FlowControlDebounceBuffer {

	public static void main(String args[]){
		java7();
	}
	
	public static void java7(){
		// debounce to the last value in each burst
		// only emit an item from the source Observable after a particular timespan has passed without the Observable emitting any other items
        //intermittentBursts().debounce(10, TimeUnit.MILLISECONDS).toBlocking().forEach(System.out::println);
		
		/*
    	 * A Connectable Observable resembles an ordinary Observable, 
    	 * except that it does not begin emitting items when it is subscribed to, 
    	 * but only when its connect() method is called. 
    	 * In this way you can wait for all intended Subscribers to subscribe to the Observable before the Observable begins emitting items.
    	 *  
    	 */
    	
        /* The following will emit a buffered list up to 20 items in total as it is debounced */
        // first we multicast the stream ... using refCount so it handles the subscribe/unsubscribe
    	// take - emit only the first n items emitted by an Observable
    	// publish - convert an ordinary Observable into a connectable Observable
    	// refCount - The RefCount operator automates the process of connecting to and disconnecting from a connectable Observable.
    	// It operates on a connectable Observable and returns an ordinary Observable.
		// When the first observer subscribes to this Observable, 
		// RefCount connects to the underlying connectable Observable. 
		// RefCount then keeps track of how many other observers subscribe to it and does not disconnect from the underlying connectable Observable until the last observer has done so.
        Observable<Integer> burstStream = intermittentBursts().take(20).publish().refCount();
        // then we get the debounced version
        Observable<Integer> debounced = burstStream.debounce(10, TimeUnit.MILLISECONDS);
        // then the buffered one that uses the debounced stream to demark window start/stop
        // buffer - periodically gather items from an Observable into bundles and emit these bundles rather than emitting the items one at a time
        // when used with debounce it will collect for each 10 milliseconds
        Observable<List<Integer>> buffered = burstStream.buffer(debounced);
        // then we subscribe to the buffered stream so it does what we want
        buffered.toBlocking().forEach(System.out::println);
	}
	
	public static Observable<Integer> intermittentBursts(){
		return Observable.create(new OnSubscribe<Integer>() {

			@Override
			public void call(Subscriber<? super Integer> subscriber) {
				//
				// Stops emitting data onces the subscriber unsubscribes
				//
				while (!subscriber.isUnsubscribed()){
					// Emit a number of numbers on the stream
					for(int i = 0; i < Math.random()*20; i++){
						System.out.println("onNext: "+i);
						subscriber.onNext(i);
					}
					try {
						// sleep for a random amount of time
						Thread.sleep((long) (Math.random() * 1000));
					} catch (Exception e){
						// do nothing
					}
				}
			}
		})
		//
		// specify which Scheduler an Observable should use when its subscription is invoked
		//
		.subscribeOn(Schedulers.newThread()); // new thread since blocking
	}
}
