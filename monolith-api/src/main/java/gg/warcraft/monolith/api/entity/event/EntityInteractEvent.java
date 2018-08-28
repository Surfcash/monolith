package gg.warcraft.monolith.api.entity.event;

import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.location.Location;

import java.util.UUID;

public interface EntityInteractEvent extends EntityEvent {

    UUID getPlayerId();

    Item getItemInHand();

    Location getInteractLocation();
}
