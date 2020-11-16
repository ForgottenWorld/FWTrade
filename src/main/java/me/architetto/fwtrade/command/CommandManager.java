package me.architetto.fwtrade.command;

import me.architetto.fwtrade.command.user.AcceptCommand;
import me.architetto.fwtrade.command.user.TradeCommand;
import me.architetto.fwtrade.utils.ChatFormatter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class CommandManager implements TabExecutor{

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(){
        subcommands.add(new TradeCommand());
        subcommands.add(new AcceptCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: can't run commands from console"));
            return true;
        }

        Player p = (Player) sender;

        if( args.length == 0 ) {
            p.sendMessage("--------------------------------");
            for (int i = 0; i < getSubcommands().size(); i++){
                p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
            }
            p.sendMessage("--------------------------------");
            return true;
        }

        for (int i = 0; i < getSubcommands().size(); i++){
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                getSubcommands().get(i).perform(p, args);
            }
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++){
                subcommandsArguments.add(getSubcommands().get(i).getName());
            }

            return subcommandsArguments;

        }else if (args.length >= 2) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }

}
