package rs_unit_test;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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

		ClickedBlockManager cbm = ClickedBlockManager.getInstance();
		TestManager tmgr = new TestManager();
		CommandManager cmgr = new CommandManager(tmgr);

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			while (!cbm.isEmpty()) {
				ClickedBlockPayload payload = cbm.getClickedBlock().orElseThrow();
				BlockPos pos = payload.pos();
				BlockState state = server.getOverworld().getBlockState(pos);
				switch (payload.click()) {
					case RIGHT -> {
						tmgr.newAssertion(new Assertion(state, pos, 1));
					}
					case LEFT -> {
						tmgr.newTestInput(new TestInput(pos, state, 1));
					}
				}
			}
		});
	}
}

