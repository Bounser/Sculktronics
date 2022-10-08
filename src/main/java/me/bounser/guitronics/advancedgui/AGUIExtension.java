package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.components.EComponent;
import me.bounser.guitronics.components.electrocomponents.Delayer;
import me.bounser.guitronics.components.electrocomponents.Diode;
import me.bounser.guitronics.components.electrocomponents.Resistor;
import me.bounser.guitronics.components.electrocomponents.Wire;
import me.bounser.guitronics.tools.Data;
import me.bounser.guitronics.tools.Miscellaneous;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.actions.Action;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.events.GuiInteractionBeginEvent;
import me.leoko.advancedgui.utils.events.GuiInteractionExitEvent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class AGUIExtension implements LayoutExtension {

    Data data = Data.getInstance();

    List<Layout> layoutList;

    @Override
    public void onLayoutLoad(LayoutLoadEvent e){

        switch(e.getLayout().getName()){
            case "Circuit1":
                GUItronics.getInstance().getLogger().info("Layout 1 found!");
                layoutList.add(e.getLayout()); break;
            case "Circuit2":
                GUItronics.getInstance().getLogger().info("Layout 2 found!");
                layoutList.add(e.getLayout()); break;
            case "Circuit4":
                GUItronics.getInstance().getLogger().info("Layout 3 found!");
                layoutList.add(e.getLayout()); break;
        }

        if(e.getLayout().getName().contains("Circuit")) {

            e.getLayout().getTemplateComponentTree().locate("u10").setClickAction((interaction, player, primaryTrigger) -> {

                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(1);

            });

            e.getLayout().getTemplateComponentTree().locate("d10").setClickAction((interaction, player, primaryTrigger) -> {

                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(3);

            });

            e.getLayout().getTemplateComponentTree().locate("r10").setClickAction((interaction, player, primaryTrigger) -> {

                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(2);

            });

            e.getLayout().getTemplateComponentTree().locate("l10").setClickAction((interaction, player, primaryTrigger) -> {

                CircuitsManager.getInstance().getCircuitFromInteraction(interaction).expand(4);

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

        Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

        int size = cir.getSize();

        if(e.getInteraction().getLayout().getName().contains(Data.getInstance().getLayout().getName())){

            int x = 0;
            int y = 0;

            switch(size){
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

            if(Data.getInstance().getDebug()) e.getPlayer().sendMessage("Interaction with circuit begins.");

            cir.addInteraction(e.getInteraction());

            // Mapping all the rects.

            for(int i = 1; i<x; i++){
                for(int j = 1; j<y; j++){

                    EComponent EComponent = cir.getEComponent(cir.getElectroComponent(i * j));

                    RectComponent pixel = new RectComponent(
                            i + j + "",
                            null,
                            false,
                            e.getInteraction(),
                            10 + j*10,
                            10 + i*10,
                            9,
                            9,
                            cir.getColor(i*j, cir.getPoweredState(i*j)),
                            Miscellaneous.getInstance().getRoundFromEComponent(EComponent)
                            );

                    Action clickAction = null;

                    int finalJ = j;
                    int finalI = i;

                    switch(EComponent){
                        case WIRE:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                cir.removeEComponent(finalI*finalJ);

                            }; break;

                        case DELAYER:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                if(player.isSneaking()){

                                    Delayer delayer = (Delayer) cir.getElectroComponent(finalI*finalJ);
                                    delayer.changeDelay();

                                } else {

                                    cir.removeEComponent(finalI*finalJ);
                                }

                            }; break;

                        case DIODE:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                if(player.isSneaking()){

                                    Diode diode = (Diode) cir.getElectroComponent(finalI*finalJ);
                                    diode.rotate();

                                } else {

                                    cir.removeEComponent(finalI*finalJ);
                                }

                            }; break;

                        default:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                switch(player.getInventory().getItemInMainHand().getType()){
                                    case ECHO_SHARD:
                                        cir.addElectroComponent(finalI*finalJ, new Wire(cir)); break;
                                    case REPEATER:
                                        cir.addElectroComponent(finalI*finalJ, new Delayer(cir, 5)); break;
                                    case COMPARATOR:
                                        cir.addElectroComponent(finalI*finalJ, new Diode(cir, 0)); break;
                                    case LIGHTNING_ROD:
                                        cir.addElectroComponent(finalI*finalJ, new Resistor(cir));
                                }

                            }; break;

                    }
                    pixel.setClickAction(clickAction);
                }
            }

            if(Data.getInstance().getDebug()) e.getPlayer().sendMessage("With cir: " + cir + "inputs: " + cir.getInputs());

            cir.updateRender(true);

        }
    }

    @EventHandler
    public void onInteractionEnd(GuiInteractionExitEvent e) {

        if(e.getInteraction().getLayout().equals(Data.getInstance().getLayout())) {

            e.getInteraction().getComponentTree().locate("lid").setHidden(false);

            Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

            cir.removeInteraction(e.getInteraction());
        }

    }
}
