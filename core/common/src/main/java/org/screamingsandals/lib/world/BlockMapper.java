package org.screamingsandals.lib.world;

import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.Optional;

@AbstractService
@ServiceDependencies(dependsOn = {
        LocationMapper.class,
        MaterialMapping.class
})
public abstract class BlockMapper {
    protected BidirectionalConverter<BlockHolder> converter = BidirectionalConverter.<BlockHolder>build()
            .registerP2W(BlockHolder.class, e -> e);

    private static BlockMapper mapping;

    protected BlockMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        mapping = this;
    }

    public static Optional<BlockHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is not initialized yet.");
        }
        
        return mapping.converter.convertOptional(obj);
    }

    public static <T> BlockHolder wrapBlock(T block) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is not initialized yet.");
        }
        return mapping.converter.convert(block);
    }

    public static <T> T convert(BlockHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    public static BlockHolder getBlockAt(LocationHolder location) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        return mapping.getBlockAt0(location);
    }

    public static void setBlockAt(LocationHolder location, MaterialHolder material) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        mapping.setBlockAt0(location, material);
    }

    public static void breakNaturally(LocationHolder location) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        mapping.breakNaturally0(location);
    }

    protected abstract BlockHolder getBlockAt0(LocationHolder location);

    protected abstract void setBlockAt0(LocationHolder location, MaterialHolder material);

    protected abstract void breakNaturally0(LocationHolder location);
}
