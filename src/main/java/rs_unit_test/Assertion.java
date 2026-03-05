package rs_unit_test;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class Assertion {
    BlockState state;
    BlockPos pos;
    // If tick is negative, assume that its valid at any time
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
            }
            return Optional.empty();
        }
        return result;
    }
    public void reset() {
        this.result = Optional.empty();
    }
}
