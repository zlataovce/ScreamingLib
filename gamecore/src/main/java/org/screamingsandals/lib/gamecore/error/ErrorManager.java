package org.screamingsandals.lib.gamecore.error;

import lombok.Data;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;

import java.util.LinkedList;
import java.util.List;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

/**
 * Utility class for writing errors and logging them.
 * This will be used with our debug-paste tool
 */
@Data
public class ErrorManager {
    private final List<BaseError> errorLog = new LinkedList<>();

    public void destroy() {
        errorLog.clear();
    }

    public BaseError newError(BaseError entry) {
        return newError(entry, false);
    }

    public BaseError newError(BaseError entry, boolean writeError) {
        errorLog.add(entry);
        writeError(entry, writeError);

        return entry;
    }

    public void writeError(BaseError error, boolean writeError) {
        if (!writeError) {
            return;
        }

        Debug.warn(error.getMessage(), true);
        final var exception = error.getException();

        if (exception != null) {
            exception.printStackTrace();
        }

        if (GameCore.getInstance().isVerbose()) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.hasPermission(GameCore.getInstance().getAdminPermissions())) {
                    player.sendMessage(m("prefix").get() + " " + error.getMessage());
                }
            });
        }
    }
}
