package gg.warcraft.monolith.api.entity.service;

import gg.warcraft.monolith.api.entity.EntityServerData;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionType;
import org.joml.Vector3fc;

import java.util.List;
import java.util.UUID;

/**
 * This adapter is injectable, however you generally have no need for it. Use the command and query services instead.
 * <p>
 * The EntityServerAdapter abstracts entity related server implementations from the Monolith domain. It can be used to
 * query the {@code EntityServerData} of an {@code Entity}, change its velocity, location, or add and remove potion
 * effects.
 */
public interface EntityServerAdapter {

    /**
     * @param entityId The id of the entity. Can not be null.
     * @return The server data of the entity. Can be null.
     */
    EntityServerData getEntityServerData(UUID entityId);

    /**
     * @param location The location.
     * @param deltaX   The max distance from the location along the X axes.
     * @param deltaY   The max distance from the location along the Y axes.
     * @param deltaZ   The max distance from the location along the Z axes.
     * @return A list with the server data of all entities found near the location within the given deltas. Never null,
     * but can be empty. Items are never null.
     */
    List<EntityServerData> getNearbyEntitiesServerData(Location location, float deltaX, float deltaY, float deltaZ);

    /**
     * @param entityId The id of the entity. Can not be null.
     * @param velocity The new velocity of the entity. Can not be null.
     */
    void setVelocity(UUID entityId, Vector3fc velocity);

    /**
     * @param entityId The id of the entity. Can not be null.
     * @param effect   The potion effect to add to the entity. Can not be null.
     */
    void addPotionEffect(UUID entityId, PotionEffect effect);

    /**
     * @param entityId The id of the entity. Can not be null.
     * @param effect   The potion effect to remove from the entity. Can not be null.
     */
    void removePotionEffect(UUID entityId, PotionType type);

    /**
     * @param type          The type of entity to spawn. Can not be null.
     * @param spawnLocation The location to spawn the entity at. Can not be null.
     */
    void spawnEntity(EntityType type, Location spawnLocation);

    /**
     * @param entityId The id of the entity. Can not be null.
     * @param location The location to teleport the entity to. Can not be null.
     */
    void teleport(UUID entityId, Location location);
}