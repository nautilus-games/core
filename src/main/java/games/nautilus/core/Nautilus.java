package games.nautilus.core;

import games.nautilus.core.rank.command.RankCommand;
import games.nautilus.core.rank.manager.RankManager;
import games.nautilus.core.user.manager.UserManager;
import games.nautilus.core.utils.framework.command.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

public final class Nautilus extends JavaPlugin {

    private CommandFramework framework;
    private Scoreboard scoreboard;

    private RankManager rankManager;
    private UserManager userManager;

    @Override
    public void onEnable() {
        this.framework = new CommandFramework(this);
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.rankManager = new RankManager(this);
        this.userManager = new UserManager(this);

        getCommand("test").setExecutor(new Test());
        this.registerCommands();
    }

    private void registerCommands() {
        new RankCommand(this);
    }

    public @NotNull CommandFramework getFramework() { return this.framework; }

    public @NotNull Scoreboard getScoreboard() { return this.scoreboard; }

    public @NotNull RankManager getRankManager() { return this.rankManager; }

    public @NotNull UserManager getUserManager() { return this.userManager; }

}
