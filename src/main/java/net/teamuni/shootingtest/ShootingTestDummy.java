package net.teamuni.shootingtest;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class ShootingTestDummy {
    private final ShootingTest main;

    public ShootingTestDummy(ShootingTest instance) {
        this.main = instance;
    }

    public void createDummy(Player player, String name, Location location) {
        Set<String> npcs = main.getDummyManager().getConfig().getConfigurationSection("dummy").getKeys(false);
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, name);
        npc.setBukkitEntityType(EntityType.ZOMBIE);

        if (npcs.contains(name)) {
            String message = main.getMessageManager().getConfig().getString("dummy_already_exist");
            if (message == null) return;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        } else {
            main.getDummyManager().getConfig().set("dummy." + name, npc.getUniqueId().toString());
            String message = main.getMessageManager().getConfig().getString("dummy_created");
            if (message == null) return;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        npc.spawn(location);
        npc.setProtected(false);
    }

    public void removeDummy(Player player, String name) {
        String npcUUID = main.getDummyManager().getConfig().getString("dummy." + name);
        String message = main.getMessageManager().getConfig().getString("dummy_removed");
        if (npcUUID == null) return;

        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(main.getDummyManager().getConfig().getString("dummy." + name)));
        npc.destroy();
        main.getDummyManager().getConfig().set("dummy." + name, null);
        main.getDummyManager().save();
        main.getDummyManager().reload();

        if (message == null) return;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
