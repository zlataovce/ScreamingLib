package org.screamingsandals.lib.world.dimension;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class DimensionMapping extends AbstractTypeMapper<DimensionHolder> {
    private static DimensionMapping dimensionMapping;

    protected final BidirectionalConverter<DimensionHolder> dimensionConverter = BidirectionalConverter.<DimensionHolder>build()
            .registerP2W(DimensionHolder.class, d -> d);

    protected DimensionMapping() {
        if (dimensionMapping != null) {
            throw new UnsupportedOperationException("DimensionMapping is already initialized!");
        }
        dimensionMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    @OfMethodAlternative(value = DimensionHolder.class, methodName = "ofOptional")
    public static Optional<DimensionHolder> resolve(Object dimension) {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }

        if (dimension == null) {
            return Optional.empty();
        }

        return dimensionMapping.dimensionConverter.convertOptional(dimension).or(() -> dimensionMapping.resolveFromMapping(dimension));
    }

    public static <T> T convertDimensionHolder(DimensionHolder holder, Class<T> newType) {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }
        return dimensionMapping.dimensionConverter.convert(holder, newType);
    }
}
