package rs_unit_test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.TargetBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Target;
import java.util.Objects;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class RedstoneUnitTestingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// PayloadTypeRegistry.playC2S().register(ClickedBlockPayload.ID, ClickedBlockPayload.CODEC);
		Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
		// Send a packet to the server containing the click type and target
		// Right click
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			// Require no off-hand
			if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
			if (hitResult instanceof BlockHitResult) {
				ItemStack currentHeld = player.getMainHandStack();
				if (currentHeld.getItem() == Items.WOODEN_AXE) {
					ClickedBlockPayload payload = new ClickedBlockPayload(hitResult.getBlockPos(), ClickType.RIGHT);
					ClientPlayNetworking.send(payload);
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
		// Left click
		AttackBlockCallback.EVENT.register((player, world, hand, hitResult, direction) -> {
			// Require no off-hand
			if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
			ItemStack currentHeld = player.getMainHandStack();
			// Only sends packet if holding a wooden axe called Wand
			if (currentHeld.getItem() == Items.WOODEN_AXE) {
				ClickedBlockPayload payload = new ClickedBlockPayload(hitResult, ClickType.LEFT);
				ClientPlayNetworking.send(payload);
				// Swing hand but don't break block
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});

	}
}

