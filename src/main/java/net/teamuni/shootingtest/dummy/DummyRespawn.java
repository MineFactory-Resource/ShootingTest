package net.teamuni.shootingtest.dummy;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class DummyRespawn implements Listener {
    private final ShootingTest main;
    @Getter
    private final Set<NPC> dummies = new HashSet<>();
    private final Map<NPC, BukkitRunnable> runnableMap = new HashMap<>();

    public DummyRespawn(ShootingTest instance) {
        this.main = instance;
        this.loadDummies();
    }

    @EventHandler
    public void onSpawn(NPCSpawnEvent event) {
        NPC dummy = CitizensAPI.getNPCRegistry().getByUniqueId(event.getNPC().getUniqueId());
        if (!dummies.contains(dummy)) return;
        if (Arrays.asList(SpawnReason.values()).contains(event.getReason())) {
            LivingEntity entity = (LivingEntity) dummy.getEntity();
            entity.setSilent(true);
            main.getDummyManager().setDummyHealth(entity);
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    entity.setVelocity(new Vector(0, 0, 0));
                }
            };
            runnable.runTaskTimer(main, 1L, 1L);
            runnableMap.put(dummy, runnable);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        NPC dummy = CitizensAPI.getNPCRegistry().getByUniqueId(event.getEntity().getUniqueId());
        if (dummy == null) return;
        if (!dummies.contains(dummy)) return;
        event.setDroppedExp(0);

        runnableMap.remove(dummy).cancel();
        scheduleRespawn(dummy, getLocation(dummy));
    }

    public void loadDummies() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            ConfigurationSection section = main.getDummyManager().getConfig().getConfigurationSection("dummy");
            int numDummies = 0;
            for (String key : section.getKeys(false)) {
                ConfigurationSection section1 = section.getConfigurationSection(key);
                if (section1 == null) continue;
                String npcUUID = section1.getString("uuid");
                if (npcUUID == null) continue;
                NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(npcUUID));
                if (npc == null) continue;
                LivingEntity entity = (LivingEntity) npc.getEntity();
                if (entity == null) continue;
                dummies.add(npc);

                ++numDummies;
            }
            Bukkit.getLogger().info("[ShootingTest] Loaded " + numDummies + " dummy(ies).");
        }, 1L);
    }

    public void scheduleRespawn(NPC npc, Location location) {
        long delay = main.getConfig().getLong("dummy_respawn_delay");
        if (delay <= 20) {
            delay = 21;
        }
        new RespawnTask(npc, location).register(main, delay);
    }

    public void respawnDummies() {
        for (NPC dummy : dummies) {
            dummy.despawn(DespawnReason.PENDING_RESPAWN);
            dummy.spawn(getLocation(dummy), SpawnReason.RESPAWN);
        }
    }

    public Location getLocation(NPC dummy) {
        ConfigurationSection section = main.getDummyManager().getConfig().getConfigurationSection("dummy." + dummy.getName());
        ConfigurationSection section1 = section.getConfigurationSection("location");

        return new Location(
                Bukkit.getServer().getWorld(section1.getString("world")),
                section1.getDouble("x"),
                section1.getDouble("y"),
                section1.getDouble("z"),
                (float) section1.getDouble("yaw"),
                (float) section1.getDouble("pitch"));
    }
}
