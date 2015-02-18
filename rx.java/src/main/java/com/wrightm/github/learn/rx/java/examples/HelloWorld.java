package com.wrightm.github.learn.rx.java.examples;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.schedulers.Schedulers;

public class HelloWorld {

	public static void main(String[] args) {

        // Hello World
        Observable.create(subscriber -> {
            subscriber.onNext("Hello World!");
            subscriber.onCompleted();
        }).subscribe(System.out::println);

        System.out.println("2--------------------------------------------------------------------------------------------------------");

        // shorten by using helper method
        Observable.just("Hello", "World!")
                .subscribe(System.out::println);

        System.out.println("3--------------------------------------------------------------------------------------------------------");
        
        // add onError and onComplete listeners
        Observable.just("Hello World!")
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Done"));

        System.out.println("4--------------------------------------------------------------------------------------------------------");

        // expand to show full classes
        Observable.create(new OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World!");
                subscriber.onCompleted();
            }

        }).subscribe(new Subscriber<String>() {

            @Override
            public void onCompleted() {
                System.out.println("Done");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String t) {
                System.out.println(t);
            }

        });
        System.out.println("5--------------------------------------------------------------------------------------------------------");

        // add error propagation
        Observable.create(subscriber -> {
            try {
                subscriber.onNext("Hello World!");
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribe(System.out::println);

        System.out.println("6--------------------------------------------------------------------------------------------------------");

        // add concurrency (manually)
        Observable.create(subscriber -> {
            new Thread(() -> {
                try {
                    subscriber.onNext(getData());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }).start();
        }).subscribe(System.out::println);

        System.out.println("7--------------------------------------------------------------------------------------------------------");

        // add concurrency (using a Scheduler)
        Observable.create(subscriber -> {
            try {
                subscriber.onNext(getData());
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(System.out::println);

        System.out.println("8--------------------------------------------------------------------------------------------------------");

        // add operator
        Observable.create(subscriber -> {
            try {
                subscriber.onNext(getData());
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
            
        })
        //Creates and returns a Scheduler intended for IO-bound work.
        // The implementation is backed by an Executor thread-pool that will grow as needed.
        // This can be used for asynchronously performing blocking IO.
        .subscribeOn(Schedulers.io())
                .map(data -> data + " --> at " + System.currentTimeMillis())
                .subscribe(System.out::println);

        System.out.println("9--------------------------------------------------------------------------------------------------------");

        // add error handling
        Observable.create(subscriber -> {
            try {
                subscriber.onNext(getData());
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .map(data -> data + " --> at " + System.currentTimeMillis())
                .onErrorResumeNext(e -> Observable.just("Fallback Data"))
                .subscribe(System.out::println);

        System.out.println("10--------------------------------------------------------------------------------------------------------");

        // infinite
        Observable.create(subscriber -> {
            int i = 0;
            while (!subscriber.isUnsubscribed()) {
                subscriber.onNext(i++);
            }
        })
        // take the first 10 emitted from the stream
        .take(10).subscribe(System.out::println);

        System.out.println("11--------------------------------------------------------------------------------------------------------");
        
        //Hello World
        Observable.create(subscriber -> {
            throw new RuntimeException("failed!");
        }).onErrorResumeNext(throwable -> {
            return Observable.just("fallback value");
        }).subscribe(System.out::println);

        System.out.println("12--------------------------------------------------------------------------------------------------------");
        
        Observable.create(subscriber -> {
            throw new RuntimeException("failed!");
        }).onErrorResumeNext(Observable.just("fallback value"))
                .subscribe(System.out::println);
        
        System.out.println("13--------------------------------------------------------------------------------------------------------");
        
        Observable.create(subscriber -> {
            throw new RuntimeException("failed!");
        }).onErrorReturn(throwable -> {
            return "fallback value";
        }).subscribe(System.out::println);

        System.out.println("14--------------------------------------------------------------------------------------------------------");
        
        Observable.create(subscriber -> {
            throw new RuntimeException("failed!");
        })
        // This retries 3 times, each time incrementing the number of seconds it waits.
        .retryWhen(attempts -> {
            return attempts
            		// Combine streams, exception and integer streams
            		.zipWith(Observable.range(1, 3), (throwable, i) -> i)
            		// delay response for a number of seconds (1,2,3)
                    .flatMap(i -> {
                        System.out.println("delay retry by " + i + " second(s)");
                        return Observable.timer(i, TimeUnit.SECONDS);
                    })
                    // add empty emit and signal an error
                    .concatWith(Observable.error(new RuntimeException("Exceeded 3 retries")));
        })
        .subscribe(System.out::println, t -> t.printStackTrace());

        
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private static String getData() {
        return "Got Data!";
    }
	
}
