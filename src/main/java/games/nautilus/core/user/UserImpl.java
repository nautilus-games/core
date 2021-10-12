package games.nautilus.core.user;

import games.nautilus.core.Nautilus;
import games.nautilus.core.rank.Rank;
import games.nautilus.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UserImpl implements User {

    private final Nautilus core;

    private final UUID id;
    private final PermissionAttachment attachment;

    private Rank display;
    private boolean hidden;

    private final Set<Rank> ranks = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();

    public UserImpl(@NotNull Nautilus core, @NotNull UUID id) {
        this.core = core; this.id = id;
        this.attachment = Bukkit.getPlayer(id).addAttachment(core);
    }

    @Override
    public @NotNull UUID getId() { return this.id; }

    @Override
    public @NotNull PermissionAttachment getAttachment() { return this.attachment; }

    @Override
    public @NotNull Rank getPrimaryRank() {
        Rank rank = null;
        for (Rank r : this.ranks) {
            if (rank == null) { rank = r; continue; }
            if (r.getPriority() > rank.getPriority()) rank = r;
        }
        return rank != null ? rank : core.getRankManager().getDefaultRank();
    }

    @Override
    public @NotNull Rank getDisplay() {
        return this.hidden ? core.getRankManager().getDefaultRank() : this.display == null ? this.getPrimaryRank() : this.display;
    }

    @Override
    public boolean isHidden() { return this.hidden; }

    @Override
    public @NotNull Set<Rank> getRanks() { return this.ranks; }

    @Override
    public @NotNull Set<String> getPermissions() { return this.permissions; }

    @Override
    public @NotNull Player getPlayer() { return Objects.requireNonNull(Bukkit.getPlayer(this.id)); }

    @Override
    public void forceUpdate() {
        this.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        this.getPlayer().setPlayerListName(null);

        for (Rank rank : this.ranks) { rank.getTeam().removePlayer(this.getPlayer()); }
        Rank rank = this.getDisplay();
        rank.getTeam().addPlayer(this.getPlayer());
        this.getPlayer().setPlayerListName(CC.trns(rank.getTabPrefix() + this.getPlayer().getName()));

        this.getPlayer().setScoreboard(core.getScoreboard());
        this.recalculatePermissions();
    }

    @Override
    public void recalculatePermissions() {
        this.attachment.getPermissions().clear();
        for (Rank rank : this.ranks) { this.applyRankPermissions(rank); }
        for (String perm : this.permissions) {
            if (perm.equals("*")) {
                for (Permission p : Bukkit.getPluginManager().getPermissions()) this.attachment.setPermission(perm, true);
            }
            this.attachment.setPermission((perm.startsWith("-") ? perm.substring(1) : perm), !perm.startsWith("-"));
        }
    }

    private void applyRankPermissions(@NotNull Rank rank) {
        for (Map.Entry<String, Boolean> perm : rank.getPermissions().entrySet()) {
            if (perm.getKey().equals("*")) {
                for (Permission p : Bukkit.getPluginManager().getPermissions()) this.attachment.setPermission(perm.getKey(), perm.getValue());
            }
            this.attachment.setPermission(perm.getKey(), perm.getValue());
        }
        for (Rank r : rank.getChildren()) { this.applyRankPermissions(r); }
    }

    @Override
    public void setDisplay(@Nullable Rank display) { this.display = display; }

    @Override
    public void setHidden(boolean hidden) { this.hidden = hidden; }
}
