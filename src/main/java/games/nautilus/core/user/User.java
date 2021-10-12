package games.nautilus.core.user;

import games.nautilus.core.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface User {

    @NotNull UUID getId();
    @NotNull PermissionAttachment getAttachment();

    @NotNull Rank getPrimaryRank();
    @NotNull Rank getDisplay();
    boolean isHidden();

    @NotNull Set<Rank> getRanks();
    @NotNull Set<String> getPermissions();

    @NotNull Player getPlayer();

    void forceUpdate();
    void recalculatePermissions();

    void setDisplay(@Nullable Rank rank);
    void setHidden(boolean hidden);

}
