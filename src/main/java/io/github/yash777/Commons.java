package io.github.yash777;

import java.io.IOException;

public class Commons {
	public static void sleepThread(long MILLISECONDS) {
		try {
			Thread.sleep( MILLISECONDS );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void sleepTimeUnit(int MINUTES) {
		try {
			java.util.concurrent.TimeUnit.MINUTES.sleep( MINUTES );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void getUserInput() {
		System.out.println("Enter something to stop recording...");
		try {
			System.in.read();
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
