package com.zen3515.mcrestful.util;

import java.util.concurrent.Callable;

import com.zen3515.mcrestful.MCRestful;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Input;

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
