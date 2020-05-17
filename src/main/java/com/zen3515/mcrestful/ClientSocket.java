package com.zen3515.mcrestful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.zen3515.mcrestful.util.Utility;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;

public class ClientSocket implements Runnable{

	private ServerPlayerEntity player;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	public volatile boolean isRunning = false;
	private int holdingItemInex;
	
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
	
	private boolean handleMessage(String[] tokCommand) {
		String msg = tokCommand[0];
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
			mcInstance.enqueue(() -> mcInstance.displayGuiScreen((Screen)null));
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
	        break;
		case "ENCHANT_KNOCKBACK":
			MCRestful.LOGGER.debug("ENCHANT_KNOCKBACK case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.KNOCKBACK, 3);
			break;
		case "ENCHANT_SHARPNESS":
			MCRestful.LOGGER.debug("ENCHANT_SHARPNESS case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.SHARPNESS, 5);
			break;
		case "ENCHANT_FIRE_ASPECT":
			MCRestful.LOGGER.debug("ENCHANT_FIRE_ASPECT case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.FIRE_ASPECT, 2);
			break;
		case "ENCHANT_INFINITY":
			MCRestful.LOGGER.debug("ENCHANT_INFINITY case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.INFINITY, 1);
			break;
		case "ENCHANT_FLAME":
			MCRestful.LOGGER.debug("ENCHANT_FLAME case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.FLAME, 1);
			break;
		case "ENCHANT_UNBREAKING":
			MCRestful.LOGGER.debug("ENCHANT_UNBREAKING case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.UNBREAKING, 5);
			break;
		case "ENCHANT_POWER":
			MCRestful.LOGGER.debug("ENCHANT_POWER case: " + msg);
			Utility.addEnchantment(this.player.getHeldItemMainhand(), Enchantments.POWER, 5);
			break;
		case "CLEAR_INVENTORY":
			MCRestful.LOGGER.debug("CLEAR_INVENTORY case: " + msg);
			this.player.inventory.clear();
			break;
		case "POTION_INVISIBLE":
			MCRestful.LOGGER.debug("POTION_INVISIBLE case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 600, 0, false, true));
			break;
		case "POTION_SPEED":
			MCRestful.LOGGER.debug("POTION_SPEED case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.SPEED, 600, 0, false, true));
			break;
		case "POTION_FIRE_RESIST":
			MCRestful.LOGGER.debug("POTION_FIRE_RESIST case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600, 0, false, true));
			break;
		case "POTION_HEAL":
			MCRestful.LOGGER.debug("POTION_HEAL case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 600, 0, false, true));
			break;
		case "POTION_WATER_BREATHING":
			MCRestful.LOGGER.debug("POTION_WATER_BREATHING case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 600, 0, false, true));
			break;
		case "POTION_NIGHT_VISION":
			MCRestful.LOGGER.debug("POTION_NIGHT_VISION case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 600, 0, false, true));
			break;
		case "POTION_POISON":
			MCRestful.LOGGER.debug("POTION_POISON case: " + msg);
			this.player.addPotionEffect(new EffectInstance(Effects.POISON, 600, 0, false, true));
			break;
		case "FILL_HUNGER":
			MCRestful.LOGGER.debug("FILL_HUNGER case: " + msg);
			this.player.getFoodStats().setFoodLevel(this.player.getFoodStats().getFoodLevel()+1);
			break;
		case "CLEAR_ARMOR":
			MCRestful.LOGGER.debug("CLEAR_ARMOR case: " + msg);
			this.player.inventory.armorInventory.clear();
			break;
		case "CLEAR_EFFECT":
			MCRestful.LOGGER.debug("CLEAR_EFFECT case: " + msg);
			this.player.clearActivePotions();
			break;
		case "GAMEMODE_CREATIVE":
			MCRestful.LOGGER.debug("GAMEMODE_CREATIVE case: " + msg);
			this.player.setGameType(GameType.CREATIVE);
			break;
		case "GAMEMODE_SURVIVAL":
			MCRestful.LOGGER.debug("GAMEMODE_SURVIVAL case: " + msg);
			this.player.setGameType(GameType.SURVIVAL);
			break;
		case "SET_FIRE":
			MCRestful.LOGGER.debug("SET_FIRE case: " + msg);
			this.player.setFire(10);
			break;
		case "MOUNT_LOOKAT":
			MCRestful.LOGGER.debug("MOUNT_LOOKAT case: " + msg);
			if(mcInstance.objectMouseOver.getType() == RayTraceResult.Type.ENTITY) {
				final int mountingEntityId = ((EntityRayTraceResult)mcInstance.objectMouseOver).getEntity().getEntityId();
				this.player.getServer().enqueue(new TickDelayedTask(this.player.getServer().getTickCounter(), () -> {
					this.player.startRiding(this.player.world.getEntityByID(mountingEntityId), true); // server side
				}));
			}
			break;
		case "MOUSE_RIGHT_USE_ITEM":
			MCRestful.LOGGER.debug("MOUSE_RIGHT_USE_ITEM case: " + msg);
			ItemStack heldItem = this.player.inventory.getCurrentItem().copy();
			if(!heldItem.isEmpty() && (heldItem.getUseAction() == UseAction.EAT || heldItem.getUseAction() == UseAction.DRINK)) {
				KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindUseItem.getKey(), true);
				Utility.launchDelayScheduleFunction(() -> {
					ItemStack holdingItem = this.player.inventory.getCurrentItem().copy();
					if(!ItemStack.areItemStackTagsEqual(holdingItem, heldItem)) {
						KeyBinding.setKeyBindState(MCRestful.gameSettings.keyBindUseItem.getKey(), false);
						return true;
					}
					return false;
				}, 0L, 50L, 200); //At most 10 sec
			}
			break;
		case "DROP":
			MCRestful.LOGGER.debug("DROP case: " + msg);
			this.player.dropItem(this.player.inventory.getCurrentItem(), true);
			mcInstance.playerController.sendPacketDropItem(this.player.inventory.getCurrentItem());
			this.player.inventory.setInventorySlotContents(this.player.inventory.currentItem, ItemStack.EMPTY);
			break;
		case "SEL_INVENTORY":
			MCRestful.LOGGER.debug("SEL_INVENTORY case: " + msg);
			int selInex = Integer.parseInt(tokCommand[1]);
			if(selInex < 9) {
				mcInstance.player.inventory.currentItem = selInex;
			} else {
				this.holdingItemInex = selInex;
			}
			break;
		case "HOLD_INVENTORY":
			MCRestful.LOGGER.debug("HOLD_INVENTORY case: " + msg);
			int holdInex = Integer.parseInt(tokCommand[1]);
			if(holdInex < 9) {
				mcInstance.player.inventory.currentItem = holdInex;
			} else {
				this.holdingItemInex = holdInex;
			}
			break;
		case "PLACE_INVENTORY":
			MCRestful.LOGGER.debug("PLACE_INVENTORY case: " + msg);
			int placeInex = Integer.parseInt(tokCommand[1]);
			ItemStack fromItem = this.player.inventory.getStackInSlot(this.holdingItemInex);
			if(fromItem.isEmpty()) {
				break;
			}
			ItemStack toItem = this.player.inventory.getStackInSlot(placeInex);
			this.player.inventory.setInventorySlotContents(placeInex, fromItem);
			this.player.inventory.setInventorySlotContents(this.holdingItemInex, toItem);
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
				String[] tokCommand = command.split(",");
				handleMessage(tokCommand);
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
