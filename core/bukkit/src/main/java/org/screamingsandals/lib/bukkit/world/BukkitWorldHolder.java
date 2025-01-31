package org.screamingsandals.lib.bukkit.world;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.screamingsandals.lib.bukkit.particle.BukkitParticleConverter;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ConfigSerializable
public class BukkitWorldHolder extends BasicWrapper<World> implements WorldHolder {

    public BukkitWorldHolder(World wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUID();
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public int getMinY() {
        if (Reflect.hasMethod(wrappedObject, "getMinHeight")) {
            return wrappedObject.getMinHeight();
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxY() {
        return wrappedObject.getMaxHeight();
    }

    @Override
    public DifficultyHolder getDifficulty() {
        return DifficultyHolder.of(wrappedObject.getDifficulty());
    }

    @Override
    public DimensionHolder getDimension() {
        return DimensionHolder.of(wrappedObject.getEnvironment());
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(int x, int z) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(x, z));
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(LocationHolder location) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(location.as(Location.class)));
    }

    @Override
    public List<EntityBasic> getEntities() {
        return wrappedObject.getEntities().stream()
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getGameRuleValue(GameRuleHolder holder) {
        if (Reflect.has("org.bukkit.GameRule")) {
            return (T) wrappedObject.getGameRuleValue(holder.as(GameRule.class));
        } else {
            var val = wrappedObject.getGameRuleValue(holder.getPlatformName());
            if (val == null) {
                return null;
            }
            try {
                return (T) Integer.valueOf(val);
            } catch (Throwable ignored) {
                if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false")) {
                    return (T) Boolean.valueOf(val);
                } else {
                    return (T) val;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setGameRuleValue(GameRuleHolder holder, T value) {
        if (Reflect.has("org.bukkit.GameRule")) {
            wrappedObject.setGameRule((GameRule<T>) holder.as(GameRule.class), value);
        } else {
            wrappedObject.setGameRuleValue(holder.getPlatformName(), value.toString());
        }
    }

    @Override
    public long getTime() {
        return wrappedObject.getTime();
    }

    @Override
    public void setTime(long time) {
        wrappedObject.setTime(time);
    }

    @Override
    public void sendParticle(ParticleHolder particle, LocationHolder location) {
        wrappedObject.spawnParticle(
                particle.particleType().as(Particle.class),
                location.as(Location.class),
                particle.count(),
                particle.offset().getX(),
                particle.offset().getY(),
                particle.offset().getZ(),
                particle.particleData(),
                particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null,
                particle.longDistance()
        );
    }
}
