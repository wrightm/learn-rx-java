package com.wrightm.github.learn.rx.java.examples;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

public class GroupByExamples {
	
	public static void main(String[] args){
		java7();
		//java8();
	}
	
	public static void java7(){
	
		Observable.range(1,100)
				.groupBy(new Func1<Integer, Boolean>(){

					@Override
					public Boolean call(Integer t1) {
						return  (t1 % 2 == 0);
					}
				})
				// GroupedObservable is returned by groupBy. 
				// A GroupedObservable will cache the items it is to emit until such time as it is subscribed to
				.flatMap( new Func1<GroupedObservable<Boolean, Integer>,Observable<List<Integer>>>(){

					@Override
					public Observable<List<Integer>> call(
							GroupedObservable<Boolean, Integer> t1) {
						return t1.toList();
					}
					
                }).forEach(System.out::println);

        System.out.println("2--------------------------------------------------------------------------------------------------------");

		Observable.range(1, 100)
			.groupBy(new Func1<Integer, Boolean>() {

				@Override
				public Boolean call(Integer t1) {
					return (t1 % 2 == 0);
				}
			})
			.flatMap(new Func1<GroupedObservable<Boolean, Integer>,Observable<List<Integer>>>(){

				@Override
				public Observable<List<Integer>> call(
						GroupedObservable<Boolean, Integer> t1) {
					
					return t1.take(10).toList();
				}
				
			}).forEach(System.out::println);
		
		System.out.println("3--------------------------------------------------------------------------------------------------------");

		Observable.range(1, 100)
			.groupBy(new Func1<Integer, Boolean>() {

				@Override
				public Boolean call(Integer t1) {
					// TODO Auto-generated method stub
					return (t1 % 2 == 0);
				}
				
			})
			.flatMap(new Func1<GroupedObservable<Boolean, Integer>, Observable<List<Integer>>>() {

				@Override
				public Observable<List<Integer>> call(
						GroupedObservable<Boolean, Integer> t1) {
					// TODO Auto-generated method stub
					return t1
							// filter - emit only those items from an Observable that pass a predicate test
							.filter(new Func1<Integer, Boolean>() {

						@Override
						public Boolean call(Integer t1) {
							// TODO Auto-generated method stub
							return (t1 <= 20);
						}
					}).toList();
				}
			});
		
		System.out.println("4--------------------------------------------------------------------------------------------------------");

		Observable.range(1, 100)
			.groupBy(new Func1<Integer, Boolean>() {

				@Override
				public Boolean call(Integer t1) {
					// TODO Auto-generated method stub
					return (t1 % 2 == 0);
				}
			})
			.flatMap(new Func1<GroupedObservable<Boolean,Integer>,Observable<List<Integer>>>(){

				@Override
				public Observable<List<Integer>> call(
						GroupedObservable<Boolean, Integer> t1) {
					return t1
							// Group list into 20 items
							.take(20).toList();
				}
				
			})
			// take the first two lists in the stream
			.take(2).forEach(System.out::println);
			
		System.out.println("5--------------------------------------------------------------------------------------------------------");

		Observable.range(1, 100)
			.groupBy(new Func1<Integer, Boolean>(){

				@Override
				public Boolean call(Integer t1) {
					return (t1 % 2 == 0);
				}
				
			}).flatMap(new Func1<GroupedObservable<Boolean,Integer>, Observable<List<Integer>>>() {

				@Override
				public Observable<List<Integer>> call(
						GroupedObservable<Boolean, Integer> t1) {
					return t1
							// Once an item doesn't match the condition the stream ends
							// then the next one will start
							.takeWhile(new Func1<Integer, Boolean>() {

								@Override
								public Boolean call(Integer t1) {
									// TODO Auto-generated method stub
									return t1 < 30;
								}
							}).toList();
				}
			})
			// Filter the empty lists that fail the condition
			.filter(l -> !l.isEmpty())
			// invoke a function on each item emitted by the Observable; block until the Observable completes
			.forEach(System.out::println);
		
		System.out.println("6--------------------------------------------------------------------------------------------------------");

		 Observable.from(Arrays.asList("a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c"))
         .groupBy(n -> n)
         .flatMap(g -> {
             return g
            		 // take first 10
            		 .take(10)
            		 // reduce - apply a function to each emitted item, sequentially, and emit only the final accumulated value
            		 .reduce((s,s2) -> s + s2);
         }).forEach(System.out::println);
		 
		 System.out.println("7--------------------------------------------------------------------------------------------------------");

		 Observable.timer(0, 1, TimeUnit.MILLISECONDS)
         .groupBy(n -> n % 2 == 0)
         .flatMap(g -> {
             return g.take(10).toList();
         }).take(2).toBlocking().forEach(System.out::println);

		 System.out.println("8--------------------------------------------------------------------------------------------------------");

		 Observable.timer(0, 1, TimeUnit.MILLISECONDS)
         .take(20)
         .groupBy(n -> n % 2 == 0)
         .flatMap(g -> {
             return g.toList();
         }).toBlocking().forEach(System.out::println);

		 
	}
	
	public static void java8(){
		// odd/even into 2 lists
        Observable.range(1, 100)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.toList();
                }).forEach(System.out::println);

        System.out.println("2--------------------------------------------------------------------------------------------------------");

        // odd/even into lists of 10
        Observable.range(1, 100)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.take(10).toList();
                }).forEach(System.out::println);

        System.out.println("3--------------------------------------------------------------------------------------------------------");

        //odd/even into lists of 10
        Observable.range(1, 100)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.filter(i -> i <= 20).toList();
                }).forEach(System.out::println);

        System.out.println("4--------------------------------------------------------------------------------------------------------");

        //odd/even into lists of 20 but only take the first 2 groups
        Observable.range(1, 100)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.take(20).toList();
                }).take(2).forEach(System.out::println);

        System.out.println("5--------------------------------------------------------------------------------------------------------");

        //odd/even into 2 lists with numbers less than 30
        Observable.range(1, 100)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.takeWhile(i -> i < 30).toList();
                }).filter(l -> !l.isEmpty()).forEach(System.out::println);

        System.out.println("6--------------------------------------------------------------------------------------------------------");

        Observable.from(Arrays.asList("a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c"))
                .groupBy(n -> n)
                .flatMap(g -> {
                    return g.take(3).reduce((s, s2) -> s + s2);
                }).forEach(System.out::println);

        System.out.println("7--------------------------------------------------------------------------------------------------------");

        Observable.timer(0, 1, TimeUnit.MILLISECONDS)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.take(10).toList();
                }).take(2).toBlocking().forEach(System.out::println);

        System.out.println("8--------------------------------------------------------------------------------------------------------");

        Observable.timer(0, 1, TimeUnit.MILLISECONDS)
                .take(20)
                .groupBy(n -> n % 2 == 0)
                .flatMap(g -> {
                    return g.toList();
                }).toBlocking().forEach(System.out::println);
	}

}
