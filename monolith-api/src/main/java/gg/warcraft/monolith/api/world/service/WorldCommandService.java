package gg.warcraft.monolith.api.world.service;

import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.Sign;
import gg.warcraft.monolith.api.world.location.Location;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

/**
 * This service is injectable.
 * <p>
 * The WorldCommandService serves as a point of entry into the world module implementation. It allows for easy updating
 * of block types and dropping items at a location.
 */
public interface WorldCommandService {

    /**
     * @param block The block to update. Can not be null.
     */
    void setBlock(Block block);

    /**
     * @param block The block to update. Can not be null.
     * @param type  The new type. Can not be null.
     */
    void setBlockType(Block block, BlockType type);

    /**
     * @param sign  The sign to update. Can not be null.
     * @param lines The new lines. Can not be null.
     */
    void setSignLines(Sign sign, String[] lines);

    /**
     * @param items    The items to drop. Can not be null, but can be empty. Items can not be null.
     * @param location The location to drop at. Can not be null.
     */
    void dropItemsAt(List<Item> items, Location location);

    void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch);

    void strikeLightning(Location location, boolean ambient);

    void createExplosion(Location location, boolean ambient);

    UUID createArrow(UUID shooterId, Location location, Vector3f direction, float speed, float spread);
}
