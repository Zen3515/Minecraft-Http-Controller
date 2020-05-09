package com.zen3515.mcrestful.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.zen3515.mcrestful.ClientSocket;
import com.zen3515.mcrestful.MCRestful;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class CommandEnable implements Command<CommandSource>{
	
	private static final CommandEnable CMD = new CommandEnable();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("enable").executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		try {
			ServerPlayerEntity player = context.getSource().asPlayer();
			if(MCRestful.currentUser == null) {
				MCRestful.currentUser = player.getName().getString();
				MCRestful.socketRunnable = new ClientSocket(player);
				MCRestful.socketThread = new Thread(MCRestful.socketRunnable);
				MCRestful.socketThread.start();
			} else {
				context.getSource().sendFeedback(new StringTextComponent("Currently in use by: " + MCRestful.currentUser), false);
			}
		} catch (CommandSyntaxException e) {
			context.getSource().sendFeedback(new StringTextComponent("You need to be a player to use this command"), false);
		}
		return 0;
	}

}
