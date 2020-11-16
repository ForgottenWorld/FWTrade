package me.architetto.fwtrade.command.user;

import me.architetto.fwtrade.FWTrade;
import me.architetto.fwtrade.command.SubCommand;
import me.architetto.fwtrade.gui.TradeManager;
import me.architetto.fwtrade.utils.ChatFormatter;
import me.architetto.fwtrade.utils.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class TradeCommand extends SubCommand {
    @Override
    public String getName(){
        return "trade";
    }

    @Override
    public String getDescription(){
        return null;
    }

    @Override
    public String getSyntax(){
        return null;
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

        Player secondTrader = Bukkit.getPlayer(args[1]);

        if (secondTrader == null || !secondTrader.isOnline()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_PLAYER_OFFLINE));
            return;
        }

        /*
        if (secondTrader.equals(sender)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_SELF_INVITE));
            return;
        }

         */

        TradeManager tradeManager = TradeManager.getInstance();

        if (tradeManager.isTrading(sender.getUniqueId(), secondTrader.getUniqueId())) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Message.ERR_PLAYER_BUSY));
            return;
        }

        TextComponent acceptInvite = new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "ACCETTA INVITO");
        acceptInvite.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwtrade accept " + sender.getName()));

        secondTrader.sendMessage(new TextComponent(ChatFormatter.formatSuccessMessage(sender.getDisplayName() + " ti ha invitato a commerciare. ")),acceptInvite);

        tradeManager.addTradeInvite(secondTrader.getUniqueId(),sender.getUniqueId());

        sender.sendMessage(ChatFormatter.formatSuccessMessage("Hai invitato " + secondTrader.getDisplayName() + " a commerciare."));

        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run() {

                tradeManager.removeTradeInvite(sender.getUniqueId());
                tradeManager.removeInviteID(sender.getUniqueId());
                sender.sendMessage(ChatFormatter.formatErrorMessage(secondTrader.getDisplayName() + "non ha accettato l'invito."));

            }
        }.runTaskLater(FWTrade.plugin,1200);
        tradeManager.addInviteID(sender.getUniqueId(),bukkitTask.getTaskId());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }




    /*
        addMoneyPaneL.addItem(new GuiItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE),event -> { event.setCancelled(true);
        midSection.clear();
        midSection.addItem(new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)));
        tradeGUI.update();}));

 */
}
