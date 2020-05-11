package com.zen3515.mcrestful.util;

import java.util.concurrent.Callable;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Input;

public class Utility {
	
	/**
	 * Schedule At FixedRate a function call
	 * @param callback - you can use lambda like this "() -> { return true; } # True mean done" 
	 * @param delayedTime - long: time in milliseconds
	 */
	public static void launchDelayScheduleFunction(Callable<Boolean> callback, long delayedTime, long period, int maxExecCount) {
		new java.util.Timer().scheduleAtFixedRate( 
	        new java.util.TimerTask() {
	        	private int executionCount = 0;
	        	private boolean isDone = false;
	        	
	            @Override
	            public void run() {
	                // your code here
	            	try {
	            		if(isDone == false && maxExecCount != 0 && executionCount < maxExecCount) {
	            			isDone = callback.call();
							executionCount += 1;
	            		} else {
	            			this.cancel();
	            		}
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        }, 
	        delayedTime, period
		);
	}
	
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

	/**
	 * press a key for a given milliseconds
	 * @param input - Input: key
	 * @param holdDuration - long: time in milliseconds
	 */
	public static void pressReleaseKey(Input input, long holdDuration) {
		KeyBinding.setKeyBindState(input, true);
		Utility.launchDelayFunction(() -> {
			KeyBinding.setKeyBindState(input, false);
			return null;
		}, holdDuration);
	}
	
	/**
	 * press a key for 1000 milliseconds
	 * @param input - Input: key
	 */
	public static void pressReleaseKey(Input input) {
		pressReleaseKey(input, 1000L);
	}
}
