package net.teamuni.shootingtest.events;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.RespawnTask;
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
import java.util.logging.Level;

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
        List<String> worlds = main.getConfig().getStringList("enable_world");
        NPC dummy = CitizensAPI.getNPCRegistry().getByUniqueId(event.getNPC().getUniqueId());
        if (!dummies.contains(dummy)) return;
        if (!worlds.contains(dummy.getEntity().getWorld().getName())) {
            Bukkit.getLogger().log(Level.SEVERE, "The spawn of a dummy named " + dummy.getName() + " has been canceled. Caused by: Invalid world");
            event.setCancelled(true);
            return;
        }
        if (Arrays.asList(SpawnReason.values()).contains(event.getReason())) {
            LivingEntity entity = (LivingEntity) dummy.getEntity();
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

        ConfigurationSection section = main.getDummyManager().getConfig().getConfigurationSection("dummy." + dummy.getName());
        ConfigurationSection section1 = section.getConfigurationSection("location");
        Location dummyLoc = new Location(
                Bukkit.getServer().getWorld(section1.getString("world")),
                section1.getDouble("x"),
                section1.getDouble("y"),
                section1.getDouble("z"),
                (float) section1.getDouble("yaw"),
                (float) section1.getDouble("pitch"));

        runnableMap.remove(dummy).cancel();
        scheduleRespawn(dummy, dummyLoc);
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
}
