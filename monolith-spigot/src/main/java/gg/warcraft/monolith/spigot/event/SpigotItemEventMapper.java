package gg.warcraft.monolith.spigot.event;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.event.ItemPickupEvent;
import gg.warcraft.monolith.api.item.event.ItemPrePickupEvent;
import gg.warcraft.monolith.app.item.event.SimpleItemPickupEvent;
import gg.warcraft.monolith.app.item.event.SimpleItemPrePickupEvent;
import gg.warcraft.monolith.spigot.entity.SpigotEntityTypeMapper;
import gg.warcraft.monolith.spigot.item.SpigotItemMapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.UUID;

public class SpigotItemEventMapper implements Listener {
    private EventService eventService;
    private final SpigotItemMapper itemMapper;
    private final SpigotEntityTypeMapper entityTypeMapper;

    @Inject
    public SpigotItemEventMapper(EventService eventService,
                                 SpigotItemMapper itemMapper,
                                 SpigotEntityTypeMapper entityTypeMapper) {
        this.eventService = eventService;
        this.itemMapper = itemMapper;
        this.entityTypeMapper = entityTypeMapper;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        UUID itemId = event.getItem().getUniqueId();
        Item item = itemMapper.map(event.getItem().getItemStack());

        UUID entityId = event.getEntity().getUniqueId();
        EntityType entityType = entityTypeMapper.map(event.getEntity().getType());

        ItemPrePickupEvent itemPrePickupEvent = new SimpleItemPrePickupEvent(itemId, item, entityId, entityType, event.isCancelled());
        eventService.publish(itemPrePickupEvent);

        event.setCancelled(!itemPrePickupEvent.isAllowed());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityPickupItemEventMonitor(EntityPickupItemEvent event) {
        UUID itemId = event.getItem().getUniqueId();
        Item item = itemMapper.map(event.getItem().getItemStack());

        UUID entityId = event.getEntity().getUniqueId();
        EntityType entityType = entityTypeMapper.map(event.getEntity().getType());

        ItemPickupEvent itemPickupEvent = new SimpleItemPickupEvent(itemId, item, entityId, entityType);
        eventService.publish(itemPickupEvent);
    }
}