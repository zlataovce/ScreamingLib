package org.screamingsandals.lib.world;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;

@AbstractService
public abstract class BlockDataMapper {
    protected BidirectionalConverter<BlockDataHolder> converter = BidirectionalConverter.<BlockDataHolder>build()
            .registerP2W(BlockDataHolder.class, e -> e);

    private static BlockDataMapper mapping;

    protected BlockDataMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("BlockDataMapper are already initialized.");
        }

        mapping = this;
    }

    public static Optional<BlockDataHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapper are not initialized yet.");
        }

        return mapping.converter.convertOptional(obj);
    }

    public static <T> T convert(BlockDataHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapper are not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    public static Optional<BlockDataHolder> getBlockDataAt(LocationHolder location) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapper are not initialized yet.");
        }

        return mapping.getBlockDataAt0(location);
    }

    public static void setBlockDataAt(LocationHolder location, BlockDataHolder blockData) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapper are not initialized yet.");
        }

        mapping.setBlockDataAt0(location, blockData);
    }

    protected abstract Optional<BlockDataHolder> getBlockDataAt0(LocationHolder location);

    protected abstract void setBlockDataAt0(LocationHolder location, BlockDataHolder blockData);
}
