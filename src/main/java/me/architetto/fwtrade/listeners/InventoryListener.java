package me.architetto.fwtrade.listeners;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiListener;
import me.architetto.fwtrade.gui.TradeManager;
import me.architetto.fwtrade.gui.TradeGui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import java.util.Objects;


public class InventoryListener implements Listener {

    TradeManager tradeManager = TradeManager.getInstance();

    /*
    @EventHandler
    public void onInvetoryClose(InventoryCloseEvent event) {

        if (!(event.getPlayer() instanceof Player))
            return;

        Player player = (Player) event.getPlayer();

        if (tradeManager.isTrading(player.getUniqueId())) {

            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
            tradeGui.getRelativeContent(player.getUniqueId());
            Player secondTrader = Bukkit.getPlayer(tradeGui.getOtherTrader(player.getUniqueId()));

            if (secondTrader != null) {
                secondTrader.closeInventory();

                tradeManager.removeTrader(player.getUniqueId());
                tradeManager.removeTrader(secondTrader.getUniqueId());
            }
            //todo ridare oggetti ai propretari
            //todo eliminare il trade dalle varie variabili in TradeManager (fatto forse)
        }



    }

     */

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();
        Bukkit.getConsoleSender().sendMessage(player.getDisplayName());

        if (tradeManager.isTrading(player.getUniqueId())
                && event.getClickedInventory() == player.getOpenInventory().getBottomInventory()) {
            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
            if (tradeGui.isAddable(player) && event.getCurrentItem() != null) {
                tradeGui.addItemToPaneInventory(player, event.getCurrentItem());
                tradeGui.updateGui();
                player.getOpenInventory().getBottomInventory().remove(event.getCurrentItem());
                //event.setCancelled(true);
            }
        }
    }
}
