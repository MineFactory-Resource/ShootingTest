package net.teamuni.shootingtest.events;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DummySpawn implements Listener {
    private final ShootingTest main;
    private final List<NPC> dummies = new ArrayList<>();

    public DummySpawn(ShootingTest instance) {
        this.main = instance;
        this.getNPCs();
    }

    @EventHandler
    public void onEntitySpawn(NPCSpawnEvent event) {
        if (dummies.contains(event.getNPC())) {
            if (event.getReason() == SpawnReason.COMMAND || event.getReason() == SpawnReason.RESPAWN || event.getReason() == SpawnReason.CHUNK_LOAD) {
                LivingEntity entity = (LivingEntity) event.getNPC().getEntity();
                main.getDummyManager().setDummyHealth(entity);
            }
        }
    }

    public void getNPCs() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            ConfigurationSection section = main.getDummyManager().getConfig().getConfigurationSection("dummy");
            int numNPCs = 0;
            for (String key : section.getKeys(false)) {
                String npcUUID = section.getString(key);
                if (npcUUID == null) continue;
                NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(npcUUID));
                if (npc == null) continue;
                dummies.add(npc);

                ++numNPCs;
            }
            Bukkit.getLogger().info("[ShootingTest] Loaded " + numNPCs + " dummy(ies).");
        }, 1L);
    }
}
