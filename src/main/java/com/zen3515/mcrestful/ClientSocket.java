package com.zen3515.mcrestful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import com.zen3515.mcrestful.util.Utility;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

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
				if(Math.abs(pitchYaw.x - targetPitch_up) < 0.5d) return null;
//				this.player.rotateTowards(pitchYaw.y, targetPitch);
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y, pitchYaw.x - 0.35f);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return null;
			}, 0L, 10L, 50);
			break;
		case "PAN_DOWN":
			final float pitch_down = this.player.getPitchYaw().x;
			final double targetPitch_down = (pitch_down <= 91 && pitch_down > 44) ? 90.0d: (pitch_down <= 44 && pitch_down > -1) ? 45.0d: (pitch_down <= -1 && pitch_down > -46) ? 0.0d: -45.0d;
			MCRestful.LOGGER.debug("PAN_DOWN case: " + msg);
			Utility.launchDelayScheduleFunction(() -> {
				Vec3d pos = this.player.getPositionVector();
				Vec2f pitchYaw = this.player.getPitchYaw();			
				if(Math.abs(pitchYaw.x - targetPitch_down) < 0.5d) return null;
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y, pitchYaw.x + 0.35f);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return null;
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
					return null;
				}
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y - 0.35f, pitchYaw.x);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return null;
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
					return null;
				}
				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y + 0.35f, pitchYaw.x);
				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				return null;
			}, 0L, 10L, 50);
			break;
			
			
//			final float pitch_left = this.player.getPitchYaw().y;
////			final double targetYaw_left = (pitch_left <= 181 && pitch_left > 136) ? 135: (pitch_left <= 136 && pitch_left > 91) ? 90: (pitch_left <= 91 && pitch_left > 46) ? 45: 
////											(pitch_left <= 46 && pitch_left > 1) ? 0: (pitch_left <= 1 && pitch_left > -44) ? -45: (pitch_left <= -44 && pitch_left > -89) ? -90: (pitch_left <= -89 && pitch_left > -134) ? -135: -180;
//			final double targetYaw_left = (pitch_left <= 1 && pitch_left > -44) ? -45: (pitch_left <= -44 && pitch_left > -89) ? -90: (pitch_left <= -89 && pitch_left > -134) ? -135: (pitch_left <= -134 && pitch_left > -179) ? -180: 
//										  (pitch_left <= -179 && pitch_left > -224) ? -225: (pitch_left <= -224 && pitch_left > -269) ? -270: (pitch_left <= -269 && pitch_left > -314) ? -315: (pitch_left <= -314 && pitch_left > -359) ? -360: -45;
//			MCRestful.LOGGER.debug("PAN_LEFT case: " + msg);
//			this.player.sendMessage(new StringTextComponent("targetYaw_left " + targetYaw_left), ChatType.CHAT);
//			Utility.launchDelayScheduleFunction(() -> {
//				Vec3d pos = this.player.getPositionVector();
//				Vec2f pitchYaw = this.player.getPitchYaw();			
//				if(Math.abs(pitchYaw.y - targetYaw_left) < 0.5d) return null;
//				this.player.setPositionAndRotation(pos.x, pos.y, pos.z, pitchYaw.y - 0.35f, pitchYaw.x);
//				this.player.setPositionAndUpdate(pos.x, pos.y, pos.z);
//				return null;
//			}, 0L, 10L, 50);
//			break;
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
