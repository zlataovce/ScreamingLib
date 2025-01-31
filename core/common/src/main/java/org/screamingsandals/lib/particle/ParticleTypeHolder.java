package org.screamingsandals.lib.particle;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class ParticleTypeHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return ParticleTypeMapping.convertParticleTypeHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    public static ParticleTypeHolder of(Object particle) {
        return ofOptional(particle).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    public static Optional<ParticleTypeHolder> ofOptional(Object particle) {
        if (particle instanceof ParticleTypeHolder) {
            return Optional.of((ParticleTypeHolder) particle);
        }
        return ParticleTypeMapping.resolve(particle);
    }
}
