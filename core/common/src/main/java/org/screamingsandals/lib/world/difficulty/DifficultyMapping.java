package org.screamingsandals.lib.world.difficulty;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class DifficultyMapping extends AbstractTypeMapper<DifficultyHolder> {
    private static DifficultyMapping difficultyMapping;

    protected final BidirectionalConverter<DifficultyHolder> difficultyConverter = BidirectionalConverter.<DifficultyHolder>build()
            .registerP2W(DifficultyHolder.class, d -> d);

    protected DifficultyMapping() {
        if (difficultyMapping != null) {
            throw new UnsupportedOperationException("DifficultyMapping is already initialized!");
        }
        difficultyMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    @OfMethodAlternative(value = DifficultyHolder.class, methodName = "ofOptional")
    public static Optional<DifficultyHolder> resolve(Object difficulty) {
        if (difficultyMapping == null) {
            throw new UnsupportedOperationException("DifficultyMapping is not initialized yet.");
        }

        if (difficulty == null) {
            return Optional.empty();
        }

        return difficultyMapping.difficultyConverter.convertOptional(difficulty).or(() -> difficultyMapping.resolveFromMapping(difficulty));
    }

    public static <T> T convertDifficultyHolder(DifficultyHolder holder, Class<T> newType) {
        if (difficultyMapping == null) {
            throw new UnsupportedOperationException("DifficultyMapping is not initialized yet.");
        }
        return difficultyMapping.difficultyConverter.convert(holder, newType);
    }
}
