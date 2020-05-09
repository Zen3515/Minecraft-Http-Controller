package com.zen3515.mcrestful.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zen3515.mcrestful.MCRestful;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public final class ModCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> cmdTut = dispatcher.register(
				Commands.literal(MCRestful.MOD_ID)
					.then(CommandEnable.register(dispatcher)) //.then(CommandDisable.register(dispatcher))
					.then(CommandDisable.register(dispatcher))
		);
		dispatcher.register(Commands.literal("mcr").redirect(cmdTut)); //for short
	}
}
