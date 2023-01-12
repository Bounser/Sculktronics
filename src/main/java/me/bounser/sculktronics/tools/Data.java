package me.bounser.sculktronics.tools;

import de.leonhard.storage.Json;
import me.bounser.sculktronics.Sculktronics;
import me.bounser.sculktronics.circuits.Circuit;
import me.bounser.sculktronics.circuits.CircuitsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Data {

    /**
     * Data structure:
     *
     *  data.json: (Simplified)
     *    users:
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
    private static Sculktronics main;
    public static Json json;

    public static Data getInstance(){
        if(instance == null){
            main = Sculktronics.getInstance();

            json = new Json("data", Sculktronics.getInstance().getDataFolder().getPath() + "/data");
            main.getConfig().options().copyDefaults();
            main.saveDefaultConfig();
            instance = new Data();

            return instance;
        }
        return instance;
    }

    public Boolean getCheckLayout(){ return main.getConfig().getBoolean("AutoLayoutInjection"); }

    public String getLayoutName(int size){ return main.getConfig().getString("Layout." + size); }

    public int getSize(int n, String uuid){ return main.getConfig().getInt(uuid + "." + n + ".size"); }

    public int getNum(String uuid){ return main.getConfig().getInt(uuid + ".num");}

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
            rgb[0] = main.getConfig().getInt("Colors.resistor.basic.r");
            rgb[1] = main.getConfig().getInt("Colors.resistor.basic.g");
            rgb[2] = main.getConfig().getInt("Colors.resistor.basic.b");
            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return resistorBasic;
        }
    }

    public Color getResistorPoweredColor(){

        if(resistorPowered == null){
            int[] rgb = new int[3];
            rgb[0] = main.getConfig().getInt("Colors.resistor.powered.r");
            rgb[1] = main.getConfig().getInt("Colors.resistor.powered.g");
            rgb[2] = main.getConfig().getInt("Colors.resistor.powered.b");
            return new Color(rgb[0],rgb[1],rgb[2]);
        } else {
            return resistorPowered;
        }
    }

    // Only called on new circuits.
    public void registerCircuit(Location loc, String owneruuid){
        int i;
        if(getUsersUUID().contains(owneruuid)){
            i = getNum(owneruuid) + 1;
        } else {
            List<String> owners = json.getStringList("users");
            owners.add(owneruuid);
            json.set("users", owners);
            i = 0;
        }
            json.set(owneruuid + "." + i + ".loc.world", loc.getWorld().getName());
            json.set(owneruuid + "." + i + ".loc.x", loc.getX());
            json.set(owneruuid + "." + i + ".loc.y", loc.getY());
            json.set(owneruuid + "." + i + ".loc.z", loc.getZ());
            json.set(owneruuid + ".num", i);
    }

    public void updateCircuit(Circuit circuit){

        String owneruuid = circuit.getOwnerUUID();
        int i = circuit.getNum();
        HashMap<Integer, Object> design = circuit.getDesign();
        String uuid = circuit.getOwnerUUID();

        json.set(owneruuid + "." + i + ".size", circuit.getSize());

        int j = 0;
        switch(circuit.getSize()){
            case 0: i = 9*9; break;
            case 3: i = 22*22; break;
            default: i = 9*22;
        }
        for(int k = 1; k<=i; k++){
            json.set(uuid + "." + i + ".design." + j, design.get(k));
        }
    }

    public List<String> getUsersUUID(){ return json.getStringList("users"); }

    public Location getLocation(int num, String uuid){
        return new Location(Bukkit.getWorld(json.getString(uuid+"." + num + ".loc.world")),
                json.getDouble(uuid + "." + num + ".loc.x"),
                json.getDouble(uuid + "." + num + ".loc.y"),
                json.getDouble(uuid + "." + num + ".loc.z"));
    }

    public HashMap<Integer, Object> getDesign(int num, String uuid){

        HashMap<Integer, Object> design = new HashMap<>();

        Circuit cir = CircuitsManager.getInstance().getCircuitFromOwner(num, uuid);
        int i;
        switch(cir.getSize()){
            case 0: i = 9*9; break;
            case 3: i = 22*22; break;
            default: i = 9*22;
        }

        for(int j = 1; j<=i; j++){
            design.put(j, json.get(uuid + "." + num + ".design." + j));
        }
        return design;
    }
}
