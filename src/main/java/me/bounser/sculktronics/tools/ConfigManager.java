package me.bounser.sculktronics.tools;

import me.bounser.sculktronics.Sculktronics;

public class ConfigManager {

    private static ConfigManager instance;
    private static Sculktronics main;

    public static ConfigManager getInstance(){
        if(instance == null){
            main = Sculktronics.getInstance();

            main.getConfig().options().copyDefaults();
            main.saveDefaultConfig();
            instance = new ConfigManager();

            return instance;
        }
        return instance;
    }

    public Boolean getCheckResources(){ return main.getConfig().getBoolean("AutoResourcesInjection"); }

    public boolean getDebug(){ return main.getConfig().getBoolean("Debug"); }

    public Boolean getClockPrevention(){ return main.getConfig().getBoolean("ClockPrevention.enabled"); }

    public int getTicksPerChange(){ return main.getConfig().getInt("ClockPrevention.ticksPerChange"); }

}
