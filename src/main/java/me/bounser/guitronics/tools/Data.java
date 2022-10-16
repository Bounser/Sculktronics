package me.bounser.guitronics.tools;

import de.leonhard.storage.Json;
import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.listeners.RedstoneListener;
import me.leoko.advancedgui.manager.LayoutManager;
import me.leoko.advancedgui.utils.Layout;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

    /** Data structure:
     *
     *      data.json: (Simplified)
     *       users:
     *       - "UUID1"
     *       - "UUID2"
     *        ...
     *    UUID1:
     *        num: 2
     *        1:
     *          loc:
     *             world: "world"
     *             x:
     *             y:
     *             z:
     *          size: 1
     *          design:
     *            1: COMPONENT
     *            2: COMPONENT
     *             ...
     *       2:
     *      ...
     *
     */

    Color wireBasic = getWireBasicColor();
    Color wirePowered = getWirePoweredColor();

    Color delayerBasic = getDelayerBasicColor();
    Color delayerPowered = getDelayerPoweredColor();

    Color diodeBasic = getDiodeBasicColor();
    Color diodePowered = getDiodePoweredColor();

    Color resistorBasic = getResistorBasicColor();
    Color resistorPowered = getResistorPoweredColor();

    private static Data instance;
    private static GUItronics main;
    public static Json json;

    public static Data getInstance(){
        if(instance == null){
            main = GUItronics.getInstance();

            json = new Json("data", GUItronics.getInstance().getDataFolder().getPath() + "/data");
            main.getConfig().options().copyDefaults();
            main.saveDefaultConfig();
            instance = new Data();

            return instance;
        }
        return instance;
    }

    public String getLayoutName(){ return main.getConfig().getString("Layout"); }

    public boolean getDebug(){ return main.getConfig().getBoolean("Debug"); }

    public Boolean getClockPrevention(){ return main.getConfig().getBoolean("ClockPrevention.enabled"); }

    public int getTicksPerChange(){ return main.getConfig().getInt("ClockPrevention.ticksPerChange"); }

    public Color getWireBasicColor(){

        if(wireBasic == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.wire.basic.r");
            rgb[1] = main.getConfig().getInt("Colors.wire.basic.g");
            rgb[2] = main.getConfig().getInt("Colors.wire.basic.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return wireBasic;
        }

    }

    public Color getWirePoweredColor(){

        if(wirePowered == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.wire.powered.r");
            rgb[1] = main.getConfig().getInt("Colors.wire.powered.g");
            rgb[2] = main.getConfig().getInt("Colors.wire.powered.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return wirePowered;
        }

    }

    public Color getDelayerBasicColor(){

        if(delayerBasic == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.delayer.basic.r");
            rgb[1] = main.getConfig().getInt("Colors.delayer.basic.g");
            rgb[2] = main.getConfig().getInt("Colors.delayer.basic.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return delayerBasic;
        }

    }

    public Color getDelayerPoweredColor(){

        if(delayerPowered == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.delayer.powered.r");
            rgb[1] = main.getConfig().getInt("Colors.delayer.powered.g");
            rgb[2] = main.getConfig().getInt("Colors.delayer.powered.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return delayerPowered;
        }

    }

    public Color getDiodeBasicColor(){

        if(diodeBasic == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.diode.basic.r");
            rgb[1] = main.getConfig().getInt("Colors.diode.basic.g");
            rgb[2] = main.getConfig().getInt("Colors.diode.basic.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return diodeBasic;
        }

    }

    public Color getDiodePoweredColor(){

        if(diodePowered == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.diode.powered.r");
            rgb[1] = main.getConfig().getInt("Colors.diode.powered.g");
            rgb[2] = main.getConfig().getInt("Colors.diode.powered.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return diodePowered;
        }

    }

    public Color getResistorBasicColor(){

        if(resistorBasic == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.resistance.basic.r");
            rgb[1] = main.getConfig().getInt("Colors.resistance.basic.g");
            rgb[2] = main.getConfig().getInt("Colors.resistance.basic.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return resistorBasic;
        }

    }

    public Color getResistorPoweredColor(){

        if(resistorPowered == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.resistance.powered.r");
            rgb[1] = main.getConfig().getInt("Colors.resistance.powered.g");
            rgb[2] = main.getConfig().getInt("Colors.resistance.powered.b");

            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return resistorPowered;
        }

    }

    public void registerCircuit(Location loc, String owneruuid){
        json.set(owneruuid + ".loc.world", loc.getWorld().getName());
        json.set(owneruuid + ".loc.x", loc.getX());
        json.set(owneruuid + ".loc.y", loc.getY());
        json.set(owneruuid + ".loc.z", loc.getZ());
        List<String> owners = json.getStringList("users");
        owners.add(owneruuid);
        json.set("users", owners);
        RedstoneListener.getInstance().addBase(loc.add(0,-1,0));
    }

    /**
     *   INPUT/OUTPUT points are numbered clockwise:
     *   ONE  #    TWO   #   FOUR
     *    0   |    0 1   |    0 1            N
     *  3 o 1 |  5 o o 2 |  7 o o 2        W X E
     *    2   |    4 3   |  6 o o 3          S
     *        |          |    5 4
     *        |    0     |
     *        |  5 o 1   |
     *        |  4 o 2   |
     *        |    3     |
     */

    public void registerInput(){

    }

    public void registerOutput(){

    }

    public void updateDesign(Circuit cir){ json.set(cir.getOwneruuid() + ".design", cir.getDesign()); }

    public List<String> getUsersUUID(){ return json.getStringList("users"); }

    public int getNumberCircuitsOfUser(String uuid){ return json.getInt(uuid + ".num"); }

    public List<Location> getLocations(String uuid){

        List<Location> locations = new ArrayList<>();

        for(int i = 1; i<=getNumberCircuitsOfUser(uuid); i++){
            for(int j = 1; j<=4; j++){
                locations.add(new Location(Bukkit.getWorld(json.getString(uuid+"." + j + ".loc.world")),
                        json.getDouble(uuid + "." + j + ".loc.x"),
                        json.getDouble(uuid + "." + j + ".loc.y"),
                        json.getDouble(uuid + "." + j + ".loc.z")));
            }
       }
        return locations;
    }

    public HashMap<Integer, Object> getDesign(String uuid){
        return (HashMap<Integer, Object>) json.getMap(uuid + ".design");
    }

    public HashMap<Circuit, List<Location>> getAllCircuitLocations(){

        HashMap<Circuit, List<Location>> circuits = new HashMap<>();

        for(String uuid : json.getStringList("users")){
            circuits.put(CircuitsManager.getInstance().getCircuitFromOwner(uuid), getLocations(uuid));
        }
        return circuits;
    }
}
