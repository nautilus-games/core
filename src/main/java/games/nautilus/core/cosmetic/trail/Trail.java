package games.nautilus.core.cosmetic.trail;

import games.nautilus.core.cosmetic.PacketQueue;
import games.nautilus.core.user.User;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.Particles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public enum Trail {

    ENCHANT {
        @Override
        public void play(User user) {
            if (active.containsKey(user)) return;
            ThreadLocalRandom random = ThreadLocalRandom.current();
            PacketQueue<PacketPlayOutWorldParticles> q = new PacketQueue<>();
            q.add(() -> {
                double x = random.nextDouble(0.5), y = random.nextDouble(1.0), z = random.nextDouble(0.5);
                x = random.nextInt(2) == 0 ? x : -x; z = random.nextInt(2) == 0 ? z : -z;

                Location l = user.getPlayer().getLocation().add(x, y, z);
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(Particles.ENCHANT, true,
                        (float) l.getX(), (float) l.getY(), (float) l.getZ(), 0, 0, 0, 0, 1);

                for (Entity e : user.getPlayer().getNearbyEntities(50, 50, 50)) {
                    if (e instanceof Player) ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet);
                }
            });
            q.runTaskContinuously(0, 40, TimeUnit.MILLISECONDS);
            active.put(user, q);
        }
    };

    public abstract void play(User user);

    public void stop(User user) {
        if (!active.containsKey(user)) return; active.get(user).cancelContinuousTasks(); active.remove(user);
    }

    private static final Map<User, PacketQueue> active = new HashMap<>();

}
