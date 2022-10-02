package net.teamuni.shootingtest.config;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.teamuni.shootingtest.ShootingTest;
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

    public DummyManager(ShootingTest instance) {
        this.main = instance;
        createDummyYml();
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
        ConfigurationSection section = this.dummyFile.getConfigurationSection("dummy");
        Set<String> npcs = section.getKeys(false);
        if (npcs.contains(name)) {
            String message = main.getMessageManager().getConfig().getString("dummy_already_exist", "&cThe name of dummy already exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, name);
        npc.spawn(location);
        npc.setProtected(false);

        LivingEntity entity = (LivingEntity) npc.getEntity();
        this.setDummyHealth(entity);
        section.set(name, npc.getUniqueId().toString());

        String message = main.getMessageManager().getConfig().getString("dummy_created", "&aDummy has been created successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void removeDummy(Player player, String name) {
        ConfigurationSection section = this.dummyFile.getConfigurationSection("dummy");
        String npcUUID = section.getString(name);
        if (npcUUID == null) {
            String message = main.getMessageManager().getConfig().getString("dummy_not_exist", "&cDummy try to remove does not exist!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        section.set(name, null);
        this.save();

        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(npcUUID));
        if (npc == null) return;
        npc.destroy();

        String message = main.getMessageManager().getConfig().getString("dummy_removed", "&aDummy has been removed successfully!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void setDummyHealth(LivingEntity entity) {
        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;
        maxHealth.setBaseValue(main.getConfig().getDouble("dummy_health"));
        entity.setHealth(maxHealth.getBaseValue());
    }
}
