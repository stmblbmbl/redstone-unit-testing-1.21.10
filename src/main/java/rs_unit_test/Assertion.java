package rs_unit_test;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class Assertion {
    final int MAX_TICKS = 100;
    BlockState state;
    BlockPos pos;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    int tick;
    Optional<Boolean> result;
    public Assertion(BlockState state, BlockPos pos, int tick) {
        this.state = state;
        this.pos = pos;
        this.tick = tick;
        this.result = Optional.empty();
    }
    public Optional<Boolean> assert_self(ServerWorld world, int tick) {
        if (result.isEmpty()) {
            if (tick == this.tick | this.tick < 0) {
                if (world.getBlockState(this.pos) == this.state) {
                    this.result = Optional.of(Boolean.TRUE);
                    return this.result;
                }
                this.result = Optional.of(Boolean.FALSE);
                return this.result;
            } else if (tick > MAX_TICKS) {
                LOGGER.info("Timed out assertion {}", this.toString());
                this.result = Optional.of(Boolean.FALSE);
            }
            return Optional.empty();
        }
        return result;
    }
    public void reset() {
        this.result = Optional.empty();
    }
}
