package games.nautilus.core.user.manager;

import games.nautilus.core.Nautilus;
import games.nautilus.core.rank.Rank;
import games.nautilus.core.user.User;
import games.nautilus.core.user.UserImpl;
import games.nautilus.core.utils.framework.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class UserManager implements Listener {

    private final Nautilus core;
    private final Map<UUID, User> users = new HashMap<>();

    public UserManager(@NotNull Nautilus core) {
        Bukkit.getPluginManager().registerEvents(this, core);
        this.core = core;
    }

    public void registerUser(@NotNull UUID uuid) {
        User user = new UserImpl(this.core, uuid);

        YamlConfiguration file = Config.USERS.getConfig();
        if (file.getConfigurationSection("").getKeys(false).contains(uuid.toString())) {

            if (file.get(uuid + ".ranks") != null && !file.getStringList(uuid + ".ranks").isEmpty()) {
                for (String s : file.getStringList(uuid + ".ranks")) core.getRankManager().getRankFromName(s).ifPresent(r -> user.getRanks().add(r));
            }

        }

        if (user.getRanks().isEmpty()) user.getRanks().add(core.getRankManager().getDefaultRank());

        users.put(uuid, user);
        user.forceUpdate();
    }

    public @NotNull Optional<User> getUserFromId(@NotNull UUID id) {
        return Optional.ofNullable(this.users.get(id));
    }

    public @NotNull Set<User> getUsersWithRank(@NotNull Rank rank) {
        Set<User> toReturn = new HashSet<>();
        for (User user : this.users.values()) if (user.getRanks().contains(rank)) toReturn.add(user);
        return toReturn;
    }

    public @NotNull Set<User> getUsersWithPrimaryRank(@NotNull Rank rank) {
        Set<User> toReturn = new HashSet<>();
        for (User user : this.users.values()) if (user.getPrimaryRank().getId() == rank.getId()) toReturn.add(user);
        return toReturn;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        this.registerUser(e.getPlayer().getUniqueId());
    }

}
