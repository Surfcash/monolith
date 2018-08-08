package gg.warcraft.monolith.app;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import gg.warcraft.monolith.api.MonolithPluginUtils;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.command.Console;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.command.service.CommandQueryService;
import gg.warcraft.monolith.api.command.service.CommandRepository;
import gg.warcraft.monolith.api.config.service.ConfigurationCommandService;
import gg.warcraft.monolith.api.config.service.ConfigurationQueryService;
import gg.warcraft.monolith.api.config.service.ConfigurationRepository;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.JsonMapper;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.core.YamlMapper;
import gg.warcraft.monolith.api.effect.Effect;
import gg.warcraft.monolith.api.effect.EffectFactory;
import gg.warcraft.monolith.api.effect.EffectRenderer;
import gg.warcraft.monolith.api.effect.EffectRendererFactory;
import gg.warcraft.monolith.api.effect.EffectVectors;
import gg.warcraft.monolith.api.effect.EffectVectorsFactory;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeCommandService;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeQueryService;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeRepository;
import gg.warcraft.monolith.api.entity.player.hiding.PlayerHidingCommandService;
import gg.warcraft.monolith.api.entity.player.hiding.PlayerHidingQueryService;
import gg.warcraft.monolith.api.entity.player.hiding.PlayerHidingRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerProfileRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityProfileRepository;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.entity.status.service.StatusQueryService;
import gg.warcraft.monolith.api.entity.status.service.StatusRepository;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.menu.service.MenuQueryService;
import gg.warcraft.monolith.api.menu.service.MenuRepository;
import gg.warcraft.monolith.api.util.MathUtils;
import gg.warcraft.monolith.api.util.StringUtils;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.block.BlockTypeUtils;
import gg.warcraft.monolith.api.world.block.BlockUtils;
import gg.warcraft.monolith.api.world.block.backup.service.BlockBackupCommandService;
import gg.warcraft.monolith.api.world.block.backup.service.BlockBackupQueryService;
import gg.warcraft.monolith.api.world.block.backup.service.BlockBackupRepository;
import gg.warcraft.monolith.api.world.block.box.BoundingBlockBox;
import gg.warcraft.monolith.api.world.block.box.BoundingBlockBoxFactory;
import gg.warcraft.monolith.api.world.block.build.service.BlockBuildCommandService;
import gg.warcraft.monolith.api.world.block.build.service.BlockBuildQueryService;
import gg.warcraft.monolith.api.world.block.build.service.BlockBuildRepository;
import gg.warcraft.monolith.api.world.block.spoofing.BlockSpoofingCommandService;
import gg.warcraft.monolith.api.world.block.spoofing.BlockSpoofingQueryService;
import gg.warcraft.monolith.api.world.block.spoofing.BlockSpoofingRepository;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldQueryService;
import gg.warcraft.monolith.app.command.ConsoleCommandSender;
import gg.warcraft.monolith.app.command.service.DefaultCommandCommandService;
import gg.warcraft.monolith.app.command.service.DefaultCommandQueryService;
import gg.warcraft.monolith.app.command.service.DefaultCommandRepository;
import gg.warcraft.monolith.app.config.MonolithMapperModule;
import gg.warcraft.monolith.app.config.service.DefaultConfigurationQueryService;
import gg.warcraft.monolith.app.config.service.DefaultConfigurationRepository;
import gg.warcraft.monolith.app.config.service.GitHubConfigurationCommandService;
import gg.warcraft.monolith.app.config.service.LocalConfigurationCommandService;
import gg.warcraft.monolith.app.core.GuavaEventService;
import gg.warcraft.monolith.app.core.JedisPersistenceService;
import gg.warcraft.monolith.app.effect.DynamicEffect;
import gg.warcraft.monolith.app.effect.PeriodicEffect;
import gg.warcraft.monolith.app.effect.SimpleEffect;
import gg.warcraft.monolith.app.effect.renderer.IterativeEffectRenderer;
import gg.warcraft.monolith.app.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.app.effect.vectors.CircleVectors;
import gg.warcraft.monolith.app.effect.vectors.DomeVectors;
import gg.warcraft.monolith.app.effect.vectors.LineVectors;
import gg.warcraft.monolith.app.effect.vectors.OriginVector;
import gg.warcraft.monolith.app.effect.vectors.PointVector;
import gg.warcraft.monolith.app.effect.vectors.RingVectors;
import gg.warcraft.monolith.app.effect.vectors.SphereVectors;
import gg.warcraft.monolith.app.entity.attribute.service.DefaultAttributeCommandService;
import gg.warcraft.monolith.app.entity.attribute.service.DefaultAttributeQueryService;
import gg.warcraft.monolith.app.entity.attribute.service.DefaultAttributeRepository;
import gg.warcraft.monolith.app.entity.player.hiding.DefaultPlayerHidingCommandService;
import gg.warcraft.monolith.app.entity.player.hiding.DefaultPlayerHidingQueryService;
import gg.warcraft.monolith.app.entity.player.hiding.DefaultPlayerHidingRepository;
import gg.warcraft.monolith.app.entity.player.service.DefaultPlayerCommandService;
import gg.warcraft.monolith.app.entity.player.service.DefaultPlayerProfileRepository;
import gg.warcraft.monolith.app.entity.player.service.DefaultPlayerQueryService;
import gg.warcraft.monolith.app.entity.service.DefaultEntityCommandService;
import gg.warcraft.monolith.app.entity.service.DefaultEntityProfileRepository;
import gg.warcraft.monolith.app.entity.service.DefaultEntityQueryService;
import gg.warcraft.monolith.app.entity.status.service.DefaultStatusCommandService;
import gg.warcraft.monolith.app.entity.status.service.DefaultStatusQueryService;
import gg.warcraft.monolith.app.entity.status.service.DefaultStatusRepository;
import gg.warcraft.monolith.app.item.SimpleItemBuilder;
import gg.warcraft.monolith.app.item.SimpleItemReader;
import gg.warcraft.monolith.app.menu.SimpleButtonBuilder;
import gg.warcraft.monolith.app.menu.SimpleMenuBuilder;
import gg.warcraft.monolith.app.menu.service.DefaultMenuCommandService;
import gg.warcraft.monolith.app.menu.service.DefaultMenuQueryService;
import gg.warcraft.monolith.app.menu.service.DefaultMenuRepository;
import gg.warcraft.monolith.app.util.DefaultMathUtils;
import gg.warcraft.monolith.app.util.DefaultStringUtils;
import gg.warcraft.monolith.app.util.DefaultTimeUtils;
import gg.warcraft.monolith.app.world.DefaultDirectionUtils;
import gg.warcraft.monolith.app.world.block.DefaultBlockTypeUtils;
import gg.warcraft.monolith.app.world.block.DefaultBlockUtils;
import gg.warcraft.monolith.app.world.block.backup.service.DefaultBlockBackupCommandService;
import gg.warcraft.monolith.app.world.block.backup.service.DefaultBlockBackupQueryService;
import gg.warcraft.monolith.app.world.block.backup.service.DefaultBlockBackupRepository;
import gg.warcraft.monolith.app.world.block.box.SimpleBoundingBlockBox;
import gg.warcraft.monolith.app.world.block.build.service.DefaultBlockBuildCommandService;
import gg.warcraft.monolith.app.world.block.build.service.DefaultBlockBuildQueryService;
import gg.warcraft.monolith.app.world.block.build.service.DefaultBlockBuildRepository;
import gg.warcraft.monolith.app.world.block.spoofing.DefaultBlockSpoofingCommandService;
import gg.warcraft.monolith.app.world.block.spoofing.DefaultBlockSpoofingQueryService;
import gg.warcraft.monolith.app.world.block.spoofing.DefaultBlockSpoofingRepository;
import gg.warcraft.monolith.app.world.service.DefaultWorldCommandService;
import gg.warcraft.monolith.app.world.service.DefaultWorldQueryService;
import org.joml.Vector3ic;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class AbstractMonolithModule extends AbstractModule {
    private final String persistenceService;
    private final String redisHost;
    private final int redisPort;
    private final String configurationService;
    private final String gitHubAccount;
    private final String gitHubRepository;
    private final String entityService;
    private final String playerService;
    private final float baseHealth;
    private final WorldType buildRepositoryWorld;
    private final Vector3ic buildRepositoryMinimumCorner;
    private final Vector3ic buildRepositoryMaximumCorner;

    public AbstractMonolithModule(String configurationService, String gitHubAccount, String gitHubRepository,
                                  String persistenceService, String redisHost, int redisPort,
                                  String entityService, String playerService, float baseHealth,
                                  WorldType buildRepositoryWorld, Vector3ic buildRepositoryMinimumCorner,
                                  Vector3ic buildRepositoryMaximumCorner) {
        this.configurationService = configurationService;
        this.gitHubAccount = gitHubAccount;
        this.gitHubRepository = gitHubRepository;
        this.persistenceService = persistenceService;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.entityService = entityService;
        this.playerService = playerService;
        this.baseHealth = baseHealth;
        this.buildRepositoryWorld = buildRepositoryWorld;
        this.buildRepositoryMinimumCorner = buildRepositoryMinimumCorner;
        this.buildRepositoryMaximumCorner = buildRepositoryMaximumCorner;
    }

    @Override
    protected void configure() {
        configureCommand();
        configureConfiguration();
        configureCore();
        configureEffect();
        configureEntity();
        configureItem();
        configureMenu();
        configurePersistence();
        configureUtil();
        configureWorld();
    }

    private void configureCommand() {
        bind(CommandCommandService.class).to(DefaultCommandCommandService.class);
        bind(CommandQueryService.class).to(DefaultCommandQueryService.class);
        bind(CommandRepository.class).to(DefaultCommandRepository.class);

        bind(CommandSender.class).annotatedWith(Console.class).to(ConsoleCommandSender.class);
    }

    private void configureConfiguration() {
        switch (configurationService) {
            case "LOCAL":
                bind(ConfigurationCommandService.class).to(LocalConfigurationCommandService.class);
                bind(ConfigurationQueryService.class).to(DefaultConfigurationQueryService.class);
                bind(ConfigurationRepository.class).to(DefaultConfigurationRepository.class);
                break;
            case "GITHUB":
                bind(String.class).annotatedWith(Names.named("GitHubAccount")).toInstance(gitHubAccount);
                bind(String.class).annotatedWith(Names.named("GitHubRepository")).toInstance(gitHubRepository);

                bind(ConfigurationCommandService.class).to(GitHubConfigurationCommandService.class);
                bind(ConfigurationQueryService.class).to(DefaultConfigurationQueryService.class);
                bind(ConfigurationRepository.class).to(DefaultConfigurationRepository.class);
                break;
            case "CUSTOM":
                // do nothing, the implementing server should provide bindings
                break;
            default:
                throw new IllegalArgumentException("Illegal configuration service in Monolith configuration: " + configurationService);
        }
    }

    private void configureCore() {
        bind(MonolithPluginUtils.class).to(DefaultMonolithPluginUtils.class);

        bind(EventService.class).to(GuavaEventService.class);

        SimpleModule monolithMapperModule = new MonolithMapperModule();

        ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.registerModule(monolithMapperModule);
        bind(ObjectMapper.class).annotatedWith(JsonMapper.class).toProvider(jsonMapper::copy);

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        yamlMapper.registerModule(monolithMapperModule);
        bind(ObjectMapper.class).annotatedWith(YamlMapper.class).toProvider(yamlMapper::copy);
    }

    private void configureEffect() {
        install(new FactoryModuleBuilder()
                .implement(EffectVectors.class, Names.named("circle"), CircleVectors.class)
                .implement(EffectVectors.class, Names.named("dome"), DomeVectors.class)
                .implement(EffectVectors.class, Names.named("line"), LineVectors.class)
                .implement(EffectVectors.class, Names.named("origin"), OriginVector.class)
                .implement(EffectVectors.class, Names.named("point"), PointVector.class)
                .implement(EffectVectors.class, Names.named("ring"), RingVectors.class)
                .implement(EffectVectors.class, Names.named("sphere"), SphereVectors.class)
                .build(EffectVectorsFactory.class));

        install(new FactoryModuleBuilder()
                .implement(EffectRenderer.class, Names.named("simple"), SimpleEffectRenderer.class)
                .implement(EffectRenderer.class, Names.named("iterative"), IterativeEffectRenderer.class)
                .build(EffectRendererFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Effect.class, Names.named("simple"), SimpleEffect.class)
                .implement(Effect.class, Names.named("periodic"), PeriodicEffect.class)
                .implement(Effect.class, Names.named("dynamic"), DynamicEffect.class)
                .build(EffectFactory.class));
    }

    private void configureEntity() {
        bind(AttributeCommandService.class).to(DefaultAttributeCommandService.class);
        bind(AttributeQueryService.class).to(DefaultAttributeQueryService.class);
        bind(AttributeRepository.class).to(DefaultAttributeRepository.class);

        bind(StatusCommandService.class).to(DefaultStatusCommandService.class);
        bind(StatusQueryService.class).to(DefaultStatusQueryService.class);
        bind(StatusRepository.class).to(DefaultStatusRepository.class);

        bind(PlayerHidingCommandService.class).to(DefaultPlayerHidingCommandService.class);
        bind(PlayerHidingQueryService.class).to(DefaultPlayerHidingQueryService.class);
        bind(PlayerHidingRepository.class).to(DefaultPlayerHidingRepository.class);

        bind(Float.class).annotatedWith(Names.named("BaseHealth")).toInstance(baseHealth);

        switch (entityService) {
            case "DEFAULT":
                bind(EntityCommandService.class).to(DefaultEntityCommandService.class);
                bind(EntityQueryService.class).to(DefaultEntityQueryService.class);
                bind(EntityProfileRepository.class).to(DefaultEntityProfileRepository.class);
                break;
            case "CUSTOM":
                // do nothing, the implementing server should provide bindings
                break;
            default:
                throw new IllegalArgumentException("Illegal entity service in Monolith configuration: " + entityService);
        }

        switch (playerService) {
            case "DEFAULT":
                bind(PlayerCommandService.class).to(DefaultPlayerCommandService.class);
                bind(PlayerQueryService.class).to(DefaultPlayerQueryService.class);
                bind(PlayerProfileRepository.class).to(DefaultPlayerProfileRepository.class);
                break;
            case "CUSTOM":
                // do nothing, the implementing server should provide bindings
                break;
            default:
                throw new IllegalArgumentException("Illegal player service in Monolith configuration: " + entityService);
        }
    }

    private void configureItem() {
        install(new FactoryModuleBuilder()
                .implement(ItemBuilder.class, SimpleItemBuilder.class)
                .build(ItemBuilderFactory.class));
        install(new FactoryModuleBuilder()
                .implement(ItemReader.class, SimpleItemReader.class)
                .build(ItemReaderFactory.class));
    }

    private void configureMenu() {
        bind(MenuCommandService.class).to(DefaultMenuCommandService.class);
        bind(MenuQueryService.class).to(DefaultMenuQueryService.class);
        bind(MenuRepository.class).to(DefaultMenuRepository.class);

        install(new FactoryModuleBuilder()
                .implement(ButtonBuilder.class, SimpleButtonBuilder.class)
                .build(ButtonBuilderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(MenuBuilder.class, SimpleMenuBuilder.class)
                .build(MenuBuilderFactory.class));
    }

    private void configurePersistence() {
        switch (persistenceService) {
            case "REDIS":
                JedisPoolConfig jedisConfiguration = new JedisPoolConfig();
                jedisConfiguration.setBlockWhenExhausted(false);
                jedisConfiguration.setMaxTotal(8);
                JedisPool jedisPool = new JedisPool(jedisConfiguration, redisHost, redisPort, 10);
                bind(JedisPool.class).toInstance(jedisPool);
                bind(PersistenceService.class).to(JedisPersistenceService.class);
                break;
            case "CUSTOM":
                // do nothing, the implementing server should provide bindings
                break;
            default:
                throw new IllegalArgumentException("Illegal persistence service in Monolith configuration: " + persistenceService);
        }
    }

    private void configureUtil() {
        bind(MathUtils.class).to(DefaultMathUtils.class);
        bind(StringUtils.class).to(DefaultStringUtils.class);
        bind(TimeUtils.class).to(DefaultTimeUtils.class);
    }

    private void configureWorld() {
        bind(WorldQueryService.class).to(DefaultWorldQueryService.class);
        bind(WorldCommandService.class).to(DefaultWorldCommandService.class);

        bind(WorldType.class).annotatedWith(Names.named("BuildRepositoryWorld"))
                .toInstance(buildRepositoryWorld);
        bind(Vector3ic.class).annotatedWith(Names.named("BuildRepositoryMinimumCorner"))
                .toInstance(buildRepositoryMinimumCorner);
        bind(Vector3ic.class).annotatedWith(Names.named("BuildRepositoryMaximumCorner"))
                .toInstance(buildRepositoryMaximumCorner);

        bind(BlockBuildCommandService.class).to(DefaultBlockBuildCommandService.class);
        bind(BlockBuildQueryService.class).to(DefaultBlockBuildQueryService.class);
        bind(BlockBuildRepository.class).to(DefaultBlockBuildRepository.class);

        bind(BlockBackupCommandService.class).to(DefaultBlockBackupCommandService.class);
        bind(BlockBackupQueryService.class).to(DefaultBlockBackupQueryService.class);
        bind(BlockBackupRepository.class).to(DefaultBlockBackupRepository.class);

        bind(BlockSpoofingCommandService.class).to(DefaultBlockSpoofingCommandService.class);
        bind(BlockSpoofingQueryService.class).to(DefaultBlockSpoofingQueryService.class);
        bind(BlockSpoofingRepository.class).to(DefaultBlockSpoofingRepository.class);

        bind(BlockUtils.class).to(DefaultBlockUtils.class);
        bind(BlockTypeUtils.class).to(DefaultBlockTypeUtils.class);
        bind(DirectionUtils.class).to(DefaultDirectionUtils.class);

        install(new FactoryModuleBuilder()
                .implement(BoundingBlockBox.class, SimpleBoundingBlockBox.class)
                .build(BoundingBlockBoxFactory.class));
    }
}
