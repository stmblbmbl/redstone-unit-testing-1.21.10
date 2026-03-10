package rs_unit_test;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class TestManager {
    HashMap<String, Test> tests;
    Test currentTest;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public TestManager() {
        tests = new HashMap<>();
    }
    public boolean newTest(String name) {
        // Close old test
        currentTest = null;
        tests.put(name, new Test());
        currentTest = tests.get(name);
        if (!tests.containsKey(name)) {LOGGER.info("Failed to add new test {}", name); }
        return tests.containsKey(name);
    }
    public void newAssertion(Assertion assertion) {
        LOGGER.info("New assertion {}", assertion);
        if (currentTest != null) {
            currentTest.addAssertion(assertion);
        }
    }
    public void removeAssertion(BlockPos pos) {
        if (currentTest != null) {
            currentTest.removeAssertion(pos);
        }
    }
    public void saveTest() {
        currentTest = null;
    }
    public Set<String> listTestNames() {
        return tests.keySet();
    }
    public void runTest(String name, MinecraftServer server) {
        tests.get(name).runTest(server);
    }
    public void listAssertions() {
        currentTest.listAssertions();
    }
    public void listAssertions(String name) {
        tests.get(name).listAssertions();
    }
    public void listAssertions(CommandContext<ServerCommandSource> context) {
        currentTest.listAssertions(context);
    }
    public void listAssertions(CommandContext<ServerCommandSource> context, String name) {
        tests.get(name).listAssertions(context);
    }
    public void deleteAssertion(BlockPos pos) {
        currentTest.deleteAssertion(pos);
    }
    public void deleteAssertion(BlockPos pos, String name) {
        tests.get(name).deleteAssertion(pos);
    }
    public void deleteAssertion(CommandContext<ServerCommandSource> context, BlockPos pos) {
        currentTest.deleteAssertion(context, pos);
    }
    public void deleteAssertion(CommandContext<ServerCommandSource> context, BlockPos pos, String name) {
        tests.get(name).deleteAssertion(context, pos);
    }
    public void listTestInputs() {
        currentTest.listInputs();
    }
    public void listTestInputs(String name) {
        tests.get(name).listInputs();
    }
    public void listTestInputs(CommandContext<ServerCommandSource> context) {
        currentTest.listInputs(context);
    }
    public void listTestInputs(CommandContext<ServerCommandSource> context, String name) {
        tests.get(name).listInputs(context);
    }
    public void deleteTestInput(BlockPos pos) {
        currentTest.deleteInput(pos);
    }
    public void deleteTestInput(BlockPos pos, String name) {
        tests.get(name).deleteInput(pos);
    }
    public void deleteTestInput(CommandContext<ServerCommandSource> context, BlockPos pos) {
        currentTest.deleteInput(context, pos);
    }
    public void deleteTestInput(CommandContext<ServerCommandSource> context, BlockPos pos, String name) {
        tests.get(name).deleteInput(context, pos);
    }
    public void newTestInput(TestInput input) {
        LOGGER.info("New input {}", input);
        if (currentTest != null) {
            currentTest.addInput(input);
        }
    }
    public void removeInput(BlockPos pos) {
        if (currentTest != null) {
            currentTest.removeInput(pos);
        }
    }
}
