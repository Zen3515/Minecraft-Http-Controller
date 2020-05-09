package com.zen3515.mcrestful.util;

import java.util.concurrent.Callable;

public class Utility {
	/**
	 * Delayed a function call
	 * @param callback - you can use lambda like this "() -> { return null; }" 
	 * @param delayedTime - long: time in milliseconds
	 */
	public static void launchDelayFunction(Callable<?> callback, long delayedTime) {
		new java.util.Timer().schedule( 
	        new java.util.TimerTask() {
	            @Override
	            public void run() {
	                // your code here
	            	try {
						callback.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        }, 
	        delayedTime 
		);
	}
}
