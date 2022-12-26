package net.teamuni.shootingtest.dummy;

import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class RespawnTask implements Runnable {
    private final NPC dummy;
    private final Location loc;

    public RespawnTask(NPC npc, Location location) {
        this.dummy = npc;
        this.loc = location;
    }

    @Override
    public void run() {
        dummy.spawn(loc, SpawnReason.RESPAWN);
    }

    public void register(ShootingTest plugin, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, delay);
    }
}
