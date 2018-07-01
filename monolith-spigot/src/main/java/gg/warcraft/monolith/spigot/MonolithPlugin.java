package gg.warcraft.monolith.spigot;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.MonolithModule;
import gg.warcraft.monolith.app.AbstractMonolithModule;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class MonolithPlugin extends JavaPlugin {

    void shutdownIfFirstTimeSetup(boolean isFirstTimeSetup) {
        if (isFirstTimeSetup) {
            Logger logger = Bukkit.getLogger();
            logger.severe("Monolith has not been configured yet, shutting down server.");
            logger.severe("If you have finished configuration make sure to set firstTimeSetup to false.");
            getServer().shutdown();
        }
    }

    void initializeInjector() {
        ServiceLoader<MonolithModule> monolithModuleLoader = ServiceLoader.load(MonolithModule.class);
        List<MonolithModule> monolithModules = Lists.newArrayList(monolithModuleLoader.iterator());
        Injector injector = Guice.createInjector(monolithModules);
        new Monolith(injector);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        FileConfiguration localConfig = getConfig();

        boolean isFirstTimeSetup = localConfig.getBoolean("firstTimeSetup");
        shutdownIfFirstTimeSetup(isFirstTimeSetup);

        boolean maintenance = localConfig.getBoolean("maintenance");
        if (maintenance) {
            // TODO setup maintenance login and session checker
        }

        String configurationService = localConfig.getString("configurationService");
        String gitHubAccount = localConfig.getString("gitHubAccount");
        String gitHubRepository = localConfig.getString("gitHubRepository");
        AbstractMonolithModule.setConfigurationService(configurationService);
        AbstractMonolithModule.setGitHubAccount(gitHubAccount);
        AbstractMonolithModule.setGitHubRepository(gitHubRepository);

        String persistenceService = localConfig.getString("persistenceService");
        String redisHost = localConfig.getString("redisHost");
        int redisPort = localConfig.getInt("redisPort");
        AbstractMonolithModule.setPersistenceService(persistenceService);
        AbstractMonolithModule.setRedisHost(redisHost);
        AbstractMonolithModule.setRedisPort(redisPort);

        String overworldName = localConfig.getString("worldName");
        String theNetherName = localConfig.getString("theNetherName");
        String theEndName = localConfig.getString("theEndName");
        SpigotMonolithModule.setPlugin(this);
        SpigotMonolithModule.setOverworldName(overworldName);
        SpigotMonolithModule.setTheNetherName(theNetherName);
        SpigotMonolithModule.setTheEndName(theEndName);

        initializeInjector();
    }
}