package me.architetto.fwtrade.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TradeGui {

    private Player playerOne;
    private Player playerTwo;

    private Gui tradeGui;

    private OutlinePane leftPane;
    private OutlinePane rightPane;

    private HashMap<Integer,GuiItem> insertedGuiItems = new HashMap<>();

    private final boolean tradeStatusLeft;
    private final boolean tradeStaturRight;

    private boolean isClosed;

    List<Integer> indexLeft = Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29, 36, 37, 38, 45, 46, 47);
    List<Integer> indexRight = Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26, 33, 34, 35, 42, 43, 44, 51, 52, 53);


    public TradeGui(Player playerOne,Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.tradeGui = prepareGui();

        this.tradeStatusLeft = false;
        this.tradeStaturRight = false;

        this.isClosed = false;
    }

    public Gui prepareGui() {

        Gui tradeGUI = new Gui(4, "TRADE INTERAFCE");

        /*

        OutlinePane background = new OutlinePane(0, 0, 7, 6, Pane.Priority.LOWEST);
        tradeGUI.addPane(background);

         */

        OutlinePane divider = new OutlinePane(4, 0, 1, 4, Pane.Priority.LOW);
        divider.addItem(new GuiItem(new ItemStack(Material.IRON_BARS)));
        divider.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        divider.setRepeat(true);
        tradeGUI.addPane(divider);

        OutlinePane tradeStatusLeft = new OutlinePane(3, 0, 1, 4, Pane.Priority.LOW);
        tradeStatusLeft.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        tradeStatusLeft.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeStatusLeft.setRepeat(true);
        tradeGUI.addPane(tradeStatusLeft);

        OutlinePane tradeStatusRight = new OutlinePane(5, 0, 1, 4, Pane.Priority.LOW);
        tradeStatusRight.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        tradeStatusRight.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeStatusRight.setRepeat(true);
        tradeGUI.addPane(tradeStatusRight);

        OutlinePane leftInventory = new OutlinePane(0,0,3,4, Pane.Priority.LOW);

        tradeGUI.addPane(leftInventory);
        this.leftPane = leftInventory;

        OutlinePane rightInventory = new OutlinePane(6,0,3,4, Pane.Priority.LOW);

        tradeGUI.addPane(rightInventory);
        this.rightPane = rightInventory;

        tradeGUI.setOnClose(event -> {
            /*
            if (event.getPlayer() != playerOne && event.getPlayer() != playerTwo)
                return;

             */

            Bukkit.getConsoleSender().sendMessage("TEST SETonClose! e' stato chiamato !");

            if (isClosed)
                return;

            this.isClosed = true;
            TradeManager.getInstance().removeTrader(playerOne.getUniqueId());
            TradeManager.getInstance().removeTrader(playerTwo.getUniqueId());

            if (isPlayerOneTradeReady() && isPlayerTwoTradeReady()) {
                addPanelContentToInventory(playerOne,"right");
                addPanelContentToInventory(playerTwo,"left");
                closeTradeInventory();
                //todo scambio degli inventari (fatto: non funziona il metodo addPanelContent....)
                return;
            }

            addPanelContentToInventory(playerOne, "left");
            addPanelContentToInventory(playerTwo, "right");
            closeTradeInventory();

        });

        return tradeGUI;
    }

    public void showGui() {
        tradeGui.show(playerOne);
        tradeGui.show(playerTwo);
    }

    public void updateGui() {
        tradeGui.update();
    }

    public Gui getGui() {
        return tradeGui;
    }

    public void addItemToPaneInventory(Player player, ItemStack itemStack) {

        GuiItem guiItem = new GuiItem(itemStack);
        guiItem.setAction(inventoryClickEvent -> {
            if (inventoryClickEvent.getWhoClicked() == player) {
                player.getInventory().addItem(guiItem.getItem());
                tradeGui.getItems().remove(guiItem);
            }
            else
                inventoryClickEvent.setCancelled(true);
        });


        if (player == playerOne) {
            this.leftPane.addItem(guiItem);
            tradeGui.update();
            return;
        }

        if (player == playerTwo) {
            this.rightPane.addItem(guiItem);
            tradeGui.update();
        }

    }

    public boolean isAddable(Player player) {
        Inventory inventory = tradeGui.getInventory();
        //this.rightPane.getItems()   todo: e poi un for che cerca un null
        if (player.equals(playerOne)) {
            for (int index : indexLeft) {
                if (inventory.getItem(index) == null)
                    return true;
            }
        }
        if (player.equals(playerTwo)) {
            for (int index : indexRight) {
                if (inventory.getItem(index) == null)
                    return true;
            }
        }
        return false;
    }



    public void addPanelContentToInventory(Player player, String string) {
        ItemStack[] itemStacks = tradeGui.getInventory().getContents();
        switch(string.toLowerCase()) {
            case "LEFT":
                for (int i : indexLeft) {
                    player.getInventory().addItem(itemStacks[i]);
                }
                break;
            case "RIGHT":
                for (int i : indexRight) {
                    player.getInventory().addItem(itemStacks[i]);
                }
                break;
        }
    }

    public void closeTradeInventory() {
        for (HumanEntity player : tradeGui.getViewers()) {
            player.closeInventory();
        }
    }

    public boolean isPlayerOneTradeReady() {
        return this.tradeStatusLeft;
    }

    public boolean isPlayerTwoTradeReady(){
        return tradeStaturRight;
    }

}
