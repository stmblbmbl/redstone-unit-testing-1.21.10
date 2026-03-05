package rs_unit_test;

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
    public boolean runTest(String name) {
        tests.get(name).runTest();
        return true;
    }
}
