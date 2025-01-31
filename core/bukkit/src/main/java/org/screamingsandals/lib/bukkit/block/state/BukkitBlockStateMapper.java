package org.screamingsandals.lib.bukkit.block.state;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.screamingsandals.lib.bukkit.block.BukkitBlockMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;

import java.util.Optional;

@Service(dependsOn = {
        BukkitBlockMapper.class
})
public class BukkitBlockStateMapper extends BlockStateMapper {

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BlockStateHolder> Optional<T> wrapBlockState0(Object blockState) {
        // ORDER IS IMPORTANT

        if (blockState instanceof Sign) {
            return Optional.of((T) new SignBlockStateHolder((Sign) blockState));
        }

        if (blockState instanceof TileState) {
            return Optional.of((T) new TileBlockStateHolder((TileState) blockState));
        }

        if (blockState instanceof BlockState) {
            return Optional.of((T) new GenericBlockStateHolder((BlockState) blockState));
        }

        return Optional.empty();
    }

    @Override
    protected <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock0(BlockHolder blockHolder) {
        return wrapBlockState0(blockHolder.as(Block.class).getState());
    }
}
