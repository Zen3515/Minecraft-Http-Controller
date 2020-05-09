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
		// TODO Auto-generated method stub
		ServerPlayerEntity player = context.getSource().asPlayer();

		MCRestful.socketThread = new Thread(new ClientSocket(player));
		MCRestful.socketThread.start();
//		context.getSource().sendFeedback(new StringTextComponent("You just type enable, you're: " + player.getName().getString()), false);
		return 0;
	}
}
