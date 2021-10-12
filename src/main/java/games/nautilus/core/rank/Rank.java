package games.nautilus.core.rank;

import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Rank {

    @NotNull UUID getId();

    boolean isDefault();
    @NotNull String getName();

    @NotNull Team getTeam();
    int getPriority();

    @NotNull String getTabPrefix();
    @NotNull String getChatPrefix();
    @NotNull String getDisplayName();

    @NotNull List<Rank> getChildren();
    @NotNull Map<String, Boolean> getPermissions();

    void setTeam(@NotNull Team team);
    void setPriority(int priority);

    void setName(@NotNull String name);
    void setDefault(boolean isDefault);

    void setTabPrefix(@NotNull String tabPrefix);
    void setChatPrefix(@NotNull String chatPrefix);
    void setDisplayName(@NotNull String displayName);

}
