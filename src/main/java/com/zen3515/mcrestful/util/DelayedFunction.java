package com.zen3515.mcrestful.util;

import java.util.TimerTask;
import java.util.concurrent.Callable;

@Deprecated
public class DelayedFunction extends TimerTask {
	
	private Callable<?> callback;
	private Long delayedTime;
	
	public DelayedFunction(Callable<?> callback, Long delayedTime) {
		this.callback = callback;
		this.delayedTime = delayedTime;
	}

	@Override
	public void run() {
		
	}

}
