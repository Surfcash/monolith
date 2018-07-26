package gg.warcraft.monolith.api.entity;

import gg.warcraft.monolith.api.world.OrientedLocation;
import org.joml.AABBf;
import org.joml.Vector3f;

import java.util.UUID;

public interface EntityServerData {

    /**
     * @return The id of the entity. Never null.
     */
    UUID getEntityId();

    /**
     * @return The type of the entity. Never null.
     */
    EntityType getType();

    /**
     * @return The location of the entity. Never null.
     */
    OrientedLocation getLocation();

    /**
     * @return The eye location of the entity. Never null.
     */
    OrientedLocation getEyeLocation();

    /**
     * @return The velocity of the entity. Never null.
     */
    Vector3f getVelocity();

    /**
     * @return The health of this entity.
     */
    float getHealth();

    /**
     * @return The equipment the entity is currently wearing. Never null.
     */
    Equipment getEquipment();

    /**
     * @return The axis aligned bounding box of this entity. Never null.
     */
    AABBf getBoundingBox();

    /**
     * @param permission The permission. Can not be null or empty.
     * @return True if the entity has the permission favorably set, false otherwise.
     */
    boolean hasPermission(String permission);
}
