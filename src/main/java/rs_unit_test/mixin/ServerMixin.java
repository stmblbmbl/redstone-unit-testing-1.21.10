package rs_unit_test.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerMixin {
	// Run at end of loadWorld
	@Inject(at = @At("RETURN"), method = "loadWorld")
	private void init_return(CallbackInfo info) {
		MinecraftServer server = (MinecraftServer) (Object) this;
	}
}