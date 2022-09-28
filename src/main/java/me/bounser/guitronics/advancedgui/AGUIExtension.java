package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.electrocomponents.ecomponents.Delayer;
import me.bounser.guitronics.electrocomponents.ecomponents.Diode;
import me.bounser.guitronics.electrocomponents.ecomponents.Resistor;
import me.bounser.guitronics.electrocomponents.ecomponents.Wire;
import me.bounser.guitronics.tools.Data;
import me.bounser.guitronics.tools.Miscellaneous;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.actions.Action;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.events.GuiInteractionBeginEvent;
import me.leoko.advancedgui.utils.events.GuiInteractionExitEvent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.event.EventHandler;

public class AGUIExtension implements LayoutExtension {

    Data data = Data.getInstance();

    /**
     *  When the interaction is created, the rects are created with a click action which states the response depending
     *  on the type of component (If any). Then it updates the circuit if there is any substantial change.
     */

    // LAYOUTS: Circuit1x1 Circuit2x1 Circuit2x2

    @EventHandler
    public void onLayoutLoad(LayoutLoadEvent e){

        switch(e.getLayout().getName()){
            case "Circuit1": GUItronics.getInstance().getLogger().info("Layout 1 found!"); break;
            case "Circuit2": GUItronics.getInstance().getLogger().info("Layout 2 found!"); break;
            case "Circuit4": GUItronics.getInstance().getLogger().info("Layout 3 found!"); break;
        }

        if(e.getLayout().getName().contains("Circuit")){

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

    @EventHandler
    public void onInteractionStart(GuiInteractionBeginEvent e) {

        // If the interaction is with a circuit, it will set its corresponding design and update the render.

        if(e.getInteraction().getLayout().getName().contains(Data.getInstance().getLayout().getName())){

            int x = 0;
            int y = 0;

            if(e.getInteraction().getLayout().getName().contains("1")){
                x = 9;
                y = 9;
            }
            if(e.getInteraction().getLayout().getName().contains("2")){
                x = 22;
                y = 9;
            }
            if(e.getInteraction().getLayout().getName().contains("3")){
                x = 22;
                y = 22;
            }

            e.getInteraction().getComponentTree().locate("lid").setHidden(true);

            if(Data.getInstance().getDebug()) e.getPlayer().sendMessage("Interaction with circuit begins.");

            Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());
            cir.addInteraction(e.getInteraction());

            // Mapping all the rects.

            for(int i = 1; i<x; i++){
                for(int j = 1; j<y; j++){

                    ElectroComponent electroComponent = cir.getElectroComponent(cir.getEComponent(i * j));

                    RectComponent pixel = new RectComponent(
                            i + j + "",
                            null,
                            false,
                            e.getInteraction(),
                            9 + j*10,
                            9 + i*10,
                            10,
                            10,
                            cir.getColor(i*j, cir.getPoweredState(i*j)),
                            Miscellaneous.getInstance().getRoundFromElectroComponent(electroComponent)
                            );

                    Action clickAction = null;

                    int finalJ = j;
                    int finalI = i;

                    switch(electroComponent){
                        case WIRE:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                cir.removeEComponent(finalI*finalJ);

                            }; break;

                        case DELAYER:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                if(player.isSneaking()){

                                    Delayer delayer = (Delayer) cir.getEComponent(finalI*finalJ);
                                    delayer.changeDelay();

                                } else {

                                    cir.removeEComponent(finalI*finalJ);
                                }

                            }; break;

                        case DIODE:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                if(player.isSneaking()){

                                    Diode diode = (Diode) cir.getEComponent(finalI*finalJ);
                                    diode.rotate();

                                } else {

                                    cir.removeEComponent(finalI*finalJ);
                                }

                            }; break;

                        default:
                            clickAction = (interaction, player, primaryTrigger) -> {

                                switch(player.getInventory().getItemInMainHand().getType()){
                                    case ECHO_SHARD:
                                        cir.addEComponent(finalI*finalJ, new Wire()); break;
                                    case REPEATER:
                                        cir.addEComponent(finalI*finalJ, new Delayer(5)); break;
                                    case COMPARATOR:
                                        cir.addEComponent(finalI*finalJ, new Diode('N')); break;
                                    case LIGHTNING_ROD:
                                        cir.addEComponent(finalI*finalJ, new Resistor());
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
