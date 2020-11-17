package me.architetto.fwtrade.command.user;

import me.architetto.fwtrade.command.SubCommand;
import me.architetto.fwtrade.gui.TradeGui;
import me.architetto.fwtrade.gui.TradeManager;
import me.architetto.fwtrade.utils.ChatFormatter;
import me.architetto.fwtrade.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptCommand extends SubCommand {
    @Override
    public String getName(){
        return "accept";
    }

    @Override
    public String getDescription(){
        return null;
    }

    @Override
    public String getSyntax(){
        return "/fwtrade accept <playername>";
    }

    @Override
    public void perform(Player sender, String[] args) {
        if (!sender.hasPermission("fwtrade.usertrade")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_NOPERM));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_SYNTAX_PLAYERNAME));
            return;
        }

        Player tradeSender = Bukkit.getPlayer(args[1]);

        if (tradeSender == null || !tradeSender.isOnline()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_PLAYER_OFFLINE));
            return;
        }

        if (sender.getGameMode() == GameMode.CREATIVE || tradeSender.getGameMode() == GameMode.CREATIVE) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("No creative mode ty"));
            return;
        }

        TradeManager tradeManager = TradeManager.getInstance();

        if (tradeManager.getInviter(tradeSender.getUniqueId()) != sender.getUniqueId()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_NO_INVITE_FOUND));
            return;
        }

        TradeGui tradeGui = new TradeGui(tradeSender,sender);

        tradeManager.addTradeer(sender.getUniqueId(),tradeGui);
        tradeManager.addTradeer(tradeSender.getUniqueId(),tradeGui);

        tradeManager.removeTradeInvite(tradeSender.getUniqueId());
        Bukkit.getScheduler().cancelTask(tradeManager.getInviteID(tradeSender.getUniqueId()));
        tradeManager.removeInviteID(tradeSender.getUniqueId());

        tradeGui.showGui();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
