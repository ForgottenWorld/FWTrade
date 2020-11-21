package me.architetto.fwtrade.listeners;

import me.architetto.fwtrade.gui.TradeManager;
import me.architetto.fwtrade.gui.TradeGui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class InventoryListener implements Listener {

    TradeManager tradeManager = TradeManager.getInstance();

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();

        if (tradeManager.isTrading(player.getUniqueId())
                && event.getClickedInventory() == player.getOpenInventory().getBottomInventory()) {
            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
            if (tradeGui.isAddable(player) && event.getCurrentItem() != null) {
                tradeGui.addItemToPaneInventory(player, event.getCurrentItem());
                tradeGui.updateGui();
                player.getOpenInventory().getBottomInventory().remove(event.getCurrentItem());
                event.setCancelled(true);
            }
        }
    }
}
