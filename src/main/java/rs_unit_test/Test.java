package rs_unit_test;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class Test {
    private boolean shouldTest = false;
    HashMap<BlockPos, Assertion> assertions;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    Test() {
        assertions = new HashMap<>();
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            if (shouldTest) {
                test(server);
            }
        });
    }
    private void test(MinecraftServer server) {
        ServerWorld overworld = server.getOverworld();
        boolean testFinished = true;
        for (Assertion assertion : assertions.values()) {
            // If any of the assertions hasn't finished yet, ignore the result
            if (assertion.assert_self(overworld, server.getTicks()).isEmpty()) {
                testFinished = false;
            }
        }
        if (testFinished) {
            shouldTest = false;
            boolean wasSuccessful = true;
            for (Assertion assertion : assertions.values()) {
                LOGGER.info("Assertion at {} returned {}", assertion.pos, assertion.result);
                if (Objects.equals(assertion.result, Optional.of(Boolean.FALSE))) {
                    wasSuccessful = false;
                }
            }
            // The test was a success! Return all assertions to original state
            for (Assertion assertion : assertions.values()) {
                assertion.reset();
            }
            // TODO make this go back to the original function somehow
            LOGGER.info("The test returned {}", wasSuccessful);
        }
    }
    public void runTest() {
        this.shouldTest = true;
    }
    public void addAssertion(Assertion assertion) {
        this.assertions.put(assertion.pos, assertion);
    }
    public void removeAssertion(BlockPos pos) {
        this.assertions.remove(pos);
    }
}
