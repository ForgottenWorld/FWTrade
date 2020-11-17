package me.architetto.fwtrade.listeners;

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

    @EventHandler
    public void onInvetoryClose(InventoryCloseEvent event) {

        if (!(event.getPlayer() instanceof Player))
            return;

        Player player = (Player) event.getPlayer();

        if (tradeManager.isTrading(player.getUniqueId())) {
            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
            tradeGui.getRelativeContent(player.getUniqueId());
            Player secondTrader = Bukkit.getPlayer(tradeGui.getOtherTrader(player.getUniqueId()));

            if (secondTrader != null && !secondTrader.equals(player)) { //todo il secondo check va tolto , è solo per evitare errori in testing
                secondTrader.closeInventory();

                tradeManager.removeTrader(player.getUniqueId());
                tradeManager.removeTrader(secondTrader.getUniqueId());
            }
            //todo ridare oggetti ai propretari
            //todo eliminare il trade dalle varie variabili in TradeManager (fatto forse)
        }



    }

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player))
            return;

        //event.getClick() potrei analizzare il tipo di click ??  credo di si

        Player player = (Player) event.getWhoClicked();

        if (tradeManager.isTrading(player.getUniqueId()) && Objects.equals(event.getClickedInventory(), player.getInventory())) {
            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
            if (tradeGui.isAddable(player.getUniqueId()) && event.getCurrentItem() != null) {
                tradeGui.addItemToInventory(player.getUniqueId(), event.getCurrentItem());
                event.getCurrentItem().setAmount(0);
                event.setCancelled(true);
            }
        }
    }
}
