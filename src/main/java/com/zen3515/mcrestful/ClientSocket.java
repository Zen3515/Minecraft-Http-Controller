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
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
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
		final Minecraft mcInstance = Minecraft.getInstance();
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
			MCRestful.LOGGER.debug("MOUSE_LEFT case: " + msg);
			Utility.clickMouse();
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
			if(mcInstance.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
				KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindAttack.getKey(), true);
				BlockRayTraceResult target_hold_left = (BlockRayTraceResult)mcInstance.objectMouseOver;
				Material target_mat = mcInstance.world.getBlockState(target_hold_left.getPos()).getMaterial();
				Utility.launchDelayScheduleFunction(() -> {
					if(mcInstance.objectMouseOver.getType() != RayTraceResult.Type.BLOCK) {
						KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindAttack.getKey(), false);
						return true;
					}
					BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)mcInstance.objectMouseOver;
					if(	!Utility.isSameBlockPos(target_hold_left.getPos(), blockraytraceresult.getPos()) ||
						target_mat != mcInstance.world.getBlockState(blockraytraceresult.getPos()).getMaterial()) {
						KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindAttack.getKey(), false);
						return true;
					}
					return false;
				}, 0L, 50L, 200); //At most 10 sec
			}
			break;
		case "GAME_PAUSE":
			MCRestful.LOGGER.debug("GAME_PAUSE case: " + msg);
			mcInstance.displayInGameMenu(false);
			break;
		case "GAME_RESUME":
			MCRestful.LOGGER.debug("GAME_RESUME case: " + msg);
//			mcInstance.setGameFocused(true);
//			Utility.pressReleaseKey(InputMappings.getInputByName("key.keyboard.escape"));
			this.player.sendMessage(new StringTextComponent("mcInstance.isGameFocused() = " + mcInstance.isGameFocused() + mcInstance.mouseHelper.isMouseGrabbed()), ChatType.CHAT);
			mcInstance.currentScreen.keyPressed(256, 0, 0); //press esc
			this.player.sendMessage(new StringTextComponent("mcInstance.isGameFocused() = " + mcInstance.isGameFocused() + mcInstance.mouseHelper.isMouseGrabbed()), ChatType.CHAT);
//			mcInstance.displayGuiScreen((Screen)null);
//			mcInstance.mouseHelper.ungrabMouse();
//			mcInstance.setGameFocused(true);
//			mcInstance.mouseHelper.grabMouse(); //TODO: bug does not grab the mouse
//			mcInstance.currentScreen.setFocused(p_setFocused_1_);
			break;
		case "WEATHER_RAIN":
			MCRestful.LOGGER.debug("WEATHER_RAIN case: " + msg);
			this.player.getServerWorld().getWorldInfo().setClearWeatherTime(0);
			this.player.getServerWorld().getWorldInfo().setRainTime(0);
			this.player.getServerWorld().getWorldInfo().setThunderTime(0);
			this.player.getServerWorld().getWorldInfo().setRaining(true);
			this.player.getServerWorld().getWorldInfo().setThundering(false);
			break;
		case "WEATHER_CLEAR":
			MCRestful.LOGGER.debug("WEATHER_CLEAR case: " + msg);
			this.player.getServerWorld().getWorldInfo().setClearWeatherTime(24000);
			this.player.getServerWorld().getWorldInfo().setRainTime(0);
			this.player.getServerWorld().getWorldInfo().setThunderTime(0);
			this.player.getServerWorld().getWorldInfo().setRaining(false);
			this.player.getServerWorld().getWorldInfo().setThundering(false);
			break;
		case "WEATHER_THUNDER":
			MCRestful.LOGGER.debug("WEATHER_THUNDER case: " + msg);
			this.player.getServerWorld().getWorldInfo().setClearWeatherTime(0);
			this.player.getServerWorld().getWorldInfo().setRainTime(0);
			this.player.getServerWorld().getWorldInfo().setThunderTime(0);
			this.player.getServerWorld().getWorldInfo().setRaining(true);
			this.player.getServerWorld().getWorldInfo().setThundering(true);
			break;
		case "TIME_DAY":
			MCRestful.LOGGER.debug("TIME_DAY case: " + msg);
			for(ServerWorld serverworld : this.player.getServer().getWorlds()) {
				serverworld.setDayTime(1000L);
			}
			break;
		case "TIME_LATE":
			MCRestful.LOGGER.debug("TIME_LATE case: " + msg);
			for(ServerWorld serverworld : this.player.getServer().getWorlds()) {
				serverworld.setDayTime(2000L);
			}
			break;
		case "TIME_NOON":
			MCRestful.LOGGER.debug("TIME_NOON case: " + msg);
			for(ServerWorld serverworld : this.player.getServer().getWorlds()) {
				serverworld.setDayTime(6000L);
			}
			break;
		case "TIME_NIGHT":
			MCRestful.LOGGER.debug("TIME_NIGHT case: " + msg);
			for(ServerWorld serverworld : this.player.getServer().getWorlds()) {
				serverworld.setDayTime(13000L);
			}
			break;
		case "TIME_MIDNIGHT":
			MCRestful.LOGGER.debug("TIME_MIDNIGHT case: " + msg);
			for(ServerWorld serverworld : this.player.getServer().getWorlds()) {
				serverworld.setDayTime(18000L);
			}
			break;
		case "DIFFICULTY_PEACEFUL":
			MCRestful.LOGGER.debug("DIFFICULTY_PEACEFUL case: " + msg);
			this.player.getServer().setDifficultyForAllWorlds(Difficulty.PEACEFUL, true);
			break;
		case "DIFFICULTY_HARD":
			MCRestful.LOGGER.debug("DIFFICULTY_HARD case: " + msg);
			this.player.getServer().setDifficultyForAllWorlds(Difficulty.HARD, true);
			break;
		case "SUMMON_PIG":
			MCRestful.LOGGER.debug("SUMMON_PIG case: " + msg);
			CompoundNBT compoundnbt = new CompoundNBT();
			compoundnbt.putString("id", EntityType.getKey(EntityType.PIG).toString());
			Vec3d posPig = Utility.getLookAt(100, this.player);
			Entity entity = EntityType.func_220335_a(compoundnbt, this.player.getServerWorld(), (pigEntity) -> {
				pigEntity.setLocationAndAngles(posPig.x, posPig.y, posPig.z, pigEntity.rotationYaw, pigEntity.rotationPitch);
	            return !this.player.getServerWorld().summonEntity(pigEntity) ? null : pigEntity;
	         });
			((MobEntity)entity).onInitialSpawn(this.player.getEntityWorld(), this.player.getEntityWorld().getDifficultyForLocation(new BlockPos(entity)), SpawnReason.COMMAND, (ILivingEntityData)null, (CompoundNBT)null);
			break;
		case "SUMMON_LIGHTNING":
			MCRestful.LOGGER.debug("SUMMON_LIGHTNING case: " + msg);
			Vec3d posLightning = Utility.getLookAt(100, this.player);
			LightningBoltEntity lightningboltentity = new LightningBoltEntity(this.player.getServerWorld(), posLightning.x, posLightning.y, posLightning.z, false);
	        this.player.getServerWorld().addLightningBolt(lightningboltentity);
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
