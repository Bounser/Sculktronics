package me.bounser.sculktronics.tools;

import de.leonhard.storage.Json;
import me.bounser.sculktronics.Sculktronics;
import me.bounser.sculktronics.circuits.Circuit;
import me.bounser.sculktronics.circuits.CircuitsManager;
import me.bounser.sculktronics.circuits.Segment;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Data {

    /**
     * Data structure:
     *
     *  UUID(X).json: (Simplified)
     *    UUID1:
     *        num: 2
     *        1:
     *          segments:
     *            1:
     *              loc:
     *              world: "world"
     *              x:
     *              y:
     *              z:
     *              design:
     *                1: COMPONENT
     *                2: COMPONENT
     *                ...
     *
     *            2:
     *              ...
     *       2:
     *         ...
     */

    private static Data instance;

    public static Data getInstance() { return instance == null ? instance = new Data() : instance; }

    public int getNum(String owneruuid) {

        Json json = new Json(owneruuid, Sculktronics.getInstance().getDataFolder().getPath() + "/data");

        return json.getInt(owneruuid + ".num");
    }

    // Only called on new circuits.
    public void registerCircuit(Location loc, String owneruuid) {

        Json json = new Json(owneruuid, Sculktronics.getInstance().getDataFolder().getPath() + "/data");

        int i;
        if (json.get("num") != null){
            i = json.getInt("num") + 1;
        } else {
            json.set("num", 1);
            i = 1;
        }
            json.set(owneruuid + "." + i + ".loc.world", loc.getWorld().getName());
            json.set(owneruuid + "." + i + ".loc.x", loc.getX());
            json.set(owneruuid + "." + i + ".loc.y", loc.getY());
            json.set(owneruuid + "." + i + ".loc.z", loc.getZ());
            json.set(owneruuid + ".num", i);
    }

    public void updateCircuit(Circuit circuit) {

        String uuid = circuit.getOwnerUUID();
        Json json = new Json(uuid, Sculktronics.getInstance().getDataFolder().getPath() + "/data");

        int i = circuit.getNum();

        for (Segment seg : circuit.getSegments()) {
            HashMap<Integer, Object> design = seg.getDesign();
            int k = 9*9;

            for (int j = 1; j<=k; j++) {
                json.set(uuid + "." + i + ".segments." + seg.getNum() +  ".design." + j, design.get(k));
            }
        }

    }

    public Location getLocation(int num, String uuid) {
        Json json = new Json(uuid, Sculktronics.getInstance().getDataFolder().getPath() + "/data");

        return new Location(Bukkit.getWorld(json.getString(uuid+"." + num + ".loc.world")),
                json.getDouble(uuid + "." + num + ".loc.x"),
                json.getDouble(uuid + "." + num + ".loc.y"),
                json.getDouble(uuid + "." + num + ".loc.z"));
    }

    public HashMap<Integer, Object> getDesign(Circuit cir, int num, String uuid) {

        Json json = new Json(uuid, Sculktronics.getInstance().getDataFolder().getPath() + "/data");
        HashMap<Integer, Object> design = new HashMap<>();

        int i = 9*9;

        for (int j = 1; j<=i; j++) {
            design.put(j, json.get(uuid + "." + cir.getNum() + ".segments." + num + ".design." + j));
        }
        return design;
    }

}
