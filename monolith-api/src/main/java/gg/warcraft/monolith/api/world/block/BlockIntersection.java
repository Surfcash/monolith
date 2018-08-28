package gg.warcraft.monolith.api.world.block;

import gg.warcraft.monolith.api.world.location.Location;

public interface BlockIntersection {

    /**
     * @return The block that was intersected. Never null.
     */
    Block getBlock();

    /**
     * @return The block face that was intersected. Never null.
     */
    BlockFace getFace();

    /**
     * @return The exact location where the block was intersected. Never null.
     */
    Location getLocation();
}
