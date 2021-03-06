package games.nautilus.core.cosmetic;

import games.nautilus.core.Nautilus;
import net.minecraft.server.v1_16_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PacketQueue<P extends Packet> {

    private final ScheduledExecutorService service;
    private final List<Runnable> runnables = new ArrayList<>();

    private boolean r = false;

    public PacketQueue() { this.service = new ScheduledThreadPoolExecutor(6); }

    public void add(P... packets) {
        Runnable r = () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                for (P packet : packets) ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        };
        runnables.add(r);
    }

    public void add(Runnable... r) { runnables.addAll(Arrays.asList(r)); }

    public void runTasksThen(long delay, TimeUnit time, Runnable runnable) {
        if (!runnables.isEmpty()) {
            this.service.schedule(() -> {
                runnables.get(0).run(); runnables.remove(0); this.runTasksThen(delay, time, runnable);
            }, delay, time);
        } else {
            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Nautilus.class), runnable);
        }
    }

    public void runTasksThenAsync(long delay, TimeUnit time, Runnable runnable) {
        if (!runnables.isEmpty()) {
            this.service.schedule(() -> {
                runnables.get(0).run(); runnables.remove(0); this.runTasksThen(delay, time, runnable);
            }, delay, time);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(Nautilus.class), runnable);
        }
    }

    public void runTaskContinuously(int index, long delay, TimeUnit time) {
        this.r = true; this.runTaskForever(index, delay, time);
    }

    public void cancelContinuousTasks() { this.r = false; }

    private void runTaskForever(int index, long delay, TimeUnit time) {
        this.service.schedule(() -> { if (this.r) { runnables.get(0).run(); this.runTaskForever(index, delay, time); } }, delay, time);
    }

}
