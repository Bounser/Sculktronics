package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.electrocomponents.*;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.actions.Action;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.events.GuiInteractionBeginEvent;
import me.leoko.advancedgui.utils.events.GuiInteractionExitEvent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AGUIExtension implements LayoutExtension {

    List<Layout> layoutList = new ArrayList<>();

    @Override
    public void onLayoutLoad(LayoutLoadEvent e){

        switch(e.getLayout().getName()){
            case "Circuit0":
                GUItronics.getInstance().getLogger().info("Layout 0 found!");
                layoutList.add(e.getLayout()); break;
            case "Circuit1":
                GUItronics.getInstance().getLogger().info("Layout 1 found!");
                layoutList.add(e.getLayout()); break;
            case "Circuit2":
                GUItronics.getInstance().getLogger().info("Layout 2 found!");
                layoutList.add(e.getLayout()); break;
            case "Circuit3":
                GUItronics.getInstance().getLogger().info("Layout 3 found!");
                layoutList.add(e.getLayout()); break;
        }

        if(e.getLayout().getName().contains("Circuit")) {

            e.getLayout().getTemplateComponentTree().locate("u10").setClickAction((interaction, player, primaryTrigger) -> {
                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(0);
            });

            e.getLayout().getTemplateComponentTree().locate("d10").setClickAction((interaction, player, primaryTrigger) -> {
                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(2);
            });

            e.getLayout().getTemplateComponentTree().locate("r10").setClickAction((interaction, player, primaryTrigger) -> {
                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(1);
            });

            e.getLayout().getTemplateComponentTree().locate("l10").setClickAction((interaction, player, primaryTrigger) -> {
                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(3);
            });

        }

    }


    /**
     *  When the interaction is created, the rects are created with a click action which states the response depending
     *  on the type of component (If any). Then it updates (Visually) the circuit if there is any substantial change.
     */

    @EventHandler
    public void onInteractionStart(GuiInteractionBeginEvent e) {

        // If the interaction is with a circuit, it will set its corresponding design and update the render.

        if(!e.getInteraction().getLayout().getName().contains("Circuit")) return;

        if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Starting interaction...");

        Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

        int size = cir.getSize();

        if(e.getInteraction().getLayout().getName().contains("Circuit")) {

            int x = 0;
            int y = 0;

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

            e.getInteraction().getComponentTree().locate("lid").setHidden(true);

            cir.addInteraction(e.getInteraction());

            cir.updatePuts();

            // Mapping all the rects.

            for (int i = 1; i < x; i++) {

                for (int j = 1; j < y; j++) {

                    Action clickAction = null;
                    EComponent EComponent = cir.getEComponent(cir.getElectroComponent(i * j));

                    RectComponent pixel;
                    if (EComponent != null) {
                        pixel = new RectComponent(
                                i + j + "",
                                null,
                                false,
                                e.getInteraction(),
                                19 + j * 10,
                                19 + i * 10,
                                8,
                                8,
                                cir.getColor(i * j, cir.getPoweredState(i * j)),
                                CircuitsManager.getInstance().getRoundFromEComponent(EComponent)
                        );

                    } else {
                        pixel = new RectComponent(
                                i + j + "",
                                null,
                                false,
                                e.getInteraction(),
                                19 + j * 10,
                                19 + i * 10,
                                8,
                                8,
                                new Color(18, 25, 33)
                        );
                    }

                    int finalJ = j;
                    int finalI = i;

                    if (EComponent != null) {
                        switch (EComponent) {

                            case WIRE:
                                clickAction = (interaction, player, primaryTrigger) -> {
                                    cir.removeEComponent(finalI * finalJ);
                                };
                                break;
                            case RESISTOR:
                                clickAction = (interaction, player, primaryTrigger) -> {
                                    Resistor resistor = (Resistor) cir.getElectroComponent(finalI * finalJ);
                                    resistor.remove();
                                    cir.removeEComponent(finalI * finalJ);
                                };
                                break;

                            case DELAYER:
                                clickAction = (interaction, player, primaryTrigger) -> {
                                    Delayer delayer = (Delayer) cir.getElectroComponent(finalI * finalJ);
                                    if (player.isSneaking()) {
                                        delayer.changeDelay();
                                    } else {
                                        delayer.remove();
                                        cir.removeEComponent(finalI * finalJ);
                                    }
                                };
                                break;

                            case DIODE:
                                clickAction = (interaction, player, primaryTrigger) -> {

                                    Diode diode = (Diode) cir.getElectroComponent(finalI * finalJ);
                                    if (player.isSneaking()) {
                                        diode.rotate();
                                    } else {
                                        diode.remove();
                                        cir.removeEComponent(finalI * finalJ);
                                    }
                                };
                                break;
                            case INVERTER:
                                clickAction = (interaction, player, primaryTrigger) -> {
                                    Inverter inverter = (Inverter) cir.getElectroComponent(finalI * finalJ);
                                    inverter.remove();
                                    cir.removeEComponent(finalI * finalJ);
                                };
                                break;
                        }
                    } else {
                        clickAction = (interaction, player, primaryTrigger) -> {

                            int[] pos = new int[2];
                            pos[0] = finalI;
                            pos[1] = finalJ;

                            switch (player.getInventory().getItemInMainHand().getType()) {
                                case ECHO_SHARD:
                                    cir.addElectroComponent(finalI * finalJ, new Wire(cir));
                                    break;
                                case REPEATER:
                                    cir.addElectroComponent(finalI * finalJ, new Delayer(cir, pos, 5));
                                    break;
                                case COMPARATOR:
                                    cir.addElectroComponent(finalI * finalJ, new Diode(cir, pos, 0));
                                    break;
                                case REDSTONE_TORCH:
                                    cir.addElectroComponent(finalI * finalJ, new Inverter(cir, pos, 0));
                                    break;
                                case LIGHTNING_ROD:
                                    cir.addElectroComponent(finalI * finalJ, new Resistor(cir, pos));
                            }
                        };
                    }
                    pixel.setClickAction(clickAction);
                }
            }
        }
    }


    @EventHandler
    public void onInteractionEnd(GuiInteractionExitEvent e) {

        if(e.getInteraction().getLayout().getName().contains("Circuit")) {

            e.getInteraction().getComponentTree().locate("lid").setHidden(false);

            Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

            cir.removeInteraction(e.getInteraction());

            if(Data.getInstance().getDebug()) Bukkit.broadcastMessage("Interaction ended.");
        }
    }
}
