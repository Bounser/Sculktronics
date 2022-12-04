package me.bounser.guitronics.tools;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

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

        if(args.length > 0){
            switch(args[0]){
                case "circuits":
                    p.sendMessage("All circuits: " + CircuitsManager.getInstance().getAllCircuits().toString());
                    break;
                case "locations":
                    p.sendMessage("Locations: " + CircuitsManager.getInstance().getCircuitLocs());
                    break;
                case "c":
                    for(Circuit circuit : CircuitsManager.getInstance().getAllCircuits()){
                        if(circuit.getLocation().distance(p.getLocation()) < 4){
                            p.sendMessage(ChatColor.BLUE + "Circuit owner: " + circuit.getOwneruuid());
                            p.sendMessage(ChatColor.DARK_BLUE + "Circuit locations: " + circuit.getLocations());
                            p.sendMessage(ChatColor.BLUE + "Circuit puts: " + circuit.getPutsLocations());
                            p.sendMessage(ChatColor.DARK_BLUE + "Circuit interactions: " + circuit.getInteractions());
                            p.sendMessage(ChatColor.BLUE + "Circuit size: " + circuit.getSize());
                            p.sendMessage(ChatColor.DARK_BLUE + "Circuit type (num): " + circuit.getNum());
                            p.sendMessage(ChatColor.BLUE + "Circuit inputs: " + circuit.getInputs());
                            p.sendMessage(ChatColor.DARK_BLUE + "Circuit outputs: " + circuit.getOutputs());
                        }
                    }
            }
        }
        return false;
    }
}
