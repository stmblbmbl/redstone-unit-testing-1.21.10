package rs_unit_test;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class TestInput {
    public BlockPos pos;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public BlockState state;

    public BlockState oldState;

    public int tick;
    public TestInput(BlockPos pos, BlockState state, int tick) {
        this.pos = pos;
        this.state = state;
        this.tick = tick;
    }
    public void setBlock(ServerWorld world, int tick) {
        if (tick == this.tick | this.tick < 0) {
            if (this.state != world.getBlockState(pos)) {
                this.oldState = world.getBlockState(pos);
            }
            world.setBlockState(pos, state);
        }
    }
    public void reset(ServerWorld world) {
        world.setBlockState(pos, oldState);
    }
}
