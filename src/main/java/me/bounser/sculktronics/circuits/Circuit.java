package me.bounser.sculktronics.circuits;

import me.bounser.sculktronics.advancedgui.AGUIInstances;
import me.bounser.sculktronics.components.ElectroComponent;
import me.bounser.sculktronics.components.EComponent;
import me.bounser.sculktronics.components.electrocomponents.*;
import me.bounser.sculktronics.tools.Data;
import me.leoko.advancedgui.manager.GuiWallManager;
import me.leoko.advancedgui.utils.GuiWallInstance;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.interactions.Interaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Circuit {

    // Object. Represents a group of segments and the general characteristics of them.

    List<Segment> segments;

    // Persistent info:
    // Location of the GUI. The location above the main base, located at the north-west block within the cicuit base.
    final Location LOC;
    
    /**
     * Sizes of the circuit. 0 = 1x1 1 = 1x2 2 = 2x1 3 = 2x2
     *                                       N
     *      Size 1: oo  Size 2:  o         W X E
     *                           o           S
     */

    int size;

    int num;
    // UUID of the owner.
    String owneruuid;
    // This is the design of the circuit. Here is stored all the information related to all things the player modified.
    HashMap<Integer, Object> design;

    // All inputs/outputs. Correspond to physical blocks and pixels within the circuit.

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

    // Repeaters
    List<Integer> inputs = new ArrayList<>();
    List<Integer> outputs = new ArrayList<>();

    // Real outputs/inputs of the design. Possible values: 5, 37, 45, 77 (Analogous to 0, 3, 1, 2)
    List<Integer> signalsIn = new ArrayList<>();
    List<Integer> signalsOut = new ArrayList<>();
    // Temp info.
    // List of wires that will be rendered in the next visual render.
    List<Integer> toRender = new ArrayList<>();
    // All interactions involved with this circuit. All changes will be done to ALL current interactions.
    List<Interaction> interactions = new ArrayList<>();

    GuiWallInstance ginstance;

    Boolean overloaded = false;

    public Circuit(Location loc, int size, String uuid, HashMap<Integer, Object> design, int number){
        LOC = loc;
        loc.getBlock().setType(Material.BLACK_CARPET);
        owneruuid = uuid;
        this.size = size;
        num = number;
        if(design == null){
            this.design = new HashMap<>();
        } else {
            this.design = design;
        }

        GuiWallInstance instance = GuiWallManager.getInstance().getActiveInstance(loc);

        if(instance != null){
            ginstance = instance;
        } else {
            ginstance = AGUIInstances.getInstance().placeGUI(loc,this, true);
        }
        checkCircuitPuts();

        if(ginstance.isInArea(Bukkit.getPlayer(UUID.fromString(uuid)).getLocation())){
            ginstance.startInteraction(Bukkit.getPlayer(UUID.fromString(uuid)));
        }
    }
    
    // Getters
    public Location getLocation(){ return LOC; }

    public List<Location> getLocations(){

        List<Location> locations = new ArrayList<>();

        locations.add(LOC);

        if(size == 1){
            locations.add(LOC.add(1,0,0));
        }
        if(size == 2){
            locations.add(LOC.add(0,0,1));
        }
        if(size == 3){
            locations.add(LOC.add(1,0,0));
            locations.add(LOC.add(0,0,1));
            locations.add(LOC.add(-1,0,1));
        }

        return locations;
    }

    public GuiWallInstance getGuiWallInstance(){ return ginstance; }

    public HashMap<Location, Integer> getPutsLocations(){

        HashMap<Location, Integer> locs = new HashMap<>();
        locs.put(getAuxLoc().add(0,-1,-1), 0);
        switch (size){
            case 0:
                locs.put(getAuxLoc().add(1,-1,0), 1);
                locs.put(getAuxLoc().add(0,-1,1), 2);
                locs.put(getAuxLoc().add(-1,-1,0), 3);
                break;
            case 1:
                locs.put(getAuxLoc().add(1,-1,-1), 1);
                locs.put(getAuxLoc().add(2,-1,0), 2);
                locs.put(getAuxLoc().add(1,-1,1), 3);
                locs.put(getAuxLoc().add(0,-1,1), 4);
                locs.put(getAuxLoc().add(-1,-1,0), 5);
                break;
            case 2:
                locs.put(getAuxLoc().add(1,-1,0), 1);
                locs.put(getAuxLoc().add(1,-1,1), 2);
                locs.put(getAuxLoc().add(0,-1,2), 3);
                locs.put(getAuxLoc().add(-1,-1,1), 4);
                locs.put(getAuxLoc().add(-1,-1,0), 5);
                break;
            case 3:
                locs.put(getAuxLoc().add(1,-1,-1), 1);
                locs.put(getAuxLoc().add(2,-1,0), 2);
                locs.put(getAuxLoc().add(2,-1,-1), 3);
                locs.put(getAuxLoc().add(1,-1,-2), 4);
                locs.put(getAuxLoc().add(0,-1,-2), 5);
                locs.put(getAuxLoc().add(-1,-1,1), 6);
                locs.put(getAuxLoc().add(-1,-1,0), 7);
                break;
        }
        return locs;
    }

    public int getPutFromLoc(Location loc){
        return getPutsLocations().get(loc);
    }

    private Location getAuxLoc(){
        return new Location(LOC.getWorld(), LOC.getX(), LOC.getY(), LOC.getZ());
    }

    public int getSize(){ return size; }

    public int getNum(){ return num; }

    public String getOwnerUUID(){ return owneruuid; }

    public HashMap<Integer, Object> getDesign(){ return design; }

    public List<Integer> getInputs(){ return inputs; }

    public List<Integer> getOutputs(){ return outputs; }

    public List<Integer> getRender(){ return toRender; }

    public Color getColor(Object pos, boolean powered){

        switch(getEComponent(design.get(pos))){
            case WIRE:
                Wire wire = (Wire) design.get(pos);
                if(powered) return wire.getPoweredColor();
                return wire.getBasicColor();
            case DIODE:
                Diode diode = (Diode) design.get(pos);
                if(powered) return diode.getPoweredColor();
                return diode.getBasicColor();
            case NOT:
                NOT inverter = (NOT) design.get(pos);
                if(powered) return inverter.getPoweredColor();
                return inverter.getBasicColor();
        }
        return null;
    }

    public EComponent getEComponent(Object ElectroComponent){

        if(ElectroComponent instanceof Wire){
            return ((Wire) ElectroComponent).getEComponent();
        }
        if(ElectroComponent instanceof Diode){
            return ((Diode) ElectroComponent).getEComponent();
        }
        if(ElectroComponent instanceof NOT){
            return ((NOT) ElectroComponent).getEComponent();
        }
        return null;
    }

    public boolean getPoweredState(Object pos){

        if(getEComponent(design.get(pos)) == null) return false;
        switch(getEComponent(design.get(pos))){
            case WIRE:
                Wire wire = (Wire) design.get(pos);
                return wire.isPowered();
            case DIODE:
                Diode diode = (Diode) design.get(pos);
                return diode.isPowered();
            case NOT:
                NOT inverter = (NOT) design.get(pos);
                return inverter.isPowered();
         }
        return false;
    }

    public ElectroComponent getElectroComponent(int pos){ return (ElectroComponent) design.get(pos); }

    // adders / removers

    public void addElectroComponent(int pos, Object ElectroComponent){

        design.put(pos, ElectroComponent);

        // Visual placement. Iteration throw all the interactions of the circuit.

    }

    public void removeEComponent(Object pos){ if(design.containsKey(pos)) design.remove(pos); }

    public void addToRender(int powered){ toRender.add(powered); }

    public void addInput(int input){
        if(!inputs.contains(input)) inputs.add(input);
    }

    public void addOutput(int output){
        if(!outputs.contains(output)) outputs.add(output);
    }

    public void removePut(Object value){
        if(inputs.contains(value)) inputs.remove(value);
        if(outputs.contains(value)) outputs.remove(value);
        updatePuts();
    }

    public void updateRender(boolean newRender){

        if(overloaded) return;

        if(newRender){
            toRender.clear();
            signalsOut.clear();
            CircuitRenderer.getInstance().render(this);
            CircuitRenderer.getInstance().outputRedstone(this);
        }

        setDesign(false);

        for(Interaction i : interactions){
            if(i != null){
                for(int j : toRender){

                    updateRect(design.get(j), i, j, true);

                }
            }
        }
    }

    public void updatePuts(){

        for(Interaction i : interactions) {

            for (int k : Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7)) {
                if (i.getComponentTree().locate(k + "t") != null)
                    i.getComponentTree().locate(k + "t").setHidden(!inputs.contains(k));
                if (i.getComponentTree().locate(k + "f") != null)
                    i.getComponentTree().locate(k + "f").setHidden(!outputs.contains(k));

            }
        }
    }

    public void checkCircuitPuts(){

        HashMap<Location, Integer> plocs = getPutsLocations();

        // TODO: FOR CIRCUITS SIZED FROM 1-3
        for(Location ploc : plocs.keySet()){

            if(ploc.getBlock().getType() == Material.REPEATER){

                Block block = ploc.getBlock();
                Block blockC = getNearestLoc(ploc, getLocations()).getBlock();
                Directional repeater = (Directional) block.getBlockData();

                if(blockC.getFace(block) == BlockFace.EAST){

                    if(repeater.getFacing() == BlockFace.EAST){
                        addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.WEST){
                        addOutput(plocs.get(ploc));
                    }
                }

                if(blockC.getFace(block) == BlockFace.WEST){

                    if(repeater.getFacing() == BlockFace.WEST){
                        addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.EAST){
                        addOutput(plocs.get(ploc));
                    }
                }

                if(blockC.getFace(block) == BlockFace.NORTH){

                    if(repeater.getFacing() == BlockFace.NORTH){
                        addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.SOUTH){
                        addOutput(plocs.get(ploc));
                    }
                }

                if(blockC.getFace(block) == BlockFace.SOUTH){

                    if(repeater.getFacing() == BlockFace.SOUTH){
                        addInput(plocs.get(ploc));
                    } else if(repeater.getFacing() == BlockFace.NORTH){
                        addOutput(plocs.get(ploc));
                    }
                }
                updatePuts();
            }
        }
    }

    public Location getNearestLoc(Location loc, List<Location> locs){

        // Indeed, although the name might be misleading without context, it returns one block of the list with distance 1.

        for(Location l : locs){

            Location laux = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
            laux.add(0,-1,0);

            if(laux.distance(loc) < 1.5){
                return laux;
            }
        }
        return null;
    }

    public void setDesign(boolean updateRender){

        for(Interaction i : interactions) {
            if (i != null) {
                for (int j : design.keySet()) {

                    updateRect(design.get(j), i, j, false);

                }
            }
            if(updateRender) {
                updateRender(true);
            }
        }
    }

    public void updateRect(Object electroComponent, Interaction i, int id, Boolean powered){

        switch(getEComponent(electroComponent)){

            case WIRE: Wire wire = (Wire) electroComponent;
                RectComponent blockw = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockw.setColor(wire.getPoweredColor());
                else blockw.setColor(wire.getBasicColor()); break;

            case DIODE: Diode diode = (Diode) electroComponent;
                RectComponent blockdi = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockdi.setColor(diode.getPoweredColor());
                else blockdi.setColor(diode.getBasicColor()); break;

            case NOT: NOT Not = (NOT) electroComponent;
                RectComponent blockr = i.getComponentTree().locate(id + "a", RectComponent.class);
                if(powered) blockr.setColor(Not.getPoweredColor());
                else blockr.setColor(Not.getBasicColor()); break;

        }
    }

    public void modifySignalsIn(boolean remove, Object value){
        if(remove){
            signalsIn.remove(value);
        } else {
            signalsIn.add((Integer) value);
        }
        CircuitRenderer.getInstance().render(this);
    }

    public void modifySignalsOut(boolean remove, Object value){
        if(remove){
            signalsOut.remove(value);
        } else {
            if(!signalsOut.contains(value)) signalsOut.add((Integer) value);
        }
        CircuitRenderer.getInstance().render(this);
    }

    public List<Integer> getSignalsIn(){ return signalsIn; }

    public List<Integer> getSignalsOut(){ return signalsOut; }

    public void addInteraction(Interaction interaction) {
        if(!interactions.contains(interaction)) interactions.add(interaction);

        // Paste the render on the interaction

    }

    public void removeInteraction(Interaction interaction) { interactions.remove(interaction); }

    // Direction: INT corresponding to clockwise change (Direction). (0-3)

    public void expand(int direction){

        if(size == 0){
            switch (direction){
                case 0:
                    LOC.add(0,0,-1); break;
                case 3:
                    LOC.add(-1,0,0); break;
                case 1:
                    LOC.add(1,-1,0).getBlock().setType(Material.SCULK_CATALYST); break;
                case 2:
                    LOC.add(0,-1,1).getBlock().setType(Material.SCULK_CATALYST); break;

            }
        } else if(size == 1){
            if(direction == 0) { LOC.add(0, 0, -1); }

            LOC.add(0,0,1).getBlock().setType(Material.SCULK_CATALYST);
            LOC.add(1,0,1).getBlock().setType(Material.SCULK_CATALYST);

        } else if(size == 2){
            if(direction == 3) { LOC.add(-1, 0, 0); }

            LOC.add(0,0,1).getBlock().setType(Material.SCULK_CATALYST);
            LOC.add(1,0,1).getBlock().setType(Material.SCULK_CATALYST);
        }
        updateCircuit();
    }

    public void updateCircuit(){
        ginstance = AGUIInstances.getInstance().placeGUI(LOC,this, true);
        Data.getInstance().updateCircuit(this);
    }

    public List<Interaction> getInteractions(){ return interactions; }

    public void setOverloaded(){ overloaded = true; }

    public void unsetOverloaded(){ overloaded = false; }

    public boolean getOverloaded(){ return overloaded; }

    public void dispose(){

    }
}
