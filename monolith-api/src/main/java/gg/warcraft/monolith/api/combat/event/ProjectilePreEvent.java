package gg.warcraft.monolith.api.combat.event;

import gg.warcraft.monolith.api.core.PreEvent;

import java.util.UUID;

public interface ProjectilePreEvent extends PreEvent {

    UUID getProjectileId();
}
