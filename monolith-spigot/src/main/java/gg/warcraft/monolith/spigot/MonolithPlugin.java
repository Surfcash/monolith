package gg.warcraft.monolith.spigot;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.block.build.service.BlockBuildCommandService;
import gg.warcraft.monolith.app.command.ConsoleCommandSender;
import gg.warcraft.monolith.app.command.PlayerCommandSender;
import gg.warcraft.monolith.app.command.event.SimpleCommandExecutedEvent;
import gg.warcraft.monolith.app.command.handler.CommandExecutedHandler;
import gg.warcraft.monolith.app.entity.player.handler.PlayerProfileInitializationHandler;
import gg.warcraft.monolith.app.entity.player.handler.PlayerProfileUpdateHandler;
import gg.warcraft.monolith.app.entity.status.handler.StatusEffectHandler;
import gg.warcraft.monolith.spigot.event.SpigotEntityEventMapper;
import gg.warcraft.monolith.spigot.event.SpigotInventoryEventMapper;
import gg.warcraft.monolith.spigot.event.SpigotPlayerEventMapper;
import gg.warcraft.monolith.spigot.event.SpigotWorldEventMapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MonolithPlugin extends JavaPlugin {
    private Injector injector;
    private EventService eventService;
    private TaskService taskService;

    void initializeInjector() {
        List<AbstractModule> monolithModules = Monolith.getModules();
        List<String> monolithModuleNames = monolithModules.stream()
                .map(module -> module.getClass().getSimpleName())
                .collect(Collectors.toList());
        getLogger().info("Found " + monolithModules.size() + " Monolith modules: " + monolithModuleNames);
        Injector injector = Guice.createInjector(monolithModules);
        new Monolith(injector);
    }

    void initializeMonolithHandlers() {
        CommandExecutedHandler commandExecutedHandler = injector.getInstance(CommandExecutedHandler.class);
        eventService.subscribe(commandExecutedHandler);

        StatusEffectHandler statusEffectHandler = injector.getInstance(StatusEffectHandler.class);
        eventService.subscribe(statusEffectHandler);

        PlayerProfileInitializationHandler playerProfileInitializationHandler =
                injector.getInstance(PlayerProfileInitializationHandler.class);
        eventService.subscribe(playerProfileInitializationHandler);

        PlayerProfileUpdateHandler playerProfileUpdateHandler =
                injector.getInstance(PlayerProfileUpdateHandler.class);
        taskService.runEachTick(playerProfileUpdateHandler);
    }

    void initializeSpigotEventMappers() {
        PluginManager pluginManager = getServer().getPluginManager();

        SpigotEntityEventMapper entityEventMapper = injector.getInstance(SpigotEntityEventMapper.class);
        pluginManager.registerEvents(entityEventMapper, this);

        SpigotInventoryEventMapper inventoryEventMapper = injector.getInstance(SpigotInventoryEventMapper.class);
        pluginManager.registerEvents(inventoryEventMapper, this);

        SpigotPlayerEventMapper playerEventMapper = injector.getInstance(SpigotPlayerEventMapper.class);
        pluginManager.registerEvents(playerEventMapper, this);

        SpigotWorldEventMapper worldEventMapper = injector.getInstance(SpigotWorldEventMapper.class);
        pluginManager.registerEvents(worldEventMapper, this);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        FileConfiguration localConfig = getConfig();

        String configurationService = localConfig.getString("configurationService");
        String gitHubAccount = localConfig.getString("gitHubAccount");
        String gitHubRepository = localConfig.getString("gitHubRepository");

        String persistenceService = localConfig.getString("persistenceService");
        String redisHost = localConfig.getString("redisHost");
        int redisPort = localConfig.getInt("redisPort");

        String entityService = localConfig.getString("entityService");

        String buildRepositoryWorldString = localConfig.getString("buildRepository.world");
        WorldType buildRepositoryWorld = WorldType.valueOf(buildRepositoryWorldString);
        Vector3ic buildRepositoryMinimumCorner = new Vector3i(
                localConfig.getInt("buildRepository.minimumCorner.x"),
                localConfig.getInt("buildRepository.minimumCorner.y"),
                localConfig.getInt("buildRepository.minimumCorner.z"));
        Vector3ic buildRepositoryMaximumCorner = new Vector3i(
                localConfig.getInt("buildRepository.maximumCorner.x"),
                localConfig.getInt("buildRepository.maximumCorner.y"),
                localConfig.getInt("buildRepository.maximumCorner.z"));

        String overworldName = localConfig.getString("worldDirectoryName");
        String netherName = localConfig.getString("netherDirectoryName");
        String theEndName = localConfig.getString("endDirectoryName");

        AbstractModule spigotMonolithModule = new SpigotMonolithModule(
                configurationService, gitHubAccount, gitHubRepository,
                persistenceService, redisHost, redisPort,
                entityService, buildRepositoryWorld,
                buildRepositoryMinimumCorner, buildRepositoryMaximumCorner,
                this, overworldName, netherName, theEndName);
        Monolith.registerModule(spigotMonolithModule);
    }

    @Override
    public void onEnable() {
        FileConfiguration localConfig = getConfig();

        boolean isFirstTimeSetup = localConfig.getBoolean("firstTimeSetup");
        if (isFirstTimeSetup) {
            getLogger().severe("Monolith has not been configured yet, shutting down server.");
            getLogger().severe("If you have finished configuration make sure to set firstTimeSetup to false.");
            getServer().shutdown();
            return;
        }

        boolean maintenance = localConfig.getBoolean("maintenance");
        if (maintenance) {
            // TODO setup maintenance login and session checker
        }

        initializeInjector();
        injector = Monolith.getInstance().getInjector();
        eventService = injector.getInstance(EventService.class);
        taskService = injector.getInstance(TaskService.class);

        initializeMonolithHandlers();
        initializeSpigotEventMappers();

        getLogger().info("Initializing build repository, this might take a little bit..");
        BlockBuildCommandService blockBuildCommandService = injector.getInstance(BlockBuildCommandService.class);
        blockBuildCommandService.initializeBuilds();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        eventService.publish(new SimpleCommandExecutedEvent(
                (sender instanceof Player)
                        ? new PlayerCommandSender(sender.getName(), ((Player) sender).getUniqueId())
                        : new ConsoleCommandSender(),
                command.getName(),
                label,
                Arrays.asList(args)));
        return true;
    }
}
