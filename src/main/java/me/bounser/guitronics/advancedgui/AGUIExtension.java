package me.bounser.guitronics.advancedgui;

import me.bounser.guitronics.GUItronics;
import me.bounser.guitronics.circuits.Circuit;
import me.bounser.guitronics.circuits.CircuitsManager;
import me.bounser.guitronics.electrocomponents.ElectroComponent;
import me.bounser.guitronics.electrocomponents.ecomponents.Delayer;
import me.bounser.guitronics.electrocomponents.ecomponents.Diode;
import me.bounser.guitronics.electrocomponents.ecomponents.Wire;
import me.bounser.guitronics.tools.Data;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.actions.Action;
import me.leoko.advancedgui.utils.components.RectComponent;
import me.leoko.advancedgui.utils.events.GuiInteractionBeginEvent;
import me.leoko.advancedgui.utils.events.GuiInteractionExitEvent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.event.EventHandler;

import java.awt.*;

public class AGUIExtension implements LayoutExtension {

    Data data = Data.getInstance();

    Color designColor;
    Color poweredColor;

    /*
       When the interaction is created, the rects are created with a click action which states the response depending
       on the type of component (If any). Then it updates the circuit if there is any substantial change.
     */

    @EventHandler
    public void onInteractionStart(GuiInteractionBeginEvent e) {

        // If the interaction is with a circuit, it will set its corresponding design and update the render.

        if(e.getInteraction().getLayout().equals(Data.getInstance().getLayout())){

            if(Data.getInstance().getDebug()) e.getPlayer().sendMessage("Interaction with circuit begins.");

            Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());
            cir.addInteraction(e.getInteraction());

            // Mapping all the rects.
            for(int i = 1; i<9; i++){
                for(int j = 1; j<9; j++){

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
                            EComponentsUtils.getInstance().getRoundFromElectroComponent(electroComponent)
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
                                        cir.addEComponent(finalI*finalJ, new Delayer()); break;
                                    case COMPARATOR:
                                        cir.addEComponent(finalI*finalJ, new Diode(null)); break;
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
            Circuit cir = CircuitsManager.getInstance().getCircuitFromGUIInstance(e.getGuiInstance());

            cir.removeInteraction(e.getInteraction());
        }

    }

    @Override
    public void onLayoutLoad(LayoutLoadEvent event) {
        if(event.getLayout().equals(data.getLayout().getName()))
            GUItronics.getInstance().getLogger().info("Layout found!");
    }
}
