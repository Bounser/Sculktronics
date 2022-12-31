package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.electrocomponents.*;
import me.leoko.advancedgui.manager.LayoutManager;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.actions.Action;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.events.GuiInteractionBeginEvent;
import me.leoko.advancedgui.utils.events.GuiInteractionExitEvent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AGUIExtension implements LayoutExtension {

    List<Layout> layoutList = new ArrayList<>();

    public AGUIExtension(){

        for(int i = 0; i < 4 ; i++){
            try {
                Layout circuit = LayoutManager.getInstance().layoutFromJson("Circuit" + i + ".json");
                if(!LayoutManager.getInstance().getLayouts().contains(circuit)){
                    LayoutManager.getInstance().getLayouts().add(circuit);
                    layoutList.add(circuit);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *  When the layout is loaded, rects are created with a click action which creates the EComponent
     *  depending on the item the player is holding. If there is already a component in there and the player isnt holding
     *  anything, the EComponent gets deleted. If there is a change the design gets updated and the circuits gets rendered.
     *  Every interaction gets the changes of the render.
     */

    @Override
    public void onLayoutLoad(LayoutLoadEvent e) {

        String layoutName = e.getLayout().getName();

        if (!layoutName.contains("Circuit")) return;

        int size = Integer.parseInt(layoutName.substring(layoutName.length() - 1));
        int x = 0, y = 0;

        switch (size) {
            case 0:
                x = 9;
                y = 9;
                break;
            case 1:
                x = 22;
                y = 9;
                break;
            case 2:
                x = 9;
                y = 22;
                break;
            case 3:
                x = 22;
                y = 22;
                break;
        }

        RectComponent back = new RectComponent(
                "back",
                null,
                false,
                e.getLayout().getDefaultInteraction(),
                19,
                19,
                x*10,
                y*10,
                new Color(0, 0, 0),
                0
        );

        for (int i = 1; i <= x; i++) {

            for (int j = 1; j <= y; j++) {

                int finalJ = j, finalI = i;
                int posf = (i - 1) * 9 + j;

                RectComponent pixel = new RectComponent(
                        posf + "",
                        null,
                        false,
                        e.getLayout().getDefaultInteraction(),
                        10 + j * 10,
                        10 + i * 10,
                        8,
                        8,
                        new Color(18, 25, 33));

                Action clickaction = (interaction, player, primaryTrigger) -> {

                    Circuit cir = CircuitsManager.getInstance().getCircuitFromInteraction(interaction);
                    if (cir != null){

                        EComponent eComponent = cir.getEComponent(cir.getElectroComponent(posf));

                        if (eComponent == null){

                            if (player.getUniqueId().toString().equals(cir.getOwnerUUID())) {
                                int[] pos = new int[2];
                                pos[0] = 10 + finalI * 10;
                                pos[1] = 10 + finalJ * 10;

                                switch (player.getInventory().getItemInMainHand().getType()) {
                                    case ECHO_SHARD:
                                        cir.addElectroComponent(posf, new Wire(cir, pos));
                                        break;
                                    case COMPARATOR:
                                        cir.addElectroComponent(posf, new Diode(cir, pos, 0));
                                        break;
                                    case REDSTONE_TORCH:
                                        cir.addElectroComponent(posf, new NOT(cir, pos, 0, false));
                                        break;

                                        // ...

                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "[!] You can't change the design of this circuit!");
                            }

                        } else {

                            if (player.getUniqueId().toString().equals(cir.getOwnerUUID())) {
                                switch (eComponent) {

                                    case WIRE:
                                        cir.removeEComponent(posf);
                                        break;
                                    case DIODE:
                                        Diode diode = (Diode) cir.getElectroComponent(posf);
                                        if (player.isSneaking()) {
                                            diode.rotate();
                                        } else {
                                            diode.removeIcon();
                                            cir.removeEComponent(posf);
                                        }
                                        break;
                                    case NOT:
                                        NOT inverter = (NOT) cir.getElectroComponent(posf);
                                        inverter.removeIcon();
                                        cir.removeEComponent(posf);
                                        break;

                                        // ...

                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "[!] You can't change the design of this circuit!");
                            }
                        }
                    }
                };
                pixel.setClickAction(clickaction);
            }
        }

        e.getLayout().getTemplateComponentTree().getComponents().add(back);
    }

    @EventHandler
    public void onInteractionStart(GuiInteractionBeginEvent e) {

        // If the interaction is with a circuit, it will update the puts locations, remove the lid and render the visuals of the circuit.

        if (!e.getInteraction().getLayout().getName().contains("Circuit")) return;

        Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

        cir.getLocation().getBlock().setType(Material.AIR);

        cir.addInteraction(e.getInteraction());

        cir.updatePuts();
    }


    @EventHandler
    public void onInteractionEnd(GuiInteractionExitEvent e) {

        if (e.getInteraction().getLayout().getName().contains("Circuit")) {

            Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

            cir.getLocation().getBlock().setType(Material.BLACK_CARPET);

            cir.removeInteraction(e.getInteraction());

            cir.updateCircuit();
        }
    }
}
