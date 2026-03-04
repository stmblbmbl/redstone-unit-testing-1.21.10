package rs_unit_test;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedstoneUnitTesting implements ModInitializer {
	public static final String MOD_ID = "redstone-unit-testing";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		// Register what I want to happen every tick
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			ServerWorld overworld = server.getOverworld();
			BlockPos position = new BlockPos(0, 64, 0);
			BlockState blockstate = Blocks.REDSTONE_ORE.getDefaultState();
			overworld.setBlockState(position, blockstate, 2);
		});
	}
}