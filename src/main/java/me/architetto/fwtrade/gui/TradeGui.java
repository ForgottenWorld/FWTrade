package me.architetto.fwtrade.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TradeGui {

    private final UUID playerOne;
    private final UUID playerTwo;

    private final Gui tradeGui;

    private OutlinePane inventoryL;
    private OutlinePane inventoryR;

    private final boolean tradeStatusLeft;
    private final boolean tradeStaturRight;

    List<Integer> indexToCheck = Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29, 36, 37, 38, 45, 46, 47);

    public TradeGui(Player playerOne,Player playerTwo) {
        this.playerOne = playerOne.getUniqueId();
        this.playerTwo = playerTwo.getUniqueId();

        this.tradeGui = prepareGui();

        this.tradeStatusLeft = false;
        this.tradeStaturRight = false;
    }

    public Gui prepareGui() {

        Gui tradeGUI = new Gui(6, "TRADE INTERAFCE");

        OutlinePane background = new OutlinePane(0, 0, 7, 6, Pane.Priority.LOWEST);
        tradeGUI.addPane(background);

        OutlinePane divider = new OutlinePane(4, 0, 1, 6, Pane.Priority.HIGH);
        divider.addItem(new GuiItem(new ItemStack(Material.IRON_BARS)));
        divider.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        divider.setRepeat(true);
        tradeGUI.addPane(divider);

        OutlinePane tradeStatusLeft = new OutlinePane(3, 0, 1, 6, Pane.Priority.HIGH);
        tradeStatusLeft.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        tradeStatusLeft.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeStatusLeft.setRepeat(true);
        tradeGUI.addPane(tradeStatusLeft);

        OutlinePane tradeStatusRight = new OutlinePane(5, 0, 1, 6, Pane.Priority.HIGH);
        tradeStatusRight.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        tradeStatusRight.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeStatusRight.setRepeat(true);
        tradeGUI.addPane(tradeStatusRight);

        OutlinePane inventoryL = new OutlinePane(0, 1, 3, 5, Pane.Priority.HIGH);
        inventoryL.setOnClick(inventoryClickEvent -> {
            if (inventoryClickEvent.getWhoClicked().getUniqueId() == playerTwo)
                inventoryClickEvent.setCancelled(true);});
        this.inventoryL = inventoryL;
        tradeGUI.addPane(inventoryL);

        OutlinePane inventoryR = new OutlinePane(6, 1, 3, 5, Pane.Priority.HIGH);
        inventoryR.setOnClick(inventoryClickEvent -> {
            if (inventoryClickEvent.getWhoClicked().getUniqueId() == playerOne)
                inventoryClickEvent.setCancelled(true);});
        this.inventoryR = inventoryR;
        tradeGUI.addPane(inventoryR);

        return tradeGUI;
    }

    public void showGui() {
        tradeGui.show(Objects.requireNonNull(Bukkit.getPlayer(playerOne)));
        tradeGui.show(Objects.requireNonNull(Bukkit.getPlayer(playerTwo)));
    }

    public void updateGui() {
        tradeGui.update();
    }

    public Gui getGui() {
        return tradeGui;
    }

    public void addItemToInventory(UUID uuid,ItemStack itemStack) {

        if (playerOne == uuid)
            inventoryL.addItem(new GuiItem(itemStack));
        if (playerTwo == uuid)
            inventoryR.addItem(new GuiItem(itemStack));

    }

    public boolean isTradeStatusLeft() {
        return this.tradeStatusLeft;
    }

    public boolean isTradeStaturRight(){
        return tradeStaturRight;
    }

    public boolean isAddable(UUID uuid) {
        Inventory inventory = tradeGui.getInventory();
        if (playerOne == uuid) {
            for (int index : indexToCheck) {
                if (inventory.getItem(index) == null)
                    return true;
            }
            return false;
        }
        return false;
        //todo va inserito anche per i slot a destra (al momento funziona solo a sinistra
    }

    public UUID getOtherTrader(UUID uuid) {
        if (uuid == playerOne)
            return playerTwo;
        if (uuid == playerTwo)
            return playerOne;
        return null;
    }


}
