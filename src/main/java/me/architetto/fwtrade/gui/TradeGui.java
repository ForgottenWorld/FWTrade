package me.architetto.fwtrade.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.component.ToggleButton;
import me.architetto.fwtrade.FWTrade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TradeGui {

    private Player playerOne;
    private Player playerTwo;

    private Gui tradeGui;

    private OutlinePane leftPane;
    private OutlinePane rightPane;

    private boolean tradeStatusSx;
    private boolean tradeStatusDx;

    private final HashMap<UUID, Boolean> clickCD;

    private boolean isClosed;

    public TradeGui(Player playerOne,Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.tradeGui = prepareGui();

        this.tradeStatusSx = false;
        this.tradeStatusDx = false;

        this.clickCD = new HashMap<UUID, Boolean>() {{
            put(playerOne.getUniqueId(), false);
            put(playerTwo.getUniqueId(), false);
        }};

        this.isClosed = false;
    }

    public Gui prepareGui() {

        Gui tradeGUI = new Gui(4, "       " + ChatColor.YELLOW + "" + ChatColor.BOLD
                + "FW" + ChatColor.RESET + " TRADE INTERFACE");

        OutlinePane infoPaper = new OutlinePane(4,0,1,1);
        infoPaper.addItem(new GuiItem(itemINFO()));
        infoPaper.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeGUI.addPane(infoPaper);

        OutlinePane goldIngot = new OutlinePane(4,3,1,1);
        goldIngot.addItem(new GuiItem(itemGoldIngot()));
        goldIngot.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeGUI.addPane(goldIngot);

        OutlinePane divider = new OutlinePane(4, 1, 1, 2, Pane.Priority.LOW);
        divider.addItem(new GuiItem(new ItemStack(Material.IRON_BARS)));
        divider.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        divider.setRepeat(true);
        tradeGUI.addPane(divider);

        OutlinePane chestInfoItemSx = new OutlinePane(3,0,1,1);
        chestInfoItemSx.addItem(new GuiItem(new ItemStack(Material.CHEST)));
        chestInfoItemSx.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeGUI.addPane(chestInfoItemSx);

        OutlinePane tradeStatusPaneSx = new OutlinePane(3, 1, 1, 2, Pane.Priority.LOW);
        tradeStatusPaneSx.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        tradeStatusPaneSx.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeStatusPaneSx.setRepeat(true);
        tradeGUI.addPane(tradeStatusPaneSx);

        ToggleButton toggleButtonSx = new ToggleButton(3, 3, 1, 1);
        toggleButtonSx.setEnabledItem(new GuiItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE)));
        toggleButtonSx.setDisabledItem(new GuiItem(new ItemStack(Material.RED_STAINED_GLASS_PANE)));
        toggleButtonSx.setOnClick(inventoryClickEvent -> {
            tradeStatusSx = !tradeStatusSx;
            checkTradeStatus();
        });
        tradeGUI.addPane(toggleButtonSx);

        OutlinePane chestInfoItemDx = new OutlinePane(5,0,1,1);
        chestInfoItemDx.addItem(new GuiItem(new ItemStack(Material.CHEST)));
        chestInfoItemDx.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeGUI.addPane(chestInfoItemDx);

        OutlinePane tradeStatusPaneDx = new OutlinePane(5, 1, 1, 2, Pane.Priority.LOW);
        tradeStatusPaneDx.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        tradeStatusPaneDx.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        tradeStatusPaneDx.setRepeat(true);
        tradeGUI.addPane(tradeStatusPaneDx);

        ToggleButton toggleButtonDx = new ToggleButton(5, 3, 1, 1);
        toggleButtonDx.setEnabledItem(new GuiItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE)));
        toggleButtonDx.setDisabledItem(new GuiItem(new ItemStack(Material.RED_STAINED_GLASS_PANE)));
        toggleButtonDx.setOnClick(inventoryClickEvent -> {
            tradeStatusDx = !tradeStatusDx;
            checkTradeStatus();
        });
        tradeGUI.addPane(toggleButtonDx);

        OutlinePane leftInventory = new OutlinePane(0,0,3,4, Pane.Priority.LOW);
        tradeGUI.addPane(leftInventory);
        this.leftPane = leftInventory;

        OutlinePane rightInventory = new OutlinePane(6,0,3,4, Pane.Priority.LOW);
        tradeGUI.addPane(rightInventory);
        this.rightPane = rightInventory;

        tradeGUI.setOnClose(event -> {
            if (isClosed)
                return;

            this.isClosed = true;
            TradeManager.getInstance().removeTrader(playerOne.getUniqueId());
            TradeManager.getInstance().removeTrader(playerTwo.getUniqueId());

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

    public void addItemToPaneInventory(Player player, ItemStack itemStack) {

        GuiItem guiItem = new GuiItem(itemStack);
        guiItem.setAction(inventoryClickEvent -> {
            if (inventoryClickEvent.getWhoClicked() == player) {
                if (this.clickCD.get(player.getUniqueId())) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }
                this.clickCD.put(player.getUniqueId(),true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(FWTrade.plugin,
                        () -> clickCD.put(player.getUniqueId(),false), 25);
                player.getInventory().addItem(guiItem.getItem());
                removeGuiItem(guiItem.getUUID());
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

    private void removeGuiItem(UUID uuid) {
        for (GuiItem guiItem : leftPane.getItems()) {
            if (guiItem.getUUID() == uuid){
                leftPane.removeItem(guiItem);
                tradeGui.update();
                return;
            }
        }

        for (GuiItem guiItem : rightPane.getItems()) {
            if (guiItem.getUUID() == uuid){
                rightPane.removeItem(guiItem);
                tradeGui.update();
                return;
            }
        }

    }

    public boolean isAddable(Player player) {
       // Inventory inventory = tradeGui.getInventory();
        if (player == playerOne && leftPane.getItems().size() != 12)
            return true;

        return player == playerTwo && rightPane.getItems().size() != 12;

    }

    private void addPanelContentToInventory(Player player, String string) {
        switch(string.toUpperCase()) {
            case "LEFT":
                for (GuiItem guiItem : leftPane.getItems()) {
                    player.getInventory().addItem(guiItem.getItem());
                }
                break;
            case "RIGHT":
                for (GuiItem guiItem : rightPane.getItems()) {
                    player.getInventory().addItem(guiItem.getItem());
                }
                break;
        }
    }

    private void closeTradeInventory() {
        for (HumanEntity player : tradeGui.getViewers()) {
            player.closeInventory();
        }
    }

    private boolean checkTradeStatus() {
        if (tradeStatusSx && tradeStatusDx) {
            addPanelContentToInventory(playerOne, "LEFT");
            addPanelContentToInventory(playerTwo,"RIGHT");
            TradeManager.getInstance().removeTrader(playerOne.getUniqueId());
            TradeManager.getInstance().removeTrader(playerTwo.getUniqueId());
            isClosed = true;
            closeTradeInventory();
            return true;
        }
        return false;
    }

    private ItemStack itemINFO() {
        ItemStack paperINFO = new ItemStack(Material.PAPER);
        ItemMeta paperINFOmeta = paperINFO.getItemMeta();
        paperINFOmeta.setDisplayName(ChatColor.YELLOW + "ES. TRADE TAX");

        List<String> paperINFOmetaLORE = new ArrayList<>();
        paperINFOmetaLORE.add(ChatColor.GREEN + "tassa sul commercio ?");
        paperINFOmetaLORE.add(ChatColor.GREEN + "potrebbe dipendere anche dalla distanza tra i due player");
        paperINFOmeta.setLore(paperINFOmetaLORE);
        paperINFO.setItemMeta(paperINFOmeta);

        return paperINFO;
    }

    private ItemStack itemGoldIngot() {
        ItemStack goldIngotItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta goldIngotMeta = goldIngotItem.getItemMeta();
        goldIngotMeta.setDisplayName(ChatColor.YELLOW + "ADD MONEY");

        List<String> goldIngotLore = new ArrayList<>();
        goldIngotLore.add(ChatColor.GREEN + "Clicca per aggiungere 100z allo scambio");
        goldIngotMeta.setLore(goldIngotLore);
        goldIngotItem.setItemMeta(goldIngotMeta);

        return goldIngotItem;
    }

}
