package rs_unit_test;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class Test {
    // TODO this also needs to contain input cases like blocks
    private boolean shouldTest = false;
    Vector<Assertion> assertions;
    Vector<TestInput> inputs;
    private int numTicks;
    private int startTick;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    Test() {
        assertions = new Vector<>();
        inputs = new Vector<>();
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            if (shouldTest) {
                test(server);
            }
        });
    }
    private void test(MinecraftServer server) {
        ServerWorld overworld = server.getOverworld();
        boolean testFinished = true;
        numTicks += 1;
        for (TestInput input : inputs) {
            input.setBlock(overworld, server.getTicks() - startTick);
        }
        for (Assertion assertion : assertions) {
            // If any of the assertions hasn't finished yet, ignore the result
            if (assertion.assert_self(overworld, server.getTicks() - startTick).isEmpty()) {
                testFinished = false;
            }
        }
        if (testFinished) {
            shouldTest = false;
            boolean wasSuccessful = true;
            for (Assertion assertion : assertions) {
                LOGGER.info("Assertion at {} returned {}", assertion.pos, assertion.result);
                if (Objects.equals(assertion.result, Optional.of(Boolean.FALSE))) {
                    wasSuccessful = false;
                } else if (Objects.equals(assertion.result, Optional.of(Boolean.TRUE))) {
                    server.getPlayerManager().broadcast(Text.literal("Assertion at " + assertion.pos + " was successful"), true);
                }
            }
            if (wasSuccessful) {
                LOGGER.info("All assertions passed: the test passed.");
            } else {
                LOGGER.error("Some assertions failed: the test failed.");
                LOGGER.error("Failed assertions:");
                for (Assertion assertion : assertions) {
                    if (assertion.result.orElseThrow().equals(Boolean.FALSE)) {
                        LOGGER.error("Assertion at {} failed.", assertion.pos);
                        server.getPlayerManager().broadcast(Text.literal("Assertion at " + assertion.pos + " failed - was " + overworld.getBlockState(assertion.pos) + ", should have been " + assertion.state), true);
                    }
                }
            }
            // The test has finished! Return all assertions to original state
            for (Assertion assertion : assertions) {
                assertion.reset();
            }
            for (TestInput input : inputs) {
                input.reset(overworld);
            }
            // TODO make this go back to the original function somehow
            LOGGER.info("The test returned {}", wasSuccessful);
            server.getPlayerManager().broadcast(Text.literal("The test returned " + wasSuccessful), true);
        }
    }
    public void runTest(MinecraftServer server) {
        this.shouldTest = true;
        this.numTicks = 0;
        startTick = server.getTicks() + 1;
    }
    public void addAssertion(Assertion assertion) {
        this.assertions.add(assertion);
    }
    public void removeAssertion(BlockPos pos) {
        assertions.removeIf(assertion -> assertion.pos.equals(pos));
    }
    public void removeAssertion(int index) {
        assertions.remove(index);
    }
    public void listAssertions() {
        for (int i = 0; i < assertions.size(); i++) {
            LOGGER.info("Assertion {}: {}, {}", i, assertions.get(i).pos, assertions.get(i).state );
        }
    }
    public void listAssertions(CommandContext<ServerCommandSource> context) {
        for (int i = 0; i < assertions.size(); i++) {
            int finalI = i;
            context.getSource().sendFeedback(() -> Text.literal("Assertion " + finalI + ": " + assertions.get(finalI).pos.toString() + " " + assertions.get(finalI).state.toString()), false);
        }
    }
    public void deleteAssertion(BlockPos pos) {
        this.assertions.removeIf(assertion -> assertion.pos.equals(pos));
    }
    public void deleteAssertion(CommandContext<ServerCommandSource> context, BlockPos pos) {
        for  (Assertion assertion : assertions) {
            if (assertion.pos.equals(pos)) {
                context.getSource().sendFeedback(() -> Text.literal("Deleting assertion: " + assertion.pos + " " + assertion.state), false);
            }
        };
        this.deleteAssertion(pos);
    }
    public void addInput(TestInput input) {
        this.inputs.add(input);
    }
    public void removeInput(BlockPos pos) {
        inputs.removeIf(input -> input.pos.equals(pos));
    }
    public void removeInput(int index) {
        inputs.remove(index);
    }
    public void listInputs() {
        for (int i = 0; i < inputs.size(); i++) {
            LOGGER.info("Input {}: {}, {}", i, inputs.get(i).pos, inputs.get(i).state );
        }
    }
    public void listInputs(CommandContext<ServerCommandSource> context) {
        for (int i = 0; i < inputs.size(); i++) {
            int finalI = i;
            context.getSource().sendFeedback(() -> Text.literal("Input " + finalI + ": " + inputs.get(finalI).pos.toString() + " " + inputs.get(finalI).state.toString()), false);
        }
    }
    public void deleteInput(BlockPos pos) {
        this.inputs.removeIf(input -> input.pos.equals(pos));
    }
    public void deleteInput(CommandContext<ServerCommandSource> context, BlockPos pos) {
        for  (TestInput input : inputs) {
            if (input.pos.equals(pos)) {
                context.getSource().sendFeedback(() -> Text.literal("Deleting input: " + input.pos + " " + input.state), false);
            }
        };
        this.deleteAssertion(pos);
    }
    public void setInputTick(BlockPos pos, int tick) {
        for (TestInput input : inputs) {
            input.tick = tick;
        }
    }
    public void setInputTick(int index, int tick) {
        inputs.get(index).tick = tick;
    }
    public void setAssertionTick(BlockPos pos, int tick) {
        for (Assertion assertion : assertions) {
            assertion.tick = tick;
        }
    }
    public void setAssertionTick(int index, int tick) {
        assertions.get(index).tick = tick;
    }
}
