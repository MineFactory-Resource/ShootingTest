package net.teamuni.shootingtest.dummy;

import com.sk89q.worldedit.regions.CuboidRegion;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
        Entity entity = event.getEntity();
        NPC dummy = CitizensAPI.getNPCRegistry().getByUniqueId(entity.getUniqueId());
        if (dummy == null) return;
        if (!main.getDummyRespawn().getDummies().contains(dummy)) return;
        LivingEntity livingEntity = (LivingEntity) dummy.getEntity();
        if (!isInRegion(livingEntity)) {
            event.setCancelled(true);
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;
        entity.setFireTicks(0);
        event.setCancelled(true);
    }

    public boolean isInRegion(LivingEntity entity) {
        for (CuboidRegion region : main.getRegionManager().getRegion().values()) {
            if (region.contains(main.getRegionManager().getBlockVector3(entity.getLocation()))) {
                return true;
            }
        }
        return false;
    }
}
