package games.nautilus.core.rank.manager;

import games.nautilus.core.Nautilus;
import games.nautilus.core.rank.Rank;
import games.nautilus.core.rank.RankImpl;
import games.nautilus.core.utils.CC;
import games.nautilus.core.utils.framework.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class RankManager {

    private final Nautilus core;
    private final Map<UUID, Rank> ranks;

    private final char[] priorities = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public RankManager(@NotNull Nautilus core) {
        this.core = core;
        this.ranks = new HashMap<>();
        Bukkit.getScheduler().runTaskLater(core, this::registerRanks, 1);
    }

    private void registerRanks() {
        YamlConfiguration file = Config.RANKS.getConfig();

        for (String key : file.getConfigurationSection("").getKeys(false)) {
            Rank rank = new RankImpl(core);
            rank.setName(key);

            rank.setTabPrefix(CC.trns(file.getString(key + ".tab-prefix")));
            rank.setChatPrefix(CC.trns(file.getString(key + ".chat-prefix")));
            rank.setDisplayName(CC.trns(file.getString(key + ".display-name")));

            if (file.get(key + ".default") != null) rank.setDefault(file.getBoolean(key + ".default"));

            int priority = file.getInt(key + ".priority");
            rank.setPriority(priority);

            for (String perm : file.getStringList(key + ".permissions")) {
                rank.getPermissions().put(perm.startsWith("-") ? perm.substring(1) : perm, !perm.startsWith("-"));
            }

            this.ranks.put(rank.getId(), rank);
        }

        for (Rank rank : this.ranks.values()) {
            if (!file.getStringList(rank.getName() + ".children").isEmpty()) {
                for (String child : file.getStringList(rank.getName() + ".children")) {
                    this.getRankFromName(child).ifPresent(c -> rank.getChildren().add(c));
                }
            }
        }
    }

    public @NotNull Map<UUID, Rank> getAllRanks() { return this.ranks; }

    public @NotNull List<Rank> getRanksPriorityOrder() {
        List<Rank> toReturn = new ArrayList<>();
        for (int i = 0; i < this.ranks.size(); i++) {
            Rank temp = null;
            for (Rank rank : this.ranks.values()) {
                if (toReturn.contains(rank)) continue;
                if (temp == null || rank.getPriority() < temp.getPriority()) temp = rank;
            }
            toReturn.add(temp);
        }
        return toReturn;
    }

    public @NotNull Optional<Rank> getRankFromName(@NotNull String name) {
        for (Map.Entry<UUID, Rank> e : this.ranks.entrySet()) {
            if (e.getValue().getName().equalsIgnoreCase(name)) return Optional.of(e.getValue());
        }
        return Optional.empty();
    }

    public @NotNull Rank getDefaultRank() {
        for (Rank rank : this.ranks.values()) { if (rank.isDefault()) return rank; }
        return this.ranks.values().stream().findFirst().get();
    }

    public char[] getPriorities() { return this.priorities; }
}
