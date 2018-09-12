package gg.warcraft.monolith.api.entity.player.service;

import gg.warcraft.monolith.api.entity.player.Currency;
import gg.warcraft.monolith.api.item.Item;

import java.util.UUID;

public interface PlayerCommandServiceDecorator extends PlayerCommandService {

    PlayerCommandService getPlayerCommandService();

    @Override
    default void addCurrency(UUID playerId, Currency currency, int amount) {
        getPlayerCommandService().addCurrency(playerId, currency, amount);
    }

    @Override
    default void removeCurrency(UUID playerId, Currency currency, int amount) {
        getPlayerCommandService().removeCurrency(playerId, currency, amount);
    }

    @Override
    default void revokeCurrency(UUID playerId, Currency currency, int amount) {
        getPlayerCommandService().revokeCurrency(playerId, currency, amount);
    }

    @Override
    default boolean giveItem(UUID playerId, Item item, boolean dropOnFullInventory) {
        return getPlayerCommandService().giveItem(playerId, item, dropOnFullInventory);
    }

    @Override
    default void update(UUID playerId) {
        getPlayerCommandService().update(playerId);
    }

    @Override
    default void sendMessage(UUID playerId, String message) {
        getPlayerCommandService().sendMessage(playerId, message);
    }

    @Override
    default void sendNotification(UUID playerId, String notification) {
        getPlayerCommandService().sendNotification(playerId, notification);
    }
}
