package org.screamingsandals.lib.entity.damage;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class DamageCauseHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return DamageCauseMapping.convertDamageCauseHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    public boolean is(Object damageCause) {
        return equals(ofOptional(damageCause).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    public boolean is(Object... damageCauses) {
        return Arrays.stream(damageCauses).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    public static DamageCauseHolder of(Object damageCause) {
        return ofOptional(damageCause).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    public static Optional<DamageCauseHolder> ofOptional(Object damageCause) {
        if (damageCause instanceof DamageCauseHolder) {
            return Optional.of((DamageCauseHolder) damageCause);
        }
        return DamageCauseMapping.resolve(damageCause);
    }
}
