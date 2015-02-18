package com.wrightm.github.learn.rx.java.examples;

import java.util.ArrayList;

import rx.Observable;

public class ScanVsReduceExample {

	public static void main(String... args) {
        
		Observable.range(0, 10)
        // reduce - apply a function to each emitted item, sequentially, and emit only the final accumulated value
		// Accumulate then return
        .reduce(new ArrayList<>(), (list, i) -> {
            list.add(i);
            return list;
        }).forEach(System.out::println);

        System.out.println("reduce vs scan");

        Observable.range(0, 10)
        // scan - apply a function to each item emitted by an Observable, sequentially, and emit each successive value
        // return list for each sucessive value
        .scan(new ArrayList<>(), (list, i) -> {
            list.add(i);
            return list;
        }).forEach(System.out::println);
    }
}
