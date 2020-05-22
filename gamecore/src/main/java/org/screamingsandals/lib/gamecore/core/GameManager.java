package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.data.file.JsonDataSource;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameLoadingEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameSavedEvent;
import org.screamingsandals.lib.gamecore.resources.SpawnerEditor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class GameManager<T extends GameFrame> {
    private final File dataFolder;
    private final Class<T> type;
    private final Map<String, Object> gameBuilders = new HashMap<>();
    private Map<String, T> registeredGames = new HashMap<>();

    public GameManager(File dataFolder, Class<T> type) {
        this.dataFolder = dataFolder;
        this.type = type;
    }

    public T loadGame(File gameFile) {
        if (gameFile.exists() && gameFile.isFile()) {
            final var dataSaver = new JsonDataSource<>(gameFile, type);
            final T game = dataSaver.load();

            if (game == null) {
                //TODO
                Debug.warn("Somethings fucked");
                return null;
            }

            if (game.checkIntegrity(true)) {
                //TODO
                Debug.warn("&cCannot load game &e" + game.getGameName() + "&c!");
                return null;
            }

            registerGame(game.getGameName(), game);
            return game;
        }
        return null;
    }

    public void saveGame(T gameFrame) {
        if (gameFrame == null) {
            Debug.warn("nothing to save!"); //TODO
            return;
        }

        final var gameFile = new File(dataFolder, gameFrame.getGameName() + ".json");
        final var dataSaver = new JsonDataSource<>(gameFile, type);
        dataSaver.save(gameFrame);

        GameCore.fireEvent(new SGameSavedEvent(gameFrame));
    }

    public void loadGames() {
        if (dataFolder.exists()) {
            try (Stream<Path> stream = Files.walk(Paths.get(dataFolder.getAbsolutePath()))) {
                final List<String> results = stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

                if (results.isEmpty()) {
                    Debug.info("&cWe did not find any arena.. &e:(", true);
                } else {
                    results.forEach(result -> loadGame(new File(result)));
                }
            } catch (Exception e) {
                GameCore.getErrorManager().newError(new GameError(null, ErrorType.GAME_LOADING_ERROR, e), true);
            }
        } else {
            Debug.info("&cWe did not find any arena.. &e:(", true);
        }
    }

    public void unregisterAll() {
        for (var game : registeredGames.values()) {
            unregisterGame(game, false);
        }
    }

    public void registerGame(String gameName, T gameFrame) {
        if (!GameCore.fireEvent(new SGameLoadingEvent(gameFrame))) {
            return;
        }

        gameFrame.start();
        registeredGames.put(gameName, gameFrame);
    }

    public void unregisterGame(String gameName) {
        final var gameFrame = registeredGames.get(gameName);

        unregisterGame(gameFrame, true);
    }

    public void unregisterGame(T gameFrame, boolean event) {
        if (gameFrame == null) {
            Debug.warn("GameFrame is null!", true);
            return;
        }

        if (event && !GameCore.fireEvent(new SGameDisabledEvent(gameFrame))) {
            return;
        }

        final String gameName = gameFrame.getGameName();

        gameFrame.stop();
        registeredGames.remove(gameName);
    }

    public boolean isGameRegistered(String gameName) {
        return registeredGames.containsKey(gameName);
    }

    public T getFirstAvailableGame() {
        for (var game : registeredGames.values()) {
            if (game.getActiveState() == GameState.WAITING) {
                return game;
            }
        }
        return null;
    }

    public T castGame(GameFrame gameFrame) {
        try {
            return type.cast(gameFrame);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
        return null;
    }

    public Collection<T> getRegisteredGames() {
        return registeredGames.values();
    }

    public Map<String, T> getRegisteredGamesMap() {
        return registeredGames;
    }

    public void registerBuilder(String gameName, Object gameBuilder) {
        gameBuilders.put(gameName, gameBuilder);
    }

    public void unregisterBuilder(String gameName) {
        gameBuilders.remove(gameName);
    }

    public boolean isInBuilder(String gameName) {
        return gameBuilders.containsKey(gameName);
    }

    public boolean isInBuilder(GameFrame gameFrame) {
        return gameBuilders.containsKey(gameFrame.getGameName());
    }

    @SuppressWarnings("unchecked")
    public <E> E getGameBuilder(String gameName) {
        return (E) gameBuilders.get(gameName);
    }

    public Optional<SpawnerEditor> getSpawnerEditor(Player player) {
        if (gameBuilders.isEmpty()) {
            return Optional.empty();
        }

        for (var builder : gameBuilders.values()) {
            final var castedBuilder = (GameBuilder) builder;
            if (castedBuilder.getSpawnerEditor() == null) {
                continue;
            }

            final var editor = castedBuilder.getSpawnerEditor();
            if (editor.getPlayer() == player) {
                return Optional.of(editor);
            }
        }

        return Optional.empty();
    }
}