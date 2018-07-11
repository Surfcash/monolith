package gg.warcraft.monolith.app.world.block.event;

import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.event.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleBlockBreakEvent extends SimpleBlockEvent implements BlockBreakEvent {
    private final List<Item> alternativeDrops;
    private final UUID playerId;

    public SimpleBlockBreakEvent(Block block, List<Item> alternativeDrops, UUID playerId) {
        super(block);
        this.alternativeDrops = alternativeDrops;
        this.playerId = playerId;
    }

    @Override
    public List<Item> getAlternativeDrops() {
        return new ArrayList<>(alternativeDrops);
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }
}
