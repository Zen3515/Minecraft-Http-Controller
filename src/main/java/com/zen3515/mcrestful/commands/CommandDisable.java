package com.zen3515.mcrestful.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.zen3515.mcrestful.MCRestful;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CommandDisable  implements Command<CommandSource>{

	private static final CommandDisable CMD = new CommandDisable();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("disable").executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		if(MCRestful.currentUser != null) {
			MCRestful.currentUser = null;
			MCRestful.socketRunnable.isRunning = false;
		} else {
			context.getSource().sendFeedback(new StringTextComponent("Not in use"), false);
		}
		return 0;
	}

}
