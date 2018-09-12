package gg.warcraft.monolith.spigot.event;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.Click;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.service.MenuQueryService;
import gg.warcraft.monolith.app.menu.SimpleClick;
import gg.warcraft.monolith.spigot.menu.MonolithMenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class SpigotInventoryEventMapper implements Listener {
    private final MenuQueryService menuQueryService;

    @Inject
    public SpigotInventoryEventMapper(MenuQueryService menuQueryService) {
        this.menuQueryService = menuQueryService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        UUID clickerId = event.getWhoClicked().getUniqueId();
        if (event.getInventory().getHolder() instanceof MonolithMenuHolder) {
            event.setCancelled(true);

            Menu menu = menuQueryService.getMenu(clickerId);
            if (menu != null) {
                if (event.isLeftClick() || event.isRightClick()) {
                    Button button = menu.getButtons().get(event.getSlot());
                    if (button != null) {
                        Click click = new SimpleClick(clickerId, event.isLeftClick(), event.isShiftClick());
                        button.getAction().accept(click);
                    }
                    // TODO publish Monolith menu click event
                }
            }
        } else {
            // TODO publish Monolith inventory pre click event
        }
    }
}
