package gg.warcraft.monolith.spigot.entity.player;

import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.Equipment;
import gg.warcraft.monolith.api.entity.player.PlayerServerData;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.location.OrientedLocation;
import org.bukkit.OfflinePlayer;
import org.joml.AABBf;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class SpigotOfflinePlayerData implements PlayerServerData {
    private final OfflinePlayer player;

    public SpigotOfflinePlayerData(OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public UUID getEntityId() {
        return player.getUniqueId();
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public OrientedLocation getLocation() {
        throw new IllegalStateException("Failed to get location for offline player with id " + player.getUniqueId());
    }

    @Override
    public OrientedLocation getEyeLocation() {
        throw new IllegalStateException("Failed to get eye location for offline player with id " + player.getUniqueId());
    }

    @Override
    public Vector3f getVelocity() {
        throw new IllegalStateException("Failed to get velocity for offline player with id " + player.getUniqueId());
    }

    @Override
    public float getHealth() {
        throw new IllegalStateException("Failed to get health for offline player with id " + player.getUniqueId());
    }

    @Override
    public Equipment getEquipment() {
        throw new IllegalStateException("Failed to get equipment for offline player with id " + player.getUniqueId());
    }

    @Override
    public AABBf getBoundingBox() {
        throw new IllegalStateException("Failed to get bounding box for offline player with id " + player.getUniqueId());
    }

    @Override
    public List<Item> getInventory() {
        throw new IllegalStateException("Failed to get inventory for offline player with id " + player.getUniqueId());
    }

    @Override
    public boolean isSneaking() {
        throw new IllegalStateException("Failed to get sneaking for offline player with id " + player.getUniqueId());
    }

    @Override
    public boolean hasPermission(String permission) {
        throw new IllegalStateException("Failed to get permission for offline player with id " + player.getUniqueId());
    }
}
