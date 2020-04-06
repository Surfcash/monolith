package gg.warcraft.monolith.spigot.entity

import java.util.UUID
import java.util.logging.Logger

import gg.warcraft.monolith.api.combat.{CombatValue, PotionEffect}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.entity.Entity.Type
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.data.{EntityData, EntityDataService}
import gg.warcraft.monolith.api.entity.status.StatusService
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.util.Ops._
import gg.warcraft.monolith.api.world.Location
import gg.warcraft.monolith.spigot.world.SpigotLocationMapper
import gg.warcraft.monolith.spigot.SpigotPlayer
import gg.warcraft.monolith.spigot.item.SpigotItemMapper
import gg.warcraft.monolith.spigot.math.SpigotVectorMapper
import org.bukkit.Server
import org.bukkit.entity.EntityType
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

import scala.jdk.CollectionConverters._

class SpigotEntityService(
    implicit server: Server,
    plugin: Plugin,
    logger: Logger,
    attributeService: AttributeService,
    statusService: StatusService,
    teamService: TeamService,
    dataService: EntityDataService,
    playerService: PlayerService,
    vectorMapper: SpigotVectorMapper,
    locationMapper: SpigotLocationMapper,
    itemMapper: SpigotItemMapper,
    entityTypeMapper: SpigotEntityTypeMapper
) extends EntityService {
  private def getSpigotEntity(id: UUID): Option[SpigotEntity] = {
    server getEntity id match {
      case it: SpigotEntity => Some(it)
      case _                => None
    }
  }

  override def getEntity(id: UUID): Option[Entity] = {
    server getEntity id match {
      case _: SpigotPlayer  => playerService getPlayer id
      case it: SpigotEntity => new SpigotEntityAdapter(it) |> Some.apply
      case _                => None
    }
  }

  override def getNearbyEntities(
      location: Location,
      radius: (Float, Float, Float)
  ): List[Entity] = {
    val spigotLoc = locationMapper map location
    spigotLoc.getWorld
      .getNearbyEntities(spigotLoc, radius._1, radius._2, radius._3)
      .asScala
      .filter { _.isInstanceOf[SpigotEntity] }
      .filter { _.getType != EntityType.ARMOR_STAND }
      .map { _.asInstanceOf[SpigotEntity] }
      .map { new SpigotEntityAdapter(_) }
      .toList
  }

  override def setVelocity(id: UUID, velocity: Vector3f): Unit =
    getSpigotEntity(id) foreach {
      _ setVelocity new SpigotVector(velocity.x, velocity.y, velocity.z)
    }

  override def addPotionEffect(id: UUID, effect: PotionEffect): Unit =
    getSpigotEntity(id) foreach { _ addPotionEffect (potionMapper map effect) }
  /*
            org.bukkit.potion.PotionEffectType spigotPotionEffectType = potionEffectTypeMapper.map(effect.getType());
            org.bukkit.potion.PotionEffect spigotPotionEffect = new org.bukkit.potion.PotionEffect(
                    spigotPotionEffectType, effect.getDuration().inTicks(), effect.getLevel() - 1,
                    effect.isAmbient(), effect.hasParticles());
            livingEntity.addPotionEffect(spigotPotionEffect);
   */

  override def removePotionEffect(id: UUID, effect: PotionEffect): Unit = {}
  /*
        Entity entity = server.getEntity(entityId);
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            org.bukkit.potion.PotionEffectType spigotPotionEffectType = potionEffectTypeMapper.map(type);
            livingEntity.removePotionEffect(spigotPotionEffectType);
        }
   */

  override def spawnEntity(
      typed: Type,
      location: Location,
      team: Option[Team] = None
  ): UUID = {
    val spigotEntityType = entityTypeMapper map typed
    val spigotLocation = locationMapper map location
    if (!spigotLocation.getChunk.isLoaded) spigotLocation.getChunk.load()
    val spigotEntity =
      spigotLocation.getWorld spawnEntity (spigotLocation, spigotEntityType)
    dataService setEntityData EntityData(spigotEntity.getUniqueId, team)
    spigotEntity.getUniqueId
  }

  override def removeEntity(id: UUID): Unit = {
    dataService deleteEntityData id
    getSpigotEntity(id) foreach { _.remove() }
  }

  override def teleportEntity(
      id: UUID,
      location: Location,
      direction: Vector3f = null
  ): Unit = getSpigotEntity(id) foreach { entity =>
    val spigotLocation = locationMapper map location
    val spigotDirection =
      if (direction != null) vectorMapper map direction
      else entity.getLocation.getDirection
    spigotLocation setDirection spigotDirection
    entity teleport spigotLocation
  }

  override def damageEntity(id: UUID, amount: CombatValue): Unit =
    getSpigotEntity(id) foreach { entity =>
      val metaData = new FixedMetadataValue(plugin, amount)
      entity setMetadata (classOf[CombatValue].getCanonicalName, metaData)
      entity damage amount.modified
    }

  override def killEntity(id: UUID): Unit =
    getSpigotEntity(id) foreach { _ setHealth 0 }

  override def healEntity(id: UUID, amount: CombatValue): Unit = {}

  /*
   private void publishHealthChangedEvent(UUID entityId, EntityType entityType, float previousHealth) {
        Entity newEntity = entityQueryService.getEntity(entityId);
        if (newEntity.getAttributes() == null) {
            return; // FIXME not all entities on server have attributes, could this be due to migration?
        }
        float newHealth = newEntity.getHealth();
        if (newHealth != previousHealth) { // FIXME should this be in the server event mappers instead? atm it will only trigger of Monolith health changes
            float maxHealth = newEntity.getAttributes().getValue(GenericAttribute.MAX_HEALTH);
            float previousPercentHealth = previousHealth / maxHealth;
            float newPercentHealth = newHealth / maxHealth;
            EntityHealthChangedEvent entityHealthChangedEvent = new EntityHealthChangedEvent(entityId, entityType,
                    previousHealth, previousPercentHealth, newHealth, newPercentHealth);
            eventService.publish(entityHealthChangedEvent);
        }
    }

    @Override
    public void heal(UUID entityId, CombatValue amount) {
        Entity entity = entityQueryService.getEntity(entityId);
        EntityType entityType = entity.getType();
        EntityPreHealEvent entityPreHealEvent = new EntityPreHealEvent(entityId, entityType, amount, false, false);
        eventService.publish(entityPreHealEvent);
        if (!entityPreHealEvent.allowed()) {
            return;
        }

        float previousHealth = entity.getHealth();

        CombatValue heal = entityPreHealEvent.heal();
        entityServerAdapter.heal(entityId, heal.modified());

        EntityHealEvent entityHealEvent = new EntityHealEvent(entityId, entityType, heal);
        eventService.publish(entityHealEvent);

        publishHealthChangedEvent(entityId, entityType, previousHealth);
    }


     @Override
    public void heal(UUID entityId, float amount) {
        Entity entity = server.getEntity(entityId);
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double newHealth = Math.min(livingEntity.getHealth() + amount, maxHealth);
            livingEntity.setHealth(newHealth);
        }
    }
   */

  override def burnEntity(id: UUID, duration: Duration): Unit = {
    getSpigotEntity(id) foreach { it =>
      val updatedFireTicks = it.getFireTicks + duration.inTicks
      it setFireTicks updatedFireTicks
    }
  }
}