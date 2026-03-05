package rs_unit_test;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class ClickedBlockManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static ClickedBlockManager instance;
    // For accounting for repeated clicks
    private int lastClick;
    private int clickDifference;
    private Queue<ClickedBlockPayload> queue;
    private ClickedBlockManager() {
        this.queue = new LinkedList<>();
        this.register();
        Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
        LOGGER.info("Initialising ClickedBlockManager");
    }
    public static ClickedBlockManager getInstance() {
        if (instance == null) {
            instance = new ClickedBlockManager();
        }
        return instance;
    }
    private void register() {
        PayloadTypeRegistry.playC2S().register(ClickedBlockPayload.ID, ClickedBlockPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ClickedBlockPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                this.clickDifference = context.server().getTicks() - lastClick;
                this.lastClick = context.server().getTicks();
                this.handle(payload);
            });
        });
    }
    private void handle(ClickedBlockPayload payload) {
        // Ignore held down things
        if (this.clickDifference > 5) {
            LOGGER.info("Adding {} to queue", payload);
            this.queue.add(payload);
        }
    }
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public Optional<ClickedBlockPayload> getClickedBlock() {
        if (this.queue.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.queue.remove());
    }
}
