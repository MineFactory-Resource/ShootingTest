package net.teamuni.shootingtest.events;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DummyDamage implements Listener {
    private final ShootingTest main;
    public DummyDamage(ShootingTest instance) {
        this.main = instance;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;
        Entity entity = event.getEntity();
        NPC dummy = CitizensAPI.getNPCRegistry().getByUniqueId(entity.getUniqueId());
        if (dummy == null) return;
        if (!main.getDummySpawn().getDummies().contains(dummy)) return;
        entity.setFireTicks(0);
        event.setCancelled(true);
    }
}
