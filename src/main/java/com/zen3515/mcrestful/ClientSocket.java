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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

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
