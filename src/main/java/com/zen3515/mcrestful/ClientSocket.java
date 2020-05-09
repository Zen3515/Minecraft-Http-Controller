package com.zen3515.mcrestful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

public class ClientSocket implements Runnable{

	private ServerPlayerEntity player;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private volatile boolean isRunning = false;
	
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
		switch (msg) {
		case "Test":
			MCRestful.LOGGER.debug("Test case: " + msg);
			this.player.sendMessage(new StringTextComponent("Test case: " + msg), ChatType.GAME_INFO);
			break;
		default:
			MCRestful.LOGGER.debug("Default case: " + msg);
			this.player.sendMessage(new StringTextComponent("Default case: " + msg), ChatType.GAME_INFO);
			break;
		}
		return false;
	}

	@Override
	public void run() {
		this.isRunning = true;
//		this.player.sendMessage(new StringTextComponent("You just type enable, you're: " + player.getName().getString()), ChatType.GAME_INFO);
		while (this.isRunning) {
			try {
				String command = this.in.readLine();
				handleMessage(command);
			} catch (IOException e) {
	        	MCRestful.LOGGER.error("I/O Error when readline\n" + e.getMessage());
//				e.printStackTrace();
			}
//			this.player.setPositionAndUpdate(	this.player.getPositionVec().getX(), 
//												this.player.getPositionVec().getY()+2.0, 
//												this.player.getPositionVec().getZ());
//            MCRestful.LOGGER.info("We are sending test connection message");
//            this.out.print("Testing connection please");
			
			break;
		}
		this.out.close();
        try {
            this.in.close();
			this.socket.close();
		} catch (IOException e) {
        	MCRestful.LOGGER.error("Couldn't get I/O for the connection.");
            System.err.println("Couldn't get I/O for the connection.");
		}
	}
}
