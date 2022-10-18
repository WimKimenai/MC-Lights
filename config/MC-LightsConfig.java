package wkimenai.mc-lights.config;

import java.util.Arrays;
import java.io.File;

public class MC-LightsConfig extends Config
{
    static String[] comment;
    @ConfigOptions(name = "title.enable")
    public boolean titleEnable;
    @ConfigOptions(name = "title.enter")
    public String titleEnter;
    @ConfigOptions(name = "subtitle.enter")
    public String subtitleEnter;
    @ConfigOptions(name = "projector.laserDistance")
    public int laserDistance;
    @ConfigOptions(name = "projector.support.id")
    public int supportID;
    @ConfigOptions(name = "projector.support.data")
    public int supportData;
    
    static {
        MC-LightsConfig.comment = new String[] { "/* This is the MC-Lights configuration */", " ", " ", " " };
    }
    
    public MC-LightsConfig(final File file) {
        super(file, Arrays.asList(MC-LightsConfig.comment));
        this.titleEnable = true;
        this.titleEnter = "§4Warning";
        this.subtitleEnter = "§cRisk of epileptic seizure";
        this.laserDistance = 20;
        this.supportID = 160;
        this.supportData = 15;
    }
}
