package games.nautilus.core;

import games.nautilus.core.chat.Icon;
import games.nautilus.core.cosmetic.trail.Trail;
import games.nautilus.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Test implements Listener, CommandExecutor {

    private final Nautilus core;

    public Test() {
        core = JavaPlugin.getPlugin(Nautilus.class);
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        core.getUserManager().getUserFromId(e.getPlayer().getUniqueId()).ifPresent(user -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(CC.trns(
                        user.getDisplay().getChatPrefix() + user.getPlayer().getName() + "&7: &f" + ChatColor.stripColor(e.getMessage())
                ));
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        core.getUserManager().getUserFromId(player.getUniqueId()).ifPresent(user -> {
            Trail.ENCHANT.play(user);
        });
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //e.getPlayer().setResourcePack("https://cdn.discordapp.com/attachments/760249269507588168/896192239305379850/Nautilus.zip");
    }

}
