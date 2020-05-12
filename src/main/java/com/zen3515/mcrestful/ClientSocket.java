package com.zen3515.mcrestful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import com.zen3515.mcrestful.util.Utility;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientSocket implements Runnable{

	private ServerPlayerEntity player;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	public volatile boolean isRunning = false;
	
	public ClientSocket(ServerPlayerEntity player) {
		this.player = player;
		try {

            this.socket = new Socket("127.0.0.1", 8989);

            this.out = new PrintWriter(socket.getOutputStream(), true);

            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));    

//            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            MCRestful.LOGGER.info("We are sending test connection message from init");
            this.out.print("Testing connection please, from init");

        } catch (UnknownHostException e) {
        	MCRestful.LOGGER.error("Unknown Host.");
            System.err.println("Unknown Host.");
           // System.exit(1);
        } catch (IOException e) {
        	MCRestful.LOGGER.error("Couldn't get I/O for the connection.");
            System.err.println("Couldn't get I/O for the connection.");
          //  System.exit(1);
        }
	}
	
	private boolean handleMessage(String msg) {
		MCRestful.LOGGER.info("Processing message: " + msg);
		this.player.sendMessage(new StringTextComponent("Processing message: " + msg), ChatType.CHAT);
		if(msg == null) {
			return false;
		}		
		switch (msg) {
		case "Test":
			MCRestful.LOGGER.debug("Test case: " + msg);
			break;
		default:
			MCRestful.LOGGER.debug("Default case: " + msg);
			break;
		case "MOVE_FORWARD":
			MCRestful.LOGGER.debug("MOVE_FORWARD case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindForward.getKey());
			break;
		case "MOVE_BACKWARD":
			MCRestful.LOGGER.debug("MOVE_BACKWARD case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindBack.getKey());
			break;
		case "MOVE_LEFT":
			MCRestful.LOGGER.debug("MOVE_LEFT case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindLeft.getKey());
			break;
		case "MOVE_RIGHT":
			MCRestful.LOGGER.debug("MOVE_RIGHT case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindRight.getKey());
			break;
		case "STOP":
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindForward.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindBack.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindLeft.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindRight.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			break;
		case "WALK_FORWARD":
			MCRestful.LOGGER.debug("WALK_FORWARD case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindForward.getKey(), true);
			break;
		case "WALK_BACKWARD":
			MCRestful.LOGGER.debug("WALK_BACKWARD case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindBack.getKey(), true);
			break;
		case "WALK_LEFT":
			MCRestful.LOGGER.debug("WALK_LEFT case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindLeft.getKey(), true);
			break;
		case "WALK_RIGHT":
			MCRestful.LOGGER.debug("WALK_RIGHT case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), false);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindRight.getKey(), true);
			break;
		case "RUN":
			MCRestful.LOGGER.debug("RUN case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindForward.getKey(), true);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindSprint.getKey(), true);
			break;
		case "CROUCH":
			MCRestful.LOGGER.debug("CROUCH case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.field_228046_af_.getKey()); //API suck
			break;
		case "JUMP":
			MCRestful.LOGGER.debug("JUMP case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindJump.getKey(), 500L);
			break;
		case "JUMP_FORWARD":
			MCRestful.LOGGER.debug("JUMP_FORWARD case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindForward.getKey());
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindJump.getKey(), 500L);
			break;
		case "JUMP_BACKWARD":
			MCRestful.LOGGER.debug("JUMP_BACKWARD case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindBack.getKey());
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindJump.getKey(), 500L);
			break;
		case "JUMP_LEFT":
			MCRestful.LOGGER.debug("JUMP_LEFT case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindLeft.getKey());
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindJump.getKey(), 500L);
			break;
		case "JUMP_RIGHT":
			MCRestful.LOGGER.debug("JUMP_RIGHT case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindRight.getKey());
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindJump.getKey(), 500L);
			break;
		case "PAN_UP":
			final float pitch_up = this.player.getPitchYaw().x;
			final double targetPitch_up = (pitch_up <= 91 && pitch_up > 46) ? 45.0d: (pitch_up <= 46 && pitch_up > 1) ? 0.0d: (pitch_up <= 1 && pitch_up > -44) ? -45.0d: -90.0d;
			MCRestful.LOGGER.debug("PAN_UP case: " + msg);
			Utility.launchDelayScheduleFunction(() -> {
				Vec3d pos = this.player.getPositionVector();
				Vec2f pitchYaw = this.player.getPitchYaw();			
				if(Math.abs(pitchYaw.x - targetPitch_up) < 0.5d) return true;
//				this.player.rotateTowards(pitchYaw.y, targetPitch);
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y, pitchYaw.x - 0.35f);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return false;
			}, 0L, 10L, 50);
			break;
		case "PAN_DOWN":
			final float pitch_down = this.player.getPitchYaw().x;
			final double targetPitch_down = (pitch_down <= 91 && pitch_down > 44) ? 90.0d: (pitch_down <= 44 && pitch_down > -1) ? 45.0d: (pitch_down <= -1 && pitch_down > -46) ? 0.0d: -45.0d;
			MCRestful.LOGGER.debug("PAN_DOWN case: " + msg);
			Utility.launchDelayScheduleFunction(() -> {
				Vec3d pos = this.player.getPositionVector();
				Vec2f pitchYaw = this.player.getPitchYaw();			
				if(Math.abs(pitchYaw.x - targetPitch_down) < 0.5d) return true;
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y, pitchYaw.x + 0.35f);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return false;
			}, 0L, 10L, 50);
			break;
		case "PAN_LEFT":
			MCRestful.LOGGER.debug("PAN_LEFT case: " + msg);
			final float yaw_left = this.player.getPitchYaw().y;
			final double modRemain_left = (yaw_left % 45.0f);
			final double targetYaw_left = yaw_left < 0 ? 
								((modRemain_left <= -44.0f) ? yaw_left - modRemain_left - 90 :yaw_left - modRemain_left - 45) :
								((modRemain_left <= 1.0f) ? yaw_left - modRemain_left - 45 :yaw_left - modRemain_left);
			Utility.launchDelayScheduleFunction(() -> {
				Vec3d pos = this.player.getPositionVector();
				Vec2f pitchYaw = this.player.getPitchYaw();			
				if(Math.abs(pitchYaw.y - targetYaw_left) < 0.5d) {
					this.player.setPositionAndRotation(pos.x, pos.y, pos.z, (float) targetYaw_left, pitchYaw.x);
					this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
					return true;
				}
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y - 0.35f, pitchYaw.x);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return false;
			}, 0L, 10L, 50);
			break;
		case "PAN_RIGHT":
			MCRestful.LOGGER.debug("PAN_RIGHT case: " + msg);
			final float yaw_right = this.player.getPitchYaw().y;
			final double modRemain_right = (yaw_right % 45.0f);
			final double targetYaw_right = yaw_right < 0 ? 
								((modRemain_right >= -1.0f) ? yaw_right - modRemain_right + 45: yaw_right - modRemain_right) :
								((modRemain_right >= 44.0f) ? yaw_right - modRemain_right + 90: yaw_right - modRemain_right + 45);
			Utility.launchDelayScheduleFunction(() -> {
				Vec3d pos = this.player.getPositionVector();
				Vec2f pitchYaw = this.player.getPitchYaw();			
				if(Math.abs(pitchYaw.y - targetYaw_right) < 0.5d) {
					this.player.setPositionAndRotation(pos.x, pos.y, pos.z, (float) targetYaw_right, pitchYaw.x);
					this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
					return true;
				}
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y + 0.35f, pitchYaw.x);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return false;
			}, 0L, 10L, 50);
			break;
		case "PAN_FLIP":
			MCRestful.LOGGER.debug("PAN_FLIP case: " + msg);
			final float yaw_flip = this.player.getPitchYaw().y;
			final double targetYaw_flip = yaw_flip + 180;
			Utility.launchDelayScheduleFunction(() -> {
				Vec3d pos = this.player.getPositionVector();
				Vec2f pitchYaw = this.player.getPitchYaw();			
				if(Math.abs(pitchYaw.y - targetYaw_flip) < 6.5d) {
					this.player.setPositionAndRotation(pos.x, pos.y, pos.z, (float) targetYaw_flip, pitchYaw.x);
					this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
					return true;
				}
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y + 3.0f, pitchYaw.x);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return false;
			}, 0L, 10L, 500);
			break;
		case "MOUSE_LEFT":
			// TODO: is it too short or long, it doesn't work as weel, might need sweep animation ?
			MCRestful.LOGGER.debug("MOUSE_LEFT case: " + msg);
//			this.player.swingArm(Hand.MAIN_HAND);
//			Minecraft.getInstance().player.swingArm(Hand.MAIN_HAND);
//			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindAttack.getKey(), 150L);
//	        MinecraftForge.EVENT_BUS.post(new InputEvent.ClickInputEvent(MCRestful.gameSettings.keyBindAttack.getKey().getKeyCode(), MCRestful.gameSettings.keyBindAttack.getKeyBinding(), Hand.MAIN_HAND));
			final RayTraceResult rayTraceResult_ML = this.player.getEntityWorld().rayTraceBlocks(new RayTraceContext(this.player.getEyePosition(0f), this.player.getEyePosition(0f).add(this.player.getLookVec().scale(5)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, this.player));
			final Vec3d hitPosition_ML = rayTraceResult_ML.getHitVec();
			final BlockPos blockPosition_ML = new BlockPos(hitPosition_ML);
			final Minecraft mcInstance = Minecraft.getInstance();
//			BlockState blockState_ML = this.player.getEntityWorld().getBlockState(blockPosition_ML);
			if(!this.player.getEntityWorld().isAirBlock(blockPosition_ML)) {
				ClickInputEvent inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(0,  MCRestful.gameSettings.keyBindAttack, Hand.MAIN_HAND);
				if (inputEvent.isCanceled()) {
					if (inputEvent.shouldSwingHand()) {
						mcInstance.particles.addBlockHitEffects(blockPosition_ML, (BlockRayTraceResult)rayTraceResult_ML);
						mcInstance.player.swingArm(Hand.MAIN_HAND);
					}
					break;
			   }
			   Direction direction = ((BlockRayTraceResult)rayTraceResult_ML).getFace();
//			   if (mcInstance.playerController.onPlayerDamageBlock(blockPosition_ML, direction)) { // This does not break
			   if (mcInstance.playerController.clickBlock(blockPosition_ML, direction)) {
			      if (inputEvent.shouldSwingHand()) {
			    	  mcInstance.particles.addBlockHitEffects(blockPosition_ML, (BlockRayTraceResult)rayTraceResult_ML);
			    	  mcInstance.player.swingArm(Hand.MAIN_HAND);
			      }
			   }
			} else {
				mcInstance.playerController.resetBlockRemoving();
			}
			break;
		case "MOUSE_RIGHT":
			MCRestful.LOGGER.debug("MOUSE_RIGHT case: " + msg);
			Utility.pressReleaseKey(MCRestful.gameSettings.keyBindUseItem.getKey(), 100L);
			break;
		case "CHARGE":
			MCRestful.LOGGER.debug("CHARGE case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindUseItem.getKey(), true);
			break;
		case "RELEASE":
			MCRestful.LOGGER.debug("RELEASE case: " + msg);
			KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindUseItem.getKey(), false);
			break;
		case "MOUSE_LEFT_HOLD_UNTIL_BREAK":
			MCRestful.LOGGER.debug("MOUSE_LEFT_HOLD_UNTIL_BREAK case: " + msg);
			// Player can only reach 5 block
			// TODO: just ray trace every time to see if it break or not
			final RayTraceResult rayTraceResult = this.player.getEntityWorld().rayTraceBlocks(new RayTraceContext(this.player.getEyePosition(0f), this.player.getEyePosition(0f).add(this.player.getLookVec().scale(5)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, this.player));
			final Vec3d hitPosition = rayTraceResult.getHitVec();
			final BlockPos blockPosition = new BlockPos(hitPosition);
			BlockState blockState = this.player.getEntityWorld().getBlockState(blockPosition);
			this.player.sendMessage(new StringTextComponent("looking at: " + hitPosition.toString() + ", block: " + blockState.toString()), ChatType.CHAT);
			Utility.launchDelayScheduleFunction(() -> {	
				
				return false;
			}, 0L, 100L, 50);
			break;
		}
		return true;
	}

	@Override
	public void run() {
		this.isRunning = true;
		while (this.isRunning) {
			try {
				String command = this.in.readLine();
				handleMessage(command);
			} catch (IOException e) {
	        	MCRestful.LOGGER.error("I/O Error when readline\n" + e.getMessage());
			}
		}
		this.out.close();
        try {
            this.in.close();
			this.socket.close();
		} catch (IOException e) {
        	MCRestful.LOGGER.error("Couldn't get I/O for the connection.");
            System.err.println("Couldn't get I/O for the connection.");
		}
		this.player.sendMessage(new StringTextComponent("Stopped listening threads"), ChatType.CHAT);
	}
}
