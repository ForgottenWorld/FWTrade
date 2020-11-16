package me.architetto.fwtrade.gui;

import java.util.HashMap;
import java.util.UUID;

public class TradeManager{

    private static TradeManager tradeManager;

    private HashMap<UUID,TradeGui> tradersList;
    private HashMap<UUID,UUID> tradeInvite;
    private HashMap<UUID,Integer> inviteID;

    private TradeManager() {
        if(tradeManager != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.tradersList = new HashMap<UUID, TradeGui>();
        this.tradeInvite = new HashMap<>();

        this.inviteID = new HashMap<>();
    }

    public static TradeManager getInstance() {
        if(tradeManager == null) {
            tradeManager = new TradeManager();
        }
        return tradeManager;
    }

    public TradeGui getTradeGui(UUID uuid) {
        return tradersList.get(uuid);
    }

    public boolean isTrading(UUID player1) {
        return tradersList.containsKey(player1);
    }

    public boolean isTrading(UUID player1, UUID player2) {
        return tradersList.containsKey(player1) || tradersList.containsKey(player2);
    }

    public void removeTrader(UUID uuid) {
        tradersList.remove(uuid);
    }

    public void addTradeer(UUID uuid,TradeGui tradeGui) {
        tradersList.put(uuid,tradeGui);
    }

    public void addTradeInvite(UUID receivingplayer, UUID playerSender) {
        tradeInvite.put(playerSender,receivingplayer);
    }

    public UUID getInviter(UUID player1) {
        return tradeInvite.get(player1);
    }

    public void removeTradeInvite(UUID player) {
        tradeInvite.remove(player);
    }

    public void addInviteID(UUID uuid, int id) {
        this.inviteID.put(uuid, id);
    }

    public Integer getInviteID(UUID uuid) {
        return this.inviteID.get(uuid);
    }

    public void removeInviteID(UUID uuid) {
        this.inviteID.remove(uuid);
    }

}
