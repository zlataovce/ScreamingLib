package org.screamingsandals.lib.sidebar;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.SClientboundSetDisplayObjectivePacket;
import org.screamingsandals.lib.packet.SClientboundSetObjectivePacket;
import org.screamingsandals.lib.packet.SClientboundSetScorePacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeamImpl;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractVisual;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ScoreSidebarImpl extends AbstractVisual<ScoreSidebar> implements ScoreSidebar {
    @Getter
    protected final List<ScoreboardTeam> teams = new LinkedList<>();
    @Getter
    protected final List<ScoreEntry> entries = new CopyOnWriteArrayList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;
    protected SenderMessage title = SenderMessage.empty();
    private final String objectiveKey;
    private final List<ScoreEntry> lines = new CopyOnWriteArrayList<>();

    public ScoreSidebarImpl(UUID uuid) {
        super(uuid);
        this.objectiveKey =
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
    }

    @Override
    public Optional<ScoreboardTeam> getTeam(String identifier) {
        return teams.stream().filter(scoreboardTeam -> identifier.equals(scoreboardTeam.identifier())).findFirst();
    }

    @Override
    public ScoreSidebar removeTeam(String identifier) {
        getTeam(identifier).ifPresent(this::removeTeam);
        return this;
    }

    @Override
    public ScoreSidebar removeTeam(ScoreboardTeam scoreboardTeam) {
        scoreboardTeam.destroy();
        teams.remove(scoreboardTeam);
        return this;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }

        return !data.isEmpty();
    }

    @Override
    public ScoreSidebar title(Component title) {
        return title(SenderMessage.of(title));
    }

    @Override
    public ScoreSidebar title(ComponentLike title) {
        if (title instanceof SenderMessage) {
            this.title = (SenderMessage) title;
        } else {
            this.title = SenderMessage.of(title);
        }
        updateTitle0();
        return this;
    }

    @Override
    public ScoreSidebar entity(String identifier, Component displayName) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresentOrElse(scoreEntry -> {
                    scoreEntry.setComponent(displayName);
                    scoreEntry.setReloadCache(true);
                    update();
                }, () -> {
                    var scoreEntry = new ScoreEntry(identifier);
                    scoreEntry.setComponent(displayName);
                    entries.add(scoreEntry);
                    update();
                });
        return this;
    }

    @Override
    public ScoreSidebar score(String identifier, int score) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresent(scoreEntry -> {
                    scoreEntry.setScore(score);
                    update();
                });
        return this;
    }

    @Override
    public ScoreSidebar removeEntity(String identifier) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresent(scoreEntry -> {
                    entries.remove(scoreEntry);
                    update();
                });
        return this;
    }

    @Override
    public ScoreSidebar update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public ScoreSidebar show() {
        if (isShown()) {
            return this;
        }

        ready = true;
        visible = true;
        viewers.forEach(a -> onViewerAdded(a, false));
        update();
        return this;
    }

    @Override
    public ScoreSidebar hide() {
        if (!isShown()) {
            return this;
        }

        visible = false;
        ready = false;
        update();
        return this;
    }

    @Override
    public void destroy() {
        data = null;
        hide();
        viewers.clear();
        SidebarManager.removeSidebar(this);
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (visible) {
            getCreateObjectivePacket(player).sendPacket(player);
            allScores().forEach(packet -> packet.sendPacket(player));
            getDisplayObjectivePacket().sendPacket(player);
            teams.forEach(scoreboardTeam ->
                    ((ScoreboardTeamImpl) scoreboardTeam).constructCreatePacket().sendPacket(player)
            );
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            teams.forEach(scoreboardTeam ->
                    ((ScoreboardTeamImpl) scoreboardTeam).constructDestructPacket().sendPacket(player)
            );
            getDestroyObjectivePacket().sendPacket(player);
        }
    }

    // INTERNAL METHODS

    private SClientboundSetObjectivePacket getCreateObjectivePacket(PlayerWrapper player) {
        var packet = notFinalObjectivePacket(player);
        packet.mode(SClientboundSetObjectivePacket.Mode.CREATE);
        return packet;
    }

    private SClientboundSetObjectivePacket getUpdateObjectivePacket(PlayerWrapper player) {
        var packet = notFinalObjectivePacket(player);
        packet.mode(SClientboundSetObjectivePacket.Mode.UPDATE);
        return packet;
    }

    private SClientboundSetObjectivePacket notFinalObjectivePacket(PlayerWrapper player) {
        return new SClientboundSetObjectivePacket()
                .objectiveKey(objectiveKey)
                .title(title.asComponent(player))
                .criteriaType(SClientboundSetObjectivePacket.Type.INTEGER);
    }

    private SClientboundSetObjectivePacket getDestroyObjectivePacket() {
        return new SClientboundSetObjectivePacket()
                .objectiveKey(objectiveKey)
                .mode(SClientboundSetObjectivePacket.Mode.DESTROY);
    }

    private SClientboundSetDisplayObjectivePacket getDisplayObjectivePacket() {
        return new SClientboundSetDisplayObjectivePacket()
                .slot(SClientboundSetDisplayObjectivePacket.DisplaySlot.SIDEBAR)
                .objectiveKey(objectiveKey);
    }

    private SClientboundSetScorePacket createScorePacket(int i, String value) {
        return new SClientboundSetScorePacket()
                .entityName(value)
                .objectiveKey(objectiveKey)
                .score(i)
                .action(SClientboundSetScorePacket.ScoreboardAction.CHANGE);
    }

    private Object destroyScore(String value) {
        return new SClientboundSetScorePacket()
                .entityName(value)
                .objectiveKey(objectiveKey)
                .action(SClientboundSetScorePacket.ScoreboardAction.REMOVE);
    }

    private List<SClientboundSetScorePacket> allScores() {
        return lines
                .stream()
                .map(entry -> createScorePacket(entry.getScore(), entry.getCache()))
                .collect(Collectors.toList());
    }

    public String crop(String baseLine) {
        if (baseLine.length() > 40) {
            return baseLine.substring(0, 40);
        }
        return baseLine;
    }

    @Override
    public ScoreboardTeam team(String identifier) {
        var team = new ScoreboardTeamImpl(this, identifier);
        teams.add(team);
        if (visible && !viewers.isEmpty()) {
            var packet = team.constructCreatePacket();
            viewers.forEach(packet::sendPacket);
        }
        return team;
    }

    protected void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            viewers.forEach(p -> getUpdateObjectivePacket(p).sendPacket(p));
        }
    }

    @Override
    protected void update0() {
        var list = entries
                .stream()
                .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
                .limit(15)
                .collect(Collectors.toList());

        var packets = new ArrayList<AbstractPacket>();
        var forRemoval = new ArrayList<ScoreEntry>();

        lines.stream().filter(scoreEntry -> !list.contains(scoreEntry)).forEach(forRemoval::add);
        forRemoval.forEach(scoreEntry -> {
            lines.remove(scoreEntry);
            destroyScore(scoreEntry.getCache());
        });

        list.forEach(scoreEntry -> {
            if (!lines.contains(scoreEntry)) {
                lines.add(scoreEntry);
            }
            if (scoreEntry.getCache() == null || scoreEntry.isReloadCache()) {
                scoreEntry.setCache(crop(AdventureHelper.toLegacy(scoreEntry.getComponent())));
                scoreEntry.setReloadCache(false);
            }
            packets.add(createScorePacket(scoreEntry.getScore(), scoreEntry.getCache()));
        });

        if (visible) {
            viewers.forEach(viewer -> packets.forEach(packet -> packet.sendPacket(viewer)));
        }
    }

    @Data
    public static class ScoreEntry {
        private final String identifier;
        private Component component;
        private int score;
        private String cache;
        private boolean reloadCache;
    }
}
