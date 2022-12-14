package net.teamuni.shootingtest.dummy;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class DummyManager {
    private final ShootingTest main;
    private File file = null;
    private FileConfiguration dummyFile = null;
    private final ConfigurationSection section;

    public DummyManager(ShootingTest instance) {
        this.main = instance;
        createDummyYml();
        this.section = this.dummyFile.getConfigurationSection("dummy");
    }

    public void createDummyYml() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "dummy.yml");
        }
        if (!file.exists()) {
            main.saveResource("dummy.yml", false);
        }
        this.dummyFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return this.dummyFile;
    }

    public void save() {
        if (this.file == null || this.dummyFile == null) return;

        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "dummy.yml");
        }

        this.dummyFile = YamlConfiguration.loadConfiguration(file);
    }

    public void createDummy(Player player, String name, Location location) {
        Set<String> dummies = section.getKeys(false);
        if (dummies.contains(name)) {
            String message = main.getMessageManager().getConfig().getString("dummy_already_exist", "&cThe name of dummy already exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        NPC dummy = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, name);
        this.createDummyInfo(section, dummy, name, location);
        main.getDummyRespawn().getDummies().add(dummy);
        dummy.spawn(location, SpawnReason.CREATE);
        dummy.setProtected(false);

        String message = main.getMessageManager().getConfig().getString("dummy_created", "&aDummy has been created successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void removeDummy(Player player, String name) {
        ConfigurationSection section1 = section.getConfigurationSection(name);
        if (section1 == null) {
            String message = main.getMessageManager().getConfig().getString("dummy_not_exist", "&cDummy try to remove does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        String npcUUID = section1.getString("uuid");
        if (npcUUID == null) {
            String message = main.getMessageManager().getConfig().getString("dummy_not_exist", "&cDummy try to remove does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        NPC dummy = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(npcUUID));
        if (dummy == null) return;
        dummy.destroy();
        section.set(name, null);

        String message = main.getMessageManager().getConfig().getString("dummy_removed", "&aDummy has been removed successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void setDummyHealth(LivingEntity entity) {
        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;
        double health = main.getConfig().getDouble("dummy_health");
        if (health <= 0) {
            health = 1;
        }
        maxHealth.setBaseValue(health);
        entity.setHealth(maxHealth.getBaseValue());
    }

    public void createDummyInfo(ConfigurationSection section, NPC npc, String name, Location location) {
        ConfigurationSection section1 = section.createSection(name);
        section1.set("uuid", npc.getUniqueId().toString());

        ConfigurationSection section2 = section1.createSection("location");
        section2.set("world", location.getWorld().getName());
        section2.set("x", location.getX());
        section2.set("y", location.getY());
        section2.set("z", location.getZ());
        section2.set("yaw", location.getYaw());
        section2.set("pitch", location.getPitch());
    }

    public void seeDummies(Player player) {
        Set<String> dummyKeys = section.getKeys(false);
        if (dummyKeys.isEmpty()) {
            String message = main.getMessageManager().getConfig().getString("dummy_empty", "&cThere are no dummies to see.");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "----------- " + ChatColor.WHITE + "List of dummies" + ChatColor.YELLOW + " -----------");
        int number = 0;
        for (String keys : dummyKeys) {
            ++number;
            player.sendMessage(ChatColor.GOLD + String.valueOf(number) + ". " + keys);
        }
    }

    public void teleportToDummy(Player player, String name) {
        ConfigurationSection section1 = section.getConfigurationSection(name);
        if (section1 == null) return;
        ConfigurationSection section2 = section1.getConfigurationSection("location");
        if (section2 == null) return;

        Location location = new Location(Bukkit.getServer().getWorld(section2.getString("world")),
                section2.getDouble("x"),
                section2.getDouble("y"),
                section2.getDouble("z"));

        player.teleport(location);

        String message = main.getMessageManager().getConfig().getString("teleport_to_dummy", "&6Teleport to " + name);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
