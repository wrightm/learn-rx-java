package com.wrightm.github.learn.rx.java.examples;


import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.PublishSubject;

public class GroupByLogic {

	public static void main(String[] args){
		// Create a TestScheduler, which is useful for debugging. 
		// It allows you to test schedules of events by manually advancing the clock at whatever pace you choos
		final TestScheduler testScheduler = Schedulers.test();	
		final PublishSubject<PlayEvent> testPlayEventSubject = PublishSubject.<PlayEvent>create();
		TestSubscriber<StreamState> testSchedulerStateStream = new TestSubscriber<StreamState>();
		
		// Publish PlayEvents
		testPlayEventSubject
			// Group Play Event By Id
			.groupBy(new Func1<PlayEvent, Integer>() {

				@Override
				public Integer call(PlayEvent t1) {
					return t1.getOriginatorId();
				}
				
			})
			// 
			.flatMap(new Func1<GroupedObservable<Integer, PlayEvent>, Observable<StreamState>>() {

				@Override
				public Observable<StreamState> call(
						GroupedObservable<Integer, PlayEvent> groupedPlayEvents) {
					System.out.println("***** new group: " + groupedPlayEvents.getKey());
					return groupedPlayEvents
							/* Timeout after last event, and preventing memory leaks so that inactive streams are closed */
							// timeout - emit items from a source Observable, 
							// but issue an exception if no item is emitted in a specified timespan
                            .timeout(3, TimeUnit.HOURS, testScheduler)
                            /* So that consecutive identical playevents are skipped, can also use skipWhile */
                            // distinctUntilChanged - suppress duplicate consecutive items emitted by the source Observable
                            .distinctUntilChanged(new Func1<PlayEvent, String>() {

								@Override
								public String call(PlayEvent playEvent) {
									return playEvent.getSession();
								}
							})
							//
							// On error return an empty observable
							// 
							.onErrorResumeNext(new Func1<Throwable, Observable<PlayEvent>>() {

								@Override
								public Observable<PlayEvent> call(Throwable t1) {
									System.out.println("     ***** complete group: " + groupedPlayEvents.getKey());
	                                // complete if we timeout or have an error of any kind (this could emit a special PlayEvent instead
									return Observable.empty();
								}
							})
							// since we have finite groups we can `reduce` to a single value, otherwise use `scan` if you want to emit along the way
                            .reduce(new StreamState(), new Func2<StreamState, PlayEvent, StreamState>() {

								@Override
								public StreamState call(StreamState streamState, PlayEvent playEvent) {
									System.out.println("    state: " + streamState + "  event: " + playEvent.id + "-" + playEvent.session);
									streamState.addEvent(playEvent);
									return streamState;
								}
							
                            })
                            .filter(state -> {
                                // if using `scan` above instead of `reduce` you could conditionally remove what you don't want to pass down
                                return true;
                            });
							
							
				}
			})
			.doOnNext(new Action1<StreamState>() {

				@Override
				public void call(StreamState t1) {
					System.out.println(">>> Output State: " + t1);
				}
			})
			.subscribe(testSchedulerStateStream);
		
		// add items to observe
		testPlayEventSubject.onNext(createPlayEvent(1, "a"));
		testPlayEventSubject.onNext(createPlayEvent(2, "a"));
		// move scheduler clock forward by 2 Hours
		testScheduler.advanceTimeBy(2, TimeUnit.HOURS);
		
		// add items to observe
		testPlayEventSubject.onNext(createPlayEvent(1, "b"));
		// move scheduler clock forward by 2 Hours
		testScheduler.advanceTimeBy(2, TimeUnit.HOURS);
		
		// add items to observe
		testPlayEventSubject.onNext(createPlayEvent(1, "a"));
		testPlayEventSubject.onNext(createPlayEvent(2, "b"));
		
		// Get Sequence of events from the items observed
		System.out.println("onNext after 4 hours: " + testSchedulerStateStream.getOnNextEvents());
		
	}
	
	public static PlayEvent createPlayEvent(int id, String v) {
        return new PlayEvent(id, v);
    }
	
	public static class PlayEvent {
		
		private int id;
		private String session;
		
		public PlayEvent(final int id, final String session){
			this.id = id;
			this.session = session;
		}
		
		public final int getOriginatorId(){
			return id;
		}
		
		public final String getSession(){
			return session;
		}
	}
	
	
	public static class StreamState {
		
		private int id = -1;
		
		public void addEvent(PlayEvent event){
			if(id == -1){
				this.id = event.id;
			}
		}
		
		@Override
        public String toString() {
            return "StreamState => id: " + id;
        }
		
	}
}
