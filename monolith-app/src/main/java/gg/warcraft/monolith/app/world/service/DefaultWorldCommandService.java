package gg.warcraft.monolith.app.world.service;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.Sign;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldServerAdapter;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class DefaultWorldCommandService implements WorldCommandService {
    private final WorldServerAdapter worldServerAdapter;

    @Inject
    public DefaultWorldCommandService(WorldServerAdapter worldServerAdapter) {
        this.worldServerAdapter = worldServerAdapter;
    }

    @Override
    public void setBlock(Block block) {
        worldServerAdapter.setBlock(block);
    }

    @Override
    public void setBlockType(Block block, BlockType type) {
        worldServerAdapter.setBlockType(block, type);
    }

    @Override
    public void setSignLines(Sign sign, String[] lines) {
        worldServerAdapter.setSignLines(sign, lines);
    }

    @Override
    public void dropItemsAt(List<Item> items, Location location) {
        worldServerAdapter.dropItemsAt(items, location);
    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        worldServerAdapter.playSound(location, sound, category, volume, pitch);
    }

    @Override
    public void strikeLightning(Location location, boolean ambient) {
        worldServerAdapter.strikeLightning(location, ambient);
    }

    @Override
    public void createExplosion(Location location, boolean ambient) {
        worldServerAdapter.createExplosion(location, ambient);
    }

    @Override
    public UUID createArrow(UUID shooterId, Location location, Vector3f direction, float speed, float spread) {
        return worldServerAdapter.createArrow(shooterId, location, direction, speed, spread);
    }
}
