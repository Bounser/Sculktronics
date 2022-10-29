package me.bounser.guitronics.tools;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.CircuitsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {

    GUItronics main;
    public DebugCommand(GUItronics main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            main.getLogger().info("This command can't be executed in console.");
        }

        Player p = (Player) sender;

        p.sendMessage("All circuits: " + CircuitsManager.getInstance().getAllCircuits().toString());
        p.sendMessage(" ");


        return false;
    }
}
