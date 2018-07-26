package gg.warcraft.monolith.spigot.menu;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.spigot.item.SpigotItemTypeMapper;
import gg.warcraft.monolith.spigot.world.MaterialData;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class SpigotMenuMapper {
    private final Server server;
    private final SpigotItemTypeMapper itemTypeMapper;

    @Inject
    public SpigotMenuMapper(Server server, SpigotItemTypeMapper itemTypeMapper) {
        this.server = server;
        this.itemTypeMapper = itemTypeMapper;
    }

    public Inventory map(Menu menu, UUID viewerId) {
        Player player = server.getPlayer(viewerId);
        Inventory inventory = server.createInventory(player, menu.getSize().getNumberOfSlots(), menu.getTitle());
        menu.getButtons().forEach((slot, button) -> {
            ItemStack item = map(button);
            inventory.setItem(slot, item);
        });
        return inventory;
    }

    public ItemStack map(Button button) {
        MaterialData iconData = itemTypeMapper.map(button.getIcon());
        ItemStack item = new ItemStack(iconData.getMaterial(), 1, (short) 0, iconData.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(button.getTitle());
        meta.setLore(button.getTooltip());
        item.setItemMeta(meta);
        return item;
    }
}