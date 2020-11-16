package me.architetto.fwtrade.listeners;

import me.architetto.fwtrade.gui.TradeManager;
import me.architetto.fwtrade.gui.TradeGui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;


public class InventoryListener implements Listener {

    TradeManager tradeManager = TradeManager.getInstance();

    @EventHandler
    public void onInvetoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;

        Player player = (Player) event.getPlayer();

        if (tradeManager.isTrading(player.getUniqueId())) {
            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
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
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {

        Bukkit.getConsoleSender().sendMessage("TEST UNO");


        if (!(event.getSource().getHolder() instanceof Player))
            return;

        Bukkit.getConsoleSender().sendMessage("TEST DUE");

        Player player = (Player) event.getSource().getHolder();

        if (tradeManager.isTrading(player.getUniqueId())) {
            TradeGui tradeGui = tradeManager.getTradeGui(player.getUniqueId());
            if (event.getDestination().equals(tradeGui.getGui().getInventory())) {
                event.setCancelled(true);
                if (tradeGui.isAddable(player.getUniqueId()))
                    tradeGui.addItemToInventory(player.getUniqueId(),event.getItem());
            }
        }
    }
}
