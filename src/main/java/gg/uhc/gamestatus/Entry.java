package gg.uhc.gamestatus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class Entry extends JavaPlugin {

    @Override
    public void onEnable() {
        // save the default if none exists already
        if(!copyDefaultConfig()) {
            // something went wrong with config writing
            setEnabled(false);
            return;
        }

        FileConfiguration configuration = getConfig();
        configuration.options().copyDefaults(true);
        saveConfig();

        MotdController controller = null;
        try {
            Map<String, String> motds = getMotdsFromConfig(configuration);
            String initialKey = getOnStartKey(configuration);

            controller = new MotdController(motds, initialKey);
            getServer().getPluginManager().registerEvents(controller, this);
        } catch (Exception ex) {
            ex.printStackTrace();
            setEnabled(false);
            getLogger().severe("Error loading configuration, check it is formatted correctly");
        }

        getCommand("gamestatus").setExecutor(new StatusCommand(controller));
    }

    protected String getOnStartKey(ConfigurationSection section) throws InvalidConfigurationException {
        if (!section.contains("on start")) return null;

        if (!section.isString("on start")) throw new InvalidConfigurationException("Expected a String at `on start` but found something else instead");

        return section.getString("on start");
    }

    protected Map<String, String> getMotdsFromConfig(ConfigurationSection section) throws InvalidConfigurationException {
        Preconditions.checkArgument(section.isConfigurationSection("motds"), "Configuration missing required section `motds`");

        ConfigurationSection motdsSection = section.getConfigurationSection("motds");

        Collection<String> keys = motdsSection.getKeys(false);

        Map<String, String> motds = Maps.newHashMap();
        for (String key : keys) {
            if (!motdsSection.isString(key)) throw new InvalidConfigurationException("Expected a String at motd `" + key + "` but found something else instead");

            motds.put(key, ChatColor.translateAlternateColorCodes('&', motdsSection.getString(key)));
        }

        return motds;
    }


    /**
     * @return true if already exists/wrote to file, false if writing to file failed
     */
    protected boolean copyDefaultConfig() {
        File dataFolder = getDataFolder();

        // make data folder if it isn't there
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            // data folder creation failed
            return false;
        }

        File configFile = new File(getDataFolder(), "config.yml");

        // config file already exists
        if (configFile.exists()) return true;

        // write the defaults
        URL defaultConfig = Resources.getResource(this.getClass(), "/default.yml");
        try {
            // write /default.yml to the config.yml file
            Files.write(Resources.toByteArray(defaultConfig), configFile);
        } catch (IOException e) {
            e.printStackTrace();
            // something failed during writing
            return false;
        }

        // config wrote successfully
        return true;
    }
}
