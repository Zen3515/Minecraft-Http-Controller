package com.zen3515.mcrestful.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import com.zen3515.mcrestful.MCRestful;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Utility {
	
	public static final Utility instance = new Utility();
	
	@Nullable
	public static Robot keyboradRobot = null;
	static {
		Robot temp;
		try {
			temp = new Robot();
		} catch (AWTException e) {
			temp = null;
		}
		keyboradRobot = temp;
	}
	
	public static void pressKeyboardEsc() {
		Utility.pressKeyboard(KeyEvent.VK_ESCAPE, 50L);
	}
	
	public static void pressKeyboard(int key, long holdDuration) {
		if(Utility.keyboradRobot == null) {
			try {
				Utility.keyboradRobot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		
		Utility.keyboradRobot.keyPress(key);
		if(holdDuration > 0){
			Utility.launchDelayFunction(() -> {
				Utility.keyboradRobot.keyRelease(key);
				return null;
			}, holdDuration);
		} else {
			Utility.keyboradRobot.keyRelease(key);
		}
	}
	
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
	
	public static void clickMouse() {
		final Minecraft mcInstance = Minecraft.getInstance();
		if (mcInstance.objectMouseOver == null) {
            MCRestful.LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");

         } else if (!mcInstance.player.isRowingBoat()) {
            net.minecraftforge.client.event.InputEvent.ClickInputEvent inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(0, MCRestful.gameSettings.keyBindAttack, Hand.MAIN_HAND);
            if (!inputEvent.isCanceled())
            switch(mcInstance.objectMouseOver.getType()) {
            case ENTITY:
            	mcInstance.playerController.attackEntity(mcInstance.player, ((EntityRayTraceResult)mcInstance.objectMouseOver).getEntity());
               break;
            case BLOCK:
               BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)mcInstance.objectMouseOver;
               BlockPos blockpos = blockraytraceresult.getPos();
               if (!mcInstance.world.isAirBlock(blockpos)) {
            	   mcInstance.playerController.clickBlock(blockpos, blockraytraceresult.getFace());
                  break;
               }
            case MISS:
               mcInstance.player.resetCooldown();
               net.minecraftforge.common.ForgeHooks.onEmptyLeftClick(mcInstance.player);
            }
            if (inputEvent.shouldSwingHand())
            	mcInstance.player.swingArm(Hand.MAIN_HAND);
         }
	}
	
	public static boolean isSameBlockPos(BlockPos b1, BlockPos b2) {
		return b1.getX() == b2.getX() && b1.getY() == b2.getY() && b1.getZ() == b2.getZ();
	}
	
	public static Vec3d getLookAt(double distance, ServerPlayerEntity player) {
		final RayTraceResult rayTraceResult = player.getEntityWorld().rayTraceBlocks(new RayTraceContext(player.getEyePosition(0f), player.getEyePosition(0f).add(player.getLookVec().scale(distance)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player));
		final Vec3d hitPosition = rayTraceResult.getHitVec();
		return hitPosition;
	}
	
	public static boolean addEnchantment(ItemStack itemstack, Enchantment enchantmentIn, int level) {
		if (!itemstack.isEmpty() && enchantmentIn.canApply(itemstack)) {
			itemstack.addEnchantment(enchantmentIn, level);
			return true;
		}
		return false;
	}
}
