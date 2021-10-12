package games.nautilus.core.rank;

import games.nautilus.core.Nautilus;
import games.nautilus.core.user.User;
import games.nautilus.core.utils.CC;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RankImpl implements Rank {

    private final Nautilus core;
    private final UUID id;

    private boolean isDefault = false;
    private String name = "";

    private Team team = null;
    private int priority;

    private String tabPrefix = "";
    private String chatPrefix = "";
    private String displayName = "";

    private final List<Rank> children = new ArrayList<>();
    private final Map<String, Boolean> permissions = new HashMap<>();

    public RankImpl(@NotNull Nautilus core) {
        this.id = UUID.randomUUID();
        this.core = core;
    }

    @Override
    public @NotNull UUID getId() { return this.id; }

    @Override
    public boolean isDefault() { return this.isDefault; }

    @Override
    public @NotNull String getName() { return this.name; }

    @Override
    public @NotNull Team getTeam() { return this.team; }

    @Override
    public int getPriority() { return this.priority; }

    @Override
    public @NotNull String getTabPrefix() { return this.tabPrefix; }

    @Override
    public @NotNull String getChatPrefix() { return this.chatPrefix; }

    @Override
    public @NotNull String getDisplayName() { return this.displayName; }

    @Override
    public @NotNull List<Rank> getChildren() { return this.children; }

    @Override
    public @NotNull Map<String, Boolean> getPermissions() { return this.permissions; }

    @Override
    public void setName(@NotNull String name) { this.name = name; }

    @Override
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    @Override
    public void setTeam(@NotNull Team team) { this.team = team; }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;

        Team team = core.getScoreboard().registerNewTeam(core.getRankManager().getPriorities()[priority] + this.name);
        team.setPrefix(this.getTabPrefix());
        team.setColor(CC.getLastColor(this.getTabPrefix()));

        if (this.team != null) {
            this.team.unregister();
        }

        this.setTeam(team);
        for (User user : core.getUserManager().getUsersWithPrimaryRank(this)) user.forceUpdate();
    }

    @Override
    public void setTabPrefix(@NotNull String tabPrefix) {
        this.tabPrefix = tabPrefix;
        if (this.team != null) {
            this.team.setPrefix(CC.trns(tabPrefix));
            this.team.setColor(CC.getLastColor(tabPrefix));
        }
    }

    @Override
    public void setChatPrefix(@NotNull String chatPrefix) { this.chatPrefix = chatPrefix; }

    @Override
    public void setDisplayName(@NotNull String displayName) { this.displayName = displayName; }

}
